package cs601.webmail.db;

import cs601.webmail.entities.Attachment;
import cs601.webmail.entities.Contact;
import cs601.webmail.entities.Email;
import cs601.webmail.misc.EnDeBase64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xyz on 10/25/14.
 */
public class JDBCCLASS {

    private static String dbFile = "email.db";
    private static Connection db = null;
    public static void CONNECTION() {

        long start = System.currentTimeMillis();
        try {
            Class.forName("org.sqlite.JDBC"); // force load of driver
            db = DriverManager.getConnection("jdbc:sqlite:" + dbFile);

            // DO SOMETHINE WITH db to read/write

            long stop = System.currentTimeMillis();
            System.out.printf("SQL exe time %1.1f minutes\n", (stop-start)/1000.0/60.0);
        }
        catch (Exception e) {
            System.out.println("JDBC error");
        }
    }

    public static boolean SEARCHUSER(String user) throws Exception{
        PreparedStatement search = db.prepareStatement("select * from user where username = ?");
        search.setString(1, user);
        ResultSet rs = search.executeQuery();
        search.close();
        if(rs.next()) return true;
            else return false;
        }


    public static boolean MATCHUSERACCOUNT(String emladdrs, String username) throws Exception{
        PreparedStatement search = db.prepareStatement("select * from emailacount where userName = ? and emailaddress = ?");
        search.setString(1, username);
        search.setString(2, emladdrs);
        ResultSet rs = search.executeQuery();
        search.close();
        if(rs.next()) return true;
        else return false;
    }

    public static String GETACCOUNTPWD(String account) throws Exception{
        PreparedStatement search = db.prepareStatement("select password from emailacount where emailaddress = ?");
        search.setString(1, account);
        ResultSet rs = search.executeQuery();
        String r = String.valueOf(rs.getString(1));
        search.close();
        //db.close();
        return r;
    }

    public  static ArrayList<String> SEARCHUSERACCOUNT(String username) throws Exception {
        ArrayList<String> AL = new ArrayList<String>();
        PreparedStatement search = db.prepareStatement("select * from emailacount where userName = ?");
        search.setString(1, username);
        ResultSet rs = search.executeQuery();
        ResultSetMetaData metaData = rs.getMetaData();
        int numberOfColumns = metaData.getColumnCount();
        while (rs.next()) {
            String columnValue = rs.getString(2);
            System.out.print(columnValue);
            AL.add(columnValue);
        }
        search.close();
        return AL;
    }
    public static boolean Login(String username, String pwd) throws Exception{
        PreparedStatement search = db.prepareStatement("select password from user where username = ?");
        search.setString(1, username);
        ResultSet rs = search.executeQuery();
        if(rs.next()) {
            String s = rs.getString("password");
            if(s.equals(pwd)) return true;
        }
        search.close();
        return false;
    }

    public static int GETUSERID(String username) throws Exception {
        int id = -1;
        PreparedStatement searchid = db.prepareStatement("select id from user where username = ?");
        searchid.setString(1, username);
        ResultSet rs = searchid.executeQuery();
        if(rs.next()) {
            id = Integer.parseInt(rs.getString("id"));
        }
        searchid.close();
        return id;
    }
    public static void USER_INSEART(String user, String pwd) throws Exception {
        PreparedStatement insert = db.prepareStatement("insert into user(username, password) values (?,?)");
        insert.setString(1, user);
        insert.setString(2, pwd);
        int n = insert.executeUpdate();
        if(n != 1) System.out.println("error");
        insert.close();
    }

    public static void ALTERPWD(String user, String pwd) throws SQLException {
        PreparedStatement insert = db.prepareStatement("update user set password = ? where username = ?");
        insert.setString(2, user);
        insert.setString(1, EnDeBase64.getBase64(pwd));
        int n = insert.executeUpdate();
        if(n != 1) System.out.println("error");
        insert.close();
    }

