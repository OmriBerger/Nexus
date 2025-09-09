package io.github.omriberger.user;

import android.content.Context;

import io.github.omriberger.utils.CryptoManager;

public class UserRepository {

    private static User cachedUser;

    public static User getUser(Context context) throws Exception {
        if (cachedUser != null) return cachedUser;

        String encrypted = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .getString("user_data", null);
        if (encrypted == null) return null;

        cachedUser = CryptoManager.decryptUser(encrypted);
        return cachedUser;
    }

    public static void saveUser(Context context, User user) throws Exception {
        String encrypted = CryptoManager.encryptUser(user);
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .edit()
                .putString("user_data", encrypted)
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

    /** Load user from SharedPreferences (decrypt) */
    public static User loadUser(Context context) {
        if (cachedUser != null) return cachedUser;

        String encrypted = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .getString("user_data", null);
        if (encrypted == null) return null;

        try {
            cachedUser = CryptoManager.decryptUser(encrypted);
            return cachedUser;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** Check if a user exists */
    public static boolean hasUser(Context context) {
        return loadUser(context) != null;
    }
}
