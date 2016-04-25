package cs601.webmail.pages;

import cs601.webmail.misc.VerifyException;
import org.stringtemplate.v4.ST;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by xyz on 11/25/14.
 */
public class DownLoad extends Page{
    public DownLoad(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public void verify() throws VerifyException, SQLException, IOException {

        String fileName = request.getParameter("fileName");
        String url = request.getParameter("url");
        url = url.replace("files", "SC");
        File file = new File(url);
        //File file = new File(dirPath, fileName);

        long fileLength = file.length();
        String length = String.valueOf(fileLength);
        //
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename="
                + fileName);

        response.setHeader("Content_Length", length);
        FileInputStream input = null;
        ServletOutputStream output = null;
        try {
            //
            input = new FileInputStream(file);
            //output = response.getOutputStream();
            byte[] block = new byte[1024000];
            int b1 = 0;
            //start downloading
            while ((b1 = input.read()) != -1) {
                //System.out.println(b1);
                out.write(b1);
            }
            out.flush();

        } catch (IOException e) {
            System.out.println("down failed：" + e.getMessage());

        } finally {
            //
            try {
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
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
