package cs601.webmail.pages;

import cs601.webmail.db.JDBCCLASS;
import cs601.webmail.misc.VerifyException;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by xyz on 11/24/14.
 */
public class AddContact extends Page {
    @Override
    public ST getTitle() {
        return null;
    }

    @Override
    public ST body() throws Exception {
        return null;
    }

    @Override
    public void generate() throws Exception {
        verify();
    }

    @Override
    public void verify() throws VerifyException, SQLException, IOException {
    try {
        String name = request.getParameter("text1");
        String emailAdd = request.getParameter("text2");
        String userAccount = (String) request.getSession().getAttribute("ACCOUNT");
        JDBCCLASS.CONNECTION();
        boolean f = JDBCCLASS.CONTAINCONTACT(name);
        if(f == false) {
            JDBCCLASS.ADDCONTACT(name, emailAdd, userAccount);
        }
        response.sendRedirect("/files/main/contact.html");
    } finally {
        JDBCCLASS.CLOSE();
    }

    }

    public AddContact(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
}
