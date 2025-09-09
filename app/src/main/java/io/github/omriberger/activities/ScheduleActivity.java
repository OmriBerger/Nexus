package io.github.omriberger.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import io.github.omriberger.schedule.LessonAdapter;
import io.github.omriberger.R;
import io.github.omriberger.schedule.ScheduleData;
import io.github.omriberger.schedule.ScheduleRepository;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class ScheduleActivity extends AppCompatActivity {

    private static final String TAG = "ScheduleActivity";
    private final TextView[] days = new TextView[5];
    private TextView selectedDay = null;
    private RecyclerView scheduleRecycler;

    private ScheduleData.WeeklySchedule weeklySchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        Log.d(TAG, "onCreate started");
        setContentView(R.layout.activity_schedule);

        LinearLayout weekLayout = findViewById(R.id.weekLayout);
        scheduleRecycler = findViewById(R.id.scheduleRecycler);
        scheduleRecycler.setLayoutManager(new LinearLayoutManager(this));

        Locale locale = getResources().getConfiguration().getLocales().get(0);
        boolean isHebrew = locale.getLanguage().equals("he") || locale.getLanguage().equals("iw");
        if (weekLayout != null) {
            weekLayout.setLayoutDirection(isHebrew ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }

        OverScrollDecoratorHelper.setUpOverScroll(scheduleRecycler, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        initializeDays();
        setupDayClickListeners();
        loadScheduleData();

        int todayIndex = getTodayIndex();
        if (todayIndex >= 0 && todayIndex < days.length) {
            selectDay(days[todayIndex]);
        } else {
            selectDay(days[0]);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat controller =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if (controller != null) {
            controller.hide(WindowInsetsCompat.Type.systemBars());
            controller.setSystemBarsBehavior(
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            );
        }
    }

    private void initializeDays() {
        days[0] = findViewById(R.id.dayLetterSun);
        days[1] = findViewById(R.id.dayLetterMon);
        days[2] = findViewById(R.id.dayLetterTue);
        days[3] = findViewById(R.id.dayLetterWed);
        days[4] = findViewById(R.id.dayLetterThu);
    }

    private void setupDayClickListeners() {
        for (int i = 0; i < days.length; i++) {
            final int dayIndex = i;
            if (days[i] != null) {
                days[i].setOnClickListener(v -> selectDay(days[dayIndex]));
            }
        }
    }

    private void selectDay(TextView day) {
        if (day == null) return;

        if (selectedDay != null) {
            selectedDay.setSelected(false);
            selectedDay.setTextColor(getColor(R.color.md_theme_onSurface));
        }

        day.setSelected(true);
        selectedDay = day;
        day.setTextColor(Color.BLACK);

        int dayIndex = -1;
        for (int i = 0; i < days.length; i++) {
            if (days[i] == day) {
                dayIndex = i;
                break;
            }
        }
        if (dayIndex >= 0) {
            populateScheduleForDay(dayIndex);
        }
    }

    private int getTodayIndex() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return 0;
            case Calendar.MONDAY:
                return 1;
            case Calendar.TUESDAY:
                return 2;
            case Calendar.WEDNESDAY:
                return 3;
            case Calendar.THURSDAY:
                return 4;
            default:
                return -1; // Friday/Saturday
        }
    }

    private void loadScheduleData() {
        new Thread(() -> {
            try {
                // Fetch from repository (cached if recent)
                ScheduleData.WeeklySchedule schedule = new ScheduleRepository(this).getSchedule();

                runOnUiThread(() -> {
                    weeklySchedule = schedule;

                    int todayIndex = getTodayIndex();
                    if (todayIndex >= 0 && todayIndex < days.length) {
                        selectedDay = days[todayIndex];
                        selectDay(days[todayIndex]);
                    } else {
                        selectedDay = days[0];
                        selectDay(days[0]);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error fetching schedule", e);
            }
        }).start();
    }


    private void populateScheduleForDay(int dayIndex) {
        if (scheduleRecycler == null || weeklySchedule == null) return;

        ScheduleData.DaySchedule daySchedule = null;
        for (ScheduleData.DaySchedule ds : weeklySchedule.data) {
            if (ds.dayIndex == dayIndex + 1) {
                daySchedule = ds;
                break;
            }
        }

        if (daySchedule == null) {
            scheduleRecycler.setAdapter(new LessonAdapter(new ArrayList<>(), new HashMap<>()));
            return;
        }

        List<ScheduleData.Lesson> lessons = new ArrayList<>();
        Map<Integer, String> hourNamesByHour = new HashMap<>();

        for (ScheduleData.HourData hourData : daySchedule.hoursData) {
            hourNamesByHour.put(hourData.hour, hourData.hourName);

            if (hourData.scheduale != null && !hourData.scheduale.isEmpty()) {
                lessons.addAll(hourData.scheduale);
            } else {
                ScheduleData.Lesson emptyLesson = new ScheduleData.Lesson();
                emptyLesson.subject = null;
                emptyLesson.hour = hourData.hour;
                emptyLesson.roomID = -1;
                lessons.add(emptyLesson);
            }
        }

        // Merge same-hour same-subject lessons and deduplicate teachers
        Map<String, ScheduleData.Lesson> merged = new LinkedHashMap<>();
        Map<String, Set<String>> teacherMap = new HashMap<>();

        for (ScheduleData.HourData hourData : daySchedule.hoursData) {
            if (hourData.scheduale == null) continue;

            for (ScheduleData.Lesson l : hourData.scheduale) {
                String key = l.hour + "|" + (l.subject != null ? l.subject : "");
                // Clone the lesson to avoid modifying the same object across refreshes
                ScheduleData.Lesson lessonClone = new ScheduleData.Lesson();
                lessonClone.hour = l.hour;
                lessonClone.subject = l.subject;
                lessonClone.roomID = l.roomID;
                lessonClone.teacherPrivateName = l.teacherPrivateName;
                lessonClone.teacherLastName = l.teacherLastName;

                merged.putIfAbsent(key, lessonClone);

                // Collect teacher names
                Set<String> teachers = teacherMap.computeIfAbsent(key, k -> new LinkedHashSet<>());
                String privateName = l.teacherPrivateName != null ? l.teacherPrivateName.trim() : "";
                String lastName = l.teacherLastName != null ? l.teacherLastName.trim() : "";
                String fullName = (privateName + " " + lastName).trim();
                if (!fullName.isEmpty()) {
                    teachers.add(fullName);
                }
            }
        }

        // Apply merged teacher names
        for (Map.Entry<String, ScheduleData.Lesson> entry : merged.entrySet()) {
            Set<String> teachers = teacherMap.get(entry.getKey());
            if (teachers != null && !teachers.isEmpty()) {
                entry.getValue().teacherPrivateName = null;
                entry.getValue().teacherLastName = String.join(" & ", teachers);
            }
        }

        lessons = new ArrayList<>(merged.values());


        // Trim trailing empty lessons
        int lastNonFreeIndex = -1;
        for (
                int i = lessons.size() - 1;
                i >= 0; i--) {
            if (lessons.

                    get(i).subject != null) {
                lastNonFreeIndex = i;
                break;
            }
        }
        if (lastNonFreeIndex >= 0) {
            lessons = lessons.

                    subList(0, lastNonFreeIndex + 1);
        } else if (!lessons.

                isEmpty()) {
            lessons = lessons.

                    subList(0, 1);
        }

        scheduleRecycler.

                setAdapter(new LessonAdapter(lessons, hourNamesByHour));
    }
}
