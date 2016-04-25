package cs601.webmail.pages;

import cs601.webmail.db.JDBCCLASS;
import cs601.webmail.entities.Email;
import cs601.webmail.misc.VerifyException;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by xyz on 11/22/14.
 */
public class Reply extends Page {
    Email email = null;

    @Override
    public void generate() throws Exception {
        super.generate();
    }

    @Override
    public ST body() throws SQLException {
        ST st = templates.getInstanceOf("reply");
        st.add("email", email);
        System.out.println(email.getContentTEXT());
        return st;
    }

    @Override
    public ST getTitle() {
        return null;
    }

    public Reply(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public void verify() throws VerifyException, SQLException, IOException {
        String id = request.getParameter("id");
        System.out.println(id);
        try {
            JDBCCLASS.CONNECTION();

            try {
                email = JDBCCLASS.SearchEmailByID(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        finally {
            JDBCCLASS.CLOSE();
        }
    }
}
