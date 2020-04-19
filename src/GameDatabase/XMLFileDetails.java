package GameDatabase;

/**
 * @author WenhanWei
 * @date 2020/3/24 - 2020
 */
public class XMLFileDetails {

    private String author;
    private String filename;
    private String levelFile = null;

    public XMLFileDetails() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "XMLFileDetails{" +
                "author='" + author + '\'' +
                ", filename='" + filename + '\'' +
                '}';
    }
}
