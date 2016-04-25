package cs601.webmail.pages;

import cs601.webmail.entities.Account;
import cs601.webmail.mailServer.MailSend;
import cs601.webmail.misc.EnDeBase64;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.stringtemplate.v4.ST;

import javax.mail.MessagingException;
import javax.net.ssl.SSLSocket;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by xyz on 10/31/14.
 */
public class SendEmail extends Page {
    private SSLSocket socket;
    private BufferedReader br;
    private DataOutputStream dot;
    private String emadrs, pwd;
    private HttpSession session;

    private static final int MEMORY_THRESHOLD   = 1024*1024*100;
    private static final int MAX_FILE_SIZE      = 1024*1024*100;
    private static final int MAX_REQUEST_SIZE   = 1024*1024*100;
    private String fileName = null;
    private String to=null;
    private String from=null;
    private String subject=null;
    private String content=null;

    public SendEmail(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        session = request.getSession();
    }
    public String getEmadrs(String name) {

           Account em = (Account)session.getAttribute(name);
           return em.getEmailaccount();
    }
    public String getPwd(String name) {

            Account pd = (Account)session.getAttribute(name);
            return pd.getPwd();
    }
    public String getSeradd(String emadrs) {
        String emailaddress = EnDeBase64.getFromBase64(emadrs);
        return "smtp."+emailaddress.substring(emailaddress.indexOf('@')+1);
    }
    public void verify() throws ServletException, IOException, MessagingException {




        DiskFileItemFactory factory = new DiskFileItemFactory();
        // sets memory threshold - beyond which files are stored in disk
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // sets temporary location to store files
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);

        // sets maximum size of upload file
        upload.setFileSizeMax(MAX_FILE_SIZE);

        // sets maximum size of request (include file + form data)
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // constructs the directory path to store upload file
        // this path is relative to application's directory
        String uploadPath = "SC/sendAtt/";

        // creates the directory if it does not exist
        File uploadDir = new File(uploadPath);

        try {
            // parses the request's content to extract file data
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);

            if (formItems != null && formItems.size() > 0) {
                // iterates over form's fields
                for (FileItem item : formItems) {
                    // processes only fields that are not form fields
                    if (!item.isFormField()) {
                        fileName = new File(item.getName()).getName();
                        if(fileName.equals("")) continue;
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);

                        // saves the file on disk
                        item.write(storeFile);
                        request.setAttribute("message",
                                "Upload has been done successfully!");
                    }
                    else {
                        String s1 = item.getFieldName();
                        String s2 = item.getString();
                        if(s1.equals("from")) from = s2;
                        else if(s1.equals("mailto")) to = s2;
                        else if(s1.equals("subject")) subject = s2;
                        else if(s1.equals("content"))content = s2;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }







        //MyUpload.getRequest().getParameter


        String name = (String) session.getAttribute("ACCOUNT");
        if(name==null) {
            try {
                response.sendRedirect("files/main/dyna-menu.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            emadrs = getEmadrs(name);
            pwd = getPwd(name);
            String seradd = getSeradd(emadrs);
            MailSend ms = new MailSend(emadrs, seradd,  pwd, to,  subject, content, request, response);
            if(fileName != null && !fileName.equals("")) {
                ms.sendAtt();
            }
            else try {
                ms.send();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ST body() {
        return templates.getInstanceOf("sendEmail");
    }

    @Override
    public ST getTitle() {
        return new ST("sendEmail page");
    }



}
