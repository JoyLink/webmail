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
 * Created by xyz on 11/11/14.
 */
public class InitAccount extends Page{
    @Override
    public ST getTitle() {
        return new ST("initial your email account");

    }

    @Override
    public ST body() {
        ST st = templates.getInstanceOf("initaccount");
        return st;
    }

    @Override
    public void generate() throws Exception {
        super.generate();
    }

    @Override
    public void verify() throws VerifyException, SQLException, IOException, ServletException, MessagingException {
        super.verify();
    }

    public InitAccount(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
}
