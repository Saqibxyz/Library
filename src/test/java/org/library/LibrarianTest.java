package org.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.users.library.Book;
import org.library.users.library.Librarian;
import org.library.users.library.Library;

import static org.junit.jupiter.api.Assertions.*;

public class LibrarianTest {

    private Librarian librarian;
    private Library library;

    @BeforeEach
    void setUp() {
        librarian = new Librarian("lib123", "Admin");
        library = new Library();
    }

    @Test
    void testGetRole() {
        assertEquals("Librarian", librarian.getRole());
    }

    @Test
    void testToStringOverride() {
        String expected = "Admin (" + librarian.getRole() + ")";
        assertTrue(librarian.toString().contains("Admin"));
        assertTrue(librarian.toString().contains("Librarian"));
    }

    @Test
    void testGenerateReport() {
        // Add dummy book so report prints something
        Book book = new Book("B001", "Java", "Author");
        library.addBook(book);

        // Just making sure it runs without error
        librarian.generateReport(library);
    }
}
