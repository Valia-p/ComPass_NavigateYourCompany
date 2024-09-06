package com.example.compass_navigateyourcompany;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class create_department_Activity extends AppCompatActivity {
    private EditText departmentNameEditText;
    private ListView departmentListView;
    private ArrayList<String> departmentList;
    private ArrayAdapter<String> adapter;
    private AppDatabase db;
    private String authToken; // hold the employer's authToken

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_department);

        db = AppDatabase.getInstance(this);

        // Get the authToken passed from the previous activity
        Intent intent = getIntent();
        authToken = intent.getStringExtra("authToken");

        Button addButton = findViewById(R.id.add_button);
        Button cancelButton = findViewById(R.id.cancel_button);
        Button submitButton = findViewById(R.id.submit_button);
        Button backButton = findViewById(R.id.back_button);
        departmentNameEditText = findViewById(R.id.department_name);
        departmentListView = findViewById(R.id.department_list);

        departmentList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, departmentList);
        departmentListView.setAdapter(adapter);

        // Add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String departmentName = departmentNameEditText.getText().toString();
                if (departmentName.isEmpty()) {
                    Toast.makeText(create_department_Activity.this, "Please enter a department name", Toast.LENGTH_SHORT).show();
                } else {
                    // Add the department name to the list
                    departmentList.add(departmentName);
                    adapter.notifyDataSetChanged();
                    departmentNameEditText.setText("");
                }
            }
        });

        // Cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the department name field
                departmentNameEditText.setText("");
                Toast.makeText(create_department_Activity.this, "Operation Cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        // Submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (departmentList.isEmpty()) {
                    Toast.makeText(create_department_Activity.this, "No departments to submit", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String cid = authToken;

                                // Insert each department into the database
                                for (String departmentName : departmentList) {
                                    Department department = new Department();
                                    department.Name = departmentName;
                                    department.Cid = cid;
                                    db.departmentDao().insert(department);
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(create_department_Activity.this, "Departments successfully added", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(create_department_Activity.this, home_employer_Activity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(create_department_Activity.this, "An error occurred while adding departments", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });

        // Back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(create_department_Activity.this, signup_employer_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}