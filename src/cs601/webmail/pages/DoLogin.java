package cs601.webmail.pages;

import cs601.webmail.db.JDBCCLASS;
import cs601.webmail.entities.Account;
import cs601.webmail.entities.User;
import cs601.webmail.misc.EnDeBase64;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by xyz on 10/26/14.
 */
public class DoLogin extends Page {
    public static final String SESSION_MEMBER = "user";
    public static final String SESSION_LOGGEDIN = "loggedin";

    public DoLogin(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    public void login(HttpSession session, User user) {
        session.setAttribute(SESSION_LOGGEDIN, "true");
        session.setAttribute(SESSION_MEMBER, user);
    }

    public void verify() {

        try {
            String username = request.getParameter("text1");
            String pwd = EnDeBase64.getBase64(request.getParameter("text2"));
            JDBCCLASS.CONNECTION();
            
            boolean f = false;
            try {
                f = JDBCCLASS.Login(username, pwd);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (f == false) {
                try {
                    response.sendRedirect("/");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    HttpSession session = request.getSession();
                    session.setMaxInactiveInterval(60 * 20);
                    int id = -1;
                    try {
                        id = JDBCCLASS.GETUSERID(username);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    User u = new User(username, pwd);
                    login(session, u);
                    String account = null;
                    try {
                        account = JDBCCLASS.GETFIRSTACCOUNTPWD(username);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if(account.equals("Not exist")){
                        response.sendRedirect("/initAccount");
                    }
                    else {
                        Account a = (Account) session.getAttribute(account);
                        String pwd2 = null;
                        if (a == null) {
                            try {

                                pwd2 = JDBCCLASS.GETACCOUNTPWD(account);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Account at = new Account(account, pwd2);
                            session.setAttribute(account, at);
                        }

                        request.getSession().setAttribute("ACCOUNT", account);
                        response.sendRedirect("/");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            JDBCCLASS.CLOSE();
        }
    }

    @Override
    public ST body() {
        return templates.getInstanceOf("doLogin");
    }

    @Override
    public ST getTitle() {
        return new ST("doLogin page");
    }


}
