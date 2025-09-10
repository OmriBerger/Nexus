package io.github.omriberger.user;

import android.content.Context;
import android.util.Base64;

import java.io.*;

public class UserRepository {

    private static User cachedUser;

    public static User getUser(Context context) {
        if (cachedUser != null) return cachedUser;

        String serializedUser = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .getString("user_data", null);
        if (serializedUser == null) return null;

        cachedUser = deserializeUser(serializedUser);
        return cachedUser;
    }

    public static void saveUser(Context context, User user) {
        String serializedUser = serializeUser(user);
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .edit()
                .putString("user_data", serializedUser)
                .apply();
        cachedUser = user;
    }

    public static void logout(Context context) {
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .edit()
                .remove("user_data")
                .apply();
        cachedUser = null;
    }

    private static String serializeUser(User user) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(user);
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static User deserializeUser(String serializedUser) {
        try {
            byte[] data = Base64.decode(serializedUser, Base64.DEFAULT);
            try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
                 ObjectInputStream ois = new ObjectInputStream(bais)) {
                return (User) ois.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean hasUser(Context context) {
        return getUser(context) != null;
    }
}
