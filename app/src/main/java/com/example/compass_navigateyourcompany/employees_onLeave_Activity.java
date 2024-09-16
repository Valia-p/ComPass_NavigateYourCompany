package com.example.compass_navigateyourcompany;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class employees_onLeave_Activity extends AppCompatActivity {

    private LinearLayout requestsContainer;
    private TextView noRequestsMessage;
    private AppDatabase db;
    private String login_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employees_onleave);

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

        backButton.setOnClickListener(v -> navigateTo(home_head_Activity.class));
        profileButton.setOnClickListener(v -> navigateTo(head_profile_Activity.class));
        homeButton.setOnClickListener(v -> navigateTo(home_head_Activity.class));
        logoutButton.setOnClickListener(this::logout);

        new FetchApprovedPassesTask().execute();
    }


    private class FetchApprovedPassesTask extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... voids) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            List<String> approvedPasses = new ArrayList<>();
            Date currentDate = new Date();

            Head head = db.headDao().getHeadByName(login_name);
            if (head != null) {
                int departmentId = head.getDepartmentId();

                // Get employees in the same department
                List<Employee> employees = db.employeeDao().findByDepartmentId(departmentId);

                // get the employees user IDs and approved passes
                for (Employee employee : employees) {
                    int userId = db.userDao().findIdByName(employee.name);
                    List<Pass> passes = db.passDao().getApprovedPassesByUserId(userId);

                    for (Pass pass : passes) {
                        if (pass.toDate.after(currentDate)) {
                            String passInfo = employee.getName() + " - " + "From :" + dateFormat.format(pass.fromDate) + " To : " + dateFormat.format(pass.toDate);
                            approvedPasses.add(passInfo);
                        }
                    }
                }
            }
            return approvedPasses;
        }

        @Override
        protected void onPostExecute(List<String> approvedPasses) {
            populatePasses(approvedPasses);
        }
    }

    private void populatePasses(List<String> requestsList) {
        if (requestsList == null || requestsList.isEmpty()) {
            noRequestsMessage.setVisibility(View.VISIBLE);
            requestsContainer.setVisibility(View.GONE);
        }
        else {

            noRequestsMessage.setVisibility(View.GONE);
            requestsContainer.setVisibility(View.VISIBLE);

            // Add each pass to the container
            for (String request : requestsList) {
                LinearLayout leavesLayout = new LinearLayout(this);
                leavesLayout.setOrientation(LinearLayout.HORIZONTAL);
                leavesLayout.setPadding(16, 12, 16, 12);
                leavesLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_corners));
                leavesLayout.setElevation(4);
                LinearLayout.LayoutParams leavesLayoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                leavesLayoutParams.setMargins(0, 0, 0, 12);
                leavesLayout.setLayoutParams(leavesLayoutParams);

                TextView requestTextView = new TextView(this);
                requestTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                requestTextView.setText(request);
                requestTextView.setTextSize(16);
                requestTextView.setPadding(25, 0, 25, 0);
                requestTextView.setTextColor(getResources().getColor(android.R.color.black));

                leavesLayout.addView(requestTextView);
                requestsContainer.addView(leavesLayout);
            }
        }
    }

    private void navigateTo(Class<?> activityClass) {
        Intent intent = new Intent(employees_onLeave_Activity.this, activityClass);
        intent.putExtra("sourceActivity", "employees_onLeave");
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

        Intent intentLogin = new Intent(employees_onLeave_Activity.this, login_Activity.class);
        startActivity(intentLogin);
        finish();
    }
}
