package com.wika.wikachat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wika.wikachat.R;
import com.wika.wikachat.utils.Constants;
import com.wika.wikachat.utils.FieldsUtil;

public class ProfileCreation extends AppCompatActivity implements ImageButton.OnClickListener {

    // Declaring views
    private TextView btn_next;
    private EditText tx_name;
    private EditText tx_age;
    private Spinner sp_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);

        // Initialising views
        btn_next = findViewById(R.id.tv_save);
        tx_name = findViewById(R.id.tx_name);
        tx_age = findViewById(R.id.tx_age);
        sp_gender = findViewById(R.id.sp_gender);

        // Setting views' event listeners
        btn_next.setOnClickListener(this);


        // Get gender list
        String[] genders_list = getResources().getStringArray(R.array.profile_creation_genders);

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item, genders_list){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    int gray = Color.parseColor("#909090");
                    tv.setTextColor(gray);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);

        sp_gender.setAdapter(spinnerArrayAdapter);

        sp_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // If an intent arrives
        if (getIntent().getExtras() != null) {
            tx_name.setText(getIntent().getStringExtra(Constants.NAME));
            tx_age.setText(getIntent().getStringExtra(Constants.AGE));
            sp_gender.setSelection(getIndexOfGender(getIntent().getStringExtra(Constants.GENDER)));
        }

    }

    @Override
    public void onClick(View v) {
         if (v.getId() == btn_next.getId()) {

            // Initialising important local variables
            String name = tx_name.getText().toString();
            String age = tx_age.getText().toString();
            String gender = sp_gender.getSelectedItem().toString();

            // Check if every field is fulfilled
            if (FieldsUtil.isFulfilled(name, age, gender) && !gender.equalsIgnoreCase(Constants.GENDER)) {
                if (Integer.parseInt(age) < 18) {
                    Toast.makeText(this, getResources().getString(R.string.profile_creation_error_age), Toast.LENGTH_SHORT).show();
                } else {
                    // Create intent and pass in the fields
                    Intent intent = new Intent(this, ProfileCreation2.class);
                    intent.putExtra(Constants.NAME, name);
                    intent.putExtra(Constants.AGE, age);
                    intent.putExtra(Constants.GENDER, gender);
                    // Start next activity
                    startActivity(intent);
                }

            } else {
                Toast.makeText(this, R.string.auth_all_fields_required, Toast.LENGTH_LONG).show();
            }

        }
    }

    /**
     * Gets the index of the specified gender in the gender spinner
     * @param gender
     * @return
     */
    private int getIndexOfGender(String gender) {
        switch (gender) {
            case "Man":
                return 1;
            case "Woman":
                return 2;
            case "Non-binary":
                return 3;
            case "Fluid":
                return 4;
            case "Other":
                return 5;
        }
        return 0;
    }

    @Override
    public void onBackPressed() {

    }
}
