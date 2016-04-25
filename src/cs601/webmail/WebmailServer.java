package cs601.webmail;

import cs601.webmail.Conection.ManyConnectors;
import cs601.webmail.misc.STListener;
import cs601.webmail.pages.*;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.HashMap;
import java.util.Map;

public class WebmailServer {
	public static final String WEBMAIL_TEMPLATES_ROOT = "resources/cs601/webmail/templates";

	public static final STListener stListener = new STListener();

	public static Map<String,Class> mapping = new HashMap<String, Class>();
	static {
		mapping.put("/", HomePage.class);
		mapping.put("/users", UserListPage.class);
        mapping.put("/register", Register.class);
        mapping.put("/doRegister", DoRegister.class);
        mapping.put("/inbox", Inbox.class);
        mapping.put("/doLogin", DoLogin.class);
        mapping.put("/logout", Logout.class);
        mapping.put("/addAcount", AddAcount.class);
        mapping.put("/sendEmail", SendEmail.class);
        mapping.put("/acountlist", AccountList.class);
        mapping.put("/alterAccount", AlterAccount.class);
        mapping.put("/showinbox", ShowInbox.class);
        mapping.put("/showInemail", ShowInemail.class);
        mapping.put("/initAccount", InitAccount.class);
        mapping.put("/showoutbox", ShowOutbox.class);
        mapping.put("/showOutemail", ShowOutmail.class);
        mapping.put("/altPWD", AlterPWD.class);
        mapping.put("/deleteemail", DeleteEmail.class);
        mapping.put("/deleteOutemail", DeleteOutEmail.class);
        mapping.put("/showTrash", ShowTrash.class);
        mapping.put("/clearTrash", ClearTrash.class);
        mapping.put("/reply",Reply.class);
        mapping.put("/forward", Forward.class);
        mapping.put("/addContact", AddContact.class);
        mapping.put("/showContact", ShowContact.class);
        mapping.put("/deleteContact", DeleteContact.class);
        mapping.put("/showTo", ShowTo.class);
        mapping.put("/download", DownLoad.class);
        mapping.put("/addAtt", AddAtt.class);
    }

	public static void main(String[] args) throws Exception {

        if ( args.length<2 ) {
			System.err.println("java cs601.webmail.Server static-files-dir log-dir");
			System.exit(1);
		}

        String staticFilesDir = args[0];
		String logDir = args[1];
        Server server = new Server(8080);
        server = ManyConnectors.connector();
		ServletContextHandler context = new
		            ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

        HandlerCollection hc = new HandlerCollection();
        hc.addHandler(context);

        //String s = System.getProperty("user.dir");
		// add a simple Servlet at "/dynamic/*"
        ServletHolder holderDynamic = new ServletHolder("dynamic", DispatchServlet.class);
		context.addServlet(holderDynamic, "/*");

        // add special pathspec of "/home/" content mapped to the homePath
        ServletHolder holderHome = new ServletHolder("static-home", DefaultServlet.class);
        holderHome.setInitParameter("resourceBase",staticFilesDir);
        holderHome.setInitParameter("dirAllowed","true");
        holderHome.setInitParameter("pathInfoOnly","true");
		context.addServlet(holderHome, "/files/*");

        // Lastly, the default servlet for root content (always needed, to satisfy servlet spec)
        // It is important that this is last.
        ServletHolder holderPwd = new ServletHolder("default", DefaultServlet.class);
        holderPwd.setInitParameter("resourceBase","/tmp/foo");
        holderPwd.setInitParameter("dirAllowed","true");
		context.addServlet(holderPwd, "/");

		// log using NCSA (common log format)
		// http://en.wikipedia.org/wiki/Common_Log_Format
		NCSARequestLog requestLog = new NCSARequestLog();
		requestLog.setFilename(logDir + "/yyyy_mm_dd.request.log");
		requestLog.setFilenameDateFormat("yyyy_MM_dd");
		requestLog.setRetainDays(90);
		requestLog.setAppend(true);
		requestLog.setExtended(true);
		requestLog.setLogCookies(false);
		requestLog.setLogTimeZone("GMT");
		RequestLogHandler requestLogHandler = new RequestLogHandler();
		requestLogHandler.setRequestLog(requestLog);

		requestLogHandler.setServer(server);
        hc.addHandler(requestLogHandler);
        server.setHandler(hc);
		server.start();
		server.join();
	}
}
