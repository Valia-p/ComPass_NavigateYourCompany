package com.example.compass_navigateyourcompany;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RequestsActivity extends AppCompatActivity {

    private ImageView profileButton, backButton, homeButton, logoutButton;
    private TextView nameTextView, fromDateTextView, toDateTextView, periodTextView;
    private Button acceptButton, declineButton;
    private AppDatabase db;
    private String login_name;
    private List<Pass> passes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests); // Ensure this is the correct layout file

        // Initialize views
        profileButton = findViewById(R.id.profile_button);
        backButton = findViewById(R.id.back_button);
        homeButton = findViewById(R.id.home_button);
        logoutButton = findViewById(R.id.logout_button);

        nameTextView = findViewById(R.id.name);
        fromDateTextView = findViewById(R.id.from_date);
        toDateTextView = findViewById(R.id.to_date);
        periodTextView = findViewById(R.id.period);

        acceptButton = findViewById(R.id.accept);
        declineButton = findViewById(R.id.decline);

        db = AppDatabase.getInstance(this);

        // Get user login name from Intent
        Intent intent = getIntent();
        if (intent != null) {
            login_name = intent.getStringExtra("Name");

        }

        Executors.newSingleThreadExecutor().execute(() -> {
            User headUser = db.userDao().findByLoginName(login_name);
            if (headUser != null) {
                String authToken = headUser.authToken;

                List<User> users = db.userDao().findEmployeeUsersByAuthToken(authToken);
                if (users == null || users.isEmpty()) {
                    runOnUiThread(() -> Toast.makeText(RequestsActivity.this, "No employees found with the same auth token", Toast.LENGTH_SHORT).show());
                    return;
                }

                List<Integer> userIds = users.stream().map(User::getId).collect(Collectors.toList());
                Log.d("RequestsActivity", "Employee User IDs: " + userIds); // Log user IDs

                List<Pass> passes = db.passDao().getPassesByUserIds(userIds);
                if (passes == null || passes.isEmpty()) {
                    runOnUiThread(() -> Toast.makeText(RequestsActivity.this, "No passes found", Toast.LENGTH_SHORT).show());
                    return;
                }

                runOnUiThread(() -> {
                    Pass pass = passes.get(0);
                    User user = users.stream().filter(u -> u.getId() == pass.userID).findFirst().orElse(null);
                    Log.d("RequestsActivity", "Passes retrieved: " + passes);
                    if (user != null) {
                        nameTextView.setText(user.loginName);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()); // Adjust format as needed
                        String fromDateString = pass.fromDate != null ? dateFormat.format(pass.fromDate) : "N/A";
                        String toDateString = pass.toDate != null ? dateFormat.format(pass.toDate) : "N/A";

                        fromDateTextView.setText(fromDateString);
                        toDateTextView.setText(toDateString);
                        long diffInMillis = pass.toDate.getTime() - pass.fromDate.getTime();
                        long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
                        periodTextView.setText(diffInDays + " days");
                    } else {
                        Toast.makeText(RequestsActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                runOnUiThread(() -> Toast.makeText(RequestsActivity.this, "Head user not found", Toast.LENGTH_SHORT).show());
            }
        });


        backButton.setOnClickListener(v -> {
            String loginName = login_name;
            Intent intentProfile = new Intent(RequestsActivity.this, home_head_Activity.class);
            intentProfile.putExtra("Name", loginName);
            startActivity(intentProfile);
            finish();
        });


        profileButton.setOnClickListener(v -> {
            String loginName = login_name;
            Intent intentProfile = new Intent(RequestsActivity.this, head_profile_Activity.class);
            intentProfile.putExtra("loginName", loginName);
            startActivity(intentProfile);
            finish();
        });

        homeButton.setOnClickListener(v -> {
            String loginName = login_name;
            Intent intentProfile = new Intent(RequestsActivity.this, home_head_Activity.class);
            intentProfile.putExtra("Name", loginName);
            startActivity(intentProfile);
            finish();
        });

        logoutButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Clear all data
            editor.apply();

            Intent intentLogin = new Intent(RequestsActivity.this, login_Activity.class);
            startActivity(intentLogin);
            finish();
        });

        acceptButton.setOnClickListener(v -> updatePassStatus(1));
        declineButton.setOnClickListener(v -> updatePassStatus(-1));
    }

    private void updatePassStatus(int isAccepted) {
        Executors.newSingleThreadExecutor().execute(() -> {
            if (passes != null && !passes.isEmpty()) {
                Pass pass = passes.get(0);
                pass.approved = isAccepted;
                db.passDao().updatePass(pass);
                runOnUiThread(() -> {
                    String message = isAccepted == 1 ? "Request Accepted" : "Request Declined";
                    Toast.makeText(RequestsActivity.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

}
