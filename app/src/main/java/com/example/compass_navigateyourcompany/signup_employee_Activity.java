package com.example.compass_navigateyourcompany;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class signup_employee_Activity extends AppCompatActivity {
    private AppDatabase db;
    private static final int PICK_FILE_REQUEST = 1;
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
    private String selectedFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_employee);

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

        signUpButton.setOnClickListener(v -> handleSignUp());

        submitDepartmentButton.setOnClickListener(v -> handleSubmitDepartment());
        findViewById(R.id.button_upload).setOnClickListener(v -> openFileChooser());
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                String filePath = getRealPathFromURI(uri);

                if (filePath != null) {
                    selectedFilePath = filePath;
                    Log.d("FileSelection", "Selected file path: " + selectedFilePath);
                    Toast.makeText(this, "Selected: " + selectedFilePath, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("FileSelection", "Failed to get file path from URI.");
                    Toast.makeText(this, "Failed to get file path", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("FileSelection", "URI is null.");
                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("FileSelection", "Result code: " + resultCode + ", Intent data: " + data);
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    private String getRealPathFromURI(Uri uri) {
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};

        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    path = cursor.getString(column_index);
                }
            }
        } catch (Exception e) {
            Log.e("getRealPathFromURI", "Failed to get real path from URI", e);
        }

        if (path == null) {
            // Fall back to using the URI path
            path = uri.getPath();
        }

        return path;
    }

    private void handleSignUp() {
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
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        new Thread(() -> {
            try {
                // Check if the name already exists
                User existingUser = db.userDao().findByLoginName(name);
                if (existingUser != null) {
                    runOnUiThread(() -> Toast.makeText(this, "User with this name already exists", Toast.LENGTH_SHORT).show());
                    return;
                }

                // Check if the authToken is valid
                Employer employer = db.employerDao().findByAuthToken(authToken);
                if (employer == null) {
                    runOnUiThread(() -> Toast.makeText(this, "Invalid AuthToken. No matching company found.", Toast.LENGTH_SHORT).show());
                    return;
                }

                // Show department selection UI
                runOnUiThread(() -> {
                    formLayout.setVisibility(View.GONE);
                    departmentSelectionLayout.setVisibility(View.VISIBLE);
                    loadDepartments(authToken);
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }



    private void handleSubmitDepartment() {
        String selectedDepartment = departmentSpinner.getSelectedItem().toString();
        if (selectedDepartment.isEmpty()) {
            Toast.makeText(this, "Please select a department", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = nameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String authToken = authTokenEditText.getText().toString();
        String yearStr = yearEditText.getText().toString();

        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid Year", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                Department department = db.departmentDao().findByName(selectedDepartment);
                if (department != null) {
                    Employee employee = new Employee();
                    employee.name = name;
                    employee.authToken = authToken;
                    employee.mail = email;
                    employee.phone = phone;
                    employee.departmentId = department.id;
                    employee.years = year;

                    db.employeeDao().insert(employee);

                    User user = new User();
                    user.loginName = name;
                    user.password = password;
                    user.authToken = authToken;
                    user.type = "Employee";

                    db.userDao().insert(user);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Sign-Up Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, home_employee_Activity.class);
                        intent.putExtra("Name", name);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Department not found", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void loadDepartments(String authToken) {
        new Thread(() -> {
            try {
                List<Department> departments = db.departmentDao().findByCid(authToken);
                List<String> departmentNames = new ArrayList<>();
                for (Department department : departments) {
                    departmentNames.add(department.Name);
                }
                runOnUiThread(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departmentNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    departmentSpinner.setAdapter(adapter);
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Failed to load departments", Toast.LENGTH_SHORT).show());
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

}
