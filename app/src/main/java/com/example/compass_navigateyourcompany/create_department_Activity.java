package com.example.compass_navigateyourcompany;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class create_department_Activity extends AppCompatActivity {
    private EditText departmentNameEditText;
    private LinearLayout departmentContainer;
    private ArrayList<String> departmentList;
    private AppDatabase db;
    private String authToken;
    private String name;
    private String sourceActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_department);

        db = AppDatabase.getInstance(this);

        // Retrieve data from Intent
        Intent intent = getIntent();
        if (intent != null) {
            name = intent.getStringExtra("Name");
            authToken = intent.getStringExtra("authToken");
            sourceActivity = intent.getStringExtra("sourceActivity"); // Get sourceActivity value
        }

        Button addButton = findViewById(R.id.add_button);
        Button cancelButton = findViewById(R.id.cancel_button);
        Button submitButton = findViewById(R.id.submit_button);
        Button backButton = findViewById(R.id.back_button);
        departmentNameEditText = findViewById(R.id.department_name);
        departmentContainer = findViewById(R.id.department_container);

        departmentList = new ArrayList<>();

        // Add button
        addButton.setOnClickListener(v -> {
            String departmentName = departmentNameEditText.getText().toString();

            if (departmentName.isEmpty()) {
                Toast.makeText(create_department_Activity.this, "Please enter a department name", Toast.LENGTH_SHORT).show();
            } else if (departmentList.contains(departmentName)) {
                Toast.makeText(create_department_Activity.this, "Department already added", Toast.LENGTH_SHORT).show();
            } else {
                departmentList.add(departmentName);
                addDepartmentToScrollView(departmentName);
                departmentNameEditText.setText("");
            }
        });

        // Cancel button
        cancelButton.setOnClickListener(v -> {
            departmentNameEditText.setText("");
            Toast.makeText(create_department_Activity.this, "Operation Cancelled", Toast.LENGTH_SHORT).show();
        });

        // Submit button
        submitButton.setOnClickListener(v -> {
            if (departmentList.isEmpty()) {
                Toast.makeText(create_department_Activity.this, "No departments to submit", Toast.LENGTH_SHORT).show();
            } else {
                new Thread(() -> {
                    try {
                        String cid = authToken;
                        for (String departmentName : departmentList) {
                            Department department = new Department();
                            department.Name = departmentName;
                            department.Cid = cid;
                            db.departmentDao().insert(department);
                        }

                        runOnUiThread(() -> {
                            Toast.makeText(create_department_Activity.this, "Departments successfully added", Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(create_department_Activity.this, home_employer_Activity.class);
                            homeIntent.putExtra("Name", name);
                            startActivity(homeIntent);
                            finish();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(create_department_Activity.this, "An error occurred while adding departments", Toast.LENGTH_SHORT).show());
                    }
                }).start();
            }
        });

        // Back button
        backButton.setOnClickListener(v -> {
            Intent backIntent;
            if ("homeEmployer".equals(sourceActivity)) {
                backIntent = new Intent(create_department_Activity.this, home_employer_Activity.class);
            } else {
                backIntent = new Intent(create_department_Activity.this, signup_employer_Activity.class);
            }
            backIntent.putExtra("Name", name); // Pass the name if needed
            startActivity(backIntent);
            finish();
        });
    }

    private void addDepartmentToScrollView(final String departmentName) {
        LinearLayout departmentEntryLayout = new LinearLayout(this);
        departmentEntryLayout.setOrientation(LinearLayout.HORIZONTAL);
        departmentEntryLayout.setPadding(0, 10, 0, 10);
        departmentEntryLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_corners));
        departmentEntryLayout.setElevation(4);
        LinearLayout.LayoutParams departmentLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        departmentLayoutParams.setMargins(0, 0, 0, 8);
        departmentEntryLayout.setLayoutParams(departmentLayoutParams);

        TextView departmentTextView = new TextView(this);
        departmentTextView.setText("- " + departmentName);
        departmentTextView.setTextSize(18);
        departmentTextView.setPadding(10, 10, 10, 10);
        departmentTextView.setTextColor(getResources().getColor(R.color.main_color));

        Button deleteButton = new Button(this);
        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        deleteButton.setBackgroundResource(R.drawable.bin); // Use your bin icon drawable
        deleteButton.setPadding(16, 16, 16, 16);
        deleteButton.setOnClickListener(v -> {
            departmentList.remove(departmentName);
            departmentContainer.removeView(departmentEntryLayout);
        });

        departmentEntryLayout.addView(departmentTextView);
        departmentEntryLayout.addView(deleteButton);

        departmentContainer.addView(departmentEntryLayout);
    }
}
