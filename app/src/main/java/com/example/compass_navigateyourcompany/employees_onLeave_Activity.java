package com.example.compass_navigateyourcompany;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

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
