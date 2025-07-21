package org.library.components;

import org.library.users.library.Book;
import org.library.users.library.Library;
import org.library.users.student.Student;

public class Return {
    public static void returnBook(Student student, Library library, String bookId) {
        Book book = library.findBookById(bookId);
        if (book == null) {
            Print.error("Book not found.");
            return;
        }
        if (book.isAvailable()) {
            Print.warning("Book is already available in library.");
            return;
        }
        book.setAvailable(true);
        student.returnOne();
        Print.success("Book returned: " + book.getTitle());
    }

}
