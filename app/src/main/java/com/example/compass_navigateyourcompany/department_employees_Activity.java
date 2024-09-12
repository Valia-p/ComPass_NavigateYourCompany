package com.example.compass_navigateyourcompany;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.Executors;

public class department_employees_Activity extends AppCompatActivity {

    private LinearLayout employeesContainer;
    private TextView noEmployeesMessage;
    private TextView departmentNameText;
    private AppDatabase db;
    private String login_name;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.department_employees);

        // Initialize views
        employeesContainer = findViewById(R.id.employees_container);
        noEmployeesMessage = findViewById(R.id.no_employees_message);
        departmentNameText = findViewById(R.id.department_name);
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

        loadDepartmentAndEmployees();

        backButton.setOnClickListener(v -> navigateTo(home_head_Activity.class));
        profileButton.setOnClickListener(v -> navigateTo(head_profile_Activity.class));
        homeButton.setOnClickListener(v -> navigateTo(home_head_Activity.class));
        logoutButton.setOnClickListener(this::logout);
    }

    private void loadDepartmentAndEmployees() {
        Executors.newSingleThreadExecutor().execute(() -> {
            User headUser = db.userDao().findByLoginName(login_name);
            Head head = db.headDao().getHeadByName(login_name);

            if (headUser != null && head != null) {
                Integer headDepartmentId = head.departmentId;

                // Fetch department name
                Department department = db.departmentDao().findById(headDepartmentId);
                runOnUiThread(() -> {
                    if (department != null) {
                        departmentNameText.setText("Department: " + department.getName());
                    } else {
                        departmentNameText.setText("Department: Unknown");
                    }
                });

                // Fetch employees
                List<Employee> employees = db.employeeDao().findByDepartmentId(headDepartmentId);
                runOnUiThread(() -> {
                    employeesContainer.removeAllViews();

                    // Check if employees list is empty
                    if (employees == null || employees.isEmpty()) {
                        showNoRequestsMessage(true); // Show message when no employees
                    } else {
                        boolean hasEmployees = false;
                        for (Employee employee : employees) {
                            hasEmployees = true;

                            TextView nameTextView = new TextView(department_employees_Activity.this);
                            nameTextView.setText(employee.name != null ? employee.name : "Unknown");
                            nameTextView.setTextSize(23);
                            nameTextView.setTypeface(null, Typeface.BOLD);
                            nameTextView.setTextColor(getResources().getColor(R.color.black));
                            nameTextView.setPadding(15, 10, 0, 0);


                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(0, 0, 0, 20);
                            nameTextView.setLayoutParams(params);

                            employeesContainer.addView(nameTextView);
                        }


                        showNoRequestsMessage(!hasEmployees);
                    }
                });
            } else {
                runOnUiThread(() -> Toast.makeText(department_employees_Activity.this, "Head user or department not found", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void showNoRequestsMessage(boolean show) {
        noEmployeesMessage.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void navigateTo(Class<?> activityClass) {
        Intent intent = new Intent(department_employees_Activity.this, activityClass);
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

        Intent intentLogin = new Intent(department_employees_Activity.this, login_Activity.class);
        startActivity(intentLogin);
        finish();
    }
}
