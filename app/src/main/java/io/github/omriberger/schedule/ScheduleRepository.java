package io.github.omriberger.schedule;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.github.omriberger.user.User;
import io.github.omriberger.user.UserRepository;
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

    private String fetchScheduleFromApi() throws IOException {
        String url = "https://webtopserver.smartschool.co.il/server/api/shotef/ShotefSchedualeData";
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        User user;
        try {
            user = UserRepository.getUser(context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        Log.d("UserProblems", "User institutionCode: " + user.getInstitutionCode());
//        Log.d("UserProblems", "User classCombo: " + user.getClassCombo());
//        Log.d("UserProblems", "User token: " + user.getToken());
//
//        boolean sameInstitution = (user.getInstitutionCode() == 441279);
//        boolean sameClassCombo = "11|5".equals(user.getClassCombo());
//
//        boolean sameToken = BuildConfig.API_TOKEN.equals(user.getToken());
//
//        Log.d("UserProblems", "Institution equal? " + sameInstitution);
//        Log.d("UserProblems", "Class combo equal? " + sameClassCombo);
//        Log.d("UserProblems", "Token equal? " + sameToken);
//        Log.d("UserProblems", "Hardcoded token length: " + BuildConfig.API_TOKEN.length() +
//                ", User token length: " + user.getToken().length());
//
//        String userToken = user.getToken();
////        String buildToken = BuildConfig.API_TOKEN;
//        if (!userToken.equals(buildToken)) {
//            for (int i = 0; i < userToken.length(); i++) {
//                char u = userToken.charAt(i);
//                char b = buildToken.charAt(i);
//                if (u != b) {
//                    Log.d("UserProblems", "Mismatch at index " + i +
//                            " user='" + u + "' (" + (int) u + ")" +
//                            " build='" + b + "' (" + (int) b + ")");
//                    break; // stop at the first mismatch
//                }
//            }
//        }
//        Log.d("UserProblems", "User token HEX: " + toHexString(userToken));
//        Log.d("UserProblems", "Build token HEX: " + toHexString(buildToken));


//        RequestBody body = RequestBody.create("{"
//                + "\"institutionCode\":" + 441279 + ","
//                + "\"selectedValue\":\"" + "11|5" + "\","
//                + "\"typeView\":1"
//                + "}", JSON);

        RequestBody body = RequestBody.create("{"
                + "\"institutionCode\":" + user.getInstitutionCode() + ","
                + "\"selectedValue\":\"" + user.getClassCombo() + "\","
                + "\"typeView\":1"
                + "}", JSON);

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

    private static String toHexString(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            sb.append(String.format("%04x ", (int) c));
        }
        return sb.toString();
    }



    /** Ensure all nested lists are initialized to avoid NullPointerExceptions */
    private void initializeEmptyLists(ScheduleData.WeeklySchedule schedule) {
        if (schedule == null || schedule.data == null) return;
        for (ScheduleData.DaySchedule day : schedule.data) {
            if (day.hoursData == null) day.hoursData = new ArrayList<>();
            for (ScheduleData.HourData hour : day.hoursData) {
                if (hour.scheduale == null) hour.scheduale = new ArrayList<>();
                if (hour.changes == null) hour.changes = new ArrayList<>();
                if (hour.events == null) hour.events = new ArrayList<>();
                if (hour.exams == null) hour.exams = new ArrayList<>();
            }
        }
    }

    private void saveCacheToDisk(String json) {
        try (FileOutputStream fos = context.openFileOutput("schedule_cache.json", Context.MODE_PRIVATE)) {
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
