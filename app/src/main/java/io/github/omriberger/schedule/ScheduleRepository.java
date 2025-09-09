package io.github.omriberger.schedule;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.github.omriberger.user.User;
import io.github.omriberger.user.UserRepository;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScheduleRepository {

    private static final long CACHE_DURATION_MS = TimeUnit.MINUTES.toMillis(15);

    private final OkHttpClient client = new OkHttpClient();
    protected final Gson gson = new Gson();

    // Using the new WeeklySchedule from ScheduleData
    protected ScheduleData.WeeklySchedule cachedSchedule;
    protected long lastFetchTime = 0;

    private final Context context;

    public ScheduleRepository(Context context) {
        this.context = context.getApplicationContext(); // avoid leaking Activity
    }

    /** Returns cached WeeklySchedule, refreshes only if older than 15 minutes */
    public synchronized ScheduleData.WeeklySchedule getSchedule() throws IOException {
        long now = System.currentTimeMillis();

        // 1. In-memory cache
        if (cachedSchedule != null && now - lastFetchTime < CACHE_DURATION_MS) {
            Log.d("ScheduleRepo", "Returning cached schedule (memory)");
            return cachedSchedule;
        }

        // 2. Disk cache
        if (cachedSchedule == null) {
            String diskJson = loadCacheFromDisk();
            if (diskJson != null) {
                cachedSchedule = gson.fromJson(diskJson, ScheduleData.WeeklySchedule.class);
                initializeEmptyLists(cachedSchedule);
                Log.d("ScheduleRepo", "Loaded schedule from disk cache");
                return cachedSchedule;
            }
        }

        // 3. Network fetch
        String json = fetchScheduleFromApi();
        cachedSchedule = gson.fromJson(json, ScheduleData.WeeklySchedule.class);
        lastFetchTime = now;
        initializeEmptyLists(cachedSchedule);

        // Save to disk for next app launch
        saveCacheToDisk(json);

        return cachedSchedule;
    }


    /** Returns raw JSON directly from the API, bypassing cache */
    public String getRawScheduleJson() throws IOException {
        return fetchScheduleFromApi();
    }

    /** Internal method to fetch JSON from API */
    @SneakyThrows
    private String fetchScheduleFromApi() {
        User user = UserRepository.getUser(context);
        String url = "https://webtopserver.smartschool.co.il/server/api/shotef/ShotefSchedualeData";
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String combo = user.getClassCombo();


        JSONObject json = new JSONObject();
        json.put("institutionCode", user.getInstitutionCode());
        json.put("selectedValue", combo);
        json.put("typeView", 1);

        RequestBody body = RequestBody.create(json.toString(), JSON);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", "webToken=" + user.getToken())
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    /** Ensure all nested lists are initialized to avoid NullPointerExceptions */
    private void initializeEmptyLists(ScheduleData.WeeklySchedule schedule) {
        if (schedule == null || schedule.data == null) return;
        for (ScheduleData.DaySchedule day : schedule.data) {
            if (day.hoursData == null) day.hoursData = new java.util.ArrayList<>();
            for (ScheduleData.HourData hour : day.hoursData) {
                if (hour.scheduale == null) hour.scheduale = new java.util.ArrayList<>();
                if (hour.changes == null) hour.changes = new java.util.ArrayList<>();
                if (hour.events == null) hour.events = new java.util.ArrayList<>();
                if (hour.exams == null) hour.exams = new java.util.ArrayList<>();
            }
        }
    }

    private void saveCacheToDisk(String json) {
        try (FileOutputStream fos = context.openFileOutput("schedule_cache.json", android.content.Context.MODE_PRIVATE)) {
            fos.write(json.getBytes());
            Log.d("ScheduleRepo", "Schedule cache saved to disk");
        } catch (IOException e) {
            Log.e("ScheduleRepo", "Failed to save schedule cache", e);
        }
    }


    private String loadCacheFromDisk() {
        try (FileInputStream fis = context.openFileInput("schedule_cache.json")) {
            return new String(fis.readAllBytes());
        } catch (IOException e) {
            Log.d("ScheduleRepo", "No cached schedule found on disk");
            return null;
        }
    }

}
