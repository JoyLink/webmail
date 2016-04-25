package cs601.webmail.pages;

import cs601.webmail.db.JDBCCLASS;
import cs601.webmail.entities.Account;
import cs601.webmail.entities.User;
import cs601.webmail.mailServer.MailManager;
import cs601.webmail.misc.EnDeBase64;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by xyz on 10/28/14.
 */
public class AddAcount extends  Page{
    public AddAcount(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    public String getServer(String s) {
        int start = s.indexOf('@');
        String firstStr = "pop.";
        String secondStr = s.substring(start+1);
        return  firstStr+secondStr;
    }
    public void verify() {
    try{
        JDBCCLASS.CONNECTION();
        String emailaddress1 = request.getParameter("addname");
        String pwd1 = request.getParameter("addpwd");

        String emailaddress2 = request.getParameter("addname1");
        String pwd2 = request.getParameter("addpwd1");
        String emailaddress = null, pwd = null;
        if(emailaddress1 == null) {
            emailaddress = emailaddress2;
            pwd = pwd2;
        }
        else {
            emailaddress =emailaddress1;
            pwd = pwd1;
        }
        String user = null;
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        if(u != null)user = u.getName();
        boolean f = false;
        try {
            f = JDBCCLASS.MATCHUSERACCOUNT(emailaddress, user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (f == true) {
            try {
                response.sendRedirect("/");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            f = false;
            //String svr = getServer(emailaddress);

            int i = emailaddress.indexOf('@');
            //String svr = "pop."+ request.getParameter("ServerAddress");
            String svr = "pop." + emailaddress.substring(i+1);
            //int port = Integer.parseInt(request.getParameter("port"));
            int port = 995;
            MailManager mm = new MailManager(svr, port);
            try {
                f = mm.judge(emailaddress, pwd);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (f) {

                try {

                    JDBCCLASS.ACOUNT_INSEART(EnDeBase64.getBase64(emailaddress), EnDeBase64.getBase64(pwd), user, port);
                    String account = EnDeBase64.getBase64((emailaddress));
                    Account a = (Account) session.getAttribute(account);

                    if (a == null) {
                        try {
                            JDBCCLASS.CONNECTION();
                            pwd2 = JDBCCLASS.GETACCOUNTPWD(account);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Account at = new Account(account, pwd2);
                        session.setAttribute(account, at);
                    }

                    request.getSession().setAttribute("ACCOUNT", account);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if(emailaddress1!=null)response.sendRedirect("files/main/success.html");
                    else response.sendRedirect("/");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else try {
                response.sendRedirect("/");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

        finally {
            JDBCCLASS.CLOSE();
        }
    }
    @Override
    public ST body() {
        return templates.getInstanceOf("doLogin");
    }

    @Override
    public ST getTitle() {
        return new ST("doLogin page");
    }

}
