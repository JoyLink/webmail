package cs601.webmail.pages;

import cs601.webmail.db.JDBCCLASS;
import cs601.webmail.misc.EnDeBase64;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by xyz on 10/25/14.
 */
public class DoRegister extends Page{

    public DoRegister(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    public void verify() {


        String username = request.getParameter("text1");
        String pwd = EnDeBase64.getBase64(request.getParameter("text2"));
        JDBCCLASS.CONNECTION();

        boolean  f = false;
        try {
            try {
                f = JDBCCLASS.SEARCHUSER(username);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (f == true)
                try {
                    response.sendRedirect("/register");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            else {
                try {
                    JDBCCLASS.USER_INSEART(username, pwd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    response.sendRedirect("/");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }finally {
            JDBCCLASS.CLOSE();
        }

    }

    @Override
    public ST body() {
        return templates.getInstanceOf("register");
    }

    @Override
    public ST getTitle() {
        return new ST("register page");
    }


}
