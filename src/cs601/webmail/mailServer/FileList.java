package cs601.webmail.mailServer;

import cs601.webmail.entities.AttachmentFile;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by xyz on 11/23/14.
 */
public class FileList {
    public static ArrayList<AttachmentFile> getFileList(String id) {
        ArrayList<AttachmentFile> filelist = new ArrayList<AttachmentFile>();
        String filePath = "SC/"+id+"/attachment/";
        String filePath2 = "files/"+id+"/attachment/";
            File root = new File(filePath);
            File[] files = root.listFiles();
            for(File file:files){
                String name = file.getName();
                if(name.startsWith(".")) continue;
                AttachmentFile af = new AttachmentFile(filePath2+name, name);
                filelist.add(af);
            }
        return filelist;
        }
//    public  static void main(String args[]) {
//        getFileList("GmailId149dc4e63a79be58");
//    }
}

