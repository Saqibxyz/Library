// reviewed
package org.library.users.library;
public class Book  {
    final private String id;
    private final String title;
    private final String author;
    private boolean available;

    public Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.available = true;
    }
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }



    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "[" + id + "] \"" + title + "\" by " + author +
                " - " + (available ? "Available" : "Not Available");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Book other)) return false;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


}
