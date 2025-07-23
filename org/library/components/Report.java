// refactored, bug free
package org.library.components;

import org.library.users.library.Book;
import org.library.users.library.Library;

import java.lang.reflect.Field;
import java.util.List;

public class Report {
    public static void generate(Library library) {
        List<Book> books = library.getAllBooks();
        if (books.isEmpty()) {
            Print.warning("No books available to generate report.");
            return;
        }
        Print.header("Admin Report: Book Inventory");
        for (Book book : books) {
            System.out.println("Book ID: " + book.getId());
            printBookFields(book);
            System.out.println("-------------------------");
        }
        Print.success("Report generation complete.");
    }
    private static void printBookFields(Book book) {
        Field[] fields = book.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(book);
                System.out.println(field.getName() + ": " + value);
            } catch (IllegalAccessException e) {
                System.out.println(field.getName() + ": <access denied>");
            }
        }
    }
}
