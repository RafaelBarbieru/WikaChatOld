package com.wika.wikachat.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wika.wikachat.R;
import com.wika.wikachat.utils.Constants;
import com.wika.wikachat.templates.Profile;
import com.wika.wikachat.utils.FireBaseWrapper;

import java.util.HashMap;
import java.util.Map;

public class ProfileCreation2 extends AppCompatActivity implements EditText.OnTouchListener, View.OnClickListener {

    private int charactersLeft = 60;

    // Declaring views
    private ImageView btn_back_arrow;
    private TextView btn_create_profile;
    private TextView lb_origin;
    private TextView lb_native;
    private TextView lb_learn;
    private TextView lb_maxLength;
    private TextView tx_define;
    private TextView tx_biography;

    // Declaring current profile
    private Profile currentProfile;

    // Firebase authentication declaration
    private FireBaseWrapper fireBaseWrapper;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    // Declaring Firestore
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation2);

        // Initialising views
        lb_maxLength = findViewById(R.id.lb_max_length);
        lb_origin = findViewById(R.id.lb_origin);
        lb_native = findViewById(R.id.lb_native);
        lb_learn = findViewById(R.id.lb_learn);
        tx_biography = findViewById(R.id.tv_biography);
        tx_define = findViewById(R.id.tx_define);
        btn_create_profile = findViewById(R.id.tv_save);
        btn_back_arrow = findViewById(R.id.btn_back_arrow);

        // Setting event listeners
        lb_origin.setOnClickListener(this);
        lb_native.setOnClickListener(this);
        lb_learn.setOnClickListener(this);
        btn_create_profile.setOnClickListener(this);
        btn_back_arrow.setOnClickListener(this);

        // Initialising current profile
        currentProfile = new Profile();

        // Initialising Firebase authentication
        fireBaseWrapper = new FireBaseWrapper(null);
        mAuth = fireBaseWrapper.getMAuth();
        firebaseUser = mAuth.getCurrentUser();
        currentProfile.setEmail(firebaseUser.getEmail());

        // Initialising firestore
        firestore = FirebaseFirestore.getInstance();

        // Getting name from intent extra and updating biography
        if (getIntent().getStringExtra(Constants.NAME) != null) {
            currentProfile.setName(getIntent().getStringExtra(Constants.NAME));
            updateFields();
        }

        // Getting the gender from intent extra
        if (getIntent().getStringExtra(Constants.GENDER) != null) {
            currentProfile.setGender(getIntent().getStringExtra(Constants.GENDER));
        }

        // Getting the birthday from intent extra
        if (getIntent().getStringExtra(Constants.AGE) != null) {
            currentProfile.setAge(getIntent().getStringExtra(Constants.AGE));
        }

        // Checking if there are intent extras
        if (getIntent().getExtras() != null && !getIntent().getExtras().isEmpty()) {

            // Checking if there is a profile extra
            if (getIntent().getSerializableExtra(Constants.CURRENT_PROFILE) != null) {
                // Assigning current profile to profile that comes from intent extra
                currentProfile = (Profile) getIntent().getSerializableExtra(Constants.CURRENT_PROFILE);
                // Updating fields
                updateFields();
            }

        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        charactersLeft--;
        lb_maxLength.setText(String.valueOf(charactersLeft));
        return false;
    }

    @Override
    public void onClick(View v) {

        // Back arrow button
        if (v.getId() == btn_back_arrow.getId()) {
            Intent intent = new Intent(this, ProfileCreation.class);
            intent.putExtra(Constants.NAME, currentProfile.getName());
            intent.putExtra(Constants.AGE, currentProfile.getAge());
            intent.putExtra(Constants.GENDER, currentProfile.getGender());
            startActivity(intent);
        }

        // Country
        if (v.getId() == lb_origin.getId()) {

            // Country of origin
            Intent intent = new Intent(ProfileCreation2.this, CountrySelection.class);
            intent.putExtra(Constants.CURRENT_PROFILE, currentProfile);
            startActivity(intent);

        }

        // Languages
        if (v.getId() == lb_native.getId() || v.getId() == lb_learn.getId()) {

            Intent intent = new Intent(ProfileCreation2.this, LanguageSelection.class);

            // Native language
            if (v.getId() == lb_native.getId())
                intent.putExtra(Constants.TYPE, Constants.LANGUAGE_NATIVE);

            // Learning language
            else if (v.getId() == lb_learn.getId())
                intent.putExtra(Constants.TYPE, Constants.LANGUAGE_LEARNING);

            intent.putExtra(Constants.CURRENT_PROFILE, currentProfile);

            startActivity(intent);

        }

        // Finish creating profile
        if (v.getId() == btn_create_profile.getId()) {
            //if (firebaseUser.isEmailVerified()) {
                finishProfileCreation();
            //} else {
                //Toast.makeText(this, getResources().getString(R.string.auth_verify_email), Toast.LENGTH_SHORT).show();
            //}

        }

    }

    /**
     * Method that updates the values of all fields
     */
    private void updateFields() {
        // Updating country of origin
        if (currentProfile.getCountryOfOrigin() != null)
            lb_origin.setText(currentProfile.getCountryOfOrigin());
        // Updating native language
        if (currentProfile.getNativeLanguage() != null)
            lb_native.setText(currentProfile.getNativeLanguage());
        // Updating learning language
        if (currentProfile.getLearningLanguage() != null)
            lb_learn.setText(currentProfile.getLearningLanguage());

        // Updating biography
        String biography = "";
        if (currentProfile.getName() != null) {
            biography += getString(R.string.autobio_1) + " " + currentProfile.getName() + ", " + getString(R.string.autobio_2) + "\n";
            if (currentProfile.getCountryOfOrigin() != null) {
                biography += getString(R.string.autobio_3) + " " + currentProfile.getCountryOfOrigin();
                if (currentProfile.getNativeLanguage() != null) {
                    biography += " " + getString(R.string.autobio_4) + " " + currentProfile.getNativeLanguage() + ". ";
                    if (currentProfile.getLearningLanguage() != null) {
                        biography += getString(R.string.autobio_5) + " " + currentProfile.getLearningLanguage() + ". " + getString(R.string.autobio_6);
                    }
                }
            }
        }
        currentProfile.setBiography(biography);
        tx_biography.setText(currentProfile.getBiography());
    }

    private void finishProfileCreation() {
        if (!tx_define.getText().toString().trim().equals("")) {
            currentProfile.setDefine(tx_define.getText().toString());
        }
        if (checkIfAllFieldsSet()) {

                // Show confirm dialog
                AlertDialog alertDialog;
                AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.profile_creation_2_finishing_warning));
                builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Write profile to database
                        writeProfileToDatabase();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog = builder.create();
                alertDialog.show();


        } else {
            Toast.makeText(this, R.string.auth_all_fields_required, Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkIfAllFieldsSet() {
        return (currentProfile.getName() != null &&
                currentProfile.getAge() != null &&
                currentProfile.getGender() != null &&
                currentProfile.getCountryOfOrigin() != null &&
                currentProfile.getNativeLanguage() != null &&
                currentProfile.getLearningLanguage() != null &&
                currentProfile.getDefine() != null &&
                currentProfile.getBiography() != null);
    }

    private void writeProfileToDatabase() {
        Map<String, Object> profile = new HashMap<>();
        profile.put("uid", currentProfile.getUid());
        profile.put("email", mAuth.getCurrentUser().getEmail());
        profile.put("name", currentProfile.getName());
        profile.put("age", currentProfile.getAge());
        profile.put("country", currentProfile.getCountryOfOrigin());
        profile.put("native", currentProfile.getNativeLanguage());
        profile.put("learning", currentProfile.getLearningLanguage());
        profile.put("define", currentProfile.getDefine());
        profile.put("biography", currentProfile.getBiography());
        profile.put("points_nice", currentProfile.getPoints()[0]);
        profile.put("points_serious", currentProfile.getPoints()[1]);
        profile.put("points_fun", currentProfile.getPoints()[2]);

        // Add a new document with a generated ID
        firestore.collection("profiles")
                .add(profile)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        currentProfile.setUid(documentReference.getId());
                        Intent intent = new Intent(ProfileCreation2.this, MyProfile.class);
                        intent.putExtra(Constants.CURRENT_PROFILE, currentProfile);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileCreation2.this, getResources().getString(R.string.profile_creation_2_error_adding_profile), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
