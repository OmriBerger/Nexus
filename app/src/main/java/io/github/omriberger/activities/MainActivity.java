package io.github.omriberger.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.github.omriberger.R;
import io.github.omriberger.databinding.ActivityMainBinding;
import io.github.omriberger.schedule.ScheduleRepository;
import io.github.omriberger.schedule.ScheduleWorker;
import io.github.omriberger.user.User;
import io.github.omriberger.user.UserRepository;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        instance = this;

        // Splash screen
        SplashScreen.installSplashScreen(this);

        // Inflate view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar & FAB
        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab)
                        .show()
        );

        // Drawer & Navigation
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Lock drawer by default
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        // Locale setup
        Locale locale = new Locale("iw");
        Locale.setDefault(locale);
        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Request notification permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
        }

        // Load user
        User currentUser = UserRepository.getUser(this);
        Intent nextIntent;

        if (currentUser == null) {
            // User not logged in -> go to login
            nextIntent = new Intent(this, LoginJsonActivity.class);
        } else {
            Log.d("MainActivity", "Loaded user: " + currentUser.getFullName());
            nextIntent = new Intent(this, ScheduleActivity.class);

            // Fetch schedule only if user exists
            ScheduleRepository repository = new ScheduleRepository(instance);
            new Thread(() -> {
                try {
                    String rawJson = repository.getRawScheduleJson();
                    Log.d("Schedule", "Raw JSON: " + rawJson);
                } catch (IOException e) {
                    Log.e("Schedule", "Error fetching raw JSON", e);
                }
            }).start();
        }

        // Pass locale to next activity
        nextIntent.putExtra("locale", "iw");
        startActivity(nextIntent);
        finish();

        // Schedule periodic work
        PeriodicWorkRequest scheduleWorkRequest =
                new PeriodicWorkRequest.Builder(ScheduleWorker.class, 15, TimeUnit.MINUTES)
                        .build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "ScheduleUpdate",
                ExistingPeriodicWorkPolicy.KEEP,
                scheduleWorkRequest
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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
}



