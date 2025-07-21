
package org.library;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class Sync {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void saveLibrary(Library library, String fileName) {
        try (Writer writer = new FileWriter(fileName)) {
            gson.toJson(library.getBooks(), writer);
        } catch (IOException e) {
            Print.error("Failed to save library: " + e.getMessage());
        }
    }
    public static Library loadLibrary(String fileName) {
        try (Reader reader = new FileReader(fileName)) {
            Type bookListType = new TypeToken<List<Book>>() {}.getType();
            List<Book> books = gson.fromJson(reader, bookListType);
            Library library = new Library();
            library.setBooks(books);
            return library;
        } catch (FileNotFoundException e) {
            Print.info("Library file not found. Starting fresh.");
            return new Library();
        } catch (IOException e) {
            Print.error("Failed to load library: " + e.getMessage());
            return new Library();
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
