package io.github.omriberger.schedule;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleWorker extends Worker {

    private final Context context;

    public ScheduleWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        ScheduleRepository repository = new ScheduleRepository(context);
        Gson gson = repository.gson;

        try {
            // Fetch new schedule
            String newScheduleJson = repository.getRawScheduleJson();
            ScheduleData.WeeklySchedule newSchedule = gson.fromJson(newScheduleJson, ScheduleData.WeeklySchedule.class);

            normalizeScheduleDays(newSchedule); // Make Sunday = 0

            String oldScheduleJson = repository.cachedSchedule != null
                    ? gson.toJson(repository.cachedSchedule)
                    : null;

            if (oldScheduleJson == null || !oldScheduleJson.equals(newScheduleJson)) {
                repository.cachedSchedule = newSchedule;
                repository.lastFetchTime = System.currentTimeMillis();

                // Collect changes, events, exams
                ScheduleMeta meta = collectMeta(newSchedule);
                List<ScheduleChange> readableChanges = convertToReadableChanges(meta);

                // Build notification message
                StringBuilder notificationMessage = new StringBuilder();
                if (!readableChanges.isEmpty()) {
                    for (ScheduleChange c : readableChanges) {
                        notificationMessage.append(c.toString()).append("\n");
                    }
                } else {
                    notificationMessage.append("Schedule updated.\nNo human-readable changes detected.");
                }

//                // Append raw JSON for debugging
//                Gson prettyGson = new Gson();
//                notificationMessage.append("\n\nRaw JSON:\n")
//                        .append(prettyGson.toJson(meta));
//
//                NotificationHelper.sendNotification(getApplicationContext(),
//                        "Schedule Updated",
//                        notificationMessage.toString()); //TODO: FIX CHANGES CHECK
            }

            return Result.success();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.retry();
        }
    }

    private void normalizeScheduleDays(ScheduleData.WeeklySchedule schedule) {
        if (schedule == null || schedule.data == null) return;
        for (ScheduleData.DaySchedule day : schedule.data) {
            day.dayIndex = day.dayIndex - 1; // JSON: 1 = Sunday → 0
        }
    }

    private ScheduleMeta collectMeta(ScheduleData.WeeklySchedule schedule) {
        ScheduleMeta meta = new ScheduleMeta();
        if (schedule == null || schedule.data == null) return meta;

        for (ScheduleData.DaySchedule day : schedule.data) {
            if (day.hoursData == null) continue;

            for (ScheduleData.HourData hour : day.hoursData) {
                if (hour.scheduale != null) {
                    for (ScheduleData.Lesson lesson : hour.scheduale) {
                        if (lesson.changes != null && !lesson.changes.isEmpty()) {
                            meta.changes.addAll(lesson.changes);
                        }
                    }
                }

                if (hour.events != null && !hour.events.isEmpty()) {
                    meta.events.addAll(hour.events);
                }

                if (hour.exams != null && !hour.exams.isEmpty()) {
                    meta.exams.addAll(hour.exams);
                }
            }
        }

        return meta;
    }

    private List<ScheduleChange> convertToReadableChanges(ScheduleMeta meta) {
        List<ScheduleChange> list = new ArrayList<>();
        if (meta.changes != null) {
            for (ScheduleData.Change c : meta.changes) {
                // Example mapping; adjust when Change fields are known
                list.add(new ScheduleChange(
                        0, // dayIndex unknown in Change
                        "unknown", // hourName unknown
                        "unknown", // subject
                        "unknown", // room
                        "unknown", // teacher
                        "Change detected"
                ));
            }
        }
        if (meta.events != null) {
            for (ScheduleData.Event e : meta.events) {
                list.add(new ScheduleChange(
                        0,
                        "unknown",
                        "Event",
                        "unknown",
                        "unknown",
                        "New event detected"
                ));
            }
        }
        if (meta.exams != null) {
            for (ScheduleData.Exam ex : meta.exams) {
                list.add(new ScheduleChange(
                        0,
                        ex.date + " " + ex.from_hour + "-" + ex.to_hour,
                        ex.title,
                        ex.rooms != null ? ex.rooms : "unknown",
                        ex.supervisors != null ? ex.supervisors : "unknown",
                        "Exam detected"
                ));
            }
        }
        return list;
    }

    // Helper class to collect metadata
    public static class ScheduleMeta {
        public List<ScheduleData.Change> changes = new ArrayList<>();
        public List<ScheduleData.Event> events = new ArrayList<>();
        public List<ScheduleData.Exam> exams = new ArrayList<>();
    }
}
