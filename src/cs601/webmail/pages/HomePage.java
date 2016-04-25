package cs601.webmail.pages;

import cs601.webmail.entities.User;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class HomePage extends Page {
	public HomePage(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public void verify() {
        HttpSession session = request.getSession();
        User u = (User)session.getAttribute("user");
        if(u != null) try {
            response.sendRedirect("/inbox");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	@Override
	public ST body() {
		return templates.getInstanceOf("home");
	}

	@Override
	public ST getTitle() {
		return new ST("Home page");
	}
}
