package cs601.webmail.pages;

import cs601.webmail.db.JDBCCLASS;
import cs601.webmail.entities.Account;
import cs601.webmail.entities.Email;
import cs601.webmail.mailServer.InboxManager;
import cs601.webmail.misc.EnDeBase64;
import cs601.webmail.misc.VerifyException;
import org.stringtemplate.v4.ST;

import javax.net.ssl.SSLSocket;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by xyz on 11/7/14.
 */
public class ShowInbox extends Page{
    HttpSession session;
    private SSLSocket socket;
    private BufferedReader br;
    private DataOutputStream dot;
    public ShowInbox(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        session = request.getSession();
    }

    public String getEmadrs(String name) {
        Account em = (Account)session.getAttribute(name);
        return em.getEmailaccount();
    }

    public String getPwd(String name) {

        Account pd = (Account)session.getAttribute(name);
        return pd.getPwd();
    }
    public String getSeradd(String emadrs) {
        String emailaddress = EnDeBase64.getFromBase64(emadrs);
        return "pop."+emailaddress.substring(emailaddress.indexOf('@')+1);
    }

    @Override
    public void verify() throws VerifyException {
        String name = (String) session.getAttribute("ACCOUNT");
        String emadrs = getEmadrs(name);
        String pwd = getPwd(name);
        String seradd = getSeradd(emadrs);
        System.out.println(emadrs);
        String db = request.getParameter("db");
        if(db!=null) {

            InboxManager im = new InboxManager(EnDeBase64.getFromBase64(emadrs), seradd, EnDeBase64.getFromBase64(pwd), request);
            try {
                im.getInbox();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<Email> list = new ArrayList<Email>();
        String sortby = request.getParameter("sortby");
        if(sortby == null) {

        }
        else if(sortby.equals("default")) {
            session.setAttribute("sort", "default");
        }
        else if(sortby.equals("subject")) {
            session.setAttribute("sort", "subject");
        }
        else if(sortby.equals("from")) {
            session.setAttribute("sort", "from");
        }
        else if(sortby.equals("time")) {
            session.setAttribute("sort", "time");
        }
        JDBCCLASS.CONNECTION();

        String col = request.getParameter("col");
        String text = request.getParameter("text");


        try {
            list = JDBCCLASS.GETEMAILS(name, col, text);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sortby = (String) session.getAttribute("sort");
        if(sortby == null) sortby = "default";
        if(sortby.equals("default")) {

        }
        else if(sortby.equals("subject")) {
            Collections.sort(list, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    return (((Email) o2).getSubject()).compareTo((((Email) o1).getSubject()));
                }
            });
        }
        else if(sortby.equals("from")) {
            Collections.sort(list, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    return (((Email)o2).getFrom()).compareTo((   ((Email) o1).getFrom()  ));
                }
            });
        }
        else if(sortby.equals("time")) {
            Collections.sort(list, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    return (((Email)o2).getTime()).compareTo((   ((Email) o1).getTime()  ));
                }
            });
        }


        String start = request.getParameter("start");
        if(start==null) start="1";
        String result = "<table border=\"1\" align=\"center\" width=\"1100\" id=\"inbox\"><tr><th>Subject</th><th>From</th><th>Time</th><th>OPERATION</th></tr>";
        int len = list.size();
        for(int i=len-3*Integer.parseInt(start)+2; i>=len-3*Integer.parseInt(start)&&i>=0; i--) {
            Email email = list.get(i);
            String subject = email.getSubject();
            String From = email.getFrom();
            String Time = email.getTime();
            String id = email.getId();
            String hasRead = email.getHasRead();
            if(hasRead.equals("0"))
                result += "<tr><th>"+"<a href=\"/showInemail?mailid="+id+"\">"+"(*)"+subject +"</th><th>"+From+"</th><th>"+Time+"</th><th>"+"<a href=\"/deleteemail?mailid="+id+"\">"+"delete"+"</th></tr>";
            else
                result += "<tr><th>"+"<a href=\"/showInemail?mailid="+id+"\">"+subject +"</th><th>"+From+"</th><th>"+Time+"</th><th>"+"<a href=\"/deleteemail?mailid="+id+"\">"+"delete"+"</th></tr>";
        }


        if(!start.equals("1") && !start.equals("2") && !start.equals(String.valueOf((int)Math.ceil(len / 3.0))) && !start.equals(String.valueOf((int)Math.ceil(len/3.0)+1)))result += "<tr><th><a href=\"/showinbox?start="+(Integer.parseInt(start)-1)+"&col="+col+"&text="+text+"\"> LAST PAGE</a></th><th>"+start+"</th><th><a href=\"/showinbox?start="+(Integer.parseInt(start)+1)+"&col="+col+"&text="+text+"\"> NEXT PAGE</a></th></tr>";
        else if(start.equals("1"))result += "<tr><th></th><th>"+start+"</th><th><a href=\"/showinbox?start="+(Integer.parseInt(start)+1)+"&col="+col+"&text="+text+"\"> NEXT PAGE</a></th></tr>";
        else if(start.equals("2"))result += "<tr><th><a href=\"/files/main/showinbox.html\">LAST PAGE</a></th><th>"+start+"</th><th><a href=\"/showinbox?start="+(Integer.parseInt(start)+1)+"&col="+col+"&text="+text+"\"> NEXT PAGE</a></th></tr>";
        else result += "<tr><th><a href=\"/showinbox?start="+(Integer.parseInt(start)-1)+"&col="+col+"&text="+text+"\"> LAST PAGE</a></th><th>"+start+"</th><th></th></tr>";
        result += "</table>";
        out.println(result);
    }

    @Override
    public void generate() {
        verify();
    }

    @Override
    public ST body() {
        return null;
    }

    @Override
    public ST getTitle() {
        return null;
    }
}
