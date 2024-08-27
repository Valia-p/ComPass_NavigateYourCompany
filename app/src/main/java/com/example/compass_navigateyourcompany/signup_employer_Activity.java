package com.example.compass_navigateyourcompany;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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

                ForegroundColorSpan redColorSpan = new ForegroundColorSpan(Color.RED);
                String hintName = nameEditText.getHint().toString();
                SpannableString spannableString1 = new SpannableString(hintName);
                int asteriskPosition1 = hintName.indexOf("*");
                if (asteriskPosition1 != -1) {
                    spannableString1.setSpan(redColorSpan, asteriskPosition1, asteriskPosition1 + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                nameEditText.setHint(spannableString1);

                String hintPassword = passwordEditText.getHint().toString();
                SpannableString spannableString2 = new SpannableString(hintPassword);
                int asteriskPosition2 = hintPassword.indexOf("*");
                if (asteriskPosition2 != -1) {
                    spannableString2.setSpan(redColorSpan, asteriskPosition2, asteriskPosition2 + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                passwordEditText.setHint(spannableString2);

                String hintEmail = emailEditText.getHint().toString();
                SpannableString spannableString3 = new SpannableString(hintEmail);
                int asteriskPosition3 = hintEmail.indexOf("*");
                if (asteriskPosition3 != -1) {
                    spannableString3.setSpan(redColorSpan, asteriskPosition3, asteriskPosition3 + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                emailEditText.setHint(spannableString3);

                String hintAuthToken = authTokenEditText.getHint().toString();
                SpannableString spannableString4 = new SpannableString(hintAuthToken);
                int asteriskPosition4 = hintAuthToken.indexOf("*");
                if (asteriskPosition4 != -1) {
                    spannableString4.setSpan(redColorSpan, asteriskPosition4, asteriskPosition4 + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                authTokenEditText.setHint(spannableString4);

                String hintPhone = phoneEditText.getHint().toString();
                SpannableString spannableString5 = new SpannableString(hintPhone);
                int asteriskPosition5 = hintPhone.indexOf("*");
                if (asteriskPosition5 != -1) {
                    spannableString5.setSpan(redColorSpan, asteriskPosition5, asteriskPosition5 + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                phoneEditText.setHint(spannableString5);

                String hintCompany = companyEditText.getHint().toString();
                SpannableString spannableString6 = new SpannableString(hintCompany);
                int asteriskPosition6 = hintCompany.indexOf("*");
                if (asteriskPosition6 != -1) {
                    spannableString6.setSpan(redColorSpan, asteriskPosition6, asteriskPosition6 + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                companyEditText.setHint(spannableString6);

                String hintAddress = addressEditText.getHint().toString();
                SpannableString spannableString7 = new SpannableString(hintAddress);
                int asteriskPosition7 = hintAddress.indexOf("*");
                if (asteriskPosition7 != -1) {
                    spannableString7.setSpan(redColorSpan, asteriskPosition7, asteriskPosition7 + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                addressEditText.setHint(spannableString7);

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