package cs601.webmail.entities;

/**
 * Created by xyz on 11/23/14.
 */
public class AttachmentFile {
    private String url;
    private String name;

    public String getName() {
        return name;
    }

    public AttachmentFile(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }
}
