package cs601.webmail.pages;

import cs601.webmail.db.JDBCCLASS;
import cs601.webmail.entities.AttachmentFile;
import cs601.webmail.entities.Email;
import cs601.webmail.mailServer.AttachmentToFile;
import cs601.webmail.mailServer.FileList;
import cs601.webmail.misc.VerifyException;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by xyz on 11/11/14.
 */
public class ShowInemail extends Page {
    public static Email email;
    @Override
    public ST getTitle() {
        return null;
    }

    @Override
    public ST body() throws Exception {
        ST st = templates.getInstanceOf("inboxpage");
        String id = email.getId();
        String from = email.getFrom();
        String to = email.getTo();
        String subject = email.getSubject();
        String time = email.getTime();
        String contentTEXT = email.getContentTEXT();
        String contentHTML = email.getContentHTML();
        String finder = email.getFinder();
        String account = email.getAccount();
        String hasRead = email.getHasRead();
        if(hasRead.equals("0")) {
            JDBCCLASS.CONNECTION();
            JDBCCLASS.SETEMAILREAD(id);
        }
        if(!contentHTML.equals("")) {
            contentTEXT = "";
        }
        contentHTML = contentHTML.replace("cid:", "files/"+id+"/");
        Email demail = new Email(id, from, to, subject, contentTEXT, contentHTML, time, finder, account, hasRead);
        AttachmentToFile.Move(id);
        ArrayList<AttachmentFile> af = FileList.getFileList(id);
        st.add("email", demail);
        st.add("filelists", af);
        return st;
    }

    @Override
    public void verify() throws VerifyException, SQLException {
        String id = request.getParameter("mailid");
        System.out.println(id);
     try {
        JDBCCLASS.CONNECTION();

        try {
            email = JDBCCLASS.SearchEmailByID(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String hasRead = email.getHasRead();
        if (hasRead.equals("0")) {
            JDBCCLASS.SETEMAILREAD(id);
        }
    }
        finally {
            JDBCCLASS.CLOSE();
        }
    }

    public ShowInemail(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
}
