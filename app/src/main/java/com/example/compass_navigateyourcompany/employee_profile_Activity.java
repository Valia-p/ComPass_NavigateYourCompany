package com.example.compass_navigateyourcompany;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class employee_profile_Activity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInctanceState) {
        super.onCreate(savedInctanceState);
        setContentView(R.layout.employee_profile);

        ImageView backButton = findViewById(R.id.back_button);
        ImageView homeButton = findViewById(R.id.home_button);
        ImageView logoutButton = findViewById(R.id.logout_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(employee_profile_Activity.this, home_employee_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(employee_profile_Activity.this, home_employee_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(head_profile_Activity.this, login_Activity.class);
//                startActivity(intent);
                finish();
            }
        });
    }
}
