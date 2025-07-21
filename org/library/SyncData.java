package org.library;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SyncData {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String USERS_FILE = "users.json";

    public static Map<String, User> loadUsers() {
        try (Reader reader = new FileReader(USERS_FILE)) {
            Type userMapType = new TypeToken<Map<String, User>>() {}.getType();
            return gson.fromJson(reader, userMapType);
        } catch (FileNotFoundException e) {
            Print.info("No users found. Starting fresh.");
            return new HashMap<>();
        } catch (IOException e) {
            Print.error("Failed to load users: " + e.getMessage());
            return new HashMap<>();
        }
    }

    public static void saveUsers(Map<String, User> users) {
        try (Writer writer = new FileWriter(USERS_FILE)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            Print.error("Failed to save users: " + e.getMessage());
        }
    }
}
