package org.library;

import org.junit.jupiter.api.*;
import org.library.disk.Sync;
import org.library.users.library.Book;
import org.library.users.library.Library;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SyncTest {

    private static final String TEST_FILE = "test_library.json";

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    @Test
    void testSaveAndLoadLibrary() {
        Library library = new Library();
        Book book = new Book("B001", "Effective Java", "Joshua Bloch");
        library.setBooks(List.of(book));

        Sync.saveLibrary(library, TEST_FILE);
        assertTrue(new File(TEST_FILE).exists());

        Library loaded = Sync.loadLibrary(TEST_FILE);
        assertEquals(1, loaded.getBooks().size());
        assertEquals("Effective Java", loaded.getBooks().get(0).getTitle());
    }

    @Test
    void testSafeAddBook() {
        Library library = new Library();
        Book book = new Book("B002", "Clean Code", "Robert C. Martin");

        Sync.safeAddBook(library, book);

        assertTrue(new File("library.json").exists());
        assertEquals(1, library.getBooks().size());

        new File("library.json").delete();
    }
}
