//// reviewed

package org.library.users.library;

import org.library.components.Report;
import org.library.users.User;

public class Librarian extends User {

    public Librarian(String userId, String name, String password) {
        super(userId, name, password);
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
