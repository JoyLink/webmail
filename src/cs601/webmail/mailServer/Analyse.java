package cs601.webmail.mailServer;

import cs601.webmail.entities.Attachment;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

import static javax.mail.internet.MimeUtility.decodeText;

/**
 * Created by xyz on 11/9/14.
 */
public class Analyse {

    private static int SIZE = 5*1024*1024;

    private static String contentText = null;
    private static String contentHTML = null;
    private static int len ;
    private static ArrayList<Attachment> attch = new ArrayList<Attachment>();

    public static String getFrom(Message msg) throws UnsupportedEncodingException, MessagingException {
        String from = null;

            from = decodeText(String.valueOf(msg.getFrom()[0]));

        return from;
    }

    public static String getSubject(Message msg) throws MessagingException, UnsupportedEncodingException {
        String subject = null;

        subject = decodeText(msg.getSubject());

        return subject;
    }

    public static String getTo(Message msg) throws MessagingException, UnsupportedEncodingException {
        String to = null;

        to = decodeText(String.valueOf(msg.getAllRecipients()[0]));

        return to;
    }

    public static String getDate(Message msg) throws MessagingException {
        String date = null;

        date = String.valueOf(msg.getSentDate());

        return date;
    }

    public static void getContent(Part msg) throws MessagingException, IOException {
        StringBuffer bodytext = new StringBuffer();

        String contentType = msg.getContentType();
        int nameindex = contentType.indexOf("name");
        boolean conname = false;
        if (nameindex != -1) {
            conname = true;
        }
        System.out.println("CONTENTTYPE:" + contentType);
        if (msg.isMimeType("text/plain") && !conname) {
            //bodytext.append((String)msg.getContent());
            contentText += (String) msg.getContent();
        } else if (msg.isMimeType("text/html") && !conname) {
            //bodytext.append((String)msg.getContent());
            contentHTML += (String) msg.getContent();
        } else if (msg.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) msg.getContent();
            int count = multipart.getCount();
            for (int i = 0; i < count; i++) {
                getContent(multipart.getBodyPart(i));
            }
        } else if (msg.isMimeType("message/rfc822")) {
            getContent((Part) msg.getContent());
        }
    }

    public static byte[] getBuffer(InputStream input) throws IOException {
        BufferedInputStream bis = null;
        byte[] attachbyte = new byte[SIZE];
        String s2 = "";
        try {
            bis = new BufferedInputStream(input);
            len = bis.read(attachbyte, 0, SIZE);
        } finally {

            bis.close();
        }
        attachbyte[len] = '\0';
        return attachbyte;
    }

    public static void saveAttachMent(Part part) throws Exception {

        String fileName = "";
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                //BodyPart mpart = mp.getBodyPart(i);
                MimeBodyPart mpart = (MimeBodyPart) mp.getBodyPart(i);
                String disposition = mpart.getDisposition();
                if ((disposition != null)
                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition
                        .equals(Part.INLINE)))) {
                    //fileName = mpart.getFileName();
                    if(disposition.equals(Part.INLINE)) {
                        fileName = mpart.getContentID();
                        fileName = fileName.replace("<", "");
                        fileName = fileName.replace(">", "");
                    }
                    else fileName = mpart.getFileName();

                    if(fileName == null) continue;
                    if (fileName.toLowerCase().indexOf("gb2312") != -1) {
                        if(disposition.equals(Part.INLINE)) {
                        fileName = MimeUtility.decodeText(fileName);
                        fileName = fileName.replace("<", "");
                        fileName = fileName.replace(">", "");
                        }
                        else fileName = mpart.getFileName();
                    }
                    Attachment a;
                    if(disposition.equals(Part.INLINE)) {
                        a = new Attachment(fileName, getBuffer(mpart.getInputStream()), "1");
                    }
                    else {
                        a = new Attachment(fileName, getBuffer(mpart.getInputStream()), "0");
                    }
                    attch.add(a);

                } else if (mpart.isMimeType("multipart/*")) {
                    saveAttachMent(mpart);
                } else {
                    //fileName = mpart.getFileName();
                    MimeBodyPart mb = new MimeBodyPart(mpart.getInputStream());
                    fileName = mb.getContentID();
                    if ((fileName != null)
                            && (fileName.toLowerCase().indexOf("GB2312") != -1)) {
                        fileName = MimeUtility.decodeText(fileName);
                        //saveFile(fileName, mpart.getInputStream());
                        Attachment a;
                        if(disposition.equals(Part.INLINE)) {
                            a = new Attachment(fileName, getBuffer(mpart.getInputStream()), "1");
                        }
                        else {
                            a = new Attachment(fileName, getBuffer(mpart.getInputStream()), "0");
                        }
                        attch.add(a);
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachMent((Message) part.getContent());
        }
    }


    public static ArrayList transfer(String sch) throws Exception {

        Properties props = new Properties();
        Session mailSession = Session.getInstance(props);
        Message msg = null;
        try {
            msg = new MimeMessage(mailSession, new ByteArrayInputStream(sch.getBytes()));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        String  subject = getSubject(msg);
        String from = getFrom(msg);
        String to = getTo(msg);
        String date = getDate(msg);

        contentText = "";
        contentHTML = "";
        ArrayList<Attachment> attachment = new ArrayList<Attachment>();
        getContent(msg);

        ArrayList al = new ArrayList();
        al.add(subject);
        al.add(String.valueOf(from));
        al.add(to);
        al.add(date);
        al.add(contentText);
        al.add(contentHTML);
        al.add(attachment);
        saveAttachMent(msg);
        al.add(attch);
        al.add(len);
        attch=new ArrayList<Attachment>();
        return al;
    }
//    public static void main(String args[]) throws IOException {
//        transfer("Content-Type: multipart/alternative; boundary=001a11344a6eec4ad905078d56a1\r\n--001a11344a6eec4ad905078d56a1\r\n Content-Type: text/plain;\r\ncharset=UTF-8\r\n hi\r\n--001a11344a6eec4ad905078d56a1\r\nContent-Type: text/html;\r\ncharset=UTF-8\r\n" +
//                "hi\r\n" +
//                "--001a11344a6eec4ad905078d56a1--\r\n.");
//    }
}
