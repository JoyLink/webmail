package cs601.webmail.entities;

/**
 * Created by xyz on 11/19/14.
 */
public class Attachment {
    private int SIZE = 1024*10*1000;

    private String attachmentName = null;
    private byte[] attach = null;
    private String pos;

    public Attachment(String amn, byte[] att, String pos) {
        this.attachmentName = amn;
        this.attach = att;
        this.pos = pos;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public byte[] getAttach() {
        return attach;
    }

    public String getPos() { return  pos;}
}
