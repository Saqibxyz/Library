package org.library.components;

import org.library.users.library.Book;
import org.library.users.library.Library;
import org.library.users.student.Student;

public class Borrow {

    public static void borrowBook(Library library, Student student, String bookId) throws CustomException {
        Book book = library.findBookById(bookId);

        if (book == null) {
            throw new CustomException("Book with ID '" + bookId + "' not found.");
        }
        if (!book.isAvailable()) {
            throw new CustomException("Book \"" + book.getTitle() + "\" is currently not available.");
        }
        if (!student.canBorrow()) {
            throw new CustomException("Borrow limit reached for student " + student.getName());
        }

        book.setAvailable(false);
        student.borrowOne(); // increment borrow count if limit not reached
        Print.success("Book borrowed successfully: " + book.getTitle());
    }

}
