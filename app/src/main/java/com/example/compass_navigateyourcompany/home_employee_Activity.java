package com.example.compass_navigateyourcompany;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class home_employee_Activity extends AppCompatActivity {

    private Spinner typeSpinner;
    private EditText fromDateEditText;
    private EditText toDateEditText;
    private Button selectImageButton;
    private Button clearButton;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_employee);

        TextView appNameTextView = findViewById(R.id.app_name);
        Button profileButton = findViewById(R.id.profile_button);
        CalendarView calendarView = findViewById(R.id.calendar_view);

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

        // clear button
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeSpinner.setSelection(0);
                fromDateEditText.setText("");
                toDateEditText.setText("");
                selectImageButton.setVisibility(View.GONE);
            }
        });

        // submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedType = typeSpinner.getSelectedItem().toString();
                String fromDate = fromDateEditText.getText().toString();
                String toDate = toDateEditText.getText().toString();

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
}
