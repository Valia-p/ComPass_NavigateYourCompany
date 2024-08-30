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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_department);


        Button addButton = findViewById(R.id.add_button);
        Button cancelButton = findViewById(R.id.cancel_button);
        Button submitButton = findViewById(R.id.submit_button);
        Button backButton = findViewById(R.id.back_button);
        departmentNameEditText = findViewById(R.id.department_name);
        departmentListView = findViewById(R.id.department_list);


        departmentList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, departmentList);
        departmentListView.setAdapter(adapter);

        // add button
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
                    departmentNameEditText.setText(""); // Clear the input field
                }
            }
        });

        // cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the department name field
                departmentNameEditText.setText("");
                Toast.makeText(create_department_Activity.this, "Operation Cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        // submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (departmentList.isEmpty()) {
                    Toast.makeText(create_department_Activity.this, "No departments to submit", Toast.LENGTH_SHORT).show();
                }
                else {

                    Intent intent = new Intent(create_department_Activity.this, home_employer_Activity.class);

                    // Optional: If you need to pass the department list to the new activity
                    intent.putStringArrayListExtra("departments", departmentList);

                    startActivity(intent);
                    finish();
                }
            }
        });

        // back button
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
