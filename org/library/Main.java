package org.library;
import org.library.components.Borrow;
import org.library.components.Print;
import org.library.components.Return;
import org.library.disk.Backup;
import org.library.disk.Sync;
import org.library.disk.SyncData;
import org.library.users.*;
import org.library.users.library.Book;
import org.library.users.library.Librarian;
import org.library.users.library.Library;
import org.library.users.student.Student;

import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Library library = Sync.safeLoadFromDisk("library.json");
    private static final Map<String, User> users = SyncData.loadUsers();
    private static final Backup backup = new Backup(library, "library.json", 30);
    public static void main(String[] args) {

        backup.startBackup();

        Print.header("Library Management System");

        while (true) {
            Print.bold("\n1. Login\n2. Register\n3. Exit");
            Print.println("Choose an option: ");
            int choice = getIntInput();

            switch (choice) {
                case 1 -> login();
                case 2 -> register();
                case 3 -> exit();
                default -> Print.error("Invalid choice. Try again.");
            }
        }
    }

    private static void login() {
        Print.println("Enter User ID:");
        String userId = scanner.nextLine().trim();

        User user = users.get(userId);
        if (user == null) {
            Print.error("User not found.");
            return;
        }

        Print.success("Welcome " + user.getName() + " (" + user.getRole() + ")");

        if (user instanceof Librarian librarian) {
            librarianMenu(librarian);
        } else if (user instanceof Student student) {
            studentMenu(student);
        }
    }

    private static void register() {
        Print.println("Enter your name:");
        String name = scanner.nextLine().trim();

        Print.println("Register as:\n1. Librarian\n2. Student");
        int role = getIntInput();

        String userId = UUID.randomUUID().toString();
        User user;

        switch (role) {
            case 1 -> user = new Librarian(userId, name);
            case 2 -> user = new Student(userId, name,2);
            default -> {
                Print.error("Invalid role selected.");
                return;
            }
        }

        users.put(userId, user);
        SyncData.saveUsers(users);
        Print.success("Registered successfully. Your ID: " + userId);
    }

    private static void librarianMenu(Librarian librarian) {
        while (true) {
            Print.header("Librarian Menu");
            Print.bold("""
                1. Add Book
                2. View All Books
                3. Filter Books by Title
                4. Generate Report
                5. Logout
            """);

            Print.println("Choose an option:");
            int choice = getIntInput();

            switch (choice) {
                case 1 -> {
                    Print.println("Enter Book Title:");
                    String title = scanner.nextLine().trim();
                    Print.println("Enter Author:");
                    String author = scanner.nextLine().trim();

                    String bookId = UUID.randomUUID().toString().substring(0, 6);
                    Book book = new Book(bookId, title, author);

                    Sync.safeAddBook(library, book);
                    Print.success("Book added successfully.");
                }
                case 2 -> library.displayAllBooks();
                case 3 -> {
                    Print.println("Enter part of title to search:");
                    String titlePart = scanner.nextLine().trim();

                    var matches = library.filterBooksByTitle(titlePart);
                    Print.info("Books matching \"" + titlePart + "\":");
                    Print.info(matches.toString());
                }
                case 4 -> librarian.generateReport(library);
                case 5 -> {
                    Sync.safeSaveToDisk(library, "library.json");
                    return;
                }
                default -> Print.error("Invalid choice.");
            }
        }
    }

    private static void studentMenu(Student student) {
        while (true) {
            Print.header("Student Menu");
            Print.bold("""
                1. View All Books
                2. View Available Books
                3. Borrow Book
                4. Return Book
                5. Logout
            """);

            Print.println("Choose an option:");
            int choice = getIntInput();

            switch (choice) {
                case 1 -> library.displayAllBooks();
                case 2 -> library.displayAvailableBooks();
                case 3 -> {
                    Print.println("Enter Book ID to borrow:");
                    String bookId = scanner.nextLine().trim();

                    new Thread(() -> {
                        synchronized (library) {
                            try {
                                Borrow.borrowBook(library, student, bookId);
                            } catch (IllegalStateException e) {
                                Print.error(e.getMessage());
                            }
                        }
                    }).start();
                }
                case 4 -> {
                    Print.println("Enter Book ID to return:");
                    String bookId = scanner.nextLine().trim();

                    new Thread(() -> {
                        synchronized (library) { // can we remove synchronisation here?
                            try {
                                Return.returnBook(student, library, bookId);
                            } catch (IllegalStateException e) {
                                Print.error(e.getMessage());
                            }
                        }
                    }).start();
                }
                case 5 -> {
                    Sync.safeSaveToDisk(library, "library.json");
                    return;
                }
                default -> Print.error("Invalid choice.");
            }
        }
    }

    private static void exit() {
        Print.success("Saving data and exiting...");
        Sync.safeSaveToDisk(library, "library.json");
        SyncData.saveUsers(users);
        backup.stopBackup();
        System.exit(0);
    }

    private static int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            Print.error("Invalid input. Please enter a number.");
            return -1;
        }
    }
}
