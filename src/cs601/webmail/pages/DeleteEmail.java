package cs601.webmail.pages;

import cs601.webmail.db.JDBCCLASS;
import cs601.webmail.misc.VerifyException;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by xyz on 11/21/14.
 */
public class DeleteEmail extends Page {
    @Override
    public ST getTitle() {
        return null;
    }

    @Override
    public ST body() throws SQLException {
        return null;
    }

    @Override
    public void generate() throws SQLException, IOException {
        verify();
    }

    @Override
    public void verify() throws VerifyException, SQLException, IOException {
        String id = request.getParameter("mailid");
        try {
            JDBCCLASS.CONNECTION();
            JDBCCLASS.MOVETOTRASHBYID(id);
        }
        finally {
            JDBCCLASS.CLOSE();
        }
        response.sendRedirect("files/main/success.html");
    }

    public DeleteEmail(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
}
