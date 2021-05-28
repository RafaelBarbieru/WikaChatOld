package com.wika.wikachat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wika.wikachat.R;
import com.wika.wikachat.templates.Profile;
import com.wika.wikachat.utils.Constants;
import com.wika.wikachat.utils.FireBaseWrapper;
import com.wika.wikachat.utils.LoadingDialog;

import java.util.HashMap;
import java.util.Map;

public class EditBiography extends AppCompatActivity implements View.OnClickListener {

    // Declaring views
    private ImageView img_back_button;
    private TextView tv_save;
    private EditText tx_bio;
    private EditText tx_extract;

    // Declaring current profile
    private Profile currentProfile;

    // Declaring Firebase wrapper
    private FireBaseWrapper fireBaseWrapper;

    // Loading screen declaration
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_biography);

        // Initialising views
        img_back_button = findViewById(R.id.btn_back_arrow);
        tv_save = findViewById(R.id.tv_save);
        tx_bio = findViewById(R.id.tx_bio);
        tx_extract = findViewById(R.id.tx_extract);

        // Setting event listeners
        img_back_button.setOnClickListener(this);
        tv_save.setOnClickListener(this);

        // Initialising current profile
        if (getIntent().getExtras() != null && getIntent().getSerializableExtra(Constants.CURRENT_PROFILE) != null) {
            currentProfile = (Profile) getIntent().getSerializableExtra(Constants.CURRENT_PROFILE);
        }

        // Initialising loading dialog
        loadingDialog = new LoadingDialog(EditBiography.this);

        // Initialising Firebase wrapper
        fireBaseWrapper = new FireBaseWrapper(loadingDialog);

        // Setting biography and define text
        tx_bio.setText(currentProfile.getBiography());
        tx_extract.setText(currentProfile.getDefine());

    }

    @Override
    public void onClick(View v) {

        // BACK BUTTON
        if (v.getId() == img_back_button.getId()) {
            Intent intent = new Intent(this, MyProfile.class);
            startActivity(intent);
        }

        // SAVE BUTTON
        if (v.getId() == tv_save.getId()) {
            currentProfile.setBiography(tx_bio.getText().toString());
            currentProfile.setDefine(tx_extract.getText().toString());

            Map<String, Object> profile = new HashMap<>();
            profile.put("biography", currentProfile.getBiography());
            profile.put("define", currentProfile.getDefine());
            fireBaseWrapper.getFirestore().collection("profiles").document(currentProfile.getUid())
                    .update(profile)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent = new Intent(EditBiography.this, MyProfile.class);
                            intent.putExtra(Constants.CURRENT_PROFILE, currentProfile);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditBiography.this, getResources().getString(R.string.profile_creation_2_error_updating_profile), Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }

}
