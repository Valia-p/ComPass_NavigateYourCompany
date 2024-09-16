package com.example.compass_navigateyourcompany;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.concurrent.Executors;

public class settings_Activity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText yearEditText;

    private String login_name;
    private AppDatabase db;
    private boolean updateSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone);
        yearEditText = findViewById(R.id.year);
        Button confirmButton = findViewById(R.id.confirm_button);
        Button backButton = findViewById(R.id.back_Button);

        db = AppDatabase.getInstance(this);



        Intent intent = getIntent();
        if (intent != null) {
            login_name = intent.getStringExtra("Name");
        }

        loadUserInfo();

        confirmButton.setOnClickListener(v -> {
            checkUsernameAvailabilityAndUpdate();
        });

        String sourceActivity = getIntent().getStringExtra("sourceActivity");
        backButton.setOnClickListener(v -> {
            Intent intentBack = new Intent();
            switch (sourceActivity) {
                case "home_head":
                    intentBack.setClass(settings_Activity.this, home_head_Activity.class);
                    break;
                case "home_employee":
                    intentBack.setClass(settings_Activity.this, home_employee_Activity.class);
                    break;
                case "home_employer":
                    intentBack.setClass(settings_Activity.this, home_employer_Activity.class);
                    break;
            }

            String usernameToSend = updateSuccess ? usernameEditText.getText().toString() : login_name;
            intentBack.putExtra("Name", usernameToSend);

            startActivity(intentBack);
            finish();
        });
    }

    private void loadUserInfo() {
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = db.userDao().findByLoginName(login_name);
            if (user != null) {
                if (Objects.equals(user.type, "Employer")) {
                    Employer employer = db.employerDao().findByName(login_name);
                    runOnUiThread(() -> {
                        usernameEditText.setText(user.loginName != null ? user.loginName : "");
                        passwordEditText.setText(user.password);
                        emailEditText.setText(employer.mail != null ? employer.mail : "");
                        phoneEditText.setText(employer.phone != null ? employer.phone : "");
                        yearEditText.setVisibility(View.GONE);
                    });
                } else if (Objects.equals(user.type, "Head")) {
                    Head head = db.headDao().getHeadByName(login_name);
                    runOnUiThread(() -> {
                        usernameEditText.setText(user.loginName != null ? user.loginName : "");
                        passwordEditText.setText(user.password);
                        emailEditText.setText(head.mail != null ? head.mail : "");
                        phoneEditText.setText(head.phone != null ? head.phone : "");
                        yearEditText.setText(String.valueOf(head.years));
                        yearEditText.setVisibility(View.VISIBLE);
                    });
                } else if (Objects.equals(user.type, "Employee")) {
                    Employee employee = db.employeeDao().findByName(login_name);
                    runOnUiThread(() -> {
                        usernameEditText.setText(user.loginName != null ? user.loginName : "");
                        passwordEditText.setText(user.password);
                        emailEditText.setText(employee.mail != null ? employee.mail : "");
                        phoneEditText.setText(employee.phone != null ? employee.phone : "");
                        yearEditText.setText(String.valueOf(employee.years));
                        yearEditText.setVisibility(View.VISIBLE);
                    });
                }
            } else {
                runOnUiThread(() -> Toast.makeText(settings_Activity.this, "User not found", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void checkUsernameAvailabilityAndUpdate() {
        String newUsername = usernameEditText.getText().toString();
        String newPassword = passwordEditText.getText().toString();
        String newEmail = emailEditText.getText().toString();
        String newPhone = phoneEditText.getText().toString();
        String newYearStr = yearEditText.getText().toString();

        Executors.newSingleThreadExecutor().execute(() -> {
            User existingUser = db.userDao().findByLoginName(newUsername);
            if (existingUser != null && !existingUser.loginName.equals(login_name)) {
                runOnUiThread(() -> Toast.makeText(settings_Activity.this, "Username already exists", Toast.LENGTH_SHORT).show());
                return;
            }

            updateUserInfo(newUsername, newPassword, newEmail, newPhone, newYearStr);
        });
    }

    private void updateUserInfo(String newUsername, String newPassword, String newEmail, String newPhone, String newYearStr) {

        if (TextUtils.isEmpty(newUsername) || TextUtils.isEmpty(newPassword)) {
            runOnUiThread(() -> Toast.makeText(this, "Username and password are required", Toast.LENGTH_SHORT).show());
            return;
        }

        Integer newYear = null;
        if (!TextUtils.isEmpty(newYearStr)) {
            try {
                newYear = Integer.parseInt(newYearStr);
            } catch (NumberFormatException e) {
                runOnUiThread(() -> Toast.makeText(this, "Invalid year format", Toast.LENGTH_SHORT).show());
                return;
            }
        }

        Integer finalNewYear = newYear;
        Executors.newSingleThreadExecutor().execute(() -> {
            try {

                User user = db.userDao().findByLoginName(login_name);
                if (user != null) {
                    user.loginName = newUsername;
                    user.password = newPassword;

                    if (Objects.equals(user.type, "Employer")) {
                        Employer employer = db.employerDao().findByName(login_name);
                        if (employer != null) {
                            employer.name = newUsername;
                            employer.mail = TextUtils.isEmpty(newEmail) ? employer.mail : newEmail;
                            employer.phone = TextUtils.isEmpty(newPhone) ? employer.phone : newPhone;
                            db.employerDao().updateEmployer(employer);
                        }
                    } else if (Objects.equals(user.type, "Head")) {
                        Head head = db.headDao().getHeadByName(login_name);
                        if (head != null) {

                            head.name = newUsername;
                            head.mail = TextUtils.isEmpty(newEmail) ? head.mail : newEmail;
                            head.phone = TextUtils.isEmpty(newPhone) ? head.phone : newPhone;
                            head.years = finalNewYear != null ? finalNewYear : head.years;
                            db.headDao().updateHead(head);
                        }
                    } else if (Objects.equals(user.type, "Employee")) {
                        Employee employee = db.employeeDao().findByName(login_name);
                        if (employee != null) {
                            employee.name = newUsername;
                            employee.mail = TextUtils.isEmpty(newEmail) ? employee.mail : newEmail;
                            employee.phone = TextUtils.isEmpty(newPhone) ? employee.phone : newPhone;
                            employee.years = finalNewYear != null ? finalNewYear : employee.years;
                            db.employeeDao().updateEmployee(employee);
                        }
                    }

                    db.userDao().updateUser(user);
                    updateSuccess = true;

                    runOnUiThread(() -> Toast.makeText(settings_Activity.this, "Information updated", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(settings_Activity.this, "User not found", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {

                runOnUiThread(() -> Toast.makeText(settings_Activity.this, "Error updating user info", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
