package com.example.compass_navigateyourcompany;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//for file uploading
import android.net.Uri;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

// SHOULD CHECK ON HOW TO UPLOAD A FILE FROM EXTERNAL STORAGE

public class signup_employee_Activity extends AppCompatActivity {

    //for file uploading
    private static final int PICK_FILE = 100;
    private Uri selectedFileUri;
    private String selectedFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_employee);

        Button backButton = findViewById(R.id.back_button);
        Button signUpButton = findViewById(R.id.signUp_button);
        ImageButton cvBtn = findViewById(R.id.button_upload);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });

        //for file uploading
        cvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFiles();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameEditText = findViewById(R.id.username);
                EditText passwordEditText = findViewById(R.id.password);
                EditText emailEditText = findViewById(R.id.email);
                EditText phoneEditText = findViewById(R.id.phone);
                Spinner departmentSpinner = findViewById(R.id.department);
                EditText authTokenEditText = findViewById(R.id.auth_token);
                EditText experienceEditText = findViewById(R.id.experience);
                TextView departmentText = findViewById(R.id.departmentText);

                String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String department = departmentSpinner.getSelectedItem().toString();
                String authToken = authTokenEditText.getText().toString();
                String experience = experienceEditText.getText().toString();
                String depText = departmentText.getText().toString();

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

                String hintPhone = phoneEditText.getHint().toString();
                SpannableString spannableString4 = new SpannableString(hintPhone);
                int asteriskPosition4 = hintPhone.indexOf("*");
                if (asteriskPosition4 != -1) {
                    spannableString4.setSpan(redColorSpan, asteriskPosition4, asteriskPosition4 + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                phoneEditText.setHint(spannableString4);

                String hintAuthToken = authTokenEditText.getHint().toString();
                SpannableString spannableString5 = new SpannableString(hintAuthToken);
                int asteriskPosition5 = hintAuthToken.indexOf("*");
                if (asteriskPosition5 != -1) {
                    spannableString5.setSpan(redColorSpan, asteriskPosition5, asteriskPosition5 + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                authTokenEditText.setHint(spannableString5);

                String hintExp = experienceEditText.getHint().toString();
                SpannableString spannableString6 = new SpannableString(hintExp);
                int asteriskPosition6 = hintExp.indexOf("*");
                if (asteriskPosition6 != -1) {
                    spannableString6.setSpan(redColorSpan, asteriskPosition6, asteriskPosition6 + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                experienceEditText.setHint(spannableString6);

                SpannableString spannableString7 = new SpannableString(depText);
                int asteriskPosition7 = depText.indexOf("*");
                if (asteriskPosition7 != -1) {
                    spannableString7.setSpan(redColorSpan, asteriskPosition7, asteriskPosition7 + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                departmentText.setText(spannableString7);

                // for file uploading
                if (selectedFileUri != null) {
                    uploadFile(selectedFilePath);
                }
//                else {
//                    Toast.makeText(signup_employee_Activity.this, "Please select your CV file", Toast.LENGTH_SHORT).show();
//                }

                if (name.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() || authToken.isEmpty() || experience.isEmpty() || department.isEmpty()) {
                    Toast.makeText(signup_employee_Activity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    //successful sign up
                    nameEditText.setText("");
                    passwordEditText.setText("");
                    emailEditText.setText("");
                    phoneEditText.setText("");
                    authTokenEditText.setText("");
                    departmentSpinner.setSelection(0);
                    experienceEditText.setText("");
                    Toast.makeText(signup_employee_Activity.this, "Sign-Up Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(signup_employee_Activity.this, home_employee_Activity.class);
                    // if i want to pass extra data: intent.putExtra("username", name);
                    // in home_employee: String username = getIntent().getStringExtra("username");
                    startActivity(intent);
                    finish();

                }
            }
        });
    }

    // for file uploading - open Files to pick a file
    private void openFiles() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE);
    }

    @Override // handle the result of file selection
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_FILE) {
            if (data != null) {
                selectedFileUri = data.getData();
                selectedFilePath = getPathFromUri(selectedFileUri);
            }
        }
    }

    // convert the uri to a file path
    private String getPathFromUri(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return null;
    }

    // method to upload the file
    private void uploadFile(String filepath) {
        File file = new File(filepath);
        if (file.exists()) {
            // placeholder for actual file upload logic
            Toast.makeText(this, "File ready for upload: " + filepath, Toast.LENGTH_SHORT).show();
            Log.d("signup_employee_Activity", "File path: " + filepath);
        }
        else {
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
        }
    }
}
