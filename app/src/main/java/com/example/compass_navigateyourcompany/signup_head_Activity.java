package com.example.compass_navigateyourcompany;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class signup_head_Activity extends AppCompatActivity {
    private AppDatabase db;

    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText authTokenEditText;
    private EditText yearEditText;
    private Spinner departmentSpinner;
    private TextView departmentText;
    private View formLayout;
    private View departmentSelectionLayout;
    private Button submitDepartmentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_head);

        db = AppDatabase.getInstance(this);

        nameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone);
        authTokenEditText = findViewById(R.id.auth_token);
        yearEditText = findViewById(R.id.year);
        departmentSpinner = findViewById(R.id.department_spinner);
        departmentText = findViewById(R.id.departmentText);

        formLayout = findViewById(R.id.form_layout);
        departmentSelectionLayout = findViewById(R.id.department_selection_layout);
        submitDepartmentButton = findViewById(R.id.submit_department_button);

        Button backButton = findViewById(R.id.back_Button);
        Button signUpButton = findViewById(R.id.signUp_button);

        backButton.setOnClickListener(v -> finish());

        signUpButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            String authToken = authTokenEditText.getText().toString();
            String yearStr = yearEditText.getText().toString();

            // Highlight required fields
            highlightRequiredFields(nameEditText);
            highlightRequiredFields(passwordEditText);
            highlightRequiredFields(emailEditText);
            highlightRequiredFields(phoneEditText);
            highlightRequiredFields(authTokenEditText);
            highlightRequiredFields(departmentText);
            highlightRequiredFields(yearEditText);

            // Validate input
            if (name.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() || authToken.isEmpty() || yearStr.isEmpty()) {
                Toast.makeText(signup_head_Activity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int year;
            try {
                year = Integer.parseInt(yearStr);
            } catch (NumberFormatException e) {
                Toast.makeText(signup_head_Activity.this, "Invalid Year", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                try {
                    // Check if the name already exists
                    User existingUser = db.userDao().findByLoginName(name);
                    if (existingUser != null) {
                        runOnUiThread(() -> Toast.makeText(signup_head_Activity.this, "User with this name already exists", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    // Check if the authToken is valid
                    Employer employer = db.employerDao().findByAuthToken(authToken);

                    if (employer == null) {
                        runOnUiThread(() -> Toast.makeText(signup_head_Activity.this, "Invalid AuthToken. No matching company found.", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    // Show department selection UI
                    runOnUiThread(() -> {
                        formLayout.setVisibility(View.GONE);
                        departmentSelectionLayout.setVisibility(View.VISIBLE);
                        loadDepartments(authToken);
                    });

                    submitDepartmentButton.setOnClickListener(v1 -> {
                        String selectedDepartment = departmentSpinner.getSelectedItem().toString();
                        if (selectedDepartment.isEmpty()) {
                            Toast.makeText(signup_head_Activity.this, "Please select a department", Toast.LENGTH_SHORT).show();
                        } else {
                            new Thread(() -> {
                                try {
                                    Department department = db.departmentDao().findByName(selectedDepartment);
                                    if (department != null) {
                                        Head head = new Head();
                                        head.name = name;
                                        head.authToken = authToken;
                                        head.mail = email;
                                        head.phone = phone;
                                        head.departmentId = department.id;
                                        head.years = year;

                                        db.headDao().insert(head);

                                        User user = new User();
                                        user.loginName = name;
                                        user.password = password;
                                        user.authToken = authToken;
                                        user.type = "Head";

                                        db.userDao().insert(user);

                                        runOnUiThread(() -> {
                                            Toast.makeText(signup_head_Activity.this, "Sign-Up Successful", Toast.LENGTH_SHORT).show();
                                            clearForm();
                                            startActivity(new Intent(signup_head_Activity.this, home_head_Activity.class));
                                            finish();
                                        });
                                    } else {
                                        runOnUiThread(() -> Toast.makeText(signup_head_Activity.this, "Department not found", Toast.LENGTH_SHORT).show());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    runOnUiThread(() -> Toast.makeText(signup_head_Activity.this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                }
                            }).start();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(signup_head_Activity.this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }).start();
        });
    }

    private void loadDepartments(String authToken) {
        new Thread(() -> {
            try {
                List<Department> departments = db.departmentDao().findByCid(authToken); // Fetch departments based on authToken
                List<String> departmentNames = new ArrayList<>();
                for (Department department : departments) {
                    departmentNames.add(department.Name);
                }
                runOnUiThread(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(signup_head_Activity.this, android.R.layout.simple_spinner_item, departmentNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    departmentSpinner.setAdapter(adapter);
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(signup_head_Activity.this, "Failed to load departments", Toast.LENGTH_SHORT).show());
            }
        }).start();
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

    private void clearForm() {
        nameEditText.setText("");
        passwordEditText.setText("");
        emailEditText.setText("");
        phoneEditText.setText("");
        authTokenEditText.setText("");
        yearEditText.setText("");
        departmentSpinner.setSelection(0);
    }
}
