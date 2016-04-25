package cs601.webmail.pages;

import cs601.webmail.db.JDBCCLASS;
import cs601.webmail.entities.Email;
import cs601.webmail.misc.VerifyException;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 * Created by xyz on 11/20/14.
 */
public class ShowOutmail extends Page {

    public static Email email;
    @Override
    public ST getTitle() {
        return null;
    }

    @Override
    public ST body() throws SQLException {
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
        if(!contentHTML.equals("")) {
            contentTEXT = "";
        }

        Email demail = new Email(id, from, to, subject, contentTEXT, contentHTML, time, finder, account, hasRead);
        st.add("email", demail);
        return st;
    }

    @Override
    public void verify() throws VerifyException, SQLException {
        String id = request.getParameter("mailid");
        System.out.println(id);
        try {
            JDBCCLASS.CONNECTION();

            try {
                email = JDBCCLASS.SearchEmailByTime(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        finally {
            JDBCCLASS.CLOSE();
        }
    }

    public ShowOutmail(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

}
