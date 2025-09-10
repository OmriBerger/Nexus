package io.github.omriberger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import io.github.omriberger.R;
import io.github.omriberger.user.User;
import io.github.omriberger.user.UserMapper;
import io.github.omriberger.user.UserRepository;
import io.github.omriberger.utils.VersionInfo;

public class LoginJsonActivity extends AppCompatActivity {

    private EditText jsonInput;
    private Button confirmButton;
    private TextView errorMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.activity_login_json);

        jsonInput = findViewById(R.id.jsonInput);
        confirmButton = findViewById(R.id.confirmButton);
        errorMessage = findViewById(R.id.errorMessage);

        confirmButton.setOnClickListener(v -> handleJsonConfirm());
        VersionInfo versionInfoChecker = new VersionInfo();
        versionInfoChecker.checkForUpdates(this, 3);
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

    private void handleJsonConfirm() {
        String jsonText = jsonInput.getText().toString().trim();

        if (TextUtils.isEmpty(jsonText)) {
            showError("JSON cannot be empty");
            return;
        }

        try {
            User user = UserMapper.fromJson(jsonText); // map manually
//            String encrypted = CryptoManager.encryptUser(user);

            UserRepository.saveUser(this,user);
//            getSharedPreferences("app_prefs", MODE_PRIVATE)
//                    .edit()
//                    .putString("user_data", encrypted)
//                    .apply();

            // later: User loadedUser = CryptoManager.decryptUser(encrypted);

            errorMessage.setVisibility(View.GONE);
            startActivity(new Intent(this, ScheduleActivity.class));
            finish();


        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }


    private void showError(String message) {
        errorMessage.setText(message);
        errorMessage.setVisibility(View.VISIBLE);
    }
}
