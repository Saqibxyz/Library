// reviewed
package org.library.components;
import org.library.users.library.Book;
import org.library.users.library.Library;
import org.library.users.student.Student;

public class Return {
    public static void returnBook(Student student, Library library, String bookId) throws InvalidReturnException {
        Book book = library.findBookById(bookId);

        if (book == null) {
            throw new InvalidReturnException("Book not found: " + bookId);
        }

        if (book.isAvailable()) {
            throw new InvalidReturnException("Book is already marked as available. Possible double return attempt.");
        }

        book.setAvailable(true);
        student.returnOne();
        Print.success("Book returned: " + book.getTitle());
    }


}
