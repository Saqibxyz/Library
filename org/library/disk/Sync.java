// updated
package org.library.disk;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.library.users.library.Book;
import org.library.users.library.Library;
import org.library.components.Print;
import org.library.components.CustomException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Sync {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
// reviewed
    public static void saveLibrary(Library library, String fileName) {
        try (Writer writer = new FileWriter(fileName)) {
            gson.toJson(library.getBooks(), writer);
        } catch (IOException e) {
            Print.error("Failed to save library: " + e.getMessage());
        }
    }
    // reviewed and corrected
    public static Library loadLibrary(String fileName) {
        try {
            FileReader fileReader = new FileReader(fileName);
            Reader reader = new BufferedReader(fileReader);

            Type bookListType = new TypeToken<List<Book>>() {}.getType();
            List<Book> books = gson.fromJson(reader, bookListType);

            if (books == null) {
                books = new ArrayList<>();
            }

            Library library = new Library();
            library.setBooks(books);
            return library;

        } catch (FileNotFoundException e) {
            Print.info("Library file not found. Starting fresh.");
            return new Library();

        } catch (Exception e) {
            try {
                throw new CustomException("Failed to load library: " + e.getMessage());
            } catch (CustomException ex) {
                Print.error(ex.getMessage());
                return new Library();
            }
        }
    }




    public static void safeAddBook(Library library, Book book) {
        synchronized (library) {
            library.addBook(book);
            saveLibrary(library, "library.json");
        }
    }

    public static void safeSaveToDisk(Library library, String fileName) {
        synchronized (library) {
            saveLibrary(library, fileName);
        }
    }

    public static Library safeLoadFromDisk(String fileName) {
        return loadLibrary(fileName);
    }
}
