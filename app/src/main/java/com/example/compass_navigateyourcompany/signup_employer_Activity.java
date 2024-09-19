package com.example.compass_navigateyourcompany;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class signup_employer_Activity extends AppCompatActivity {
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_employer);

        db = AppDatabase.getInstance(this);

        Button backButton = findViewById(R.id.back_button);
        Button signUpButton = findViewById(R.id.signup_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nameEditText = findViewById(R.id.username);
                EditText passwordEditText = findViewById(R.id.password);
                EditText emailEditText = findViewById(R.id.email);
                EditText companyEditText = findViewById(R.id.company);
                EditText authTokenEditText = findViewById(R.id.auth_token);
                EditText addressEditText = findViewById(R.id.address);
                EditText phoneEditText = findViewById(R.id.phone);

                // Highlight required fields
                highlightRequiredFields(nameEditText);
                highlightRequiredFields(passwordEditText);
                highlightRequiredFields(emailEditText);
                highlightRequiredFields(phoneEditText);
                highlightRequiredFields(authTokenEditText);
                highlightRequiredFields(addressEditText);
                highlightRequiredFields(companyEditText);

                String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String company = companyEditText.getText().toString();
                String authToken = authTokenEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String phone = phoneEditText.getText().toString();

                // Validate input
                if (name.isEmpty() || password.isEmpty() || email.isEmpty() || company.isEmpty() || authToken.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(signup_employer_Activity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Check if the employer already exists by name or authToken
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            User existingEmployerByName = db.userDao().findByLoginName(name);
                            Employer existingEmployerByAuthToken = db.employerDao().findByAuthToken(authToken);

                            if (existingEmployerByName != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(signup_employer_Activity.this, "User with this name already exists", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            // Check if authToken already exists
                            else if (existingEmployerByAuthToken != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(signup_employer_Activity.this, "Employer with this authToken already exists", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                // Insert the employer into the database
                                Employer employer = new Employer();
                                employer.name = name;
                                employer.authToken = authToken;
                                employer.companyName = company;
                                employer.address = address;
                                employer.phone = phone;
                                employer.mail = email;

                                db.employerDao().insert(employer);

                                Employer insertedEmployer = db.employerDao().findByName(name);

                                if (insertedEmployer != null) {
                                    User user = new User();
                                    user.loginName = name;
                                    user.password = password;
                                    user.authToken = authToken;
                                    user.type = "Employer";

                                    db.userDao().insert(user);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(signup_employer_Activity.this, "Sign-Up Successful", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(signup_employer_Activity.this, create_department_Activity.class);

                                            // Pass the authToken
                                            intent.putExtra("authToken", authToken);
                                            intent.putExtra("Name", name);

                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            }
                        }
                    }).start();
                }
            }
        });
    }

    private void highlightRequiredFields(TextView textView) {
        if (textView == null) return;

        CharSequence hint = textView.getHint();
        if (hint == null) return;

        String hintString = hint.toString();
        SpannableString spannableString = new SpannableString(hintString);
        ForegroundColorSpan redColorSpan = new ForegroundColorSpan(Color.RED);
        int asteriskPosition = hintString.indexOf("*");
        if (asteriskPosition != -1) {
            spannableString.setSpan(redColorSpan, asteriskPosition, asteriskPosition + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setHint(spannableString);
    }
}
