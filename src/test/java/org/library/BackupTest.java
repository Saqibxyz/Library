package org.library;

import org.junit.jupiter.api.Test;
import org.library.disk.Backup;
import org.library.users.library.Book;
import org.library.users.library.Library;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BackupTest {

    @Test
    void testBackupStartAndStop() throws InterruptedException {
        Library library = new Library();
        Book book = new Book("B001", "Java Basics", "Alice");
        library.setBooks(List.of(book));

        String testFile = "test_backup_library.json";
        Backup backup = new Backup(library, testFile, 1); // 1-second interval
        backup.startBackup();

        Thread.sleep(2200); // Let it backup at least once
        backup.stopBackup();

        File file = new File(testFile);
        assertTrue(file.exists(), "Backup file should exist after backup thread runs");

        // Cleanup
        file.delete();
    }
}
