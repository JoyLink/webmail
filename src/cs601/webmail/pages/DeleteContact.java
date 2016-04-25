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
public class DeleteContact extends Page {
    public DeleteContact(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public void verify() throws VerifyException, SQLException, IOException {
        String name = request.getParameter("name");
        String EmailAddress = request.getParameter("emailaddress");
        try {
            JDBCCLASS.CONNECTION();
            JDBCCLASS.DELETECONTACT(name, EmailAddress);
        }finally {
            JDBCCLASS.CLOSE();
        }
        response.sendRedirect("/files/main/contact.html");
    }

    @Override
    public void handleDefaultArgs() {
        super.handleDefaultArgs();
    }

    @Override
    public void generate() throws Exception {
        verify();
    }

    @Override
    public ST body() throws Exception {
        return null;
    }

    @Override
    public ST getTitle() {
        return null;
    }
}
