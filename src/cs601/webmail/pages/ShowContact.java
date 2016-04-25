package cs601.webmail.pages;

import cs601.webmail.db.JDBCCLASS;
import cs601.webmail.entities.Contact;
import cs601.webmail.misc.VerifyException;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by xyz on 11/24/14.
 */
public class ShowContact extends Page {

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

    @Override
    public void verify() throws VerifyException, SQLException, IOException {
        String account = (String) request.getSession().getAttribute("ACCOUNT");
        ArrayList<Contact> al = new ArrayList<Contact>();
        try {
            JDBCCLASS.CONNECTION();
            al = JDBCCLASS.GETCONTACT(account);

            String result = "<table border=\"0\" align=\"center\" width=\"900\" id=\"inbox\"><tr><th>Name</th><th>Account</th><th>Operation</th></tr>";
            int len = al.size();
            for (int i = 0; i < len; i++) {
                Contact contact = al.get(i);
                String name = contact.getName();
                String EmailAddreass = contact.getEmailAddress();
                result += "<tr><th>" + name + "</th><th>" + EmailAddreass + "</th><th><a href=\"/deleteContact?name=" + name + "&emailaddress=" + EmailAddreass + "\">delete</a></th></tr>";
            }
            result += "</table>";
            out.println(result);
        } finally {
            JDBCCLASS.CLOSE();
        }
    }

    public ShowContact(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
}
