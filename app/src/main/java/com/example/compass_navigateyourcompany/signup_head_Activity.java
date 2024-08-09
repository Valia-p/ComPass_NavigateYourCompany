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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class signup_head_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_head);

        Button backButton = findViewById(R.id.back_Button);
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

                String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String authToken = authTokenEditText.getText().toString();
                String department = departmentSpinner.getSelectedItem().toString();

                String hintName = nameEditText.getHint().toString();
                SpannableString spannableString = new SpannableString(hintName);
                int asteriskPosition = hintName.indexOf("*");
                if (asteriskPosition != -1) {
                    ForegroundColorSpan redColorSpan = new ForegroundColorSpan(Color.RED);
                    spannableString.setSpan(redColorSpan, asteriskPosition, asteriskPosition + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                nameEditText.setHint(spannableString);

                //unsuccessful sign up
                if (name.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() || authToken.isEmpty() || department.isEmpty()) {
                    Toast.makeText(signup_head_Activity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    //successful sign up
                    Toast.makeText(signup_head_Activity.this, "Sign-Up Successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
