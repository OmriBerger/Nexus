package io.github.omriberger.user;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UserMapper {

    public static User fromJson(String json) {
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();

        if (!root.has("data") || !root.get("data").isJsonObject()) {
            throw new IllegalArgumentException("JSON missing 'data' field");
        }

        JsonObject data = root.getAsJsonObject("data");

        Log.d("UserMapper", "Mapped");
        return new User(
                getString(data, "id"),
                getString(data, "userId"),
                getString(data, "schoolName"),
                getString(data, "firstName"),
                getString(data, "lastName"),
                getString(data, "studentEmail"),
                getBoolean(data, "studentGender"),
                getInt(data, "institutionCode"),
                getString(data, "classCode"),
                getInt(data, "classNumber"),
                getString(data, "token"),
                getString(data, "cellphone")
        );
    }

    // ---- Safe helpers ----
    private static String getString(JsonObject obj, String key) {
        JsonElement el = obj.get(key);
        if (el == null || el.isJsonNull()) {
            throw new IllegalArgumentException("Missing string field: " + key);
        }
        return el.getAsString();
    }

    private static boolean getBoolean(JsonObject obj, String key) {
        JsonElement el = obj.get(key);
        if (el == null || el.isJsonNull()) {
            throw new IllegalArgumentException("Missing boolean field: " + key);
        }
        return el.getAsBoolean();
    }

    private static int getInt(JsonObject obj, String key) {
        JsonElement el = obj.get(key);
        if (el == null || el.isJsonNull()) {
            throw new IllegalArgumentException("Missing int field: " + key);
        }
        return el.getAsInt();
    }
}
