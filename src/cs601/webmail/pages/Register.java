package cs601.webmail.pages;

import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xyz on 10/25/14.
 */
public class Register extends Page{
    public Register(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    public void verify() {

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
