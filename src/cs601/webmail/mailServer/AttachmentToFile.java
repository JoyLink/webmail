package cs601.webmail.mailServer;

import cs601.webmail.db.JDBCCLASS;
import cs601.webmail.entities.Attachment;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by xyz on 11/21/14.
 */
public class AttachmentToFile {

    public static void Move(String id) throws Exception {

        String rount = "SC/"+id+"/";
        String rount2 = "SC/"+id+"/attachment";
        File file = new File(rount);
        file.mkdir();
        file = new File(rount2);
        file.mkdir();
        System.out.println("hi");
        JDBCCLASS.CONNECTION();
        ArrayList<Attachment> arrayList = JDBCCLASS.GETATTACHMENTSBYID(id);
        int len = arrayList.size();
        for(int i=0; i<len; i++) {
            Attachment a = arrayList.get(i);
            String filename = null;
            if(a.getPos().equals("1")) {
                filename = rount + a.getAttachmentName();
            }
            else {
                filename = rount + "attachment/" + a.getAttachmentName();
            }
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            InputStream is = null;
            BufferedInputStream bis = null;
            try {
                file = new File(filename);
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);
                is = new ByteArrayInputStream(arrayList.get(i).getAttach());
                bis = new BufferedInputStream(is);
                int b1 = 0;
                while ((b1 = bis.read()) != -1) {
                    //System.out.println(b1);
                    bos.write(b1);
                    bos.flush();
                }
            } catch (Exception e) {
                throw e;
            } finally {
                if(is!=null){
                    is.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (bos != null) {
                    bos.close();
                }
                JDBCCLASS.CLOSE();
            }

        }
    }
//    public static void main(String args[]) throws Exception {
//        Move("GmailId149db2ec61e08e7f");
//    }
}
