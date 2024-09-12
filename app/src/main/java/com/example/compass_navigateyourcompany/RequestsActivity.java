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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RequestsActivity extends AppCompatActivity {

    private LinearLayout requestsContainer;
    private AppDatabase db;
    private String login_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests);

        // Initialize views
        requestsContainer = findViewById(R.id.requests_container);
        ImageView backButton = findViewById(R.id.back_button);
        ImageView profileButton = findViewById(R.id.profile_button);
        ImageView homeButton = findViewById(R.id.home_button);
        ImageView logoutButton = findViewById(R.id.logout_button);

        db = AppDatabase.getInstance(this);

        // Get user login name from Intent
        Intent intent = getIntent();
        if (intent != null) {
            login_name = intent.getStringExtra("Name");
        }

        loadRequests();

        backButton.setOnClickListener(v -> navigateTo(home_head_Activity.class));
        profileButton.setOnClickListener(v -> navigateTo(head_profile_Activity.class));
        homeButton.setOnClickListener(v -> navigateTo(home_head_Activity.class));
        logoutButton.setOnClickListener(this::logout);
    }

    private void loadRequests() {
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
                Log.d("RequestsActivity", "Employee User IDs: " + userIds);

                List<Pass> passes = db.passDao().getPassesByUserIds(userIds);
                if (passes == null || passes.isEmpty()) {
                    runOnUiThread(() -> Toast.makeText(RequestsActivity.this, "No passes found", Toast.LENGTH_SHORT).show());
                    return;
                }

                runOnUiThread(() -> {
                    requestsContainer.removeAllViews(); // Clear existing views

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                    for (Pass pass : passes) {
                        User user = users.stream().filter(u -> u.getId() == pass.userID).findFirst().orElse(null);

                        // Create and configure the name TextView
                        TextView nameTextView = new TextView(RequestsActivity.this);
                        nameTextView.setText(user != null ? user.loginName : "Unknown");
                        nameTextView.setTextSize(23);
                        nameTextView.setTypeface(null, Typeface.BOLD);
                        nameTextView.setTextColor(getResources().getColor(R.color.black));
                        nameTextView.setPadding(15, 10, 0, 0);

                        // Create and configure the from date TextView
                        TextView fromDateTextView = new TextView(RequestsActivity.this);
                        fromDateTextView.setText(pass.fromDate != null ? dateFormat.format(pass.fromDate) : "N/A");
                        fromDateTextView.setTextSize(18);
                        fromDateTextView.setTextColor(getResources().getColor(R.color.black));
                        fromDateTextView.setPadding(30, 0, 0, 10);

                        // Create and configure the to date TextView
                        TextView toDateTextView = new TextView(RequestsActivity.this);
                        toDateTextView.setText(pass.toDate != null ? dateFormat.format(pass.toDate) : "N/A");
                        toDateTextView.setTextSize(18);
                        toDateTextView.setTextColor(getResources().getColor(R.color.black));
                        toDateTextView.setPadding(30, 0, 0, 10);

                        // Create and configure the period TextView
                        long diffInMillis = pass.toDate.getTime() - pass.fromDate.getTime();
                        long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
                        TextView periodTextView = new TextView(RequestsActivity.this);
                        periodTextView.setText(diffInDays + " days");
                        periodTextView.setTextSize(18);
                        periodTextView.setTextColor(getResources().getColor(R.color.black));
                        periodTextView.setPadding(30, 0, 0, 10);

                        // Create and configure the accept button
                        Button acceptButton = new Button(RequestsActivity.this);
                        acceptButton.setText("Accept");
                        acceptButton.setOnClickListener(v -> updatePassStatus(pass, 1));

                        // Create and configure the decline button
                        Button declineButton = new Button(RequestsActivity.this);
                        declineButton.setText("Decline");
                        declineButton.setOnClickListener(v -> updatePassStatus(pass, -1));

                        // Add all views to a container layout
                        LinearLayout requestLayout = new LinearLayout(RequestsActivity.this);
                        requestLayout.setOrientation(LinearLayout.VERTICAL);
                        requestLayout.setPadding(16, 16, 16, 16);

                        requestLayout.addView(nameTextView);
                        requestLayout.addView(fromDateTextView);
                        requestLayout.addView(toDateTextView);
                        requestLayout.addView(periodTextView);
                        requestLayout.addView(acceptButton);
                        requestLayout.addView(declineButton);

                        // Add the request layout to the container
                        requestsContainer.addView(requestLayout);
                    }
                });
            } else {
                runOnUiThread(() -> Toast.makeText(RequestsActivity.this, "Head user not found", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void updatePassStatus(Pass pass, int isAccepted) {
        Executors.newSingleThreadExecutor().execute(() -> {
            pass.approved = isAccepted;
            db.passDao().updatePass(pass);
            runOnUiThread(() -> {
                String message = isAccepted == 1 ? "Request Accepted" : "Request Declined";
                Toast.makeText(RequestsActivity.this, message, Toast.LENGTH_SHORT).show();
                loadRequests(); // Reload requests
            });
        });
    }

    private void navigateTo(Class<?> activityClass) {
        Intent intent = new Intent(RequestsActivity.this, activityClass);
        intent.putExtra("loginName", login_name);
        intent.putExtra("Name", login_name);
        startActivity(intent);
        finish();
    }

    private void logout(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intentLogin = new Intent(RequestsActivity.this, login_Activity.class);
        startActivity(intentLogin);
        finish();
    }
}
