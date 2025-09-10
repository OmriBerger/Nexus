package io.github.omriberger.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import java.io.IOException;

import io.github.omriberger.BuildConfig;
import lombok.Getter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Getter
public class VersionInfo {
    private String downloadUrl;
    private int version;
    private String releaseNotes;

    private static final String PREFS_NAME = "update_prefs";
    private static final String KEY_LAST_LATER = "last_later";

    /**
     * Checks for updates and shows a Material 3 dialog.
     * If the user taps "Later", it won't show again for the specified delay in days.
     *
     * @param activity The activity to show the dialog on.
     * @param remindLaterDelayDays Delay before showing again if "Later" is tapped.
     */
    public void checkForUpdates(AppCompatActivity activity, int remindLaterDelayDays) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long lastLater = prefs.getLong(KEY_LAST_LATER, 0);
        long now = System.currentTimeMillis();
        long delayMillis = remindLaterDelayDays * 24L * 60L * 60L * 1000L;

        // Skip check if user recently tapped "Later"
        if (now - lastLater < delayMillis) return;

        OkHttpClient client = new OkHttpClient();
        String url = "https://raw.githubusercontent.com/OmriBerger/Nexus/refs/heads/master/app/src/main/res/raw/version.json";

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace(); // fail silently
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) return;

                String json = response.body().string();
                Gson gson = new Gson();
                VersionInfo versionInfo = gson.fromJson(json, VersionInfo.class);

                if (versionInfo != null && BuildConfig.VERSION_CODE < versionInfo.getVersion()) {
                    activity.runOnUiThread(() -> new MaterialAlertDialogBuilder(activity)
                            .setTitle("Update Available")
                            .setMessage(versionInfo.getReleaseNotes() != null
                                    ? versionInfo.getReleaseNotes()
                                    : "A new version is available.")
                            .setPositiveButton("Download", (dialog, which) -> {
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(versionInfo.getDownloadUrl()));
                                activity.startActivity(intent);
                            })
                            .setNegativeButton("Later", (dialog, which) -> {
                                // Save the timestamp of when user tapped "Later"
                                prefs.edit().putLong(KEY_LAST_LATER, System.currentTimeMillis()).apply();
                            })
                            .setCancelable(false)
                            .show());
                }
            }
        });
    }
}
