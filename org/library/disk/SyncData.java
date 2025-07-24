// checked and corrected
package org.library.disk;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.library.users.User;
import org.library.users.library.Librarian;
import org.library.users.student.Student;
import org.library.components.Print;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SyncData {

    private static final String USERS_FILE = "users.json";
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(User.class, new UserAdapter())
            .setPrettyPrinting()
            .create();
// reviewed
    public static void saveUsers(Map<String, User> users, String filePath) {
        try (Writer writer = new FileWriter(filePath)) {
            JsonObject obj = new JsonObject();
            for (Map.Entry<String, User> entry : users.entrySet()) {
                JsonElement userJson = gson.toJsonTree(entry.getValue(), User.class);
                obj.add(entry.getKey(), userJson);
            }
            gson.toJson(obj, writer);
        } catch (IOException e) {
            Print.error("Failed to save users: " + e.getMessage());
        }
    }


    public static Map<String, User> loadUsers() {
        return loadUsers(USERS_FILE);
    }
//overload
    public static Map<String, User> loadUsers(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (Reader reader = new FileReader(filePath)) {
            Type type = new TypeToken<Map<String, User>>() {}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            Print.error("Failed to load users: " + e.getMessage());
            return new HashMap<>();
        }
    }
    private static class UserAdapter implements JsonSerializer<User>, JsonDeserializer<User> {

        @Override
        public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = context.serialize(src).getAsJsonObject();

            obj.addProperty("role", src.getRole());

            return obj;
        }

        @Override
        public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            JsonElement roleElement = obj.get("role");

            if (roleElement == null || roleElement.isJsonNull()) {
                throw new JsonParseException("Missing 'role' field in user JSON: " + obj);
            }

            String role = roleElement.getAsString();

            Class<? extends User> userClass = switch (role) {
                case "Student" -> Student.class;
                case "Librarian" -> Librarian.class;
                default -> throw new JsonParseException("Unknown role: " + role);
            };

            return context.deserialize(json, userClass);
        }
    }
}
