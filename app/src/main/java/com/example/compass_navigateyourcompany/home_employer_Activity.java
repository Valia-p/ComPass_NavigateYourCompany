package com.example.compass_navigateyourcompany;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import java.util.List;


public class home_employer_Activity extends AppCompatActivity {

    private LinearLayout userListContainer;
    private AppDatabase db;
    private String login_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_employer);

        userListContainer = findViewById(R.id.employee_container);
        ImageView profileButton = findViewById(R.id.profile_button);
        ImageView homeButton = findViewById(R.id.home_button);
        ImageView logoutButton = findViewById(R.id.logout_button);
        ImageView settingsButton = findViewById(R.id.settings_button);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        db = AppDatabase.getInstance(this);


        Intent intent = getIntent();
        if (intent != null) {
            login_name = intent.getStringExtra("Name");
        }

        new LoadUsersTask().execute(login_name);

        settingsButton.setOnClickListener(v -> drawerLayout.openDrawer(navigationView));
        profileButton.setOnClickListener(v -> navigateTo(employer_profile_Activity.class));
        homeButton.setOnClickListener(v -> navigateTo(home_employer_Activity.class));
        logoutButton.setOnClickListener(this::logout);
    }

    private class LoadUsersTask extends AsyncTask<String, Void, List<Department>> {
        @Override
        protected List<Department> doInBackground(String... params) {
            String loginName = params[0];
            User currentUser = db.userDao().findByLoginName(loginName);
            if (currentUser != null) {
                String authToken = currentUser.authToken;
                return db.departmentDao().findByCid(authToken);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Department> departments) {
            if (departments != null) {
                userListContainer.removeAllViews(); // Clear existing views

                for (Department department : departments) {
                    // Create and configure department layout
                    LinearLayout departmentLayout = new LinearLayout(home_employer_Activity.this);
                    departmentLayout.setOrientation(LinearLayout.VERTICAL);
                    departmentLayout.setPadding(16, 16, 16, 16);
                    departmentLayout.setBackgroundResource(R.drawable.rounded_corners);

                    // Create and configure department TextView
                    TextView departmentTextView = new TextView(home_employer_Activity.this);
                    departmentTextView.setText(department.getName());
                    departmentTextView.setTextSize(24);
                    departmentTextView.setTypeface(null, Typeface.BOLD);
                    departmentTextView.setTextColor(getResources().getColor(R.color.bg_color));
                    departmentTextView.setPadding(0, 0, 0, 8);

                    // Add department TextView to department layout
                    departmentLayout.addView(departmentTextView);

                    // Fetch and add head of the department
                    new LoadHeadTask(departmentLayout).execute(department.id);

                    // Add department layout to user list container
                    userListContainer.addView(departmentLayout);
                }
            } else {
                Toast.makeText(home_employer_Activity.this, "User not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class LoadHeadTask extends AsyncTask<Integer, Void, Head> {
        private LinearLayout departmentLayout;

        LoadHeadTask(LinearLayout departmentLayout) {
            this.departmentLayout = departmentLayout;
        }

        @Override
        protected Head doInBackground(Integer... params) {
            return db.headDao().getHeadByDepartmentId(params[0]);
        }

        @Override
        protected void onPostExecute(Head head) {
            if (head != null) {
                LinearLayout headLayout = new LinearLayout(home_employer_Activity.this);
                headLayout.setOrientation(LinearLayout.VERTICAL);
                headLayout.setPadding(16, 16, 16, 16);
                headLayout.setBackgroundResource(R.drawable.rounded_corners);

                // Create and configure head TextView
                TextView headTextView = new TextView(home_employer_Activity.this);
                headTextView.setText("Head: " + head.name);
                headTextView.setTextSize(20);
                headTextView.setTypeface(null, Typeface.BOLD);
                headTextView.setTextColor(getResources().getColor(R.color.bg_color));
                headTextView.setPadding(0, 0, 0, 8);

                // Add head TextView to head layout
                headLayout.addView(headTextView);

                // Fetch and add employees in the department
                new LoadEmployeesTask(headLayout).execute(head.departmentId);

                // Add head layout to department layout
                departmentLayout.addView(headLayout);
            }
        }
    }

    private class LoadEmployeesTask extends AsyncTask<Integer, Void, List<Employee>> {
        private LinearLayout headLayout;

        LoadEmployeesTask(LinearLayout headLayout) {
            this.headLayout = headLayout;
        }

        @Override
        protected List<Employee> doInBackground(Integer... params) {
            return db.employeeDao().findByDepartmentId(params[0]);
        }

        @Override
        protected void onPostExecute(List<Employee> employees) {
            if (employees != null && !employees.isEmpty()) {
                LinearLayout employeesLayout = new LinearLayout(home_employer_Activity.this);
                employeesLayout.setOrientation(LinearLayout.VERTICAL);
                employeesLayout.setPadding(16, 16, 16, 16);
                employeesLayout.setBackgroundResource(R.drawable.rounded_corners);

                for (Employee employee : employees) {
                    // Create and configure employee TextView
                    TextView employeeTextView = new TextView(home_employer_Activity.this);
                    employeeTextView.setText(" - " + employee.name);
                    employeeTextView.setTextSize(18);
                    employeeTextView.setTextColor(getResources().getColor(R.color.bg_color));
                    employeeTextView.setPadding(0, 0, 0, 8);

                    // Add employee TextView to employees layout
                    employeesLayout.addView(employeeTextView);
                }

                // Add employees layout to head layout
                headLayout.addView(employeesLayout);
            }
        }
    }

    private void navigateTo(Class<?> activityClass) {
        Intent intent = new Intent(home_employer_Activity.this, activityClass);
        intent.putExtra("Name", login_name);
        startActivity(intent);
        finish();
    }

    private void logout(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intentLogin = new Intent(home_employer_Activity.this, login_Activity.class);
        startActivity(intentLogin);
        finish();
    }
}
