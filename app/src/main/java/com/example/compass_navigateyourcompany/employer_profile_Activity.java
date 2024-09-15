package com.example.compass_navigateyourcompany;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
    private String loginName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employer_profile);

        db = AppDatabase.getInstance(this);

        Intent intent = getIntent();
        if (intent != null) {
            loginName = intent.getStringExtra("Name");
        }

        companyTextView = findViewById(R.id.companyName);
        employerTextView = findViewById(R.id.employer_name);
        phoneTextView = findViewById(R.id.phone);
        emailTextView = findViewById(R.id.email);
        addressTextView = findViewById(R.id.address);

        ImageView backButton = findViewById(R.id.back_button);
        ImageView homeButton = findViewById(R.id.home_button);
        ImageView logoutButton = findViewById(R.id.logout_button);


        backButton.setOnClickListener(v -> navigateTo(home_employer_Activity.class));
        homeButton.setOnClickListener(v -> navigateTo(home_employer_Activity.class));
        logoutButton.setOnClickListener(this::logout);

        loadProfile(loginName);
    }


    private void loadProfile(String employerLoginName) {
        new Thread(() -> {
            try {

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

    private void navigateTo(Class<?> activityClass) {
        Intent intent = new Intent(employer_profile_Activity.this, activityClass);
        intent.putExtra("Name", loginName);
        startActivity(intent);
        finish();
    }

    private void logout(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intentLogin = new Intent(employer_profile_Activity.this, login_Activity.class);
        startActivity(intentLogin);
        finish();
    }
}