    public static void ACOUNT_INSEART(String emladrss, String pwd, String username, int port) throws Exception {
        PreparedStatement insert = db.prepareStatement("insert into emailacount(emailaddress, password, userName, port) values (?,?,?,?)");
        insert.setString(1, emladrss);
        insert.setString(2, pwd);
        insert.setString(3, username);
        insert.setInt(4, port);
        int n = insert.executeUpdate();
        if(n != 1) System.out.println("error");
        insert.close();
    }
    public static void ADDMAIL(Email mail) throws Exception{
        String eid = mail.getId();
        String from = mail.getFrom();
        String to = mail.getTo();
        String subject = mail.getSubject();
        String contentTEXT = mail.getContentTEXT();
        String contentHTML = mail.getContentHTML();
        String time = mail.getTime();
        String finder = mail.getFinder();
        String account = mail.getAccount();
        //PreparedStatement insert = db.prepareStatement("insert into emails (from, to, subject, content, time, folder, account) values (?, ?, ?, ?, ?, ?, ?)");
        PreparedStatement insert = db.prepareStatement("insert into emails(eid, mailfrom, mailto, subject, contentTEXT, contentHTML, time, folder, account, hasRead) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        insert.setString(1, eid);
        insert.setString(2, from);
        insert.setString(3, to);
        insert.setString(4, subject);
        insert.setString(5, contentTEXT);
        insert.setString(6, contentHTML);
        insert.setString(7, time);
        insert.setString(8, finder);
        insert.setString(9, account);
        insert.setString(10, "0");
        int n = insert.executeUpdate();
        if(n != 1) System.out.println("error");
        insert.close();
    }

    public static List<Email> GETEMAILS(String emailaddress, String col, String text) throws SQLException {
        List<Email> list = new ArrayList<Email>();
        if (col != null && text != null && !col.equals("null") && !text.equals("null")) {
            String sql = "select * from emails where account = ? and folder = ? and " + col + " like ?";
            PreparedStatement search = db.prepareStatement( sql);
            search.setString(1, emailaddress);
            search.setString(2, "INBOX");

            search.setString(3, "%"+text+"%");
            ResultSet rs = search.executeQuery();
            while (rs.next()) {
                String eid = rs.getString("eid");
                String contentTEXT = rs.getString("contentTEXT");
                String contentHTML = rs.getString("contentHTML");
                String tfrom = rs.getString("mailfrom");
                String tto = rs.getString("mailto");
                String subject = rs.getString("subject");

                String time = rs.getString("time");
                String finder = rs.getString("folder");
                String account = rs.getString("account");
                String hasRead = rs.getString("hasRead");
                Email mail = new Email(eid, tfrom, tto, subject, contentTEXT, contentHTML, time, finder, account, hasRead);
                list.add(mail);
            }
            search.close();
        }
        else {

            PreparedStatement search = db.prepareStatement("select * from emails where account = ? and folder = ?");
            search.setString(1, emailaddress);
            search.setString(2, "INBOX");
            ResultSet rs = search.executeQuery();
            while (rs.next()) {
                String eid = rs.getString("eid");
                String contentTEXT = rs.getString("contentTEXT");
                String contentHTML = rs.getString("contentHTML");
                String tfrom = rs.getString("mailfrom");
                String tto = rs.getString("mailto");
                String subject = rs.getString("subject");

                String time = rs.getString("time");
                String finder = rs.getString("folder");
                String account = rs.getString("account");
                String hasRead = rs.getString("hasRead");
                Email mail = new Email(eid, tfrom, tto, subject, contentTEXT, contentHTML, time, finder, account, hasRead);
                list.add(mail);
            }
            search.close();
        }
        return list;
    }

    public static List<Email> GETOUTEMAILS(String emailaddress , String col, String text) throws SQLException {
        List<Email> list = new ArrayList<Email>();
        if(col != null && text != null && !col.equals("null") && !text.equals("null")) {
            String sql = "select * from emails where account = ? and folder = ? and " + col + " like ?";
            PreparedStatement search = db.prepareStatement( sql);
            search.setString(1, emailaddress);
            search.setString(2, "OUTBOX");
            search.setString(3, "%"+text+"%");
            ResultSet rs = search.executeQuery();
            while (rs.next()) {
                String eid = rs.getString("eid");
                String contentTEXT = rs.getString("contentTEXT");
                String contentHTML = rs.getString("contentHTML");
                String tfrom = rs.getString("mailfrom");
                String tto = rs.getString("mailto");
                String subject = rs.getString("subject");

                String time = rs.getString("time");
                String finder = rs.getString("folder");
                String account = rs.getString("account");
                String hasRead = rs.getString("hasRead");
                Email mail = new Email(eid, tfrom, tto, subject, contentTEXT, contentHTML, time, finder, account, hasRead);
                list.add(mail);
            }
            search.close();
        }
        else{
            PreparedStatement search = db.prepareStatement("select * from emails where account = ? and folder = ?");
            search.setString(1, emailaddress);
            search.setString(2, "OUTBOX");
            ResultSet rs = search.executeQuery();
            while (rs.next()) {
                String eid = rs.getString("eid");
                String contentTEXT = rs.getString("contentTEXT");
                String contentHTML = rs.getString("contentHTML");
                String tfrom = rs.getString("mailfrom");
                String tto = rs.getString("mailto");
                String subject = rs.getString("subject");

                String time = rs.getString("time");
                String finder = rs.getString("folder");
                String account = rs.getString("account");
                String hasRead = rs.getString("hasRead");
                Email mail = new Email(eid, tfrom, tto, subject, contentTEXT, contentHTML, time, finder, account, hasRead);
                list.add(mail);
            }
            search.close();
        }
        return list;
    }

    public static Email SearchEmailByID(String time) throws SQLException {
        Email email = null;
        PreparedStatement search = db.prepareStatement("select * from emails where eid = ?");
        search.setString(1, time);
        ResultSet rs = search.executeQuery();
        if(rs.next()) {
            String eid = rs.getString("eid");
            String tfrom = rs.getString("mailfrom");
            String tto = rs.getString("mailto");
            String subject = rs.getString("subject");
            String contentTEXT = rs.getString("contentTEXT");
            String contentHTML = rs.getString("contentHTML");
            String ttime = rs.getString("time");
            String finder = rs.getString("folder");
            String account = rs.getString("account");
            String hasRead = rs.getString("hasRead");
            email = new Email(eid, tfrom, tto, subject, contentTEXT, contentHTML,ttime, finder, account, hasRead);
        }
        search.close();
        return email;
    }

    public static Email SearchEmailByTime(String time) throws SQLException {
        Email email = null;
        PreparedStatement search = db.prepareStatement("select * from emails where time = ?");
        search.setString(1, time);
        ResultSet rs = search.executeQuery();
        if(rs.next()) {
            String eid = rs.getString("eid");
            String tfrom = rs.getString("mailfrom");
            String tto = rs.getString("mailto");
            String subject = rs.getString("subject");
            String contentTEXT = rs.getString("contentTEXT");
            String contentHTML = rs.getString("contentHTML");
            String ttime = rs.getString("time");
            String finder = rs.getString("folder");
            String account = rs.getString("account");
            String hasRead = rs.getString("hasRead");
            email = new Email(eid, tfrom, tto, subject, contentTEXT, contentHTML,ttime, finder, account, hasRead);
        }
        search.close();
        return email;
    }

    public static void SETEMAILREAD(String id) throws SQLException {
        PreparedStatement update = db.prepareStatement("update emails set hasRead = ? where eid = ?");
        update.setString(1, "1");
        update.setString(2, id);
        update.executeUpdate();
        update.close();
    }

    public static String GETFIRSTACCOUNTPWD(String username) throws SQLException {
        String result = null;
        PreparedStatement search = db.prepareStatement("select * from emailacount where userName = ?");
        search.setString(1, username);
        ResultSet rs = search.executeQuery();
        if(rs.next()) {
            return rs.getString("emailaddress");
        }
        search.close();
        return "Not exist";
    }

    public static void MOVETOTRASHBYID(String id) throws SQLException {
        PreparedStatement move = db.prepareStatement("update emails set folder = ? where eid = ?");
        move.setString(1, "TRASH");
        move.setString(2, id);
        move.executeUpdate();
        move.close();
    }

    public static void MOVETOTRASHBYTIME(String time) throws SQLException {
        PreparedStatement move = db.prepareStatement("update emails set folder = ? where time = ? ");
        move.setString(1, "TRASH");
        move.setString(2, time);
        move.executeUpdate();
        move.close();
    }

    public static List GETTRASH(String name) throws SQLException {
        List<Email> list = new ArrayList<Email>();
        PreparedStatement search = db.prepareStatement("select * from emails where account = ? and folder = ?");
        search.setString(1, name);
        search.setString(2, "TRASH");
        ResultSet rs = search.executeQuery();
        while (rs.next()) {
            String eid = rs.getString("eid");
            String contentTEXT = rs.getString("contentTEXT");
            String contentHTML = rs.getString("contentHTML");
            String tfrom = rs.getString("mailfrom");
            String tto = rs.getString("mailto");
            String subject = rs.getString("subject");

            String time = rs.getString("time");
            String finder = rs.getString("folder");
            String account = rs.getString("account");
            String hasRead = rs.getString("hasRead");
            Email mail = new Email(eid, tfrom, tto, subject, contentTEXT, contentHTML,time, finder, account, hasRead);
            list.add(mail);
        }
        search.close();
        return list;
    }
    public static void ADDATTACHMENT(String eid, ArrayList<Attachment> attch, String l) throws SQLException {
        int len = attch.size();
        for(int i=0; i<len; i++) {
            String filename = attch.get(i).getAttachmentName();
            byte[] at = attch.get(i).getAttach();
            String pos = attch.get(i).getPos();
            InputStream is = new ByteArrayInputStream(at);
            PreparedStatement insert = db.prepareStatement("insert into attachment(emailID, attachmentcontent, filename, pos) values (?, ?, ?, ?)");
            insert.setString(1, eid);
            insert.setBinaryStream(2, is, Integer.parseInt(l));
            insert.setString(3, filename);
            insert.setString(4, pos);
            insert.executeUpdate();
            insert.close();
        }

    }

    public static ArrayList<Attachment> GETATTACHMENTSBYID(String id) throws SQLException, IOException {
        ArrayList<Attachment> attachmentArrayList = new ArrayList<Attachment>();

        PreparedStatement search = db.prepareStatement("select * from attachment where emailID = ?");
        search.setString(1, id);
        ResultSet rs = search.executeQuery();
        while (rs.next()) {
            String attachmentName = rs.getString("filename");
            InputStream attach = rs.getBinaryStream("attachmentcontent");
            String pos = rs.getString("pos");
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096000];
            int n = 0;
            while (-1 != (n = attach.read(buffer))) {
                output.write(buffer, 0, n);
            }
            Attachment attch = new Attachment(attachmentName, output.toByteArray(), pos);
            attachmentArrayList.add(attch);
        }
        search.close();
        return attachmentArrayList;
    }

