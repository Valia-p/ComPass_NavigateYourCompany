package com.example.compass_navigateyourcompany;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class select_type_Activity extends AppCompatActivity {
    private Button employerButton;
    private Button headButton;
    private Button employeeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_type);

        employerButton = findViewById(R.id.button1);
        headButton = findViewById(R.id.button2);
        employeeButton = findViewById(R.id.button3);

        employerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Employer button
                Intent intent = new Intent(select_type_Activity.this, signup_employer_Activity.class);
                startActivity(intent);
            }
        });

        headButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Head button
                Intent intent = new Intent(select_type_Activity.this, signup_head_Activity.class);
                startActivity(intent);
            }
        });

        employeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Employee button
                Intent intent;
                intent = new Intent(select_type_Activity.this, signup_employee_Activity.class);
                startActivity(intent);
            }
        });
    }
}
