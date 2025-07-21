package org.library;

import org.library.users.Library;
import org.library.users.Student;

public class Borrow {

    public static void borrowBook(Library library, Student student, String bookId) {
        Book book = library.findBookById(bookId);
        if (book == null) {
            Print.error("Book with ID '" + bookId + "' not found.");
            return;
        }
        if (!book.isAvailable()) {
            Print.warning("Book \"" + book.getTitle() + "\" is currently not available.");
            return;
        }
        if (student.canBorrow()) {
            Print.warning("Borrow limit reached for student " + student.getName());
            return;
        }
        book.setAvailable(false);
        student.borrowOne();
        Print.success("Book borrowed successfully: " + book.getTitle());
    }
}
