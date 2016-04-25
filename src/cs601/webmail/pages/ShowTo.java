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
public class ShowTo extends Page{
    public ShowTo(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public void verify() throws VerifyException, SQLException, IOException {
        try {
            JDBCCLASS.CONNECTION();
            ArrayList<Contact> al = JDBCCLASS.GETCONTACT((String) request.getSession().getAttribute("ACCOUNT"));
            int len = al.size();
            String result = "<select name='to' id=\'to\' onchange=\"choose()\"><option>To</option>";
            for(int i=0; i<len; i++) {
                String emailaddress = al.get(i).getEmailAddress();
                String name = al.get(i).getName();
                result += "<option>"+name+"  "+emailaddress+"</option>";
            }
            result += "</select>";
            out.println(result);
        } finally {
            JDBCCLASS.CLOSE();
        }

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
