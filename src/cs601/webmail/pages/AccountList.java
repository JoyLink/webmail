package cs601.webmail.pages;

import cs601.webmail.db.JDBCCLASS;
import cs601.webmail.entities.User;
import cs601.webmail.misc.EnDeBase64;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by xyz on 11/1/14.
 */
public class AccountList extends Page{
    public AccountList(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public void verify() {
        ArrayList<String> al = null;
        User u = (User) request.getSession().getAttribute("user");
        try {
            if (u == null) {
                try {
                    response.sendRedirect("/");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                String usrname = u.getName();
                JDBCCLASS.CONNECTION();
                try {
                    al = JDBCCLASS.SEARCHUSERACCOUNT(usrname);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String result = "<select name='account' id='account' onchange=\"choose()\">";
                result += "<option value=\"select one account\"> select one account </option>";
                int len = al.size();
                for (int i = 0; i < len; i++) {
                    String account = EnDeBase64.getFromBase64(al.get(i));
                    result += "<option value=\"" + account + "\">" + account + "</option>";
                }
                result += "</select>";
                out.println(result);
            }
        }
            finally{
                JDBCCLASS.CLOSE();
            }
        }

    @Override
    public void generate() {
        verify();
    }

    @Override
    public ST body() {
        return null;
    }

    @Override
    public ST getTitle() {
        return null;
    }
}
