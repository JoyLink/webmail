package cs601.webmail.entities;

/**
 * Created by xyz on 11/3/14.
 */
public class Account {
    private String emailAddress;
    private String pwd;
    public Account(String name, String pwd) {
        this.emailAddress = name;
        this.pwd = pwd;
    }
    public String getEmailaccount() { return emailAddress; }
    public String getPwd() { return pwd; }
    public String toString() { return emailAddress+":"+pwd; }
}
