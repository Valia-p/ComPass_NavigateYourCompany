package com.example.compass_navigateyourcompany;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class signup_employer_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_employer);

        Button backButton = findViewById(R.id.back_button);
        Button signUpButton = findViewById(R.id.signup_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Ends the current activity and returns to the previous one
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input from EditText fields
                EditText nameEditText = findViewById(R.id.username);
                EditText passwordEditText = findViewById(R.id.password);
                EditText emailEditText = findViewById(R.id.email);
                EditText companyEditText = findViewById(R.id.company);
                EditText authTokenEditText = findViewById(R.id.auth_token);
                EditText addressEditText = findViewById(R.id.address);
                EditText phoneEditText = findViewById(R.id.phone);

                String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String company = companyEditText.getText().toString();
                String authToken = authTokenEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String phone = phoneEditText.getText().toString();

                // Perform sign-up logic here
                if (name.isEmpty() || password.isEmpty() || email.isEmpty() || company.isEmpty() || authToken.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(signup_employer_Activity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Assume sign-up is successful
                    Toast.makeText(signup_employer_Activity.this, "Sign-Up Successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}