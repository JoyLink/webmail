package cs601.webmail.pages;

import cs601.webmail.db.JDBCCLASS;
import cs601.webmail.entities.User;
import cs601.webmail.misc.VerifyException;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by xyz on 11/21/14.
 */
public class AlterPWD extends Page {

    public AlterPWD(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public void generate() throws SQLException, IOException {
        verify();
    }

    @Override
    public void verify() throws VerifyException, SQLException, IOException {
        try{

            String pwd = request.getParameter("pwd");
            User user = (User) request.getSession().getAttribute("user");
            String username = user.getName();
            JDBCCLASS.CONNECTION();
            JDBCCLASS.ALTERPWD(username, pwd);

        }
        finally {
            JDBCCLASS.CLOSE();
        }
        response.sendRedirect("files/main/success.html");
    }

    @Override
    public ST body() throws SQLException {
        return null;
    }

    @Override
    public ST getTitle() {
        return null;
    }
}
