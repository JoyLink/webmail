package cs601.webmail.pages;

import cs601.webmail.db.JDBCCLASS;
import cs601.webmail.entities.Email;
import cs601.webmail.misc.VerifyException;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xyz on 11/21/14.
 */
public class ShowTrash extends Page {
    @Override
    public ST getTitle() {
        return null;
    }

    @Override
    public ST body() throws SQLException {
        return null;
    }

    @Override
    public void generate() throws SQLException, IOException {
        verify();
    }

    @Override
    public void verify() throws VerifyException, SQLException, IOException {
        String name = (String) request.getSession().getAttribute("ACCOUNT");

        List<Email> list = new ArrayList<Email>();
        JDBCCLASS.CONNECTION();
        try {
            list = JDBCCLASS.GETTRASH(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String result = "<table border=\"0\" align=\"center\" width=\"900\" id=\"inbox\"><tr><th>Subject</th><th>From</th><th>Time</th></tr>";
        int len = list.size();
        for(int i=0; i<len; i++) {
            Email email = list.get(i);
            String subject = email.getSubject();
            String From = email.getFrom();
            String Time = email.getTime();
            result += "<tr><th>"+"<a href=\"/showOutemail?mailid="+Time+"\">"+subject+"</th><th>"+From+"</th><th>"+Time+"</th></tr>";
        }
        result += "</table>";
        out.println(result);
    }

    public ShowTrash(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
}
