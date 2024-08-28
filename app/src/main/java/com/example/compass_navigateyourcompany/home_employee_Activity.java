package com.example.compass_navigateyourcompany;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.Calendar;

public class home_employee_Activity extends AppCompatActivity {

    private Spinner typeSpinner;
    private EditText fromDateEditText;
    private EditText toDateEditText;
    private Button selectImageButton;
    private Button clearButton;
    private Button submitButton;

    //for file uploading
    private static final int PICK_FILE = 100;
    private Uri selectedFileUri;
    private String selectedFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_employee);

        TextView appNameTextView = findViewById(R.id.app_name);
        ImageView profileButton = findViewById(R.id.profile_button);
        CalendarView calendarView = findViewById(R.id.calendar_view);

        ImageView settingsButton = findViewById(R.id.settings_button);
        ImageView homeButton = findViewById(R.id.home_button);
        ImageView logoutButton = findViewById(R.id.logout_button);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        typeSpinner = findViewById(R.id.type_spinner);
        fromDateEditText = findViewById(R.id.from_date);
        toDateEditText = findViewById(R.id.to_date);
        selectImageButton = findViewById(R.id.select_image_button);
        clearButton = findViewById(R.id.clear_button);
        submitButton = findViewById(R.id.submit_button);

        // Set up the spinner with options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        // Set up date pickers for date fields
        fromDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(fromDateEditText);
            }
        });

        toDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(toDateEditText);
            }
        });

        // Add listener for type spinner to check selected type
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = parent.getItemAtPosition(position).toString();
                if (selectedType.equals("type1")) {
                    selectImageButton.setVisibility(View.VISIBLE);
                } else {
                    selectImageButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectImageButton.setVisibility(View.GONE);
            }
        });

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openFiles(); }
        });

        // clear button
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeSpinner.setSelection(0);
                fromDateEditText.setText("");
                toDateEditText.setText("");
            }
        });


        // submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedType = typeSpinner.getSelectedItem().toString();
                String fromDate = fromDateEditText.getText().toString();
                String toDate = toDateEditText.getText().toString();

                // for file uploading
                if (selectedFileUri != null) {
                    uploadFile(selectedFilePath);
                }
//                else {
//                    Toast.makeText(home_employee_Activity.this, "Please select your document", Toast.LENGTH_SHORT).show();
//                }

                if (selectedType.equals("type1") && selectImageButton.getVisibility() == View.GONE) {
                    Toast.makeText(home_employee_Activity.this, "Document is required for type1", Toast.LENGTH_SHORT).show();
                } else if (fromDate.isEmpty() || toDate.isEmpty()) {
                    Toast.makeText(home_employee_Activity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Assume submit is successful
                    Toast.makeText(home_employee_Activity.this, "Submit Successful", Toast.LENGTH_SHORT).show();
                }
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_employee_Activity.this, employee_profile_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(navigationView);
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_employee_Activity.this, home_employee_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(home_head_Activity.this, login_Activity.class);
//                startActivity(intent);
                finish();
            }
        });
    }

    private void showDatePickerDialog(final EditText dateEditText) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateEditText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
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
