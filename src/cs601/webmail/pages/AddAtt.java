package cs601.webmail.pages;

import cs601.webmail.misc.VerifyException;
import org.stringtemplate.v4.ST;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by xyz on 11/28/14.
 */
public class AddAtt extends Page {


    public AddAtt(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public void verify() throws VerifyException, SQLException, IOException, ServletException, MessagingException {


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
