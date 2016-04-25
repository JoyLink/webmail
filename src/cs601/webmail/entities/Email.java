package cs601.webmail.entities;

/**
 * Created by xyz on 11/4/14.
 */
public class Email {

    private String from;
    private String to;
    private String subject;
    private String contentTEXT;
    private String contentHTML;
    private String time;
    private String finder;
    private String account;
    private String id;
    private String hasRead;
    public Email(String id, String from, String to, String subject, String contentTEXT, String contentHTML, String time, String finder, String account, String hasRead) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.contentTEXT = contentTEXT;
        this.contentHTML = contentHTML;
        this.time = time;
        this.finder = finder;
        this.account = account;
        this.hasRead = hasRead;

    }

    public String getId() { return id;}

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getContentTEXT() {
        return contentTEXT;
    }

    public String getContentHTML() { return contentHTML; }

    public String getTime() {
        return time;
    }

    public String getFinder() {
        return finder;
    }

    public String getAccount() {
        return account;
    }

    public String getHasRead() { return  hasRead; }

}
