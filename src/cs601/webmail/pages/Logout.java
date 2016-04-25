package cs601.webmail.pages;

import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by xyz on 10/26/14.
 */
public class Logout extends Page{
    /** Session variable pointing to a user object */
    public static final String SESSION_MEMBER = "user";

    /** Indicates currently logged in (could be just visiting site) */
    public static final String SESSION_LOGGEDIN = "loggedin";
    private HttpSession session = request.getSession();
    public Logout(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    public void logout(HttpSession session) {
        session.removeAttribute(SESSION_LOGGEDIN);
        session.removeAttribute(SESSION_MEMBER);
        session.invalidate();
    }

    public void verify() {
        logout(session);
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ST body() {
        return templates.getInstanceOf("logout");
    }

    @Override
    public ST getTitle() {
        return new ST("logout page");
    }


}
