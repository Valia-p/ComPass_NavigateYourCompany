package com.example.compass_navigateyourcompany;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class home_employer_Activity extends AppCompatActivity {

    private LinearLayout userListContainer;
    private AppDatabase db;
    private String login_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_employer); // Ensure this is the correct XML file

        // Initialize views
        userListContainer = findViewById(R.id.employee_container);
        ImageView backButton = findViewById(R.id.settings_button);
        ImageView profileButton = findViewById(R.id.profile_button);
        ImageView homeButton = findViewById(R.id.home_button);
        ImageView logoutButton = findViewById(R.id.logout_button);

        db = AppDatabase.getInstance(this);

        // Get user login name from Intent
        Intent intent = getIntent();
        if (intent != null) {
            login_name = intent.getStringExtra("Name");
        }

        loadUsers();

        backButton.setOnClickListener(v -> navigateTo(home_employer_Activity.class));
        profileButton.setOnClickListener(v -> navigateTo(employer_profile_Activity.class));
        homeButton.setOnClickListener(v -> navigateTo(home_employer_Activity.class));
        logoutButton.setOnClickListener(this::logout);
    }

    private void loadUsers() {
        Executors.newSingleThreadExecutor().execute(() -> {
            User currentUser = db.userDao().findByLoginName(login_name);
            if (currentUser != null) {
                String authToken = currentUser.authToken;
                List<User> users = db.userDao().findEmployeeUsersByAuthToken(authToken);

                if (users == null || users.isEmpty()) {
                    runOnUiThread(() -> Toast.makeText(home_employer_Activity.this, "No employees found with the same auth token", Toast.LENGTH_SHORT).show());
                    return;
                }

                runOnUiThread(() -> {
                    userListContainer.removeAllViews(); // Clear existing views

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                    for (User user : users) {
                        // Create and configure the user layout
                        LinearLayout userLayout = new LinearLayout(home_employer_Activity.this);
                        userLayout.setOrientation(LinearLayout.VERTICAL);
                        userLayout.setPadding(16, 16, 16, 16);
                        userLayout.setBackgroundResource(R.drawable.rounded_corners); // Assuming you have a drawable for rounded corners

                        // Create and configure name TextView
                        TextView nameTextView = new TextView(home_employer_Activity.this);
                        nameTextView.setText(user.loginName != null ? user.loginName : "Unknown");
                        nameTextView.setTextSize(20);
                        nameTextView.setTypeface(null, Typeface.BOLD);
                        nameTextView.setTextColor(getResources().getColor(R.color.bg_color));
                        nameTextView.setPadding(0, 0, 0, 8);

                        // Add TextView to layout
                        userLayout.addView(nameTextView);

                        // Add layout to user list container
                        userListContainer.addView(userLayout);
                    }
                });
            } else {
                runOnUiThread(() -> Toast.makeText(home_employer_Activity.this, "User not found", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void navigateTo(Class<?> activityClass) {
        Intent intent = new Intent(home_employer_Activity.this, activityClass);
        intent.putExtra("Name", login_name);
        startActivity(intent);
        finish();
    }

    private void logout(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all data
        editor.apply();

        Intent intentLogin = new Intent(home_employer_Activity.this, login_Activity.class);
        startActivity(intentLogin);
        finish();
    }
}
