package com.example.compass_navigateyourcompany;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class employee_profile_Activity extends AppCompatActivity{


    private AppDatabase db;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private TextView departmentTextView;
    private TextView supervisorTextView;
    private TextView yearsTextView;



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

        findViewById(R.id.back_button).setOnClickListener(v -> {
            Intent intent = new Intent(employee_profile_Activity.this, home_employee_Activity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.home_button).setOnClickListener(v -> {
            Intent intent = new Intent(employee_profile_Activity.this, home_employee_Activity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.logout_button).setOnClickListener(v -> {
            // Optionally handle logout
            finish();
        });


        loadProfile();
    }

    private void loadProfile() {
        new Thread(() -> {
            try {

                //String employeeLoginName = getIntent().getStringExtra("loginName");
                String employeeLoginName = "Gio";
                Employee employee = db.employeeDao().findByName(employeeLoginName);
                if (employee == null) {
                    runOnUiThread(() -> Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show());
                    return;
                }

                Department department = db.departmentDao().findById(employee.departmentId);
                String departmentName = department != null ? department.Name : "Unknown Department";

                String supervisor = db.headDao().findSupervisor(employee.authToken, employee.departmentId);

                // Update UI
                runOnUiThread(() -> {
                    nameTextView.setText(employee.name);
                    emailTextView.setText(employee.mail);
                    phoneTextView.setText(employee.phone);
                    departmentTextView.setText(departmentName);
                    supervisorTextView.setText(supervisor);
                    yearsTextView.setText(String.valueOf(employee.years));
                });
            }
            catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}

