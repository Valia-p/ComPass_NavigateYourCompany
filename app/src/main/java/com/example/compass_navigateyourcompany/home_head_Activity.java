package com.example.compass_navigateyourcompany;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class home_head_Activity extends AppCompatActivity{
    private String login_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_head);

        ImageView profileButton = findViewById(R.id.profile_button);
        Button requestsButton = findViewById(R.id.buttonRequests);
        Button onLeaveButton = findViewById(R.id.onLeaveBtn);
        Button employeesButton = findViewById(R.id.employeesBtn);
        ImageView settingsButton = findViewById(R.id.settings_button);
        ImageView homeButton = findViewById(R.id.home_button);
        ImageView logoutButton = findViewById(R.id.logout_button);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Get user login name from Intent
        Intent intent = getIntent();
        if (intent != null) {
            login_name = intent.getStringExtra("Name");

        }

        settingsButton.setOnClickListener(v -> drawerLayout.openDrawer(navigationView));

        profileButton.setOnClickListener(v -> {
            Intent intentProfile = new Intent(home_head_Activity.this, head_profile_Activity.class);
            intentProfile.putExtra("sourceActivity", "home_head");
            intentProfile.putExtra("loginName", login_name);
            startActivity(intentProfile);
            finish();
        });

        homeButton.setOnClickListener(v -> {
            Intent intentProfile = new Intent(home_head_Activity.this, home_head_Activity.class);
            intentProfile.putExtra("Name", login_name);
            startActivity(intentProfile);
            finish();
        });

        logoutButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Clear all data
            editor.apply();

            Intent intentLogin = new Intent(home_head_Activity.this, login_Activity.class);
            startActivity(intentLogin);
            finish();
        });

        requestsButton.setOnClickListener(v -> {
            Intent intentRequests = new Intent(home_head_Activity.this, RequestsActivity.class);
            intentRequests.putExtra("Name",login_name);
            startActivity(intentRequests);
            finish();
        });

        employeesButton.setOnClickListener(v -> {
            Intent intentRequests = new Intent(home_head_Activity.this, department_employees_Activity.class);
            intentRequests.putExtra("Name",login_name);
            startActivity(intentRequests);
            finish();
        });

        onLeaveButton.setOnClickListener(v -> {
            Intent intentRequests = new Intent(home_head_Activity.this, employees_onLeave_Activity.class);
            intentRequests.putExtra("Name",login_name);
            startActivity(intentRequests);
            finish();
        });

    }
}
