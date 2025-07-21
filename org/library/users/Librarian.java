package org.library;

public class Librarian extends User {

    public Librarian(String userId, String name) {
        super(userId, name);
    }
    @Override
    public String getRole() {
        return "Librarian";
    }
    public void generateReport(Library library) {
        Report.generate(library);
    }
    @Override
    public String toString() {
        return super.toString();
    }
}
