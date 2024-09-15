package com.example.compass_navigateyourcompany;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class employee_profile_Activity extends AppCompatActivity {

    private AppDatabase db;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private TextView departmentTextView;
    private TextView supervisorTextView;
    private TextView yearsTextView;
    private TextView leavesTextView;
    private LinearLayout requestsContainer;
    private TextView noRequestsMessage;

    private String loginName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_profile);

        db = AppDatabase.getInstance(this);

        nameTextView = findViewById(R.id.EmployeeName);
        emailTextView = findViewById(R.id.email);
        phoneTextView = findViewById(R.id.phone);
        departmentTextView = findViewById(R.id.department_Text);
        supervisorTextView = findViewById(R.id.supervisor_name);
        yearsTextView = findViewById(R.id.years);
        leavesTextView = findViewById(R.id.leaves);
        requestsContainer = findViewById(R.id.requests_container);
        noRequestsMessage = findViewById(R.id.no_requests_message);

        ImageView backButton = findViewById(R.id.back_button);
        ImageView homeButton = findViewById(R.id.home_button);
        ImageView logoutButton = findViewById(R.id.logout_button);

        // Get user login name from Intent
        Intent intent = getIntent();
        if (intent != null) {
            loginName = intent.getStringExtra("loginName");
        }

        backButton.setOnClickListener(v -> navigateTo(home_employee_Activity.class));
        homeButton.setOnClickListener(v -> navigateTo(home_employee_Activity.class));
        logoutButton.setOnClickListener(this::logout);

        loadProfile();
    }

    private void loadProfile() {
        new Thread(() -> {
            try {
                String employeeLoginName = getIntent().getStringExtra("loginName");

                Employee employee = db.employeeDao().findByName(employeeLoginName);
                User user = db.userDao().findByLoginName(loginName);
                if (employee == null) {
                    runOnUiThread(() -> Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show());
                    return;
                }

                Department department = db.departmentDao().findById(employee.departmentId);
                String departmentName = department != null ? department.Name : "Unknown Department";

                String supervisor = db.headDao().findSupervisor(employee.authToken, employee.departmentId);
                Integer leaves = db.passDao().countApprovedLeaves(user.id);

                // Update UI
                runOnUiThread(() -> {
                    nameTextView.setText(employee.name);
                    emailTextView.setText(employee.mail);
                    phoneTextView.setText(employee.phone);
                    departmentTextView.setText(departmentName);
                    supervisorTextView.setText(supervisor);
                    yearsTextView.setText(String.valueOf(employee.years));
                    leavesTextView.setText(String.valueOf(leaves));

                    // Fetch leave requests after loading the profile
                    new FetchLeaveRequestsTask().execute(employee.name);
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private class FetchLeaveRequestsTask extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(String... params) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            List<String> leaveRequests = new ArrayList<>();
            Date currentDate = new Date();

            // Calculate the cutoff date (2 days ago)
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_YEAR, -2);
            Date cutoffDate = calendar.getTime();

            String name = params[0];
            User user = db.userDao().findByLoginName(name);
            List<Pass> passes = db.passDao().getAllPasses(user.id);

            // Filter and sort passes by fromDate in descending order
            List<Pass> filteredPasses = new ArrayList<>();
            for (Pass pass : passes) {
                if (pass.toDate.after(currentDate) && pass.fromDate.after(cutoffDate)) {
                    filteredPasses.add(pass);
                }
            }

            // Sort passes by fromDate in ascending order
            filteredPasses.sort((p1, p2) -> p1.fromDate.compareTo(p2.fromDate));

            for (Pass pass : filteredPasses) {

                String leaveType = pass.type;
                Integer value = pass.approved;
                String  status;
                if(value == 1 ){
                    status = "Approved";
                } else if (value == 0) {
                    status = "Pending";
                }
                else status = "Rejected";


                String passInfo = String.format(" %s | From: %s | To: %s | Status: %s",
                        leaveType,
                        dateFormat.format(pass.fromDate),
                        dateFormat.format(pass.toDate),
                        status);

                leaveRequests.add(passInfo);
                }

            return leaveRequests;
        }


        @Override
        protected void onPostExecute(List<String> leaveRequests) {
            populateLeaveRequests(leaveRequests);
        }
    }

    private void populateLeaveRequests(List<String> requestsList) {
        if (requestsList == null || requestsList.isEmpty()) {
            noRequestsMessage.setVisibility(View.VISIBLE);
            requestsContainer.setVisibility(View.GONE);
        } else {
            noRequestsMessage.setVisibility(View.GONE);
            requestsContainer.setVisibility(View.VISIBLE);

            requestsContainer.removeAllViews(); // Clear previous views

            // Add each request to the container
            for (String request : requestsList) {
                TextView requestTextView = new TextView(this);
                requestTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                requestTextView.setText(request);
                requestTextView.setTextSize(16);
                requestTextView.setPadding(8, 8, 8, 8);

                requestTextView.setBackgroundColor(getResources().getColor(android.R.color.white));
                requestTextView.setTextColor(getResources().getColor(android.R.color.black));

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) requestTextView.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, 8);
                requestTextView.setLayoutParams(layoutParams);

                requestsContainer.addView(requestTextView);
            }
        }
    }

    private void navigateTo(Class<?> activityClass) {
        Intent intent = new Intent(employee_profile_Activity.this, activityClass);
        intent.putExtra("Name", loginName);
        startActivity(intent);
        finish();
    }

    private void logout(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intentLogin = new Intent(employee_profile_Activity.this, login_Activity.class);
        startActivity(intentLogin);
        finish();
    }
}
