package cs601.webmail.pages;

import cs601.webmail.db.JDBCCLASS;
import cs601.webmail.entities.Account;
import cs601.webmail.misc.EnDeBase64;
import cs601.webmail.misc.VerifyException;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by xyz on 11/2/14.
 */
public class AlterAccount extends Page{
    public AlterAccount(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
    @Override
    public void verify() throws VerifyException {
        try{
            System.out.println("hi");
        HttpSession session = request.getSession();
        String account = EnDeBase64.getBase64(request.getParameter("account"));
            System.out.println(account);
        Account a = (Account) session.getAttribute(account);
        String pwd = null;
        if (a == null) {
            try {
                JDBCCLASS.CONNECTION();
                pwd = JDBCCLASS.GETACCOUNTPWD(account);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Account at = new Account(account, pwd);
            session.setAttribute(account, at);
        }
        session.setAttribute("ACCOUNT", account);
        try {
            response.sendRedirect("/inbox");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    finally {
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
