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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class signup_employee_Activity extends AppCompatActivity {
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_employee);

        db = AppDatabase.getInstance(this);

        Button backButton = findViewById(R.id.back_button);
        Button signUpButton = findViewById(R.id.signUp_button);

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
                EditText phoneEditText = findViewById(R.id.phone);
                EditText authTokenEditText = findViewById(R.id.auth_token);
                Spinner departmentSpinner = findViewById(R.id.department);
                TextView departmentText = findViewById(R.id.departmentText);

                String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String authToken = authTokenEditText.getText().toString();
                String department = departmentSpinner.getSelectedItem().toString();

                // Highlight required fields
                highlightRequiredFields(nameEditText);
                highlightRequiredFields(passwordEditText);
                highlightRequiredFields(emailEditText);
                highlightRequiredFields(phoneEditText);
                highlightRequiredFields(authTokenEditText);
                highlightRequiredFields(departmentText);

                // Validate input
                if (name.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() || authToken.isEmpty() || department.isEmpty()) {
                    Toast.makeText(signup_employee_Activity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if the authToken is valid
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Employer employer = db.employerDao().findByAuthToken(authToken);

                            if (employer == null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(signup_employee_Activity.this, "Invalid AuthToken. No matching employer found.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }

                            // Check if the employee already exists
                            Employee existingEmployee = db.employeeDao().findByName(name);

                            if (existingEmployee != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(signup_employee_Activity.this, "Employee already exists", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                // Insert the employee into the database
                                Employee employee = new Employee();
                                employee.name = name;
                                employee.authToken = authToken;
                                employee.mail = email;
                                employee.phone = phone;
                                employee.departmentId = Integer.parseInt(department);
                                employee.years = 0;

                                db.employeeDao().insert(employee);

                                Employee insertedEmployee = db.employeeDao().findByName(name);

                                if (insertedEmployee != null) {
                                    User user = new User();
                                    user.loginName = name;
                                    user.password = password;
                                    user.authToken = authToken;
                                    user.type = "Employee";

                                    db.userDao().insert(user);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(signup_employee_Activity.this, "Sign-Up Successful", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(signup_employee_Activity.this, home_employee_Activity.class);
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
        String hint = textView.getHint().toString();
        SpannableString spannableString = new SpannableString(hint);
        ForegroundColorSpan redColorSpan = new ForegroundColorSpan(Color.RED);
        int asteriskPosition = hint.indexOf("*");
        if (asteriskPosition != -1) {
            spannableString.setSpan(redColorSpan, asteriskPosition, asteriskPosition + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setHint(spannableString);
    }
}
