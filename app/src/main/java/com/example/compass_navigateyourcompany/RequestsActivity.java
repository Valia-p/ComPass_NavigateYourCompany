package com.example.compass_navigateyourcompany;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RequestsActivity extends AppCompatActivity {

    private LinearLayout requestsContainer;
    private TextView noRequestsMessage;
    private AppDatabase db;
    private String login_name;

    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests);

        // Initialize views
        requestsContainer = findViewById(R.id.requests_container);
        noRequestsMessage = findViewById(R.id.no_requests_message);
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

    @SuppressLint("ResourceAsColor")
    private void loadRequests() {
        Executors.newSingleThreadExecutor().execute(() -> {
            User headUser = db.userDao().findByLoginName(login_name);
            Head head = db.headDao().getHeadByName(login_name);

            if (headUser != null && head != null) {
                String authToken = headUser.authToken;
                Integer headDepartmentId = head.departmentId; // Get the department ID of the head

                // Fetch employees by auth token
                List<User> users = db.userDao().findEmployeeUsersByAuthToken(authToken);

                if (users == null || users.isEmpty()) {
                    runOnUiThread(() -> Toast.makeText(RequestsActivity.this, "No employees found with the same auth token", Toast.LENGTH_SHORT).show());
                    return;
                }

                // Filter users based on the department ID of the head
                List<User> filteredUsers = users.stream()
                        .filter(user -> {
                            Integer userDepartmentId = db.employeeDao().findDepartmentIdByLoginName(user.loginName);
                            return userDepartmentId != null && userDepartmentId.equals(headDepartmentId);
                        })
                        .collect(Collectors.toList());

                if (filteredUsers.isEmpty()) {
                    runOnUiThread(() -> showNoRequestsMessage(true));
                    return;
                }

                List<Integer> userIds = filteredUsers.stream().map(User::getId).collect(Collectors.toList());
                Log.d("RequestsActivity", "Filtered Employee User IDs: " + userIds);


                List<Pass> passes = db.passDao().getPassesByUserIds(userIds);
                if (passes == null || passes.isEmpty()) {
                    runOnUiThread(() -> showNoRequestsMessage(true));
                    return;
                }

                runOnUiThread(() -> {
                    requestsContainer.removeAllViews();
                    boolean hasRequests = false;

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    Date currentDate = new Date();

                    for (Pass pass : passes) {
                        if (pass.approved == 0 && (pass.fromDate.after(currentDate) || pass.fromDate.equals(currentDate) )) {
                            hasRequests = true;

                            User user = filteredUsers.stream().filter(u -> u.getId() == pass.userID).findFirst().orElse(null);

                            // Create a rounded corner background layout (this assumes you have a drawable for rounded corners)
                            LinearLayout roundedLayout = new LinearLayout(this);
                            LinearLayout.LayoutParams roundedLayoutParams = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            roundedLayout.setLayoutParams(roundedLayoutParams);
                            roundedLayout.setOrientation(LinearLayout.VERTICAL);
                            roundedLayout.setPadding(0, 10, 0, 10);
                            roundedLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_corners));
                            roundedLayout.setElevation(4);

                            // Create and add views for the request...
                            TextView nameTextView = new TextView(RequestsActivity.this);
                            nameTextView.setText(user != null ? user.loginName : "Unknown");
                            nameTextView.setTextSize(23);
                            nameTextView.setTypeface(null, Typeface.BOLD);
                            nameTextView.setTextColor(getResources().getColor(R.color.main_color));
                            nameTextView.setPadding(15, 5, 8, 0);

                            TextView typeTextView = new TextView(RequestsActivity.this);
                            typeTextView.setText("Type: " + (pass.type != null ? pass.type : "N/A"));
                            typeTextView.setTextSize(18);
                            typeTextView.setTextColor(getResources().getColor(R.color.black));
                            typeTextView.setPadding(20, 5, 0, 10);

                            TextView fromDateTextView = new TextView(RequestsActivity.this);
                            fromDateTextView.setText("From: " + (pass.fromDate != null ? dateFormat.format(pass.fromDate) : "N/A"));
                            fromDateTextView.setTextSize(18);
                            fromDateTextView.setTextColor(getResources().getColor(R.color.black));
                            fromDateTextView.setPadding(20, 5, 0, 10);

                            TextView toDateTextView = new TextView(RequestsActivity.this);
                            toDateTextView.setText("To: " + (pass.toDate != null ? dateFormat.format(pass.toDate) : "N/A"));
                            toDateTextView.setTextSize(18);
                            toDateTextView.setTextColor(getResources().getColor(R.color.black));
                            toDateTextView.setPadding(20, 5, 0, 10);

                            long diffInMillis = pass.toDate.getTime() - pass.fromDate.getTime();
                            long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
                            TextView periodTextView = new TextView(RequestsActivity.this);
                            periodTextView.setText("Period: " + diffInDays + " days");
                            periodTextView.setTextSize(18);
                            periodTextView.setTextColor(getResources().getColor(R.color.black));
                            periodTextView.setPadding(20, 5, 0, 10);

                            // Add the Accept and Decline buttons
                            LinearLayout buttonLayout = new LinearLayout(this);
                            buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
                            buttonLayout.setGravity(Gravity.CENTER_HORIZONTAL);

                            Button acceptButton = new Button(RequestsActivity.this);
                            acceptButton.setText("Accept");
                            acceptButton.setTextColor(ContextCompat.getColorStateList(this, R.color.white));
                            acceptButton.setBackgroundResource(R.drawable.button_bg_corners);
                            acceptButton.setOnClickListener(v -> updatePassStatus(pass, 1));

                            Button declineButton = new Button(RequestsActivity.this);
                            declineButton.setText("Decline");
                            declineButton.setTextColor(ContextCompat.getColorStateList(this, R.color.white));
                            declineButton.setBackgroundResource(R.drawable.button_bg_corners);
                            declineButton.setOnClickListener(v -> updatePassStatus(pass, -1));

                            // Add margin to the Decline button to create space between buttons
                            LinearLayout.LayoutParams declineParams = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            declineParams.setMargins(16, 0, 0, 0); // This will add a margin of 16px (~5dp)
                            declineButton.setLayoutParams(declineParams);

                            buttonLayout.addView(acceptButton);
                            buttonLayout.addView(declineButton);

                            LinearLayout requestLayout = new LinearLayout(RequestsActivity.this);
                            requestLayout.setOrientation(LinearLayout.VERTICAL);
                            requestLayout.setPadding(16, 16, 16, 16);

                            roundedLayout.addView(nameTextView);
                            roundedLayout.addView(typeTextView);
                            roundedLayout.addView(fromDateTextView);
                            roundedLayout.addView(toDateTextView);
                            roundedLayout.addView(periodTextView);
                            roundedLayout.addView(buttonLayout);

                            requestLayout.addView(roundedLayout);

                            if ("Sick Leave".equals(pass.type) && pass.filePath != null && !pass.filePath.isEmpty()) {
                                Button documentButton = new Button(RequestsActivity.this);
                                documentButton.setText("Download Document");
                                documentButton.setOnClickListener(v -> {
                                    if (checkStoragePermissions()) {
                                        downloadDocument(pass.filePath);
                                    }
                                });
                                requestLayout.addView(documentButton);
                            }

                            requestsContainer.addView(requestLayout);
                        }
                    }

                    showNoRequestsMessage(!hasRequests);
                });
            } else {
                runOnUiThread(() -> Toast.makeText(RequestsActivity.this, "Head user or department not found", Toast.LENGTH_SHORT).show());
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
                loadRequests(); // Refresh the requests after accepting or declining
            });
        });
    }

    private void showNoRequestsMessage(boolean show) {
        noRequestsMessage.setVisibility(show ? View.VISIBLE : View.GONE);
        requestsContainer.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void navigateTo(Class<?> activityClass) {
        Intent intent = new Intent(RequestsActivity.this, activityClass);
        intent.putExtra("sourceActivity", "RequestsActivity");
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

    private boolean checkStoragePermissions() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            return false;
        }
        return true;
    }

    private void downloadDocument(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Downloading Document");
        request.setDescription("Please wait...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "document.pdf");

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
            Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Download manager not available", Toast.LENGTH_SHORT).show();
        }
    }
}
