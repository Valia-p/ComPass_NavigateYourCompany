package com.example.compass_navigateyourcompany;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class home_employer_Activity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private ImageView profileButton, homeButton, logoutButton, settingsButton;
    private AppDatabase db;
    private String login_name;
    private TextView departmentTextView, employeeNameTextView;
    private User user = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_employer); // Ensure this is the correct XML file

        // Initialize views and database
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileButton = findViewById(R.id.profile_button);
        homeButton = findViewById(R.id.home_button);
        logoutButton = findViewById(R.id.logout_button);
        settingsButton = findViewById(R.id.settings_button);
        departmentTextView = findViewById(R.id.department);
        employeeNameTextView = findViewById(R.id.employee_name);

        Intent intent = getIntent();
        if (intent != null) {
            login_name = intent.getStringExtra("Name");
            user = db.userDao().findByLoginName(login_name);
            Log.d("home_employer_Activity", "Received loginName: " + login_name);
        }

        DepartmentInfo(user);

        settingsButton.setOnClickListener(v -> drawerLayout.openDrawer(navigationView));

        profileButton.setOnClickListener(v -> {
            String loginName = login_name;
            Intent intentProfile = new Intent(home_employer_Activity.this, employer_profile_Activity.class);
            intentProfile.putExtra("loginName", loginName);
            startActivity(intentProfile);
            finish();
        });

        homeButton.setOnClickListener(v -> {
            String loginName = login_name;
            Intent intentProfile = new Intent(home_employer_Activity.this, home_employer_Activity.class);
            intentProfile.putExtra("Name", loginName);
            startActivity(intentProfile);
            finish();
        });

        logoutButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Clear all data
            editor.apply();

            Intent intentLogin = new Intent(home_employer_Activity.this, login_Activity.class);
            startActivity(intentLogin);
            finish();
        });
    }

    private void DepartmentInfo(User user) {
        new Thread(() -> {
            try {

                List<Department> departments = db.departmentDao().findByCid(user.authToken);

                // Updating the UI
                runOnUiThread(() -> {
                    if (departments != null && !departments.isEmpty()) {
                        StringBuilder departmentNames = new StringBuilder();
                        for (Department department : departments) {
                            departmentNames.append(department.getName()).append("\n");
                        }
                        departmentTextView.setText(departmentNames.toString().trim());
                    } else {
                        departmentTextView.setText("No departments found");
                    }
                    employeeNameTextView.setText(login_name);
                });
            } catch (Exception e) {
                Log.e("home_employer_Activity", "Error fetching department info", e);
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            super.onBackPressed();
        }
    }
}
