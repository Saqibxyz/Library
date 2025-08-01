// reviewed
package org.library.users.library;

import org.library.components.Print;

import java.util.*;
import java.util.stream.Collectors;

public class Library  {
    private final Map<String, Book> bookMap; // bookId → Book

    public Library() {
        this.bookMap = new HashMap<>();
    }

    public void setBooks(List<Book> books) {
        bookMap.clear();
        for (Book book : books) {
            bookMap.put(book.getId(), book);
        }
    }

    public List<Book> getBooks() {
        return new ArrayList<>(bookMap.values());
    }

    public void addBook(Book book) {
        if (bookMap.containsKey(book.getId())) {
            Print.warning("Book with ID '" + book.getId() + "' already exists.");
        } else {
            bookMap.put(book.getId(), book);
            Print.success("Book added: " + book.getTitle());
        }
    }

    public Book findBookById(String bookId) {
        return bookMap.get(bookId);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(bookMap.values());
    }

    public List<Book> filterBooksByTitle(String titlePart) {
        return bookMap.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(titlePart.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> getAvailableBooks() {
        List<Book> availableBooks = new ArrayList<>();
        for (Book book : bookMap.values()) {
            if (book.isAvailable()) {
                availableBooks.add(book);
            }
        }
        return availableBooks;
    }

    public void displayAllBooks() {
        if (bookMap.isEmpty()) {
            Print.warning("No books in the library.");
            return;
        }
        Print.header("Library Inventory");
        for (Book book : bookMap.values()) {
            System.out.println(book);
        }
    }

    public void displayAvailableBooks() {
        List<Book> available = getAvailableBooks();
        if (available.isEmpty()) {
            Print.warning("No books available at the moment.");
        } else {
            Print.header("Available Books");
            for (Book book : available) {
                System.out.println(book);
            }
        }
    }

    public <T extends Book> List<T> getBooksByType(Class<T> type) {
        return bookMap.values().stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
    }
}
