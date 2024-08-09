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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class signup_head_Activity extends AppCompatActivity {

    Button backButton = findViewById(R.id.back_Button);
    Button signUpButton = findViewById(R.id.signUp_button);
    EditText nameEditText = findViewById(R.id.username);
    EditText passwordEditText = findViewById(R.id.password);
    EditText emailEditText = findViewById(R.id.email);
    EditText phoneEditText = findViewById(R.id.phone);
    EditText authTokenEditText = findViewById(R.id.auth_token);
    Spinner departmentSpinner = findViewById(R.id.department);
    TextView departmentText = findViewById(R.id.departmentText);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_head);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String authToken = authTokenEditText.getText().toString();
                String department = departmentSpinner.getSelectedItem().toString();
                String depText = departmentText.toString();

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

                SpannableString spannableString6 = new SpannableString(depText);
                int asteriskPosition6 = depText.indexOf("*");
                if (asteriskPosition6 != -1) {
                    spannableString6.setSpan(redColorSpan, asteriskPosition6, asteriskPosition6 + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                departmentText.setHint(spannableString6);

                //unsuccessful sign up
                if (name.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() || authToken.isEmpty() || department.isEmpty()) {
                    Toast.makeText(signup_head_Activity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    //successful sign up
                    //clearForm();
                    Toast.makeText(signup_head_Activity.this, "Sign-Up Successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clearForm() {
        nameEditText.setText("");
        passwordEditText.setText("");
        emailEditText.setText("");
        phoneEditText.setText("");
        authTokenEditText.setText("");
        departmentSpinner.setSelection(0);
    }
}
