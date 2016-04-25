package cs601.webmail.pages;

import cs601.webmail.db.JDBCCLASS;
import cs601.webmail.misc.VerifyException;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by xyz on 11/22/14.
 */
public class ClearTrash extends Page {
    @Override
    public void generate() throws SQLException, IOException {
        verify();
    }

    @Override
    public ST body() throws SQLException {
        return null;
    }

    @Override
    public ST getTitle() {
        return null;
    }

    @Override
    public void verify() throws VerifyException, SQLException, IOException {
        String account = String.valueOf(request.getSession().getAttribute("ACCOUNT"));
        try{
            JDBCCLASS.CONNECTION();
            JDBCCLASS.CLRERTRASH(account);
            response.sendRedirect("files/main/second-menu.html");
        }
        finally {
            JDBCCLASS.CLOSE();
        }
       // response.sendRedirect("files/main/success.html");
    }

    public ClearTrash(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
}
