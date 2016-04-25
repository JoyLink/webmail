package cs601.webmail.pages;

import cs601.webmail.entities.User;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by xyz on 10/26/14.
 */
public class Inbox extends Page {
    public Inbox(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    public void verify() {
        HttpSession session = request.getSession();
        User u = (User)session.getAttribute("user");
        if ( u==null ) {
            try {
                response.sendRedirect("/");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public ST body() {
        ST st = templates.getInstanceOf("inbox");
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        if(u != null) {
            String uname = u.getName();
            System.out.println(uname);
            st.add("user", uname);
        }
        return st;
    }

    @Override
    public ST getTitle() {
        return new ST("inbox page");
    }
}
