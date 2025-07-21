package org.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.components.Borrow;
import org.library.components.Report;
import org.library.components.Return;
import org.library.users.library.Book;
import org.library.users.library.Library;
import org.library.users.student.Student;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryComponentTest {

    private Library library;
    private Student student;
    private Book book;

    @BeforeEach
    void setUp() {
        library = new Library();
        student = new Student("stu123", "Saqib", 2);
        book = new Book("B001", "Java Basics", "James Gosling");

        library.addBook(book);  // Adds to map
    }

    @Test
    void testBorrowBookSuccess() {
        assertTrue(book.isAvailable());
        assertFalse(student.canBorrow());

        Borrow.borrowBook(library, student, book.getId());

        assertFalse(book.isAvailable()); // should now be false
    }

    @Test
    void testBorrowBookNotFound() {
        Borrow.borrowBook(library, student, "INVALID_ID");
        // No exception thrown = pass
    }

    @Test
    void testBorrowLimitReached() {
        student.borrowOne(); // 1
        student.borrowOne(); // 2 -> limit reached (custom limit 2)
        assertTrue(student.canBorrow());

        Borrow.borrowBook(library, student, book.getId());
        assertTrue(book.isAvailable()); // book should NOT be borrowed
    }

    @Test
    void testReturnBookSuccess() {
        // Borrow first
        Borrow.borrowBook(library, student, book.getId());
        assertFalse(book.isAvailable());

        // Return now
        Return.returnBook(student, library, book.getId());
        assertTrue(book.isAvailable());
    }

    @Test
    void testReturnBookAlreadyAvailable() {
        Return.returnBook(student, library, book.getId());
        assertTrue(book.isAvailable());
    }

    @Test
    void testReturnInvalidBookId() {
        Return.returnBook(student, library, "XYZ999");
        // No crash = success
    }

    @Test
    void testReportGeneration() {
        Report.generate(library);  // Visually prints info
        // No assertion — just checking it runs without crash
    }
}
