package cs601.webmail.mailServer;

import cs601.webmail.db.JDBCCLASS;
import cs601.webmail.entities.Attachment;
import cs601.webmail.entities.Email;
import cs601.webmail.entities.GetMail;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by xyz on 11/7/14.
 */
public class InboxManager {
    private  String server;
    private  int port;
    //private  Socket socket;
    private  String emadrs;
    private String seradd;
    private String pwd;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private SSLSocket socket;
    private BufferedReader br;
    private DataOutputStream dot;
    private HttpSession session;
    private String usrname;
    private String eaccount;
    private ArrayList<Attachment> attachments;
    public InboxManager(String emadrs,String seradd, String pwd, HttpServletRequest request) {
        this.seradd = seradd;
        this.pwd = pwd;
        this.emadrs = emadrs;
        this.request = request;
        eaccount = String.valueOf(request.getSession().getAttribute("ACCOUNT"));

    }

    public void getInbox() throws Exception {
        try {
            Security.addProvider(
                    new com.sun.net.ssl.internal.ssl.Provider());

            SSLSocketFactory factory =
                    (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket) factory.createSocket(seradd, 995);
            socket.startHandshake();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dot = new DataOutputStream(socket.getOutputStream());
            System.out.println(br.readLine());
            dot.writeBytes("user " + emadrs + "\r\n");
            System.out.println(br.readLine());
            dot.writeBytes("pass " + pwd + "\r\n");
            System.out.println(br.readLine());

            dot.writeBytes("stat\r\n");
            String s0 = br.readLine();
            int m = Integer.parseInt(s0.split(" ")[1]);
            System.out.println(s0);

            for (int i = 1; i <= m; i++) {
                dot.writeBytes("retr " + i + "\r\n");
                getMail(i);

            }

            //System.out.println(br.readLine());
            for(int i=1; i<=m; i++) {
                dot.writeBytes("dele " + i + "\r\n");
                System.out.println(br.readLine());
            }
            dot.writeBytes("QUIT\r\n");
        } finally {
            dot.close();
            br.close();
        }
    }
    public void getMail(int id) throws Exception{
        String s = "", subject = "", from = "", to = "", date = "", boundary = "", content = "", eid = null, contentTEXT = null, contentHTML = null;
        StringBuffer mime = new StringBuffer();
        s = br.readLine();
        while (true) {
            s = br.readLine();
            if(s.equals(".")) break;
            mime.append(s+"\r\n");
        }
        dot.writeBytes("uidl "+id+"\r\n");
        eid = br.readLine().split(" ")[2];
        ArrayList arrayList = Analyse.transfer(String.valueOf(mime));
        date = (String) arrayList.get(3);
        Date now = new Date(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String time = dateFormat.format(now);

        String ffrom = GetMail.getEmail((String) arrayList.get(1));
        String fto = GetMail.getEmail((String) arrayList.get(2));

        contentTEXT = (String) arrayList.get(4);
        contentHTML = (String) arrayList.get(5);

        subject = (String) arrayList.get(0);

        Email email = new Email(eid, ffrom, fto, subject, contentTEXT, contentHTML, time, "INBOX", eaccount, "0");
        ArrayList<Attachment> attch = (ArrayList<Attachment>) arrayList.get(7);
        String len = String.valueOf(arrayList.get(8));
        JDBCCLASS.CONNECTION();
        JDBCCLASS.ADDMAIL(email);
        JDBCCLASS.ADDATTACHMENT(eid, attch, len);
        JDBCCLASS.CLOSE();

    }

//    public static void main(String[] args) throws Exception{
//        InboxManager im = new InboxManager("usfcs6012@gmail.com","pop.gmail.com","usfusfusf");
//            im.getInbox();
//    }
}
