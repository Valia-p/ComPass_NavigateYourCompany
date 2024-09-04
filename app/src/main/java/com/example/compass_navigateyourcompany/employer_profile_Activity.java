package com.example.compass_navigateyourcompany;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class employer_profile_Activity extends AppCompatActivity{

    private AppDatabase db;
    private TextView companyTextView;
    private TextView employerTextView;
    private TextView phoneTextView;
    private TextView emailTextView;
    private TextView addressTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employer_profile);

        db = AppDatabase.getInstance(this);

        companyTextView = findViewById(R.id.companyName);
        employerTextView = findViewById(R.id.employer_name);
        phoneTextView = findViewById(R.id.phone);
        emailTextView = findViewById(R.id.email);
        addressTextView = findViewById(R.id.address);


        findViewById(R.id.back_button).setOnClickListener(v -> {
            Intent intent = new Intent(employer_profile_Activity.this, home_employer_Activity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.home_button).setOnClickListener(v -> {
            Intent intent = new Intent(employer_profile_Activity.this, home_employer_Activity.class);
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
                //String employerLoginName = getIntent().getStringExtra("loginName");
                String employerLoginName = "Valia";
                Employer employer = db.employerDao().findByName(employerLoginName);
                if (employer == null) {
                    runOnUiThread(() -> Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show());
                    return;
                }


                // Update UI
                runOnUiThread(() -> {
                    companyTextView.setText(employer.companyName);
                    employerTextView.setText(employerLoginName);
                    emailTextView.setText(employer.mail);
                    phoneTextView.setText(employer.phone);
                    addressTextView.setText(employer.address);
                });
            }
            catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
