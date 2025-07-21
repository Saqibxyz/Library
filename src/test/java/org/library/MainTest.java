package org.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.users.User;
import org.library.users.library.Librarian;
import org.library.users.student.Student;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    private Map<String, User> testUsers;

    @BeforeEach
    void setUp() {
        testUsers = new HashMap<>();
    }

    @Test
    void testStudentRegistration() {
        String name = "Saqib";
        String userId = UUID.randomUUID().toString();
        Student student = new Student(userId, name, 2);
        testUsers.put(userId, student);

        assertEquals("Saqib", testUsers.get(userId).getName());
        assertEquals("Student", testUsers.get(userId).getRole());
    }

    @Test
    void testLibrarianRegistration() {
        String name = "Admin";
        String userId = UUID.randomUUID().toString();
        Librarian librarian = new Librarian(userId, name);
        testUsers.put(userId, librarian);

        assertEquals("Admin", testUsers.get(userId).getName());
        assertEquals("Librarian", testUsers.get(userId).getRole());
    }

    @Test
    void testUserLoginSuccess() {
        String userId = "test123";
        User mockUser = new Student(userId, "John", 2);
        testUsers.put(userId, mockUser);

        assertTrue(testUsers.containsKey("test123"));
        assertEquals("John", testUsers.get("test123").getName());
    }

    @Test
    void testUserLoginFail() {
        String userId = "notExist";
        assertFalse(testUsers.containsKey(userId));
    }
    @Test
    void testExitMethodCoverage() {


    }
}
