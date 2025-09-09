package io.github.omriberger.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import io.github.omriberger.R;

public class NotificationHelper {

    private static final String CHANNEL_ID = "schedule_channel";
    private static final String CHANNEL_NAME = "Schedule Updates";

    public static void sendNotification(Context context, String title, String message) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
        );
        manager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_event_note)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);

        // Show notification (use a unique ID each time or constant if overwriting is fine)
        manager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
