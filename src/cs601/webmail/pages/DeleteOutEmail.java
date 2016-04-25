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
public class DeleteOutEmail extends  Page {
    @Override
    public ST getTitle() {
        return null;
    }

    @Override
    public ST body() throws SQLException {
        return null;
    }

    @Override
    public void generate() throws Exception {
        super.generate();
    }

    @Override
    public void verify() throws VerifyException, SQLException, IOException {
        String time = request.getParameter("mailid");
        try {
            JDBCCLASS.CONNECTION();
            JDBCCLASS.MOVETOTRASHBYTIME(time);
        }
        finally {
            JDBCCLASS.CLOSE();
        }
        response.sendRedirect("files/main/success.html");
    }

    public DeleteOutEmail(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
}
