package cs601.webmail.mailServer;

import cs601.webmail.db.JDBCCLASS;
import cs601.webmail.entities.AttachmentFile;
import cs601.webmail.entities.Email;
import cs601.webmail.misc.EnDeBase64;
import sun.misc.BASE64Encoder;

import javax.mail.MessagingException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/**
 * Created by xyz on 11/7/14.
 */
public class MailSend {
    private  String server;
    private  int port;
    //private  Socket socket;
    private  String emadrs;
    private String seradd;
    private String pwd;
    private String to;
    private String subject;
    private String content;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private SSLSocket socket;
    private BufferedReader br;
    private DataOutputStream dot;
    private HttpSession session;
    private String name = null;

    public MailSend(String emadrs,String seradd, String pwd, String to, String subject, String content, HttpServletRequest request, HttpServletResponse response) {
        this.seradd = seradd;
        this.pwd = pwd;
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.request = request;
        this.response = response;
        this.emadrs = emadrs;

    }

    public String getContent() throws MessagingException, IOException {
        ArrayList<AttachmentFile> filelist = new ArrayList<AttachmentFile>();
        String filePath = "SC/sendAtt/";

        File root = new File(filePath);
        File[] files = root.listFiles();
        for(File file:files){
            name = file.getName();
            if(name.startsWith(".")) continue;
            break;
        }
        File file = new File(name);
        FileInputStream input = new FileInputStream(filePath+name);
        //output = response.getOutputStream();
        byte[] block = new byte[1024*1024];
        int b1 = 0;
        int i=0;
        //start downloading
        while ((b1 = input.read()) != -1) {
            //System.out.println(b1);
            block[i]= (byte) b1;
            i++;
        }
        block[i] = '\0';
        String s = new BASE64Encoder().encode(block);
        return s;

    }

    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);

        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }

        }
        return flag;
    }


    public void sendAtt() throws MessagingException {
        try {
            String boundary = new Date().toString();
            Security.addProvider(
                    new com.sun.net.ssl.internal.ssl.Provider());

            SSLSocketFactory factory =
                    (SSLSocketFactory) SSLSocketFactory.getDefault();
            //socket = (SSLSocket)factory.createSocket("smtp.gmail.com", 465);
            socket = (SSLSocket) factory.createSocket(seradd, 465);

            socket.startHandshake();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dot = new DataOutputStream(socket.getOutputStream());
            System.out.println(br.readLine());

            //dot.writeBytes("HELO smtp.gmail.com\r\n");
            dot.writeBytes("HELO " + seradd + "\r\n");
            System.out.println(br.readLine());

            dot.writeBytes("AUTH LOGIN\r\n");
            System.out.println(br.readLine());
            //dot.writeBytes("eDgzNDIwODA5NEBnbWFpbC5jb20=\r\n");
            dot.writeBytes(emadrs + "\r\n");
            System.out.println(br.readLine());
            //dot.writeBytes("MXYxemh1b2dl\r\n");
            dot.writeBytes(pwd + "\r\n");
            System.out.println(br.readLine());
            dot.writeBytes("MAIL FROM:<" + EnDeBase64.getFromBase64(emadrs) + ">\r\n");
            System.out.println(br.readLine());
            dot.writeBytes("RCPT TO:<" + to + ">\r\n");
            System.out.println(br.readLine());

            dot.writeBytes("DATA\r\n");
            System.out.println(br.readLine());
            dot.writeBytes("Subject: " + subject + "\r\n");
           // System.out.println(br.readLine());
            dot.writeBytes("MIME-Version: 1.0\r\n");
            //System.out.println(br.readLine());
            dot.writeBytes("Content-type: multipart/mixed; boundary=\"#"+boundary+"#\"\r\n\r\n");
            //System.out.println(br.readLine());
            dot.writeBytes("--#"+boundary+"#\r\n");
            //System.out.println(br.readLine());
            dot.writeBytes("Content-Type: text/plain; charset=gb2312\r\n");
            //System.out.println(br.readLine());
            dot.writeBytes("Content-Transfer-Encoding: quoted-printable\r\n");
            //System.out.println(br.readLine());
            dot.writeBytes(content + "\r\n");
            //System.out.println(br.readLine());
            dot.writeBytes("\r\n--#"+boundary+"#\r\n");
            //System.out.println(br.readLine());

            String att = getContent();
            byte[] bytes = att.getBytes();
            dot.writeBytes("Content-Type: application/octet-stream; name="+name+"\r\n");
            //System.out.println(br.readLine());
            dot.writeBytes( "Content-Disposition: attachment; filename="+name+"\r\n");
            //System.out.println(br.readLine());
            dot.writeBytes( "Content-Transfer-Encoding: base64\r\n");
            //System.out.println(br.readLine());
            dot.writeBytes("\r\n");


            dot.writeBytes(att+"\r\n");
           // System.out.println(br.readLine());
            dot.writeBytes("\r\n--#"+boundary+"#--\r\n");
            dot.writeBytes("\r\n");

            dot.writeBytes(".\r\n");
            System.out.println(br.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dot.writeBytes("QUIT\r\n");
                System.out.println(br.readLine());
                JDBCCLASS.CLOSE();
                //System.out.println(br.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String time = dateFormat.format(now);

        session = request.getSession();
        String account = (String) session.getAttribute("ACCOUNT");

        Email email = new Email(null, EnDeBase64.getFromBase64(emadrs), to, subject, content, "", time, "OUTBOX", account, "0");
        try {
            JDBCCLASS.CONNECTION();
            JDBCCLASS.ADDMAIL(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            response.sendRedirect("/files/main/outbox.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        delAllFile("SC/sendAtt/");
    }

    public void send() throws IOException, MessagingException {
    try {
            Security.addProvider(
                    new com.sun.net.ssl.internal.ssl.Provider());

            SSLSocketFactory factory =
                    (SSLSocketFactory) SSLSocketFactory.getDefault();
            //socket = (SSLSocket)factory.createSocket("smtp.gmail.com", 465);
            socket = (SSLSocket) factory.createSocket(seradd, 465);

            socket.startHandshake();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dot = new DataOutputStream(socket.getOutputStream());
            System.out.println(br.readLine());

            //dot.writeBytes("HELO smtp.gmail.com\r\n");
            dot.writeBytes("HELO " + seradd + "\r\n");
            System.out.println(br.readLine());

            dot.writeBytes("AUTH LOGIN\r\n");
            System.out.println(br.readLine());
            //dot.writeBytes("eDgzNDIwODA5NEBnbWFpbC5jb20=\r\n");
            dot.writeBytes(emadrs + "\r\n");
            System.out.println(br.readLine());
            //dot.writeBytes("MXYxemh1b2dl\r\n");
            dot.writeBytes(pwd + "\r\n");
            System.out.println(br.readLine());
            dot.writeBytes("MAIL FROM:<" + EnDeBase64.getFromBase64(emadrs) + ">\r\n");
            System.out.println(br.readLine());
            dot.writeBytes("RCPT TO:<" + to + ">\r\n");
            System.out.println(br.readLine());

            dot.writeBytes("DATA\r\n");

            System.out.println(br.readLine());
            dot.writeBytes("Subject: " + subject + "\r\n");
            dot.writeBytes("\r\n");
            dot.writeBytes(content + "\r\n");
            dot.writeBytes(".\r\n");
            System.out.println(br.readLine());
//th111cs6012

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dot.writeBytes("QUIT\r\n");
                System.out.println(br.readLine());
                JDBCCLASS.CLOSE();
                //System.out.println(br.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String time = dateFormat.format(now);

        session = request.getSession();
        String account = (String) session.getAttribute("ACCOUNT");

        Email email = new Email(null, EnDeBase64.getFromBase64(emadrs), to, subject, content, "", time, "OUTBOX", account, "0");
        try {
            JDBCCLASS.CONNECTION();
            JDBCCLASS.ADDMAIL(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            response.sendRedirect("/files/main/outbox.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
