
package org.library.users.library;

public class EBook extends Book {
    private final String downloadLink;

    public EBook(String id, String title, String author, String downloadLink) {
        super(id, title, author);
        this.downloadLink = downloadLink;
    }

    public String getDownloadLink() {
        return downloadLink;
    }


    @Override
    public String toString() {
        return super.toString() + " [EBook: Download at " + downloadLink + "]";
    }
}
