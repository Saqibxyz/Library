// updated after reviewing
package org.library;

import org.library.components.*;
import org.library.disk.BackupLibrary;
import org.library.disk.BackupUsers;
import org.library.disk.Sync;
import org.library.disk.SyncData;
import org.library.users.*;
import org.library.users.library.Book;
import org.library.users.library.EBook;
import org.library.users.library.Librarian;
import org.library.users.library.Library;
import org.library.users.student.Student;

import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Library library = Sync.safeLoadFromDisk("library.json");
    private static final Map<String, User> users = SyncData.loadUsers();
    private static final BackupLibrary backup = new BackupLibrary(library, "library.json", 30);
    public static BackupUsers backupUsers=new BackupUsers(users,30);
    public static void main(String[] args) {
        backup.startBackup();
        backupUsers.startBackup();
        Print.header("Library Management System");


            while(true) {

                Print.bold("\n1. Login\n2. Register\n3. Forgot ID?\n4. Exit");
                Print.println("Choose an option:");
                int choice = getIntInput();

                switch (choice) {
                    case 1 -> login();
                    case 2 -> register();
                    case 3 -> recoverId();
                    case 4 -> exit();
                    default -> Print.error("Invalid choice. Try again.");
                }

            }

    }

    private static void login() {

        String userId = getStringInput("Enter your User ID");
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

    String name = getStringInput("Enter your name");
    String password = getStringInput("Create a password");
    Print.println("Register as:\n1. Librarian\n2. Student");
    int role = getIntInput();

    String userId = UUID.randomUUID().toString().substring(0,8);
    User user;

    switch (role) {
        case 1 -> user = new Librarian(userId, name, password);
        case 2 -> user = new Student(userId, name, password, 2);
        default -> {
            Print.error("Invalid role selected.");
            return;
        }
    }

    users.put(userId, user);
    SyncData.saveUsers(users);
    Print.success("Registered successfully! Your ID: " + userId);
}

    private static void librarianMenu(Librarian librarian) {
        while (true) {
            Print.header("Librarian Menu");
            Print.bold("""
                1. Add Book
                2. View All Books
                3. Filter Books by Title
                4. Generate Report
                5. View EBooks Only
                6. Logout
                """);

            Print.println("Choose an option: ");
            int choice = getIntInput();

            switch (choice) {
                case 1 -> {
                    Print.println("Add as:\n1. Physical Book\n2. EBook");
                    int type = getIntInput();
                    if (type != 1 && type != 2) {
                        Print.error("Invalid book type.");
                        break;
                    }



                    String  title = getStringInput("Enter Book Title");
                    String author = getStringInput("Enter Author");
                    String bookId = UUID.randomUUID().toString().substring(0, 6);
                    Book book;

                    if (type == 2) {
                        String link=getStringInput("Enter Book link");
                        book = new EBook(bookId, title, author, link);
                    } else {
                        book = new Book(bookId, title, author);
                    }

                    Sync.safeAddBook(library, book);
                    Print.success("Book added successfully.");
                }

                case 2 -> library.displayAllBooks();

                case 3 -> {

                    String titlePart =getStringInput("Enter part of title to search");
                    var matches = library.filterBooksByTitle(titlePart);
                    if (matches.isEmpty()) {
                        Print.info("No matching books found.");
                    } else {
                        Print.info("Books matching \"" + titlePart + "\":");
                        matches.forEach(book -> Print.println(book.toString()));
                    }
                }

                case 4 -> generateReflectionReport();

                case 5 -> {
                    List<EBook> ebooks = library.getBooksByType(EBook.class);
                    if (ebooks.isEmpty()) {
                        Print.info("No EBooks found.");
                    } else {
                        ebooks.forEach(book -> Print.println(book.toString()));
                    }
                }

                case 6 -> {
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

        Print.println("Choose an option: ");
        int choice = getIntInput();

        switch (choice) {
            case 1 -> library.displayAllBooks();

            case 2 -> library.displayAvailableBooks();

            case 3 -> {

                String bookId = getStringInput("Enter Book ID to borrow");
                new Thread(() -> {
                    synchronized (library) {
                        try {
                            Borrow.borrowBook(library, student, bookId);
                        } catch (CustomException e) {
                            Print.error(e.getMessage());
                        }
                    }
                }).start();
            }

            case 4 -> {
                if (!student.canReturn()) {
                    Print.info("No books borrowed.");
                } else {
                    String bookId = getStringInput("Enter Book ID to return");
                    new Thread(() -> {
                        synchronized (library) {
                            try {
                                Return.returnBook(student, library, bookId);
                            } catch (InvalidReturnException e) {
                                Print.error(e.getMessage());
                            }
                        }
                    }).start();
                }
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
        backupUsers.stopBackup();
        System.exit(0);
    }

    private static int getIntInput() {
        while (true)
        {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                Print.error("Invalid input. Please enter a number.");
            }
        }

    }

    private static void generateReflectionReport() {
        List<Book> books = library.getAllBooks();
        Print.header("Library Report:");

        for (Book book : books) {
            Print.println("\nBook Details:");
            for (var field : book.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Print.println(field.getName() + ": " + field.get(book));
                } catch (IllegalAccessException e) {
                    Print.error("Error accessing field: " + field.getName());
                }
            }

            System.out.println("----------------------------------------");
        }
    }
    public static String getStringInput(String promptMessage) {
        while (true) {
            System.out.print(promptMessage + ": ");
            String input = scanner.nextLine();
            if (input == null || input.trim().isEmpty()) {
                Print.error("Input cannot be empty. Please try again.");
                continue;
            }
            input = input.trim();
            return input;
        }
    }
    private static void recoverId() {
        Print.println("Enter your name:");
        String name = scanner.nextLine().trim();
        if(name.isEmpty())
        {
            Print.error("Name Cannot be empty");
            return;
        }

        Print.println("Enter your password:");
        String password = scanner.nextLine().trim();
        if(password.isEmpty())
        {
            Print.error("Enter valid password");
            return;
        }
        Optional<User> matched = users.values().stream()
                .filter(u -> u.getName().equalsIgnoreCase(name) && u.verifyPassword(password))
                .findFirst();

        if (matched.isEmpty()) {
            Print.error("No matching user found or incorrect password.");
        } else {
            Print.success("");
            System.out.printf(Print.GREEN+"Your ID(s):"+Print.RESET);
            System.out.printf(Print.BLUE+matched.get().getUserId()+Print.RESET);
        }
    }

}