    public static void CLRERTRASH(String account) throws SQLException {
        PreparedStatement clear = db.prepareStatement("update emails set folder = ? where account = ? and folder = ?");
        clear.setString(1, "DELETE");
        clear.setString(2, account);
        clear.setString(3, "TRASH");
        clear.executeUpdate();
        clear.close();
    }

    public static void ADDCONTACT(String name, String email, String account) throws SQLException {
        PreparedStatement insert = db.prepareStatement("insert into contact(name, emailaddress, account) values (?, ?, ?)");
        insert.setString(1, name);
        insert.setString(2, email);
        insert.setString(3, account);
        insert.executeUpdate();
        insert.close();
    }

    public static boolean CONTAINCONTACT(String name) throws SQLException {
        PreparedStatement search = db.prepareStatement("select * from contact where name = ?");
        search.setString(1, name);
        ResultSet rs = search.executeQuery();
        search.close();
        if(rs.next()) return true;
        return false;
    }

    public static ArrayList<Contact> GETCONTACT(String account) throws SQLException {
        ArrayList<Contact> al = new ArrayList<Contact>();
        PreparedStatement search = db.prepareStatement("select * from contact where account = ?");
        search.setString(1, account);
        ResultSet rs =  search.executeQuery();
        while (rs.next()) {
            String name = rs.getString("name");
            String emailaddress = rs.getString("emailaddress");
            Contact c = new Contact(name, emailaddress);
            al.add(c);
        }
        search.close();
        return al;
    }

    public static void DELETECONTACT(String name, String EmailAddress) throws SQLException {
        PreparedStatement dl = db.prepareStatement("delete from contact where name = ? and emailaddress = ?");
        dl.setString(1, name);
        dl.setString(2, EmailAddress);
        dl.executeUpdate();
        dl.close();
    }
    public static void CLOSE() {
        try {
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
