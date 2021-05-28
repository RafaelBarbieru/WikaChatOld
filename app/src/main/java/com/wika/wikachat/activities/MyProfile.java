package com.wika.wikachat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wika.wikachat.R;
import com.wika.wikachat.templates.Profile;
import com.wika.wikachat.utils.Constants;
import com.wika.wikachat.utils.FireBaseWrapper;
import com.wika.wikachat.utils.LoadingDialog;

import java.util.HashMap;
import java.util.Map;

public class MyProfile extends AppCompatActivity implements View.OnClickListener {

    // Declaring views
    private ImageView img_profile;
    private TextView tv_name;
    private TextView tv_age;
    private TextView tv_nativeLanguage;
    private TextView tv_learningLanguage;
    private TextView tv_define;
    private ImageView img_nice;
    private TextView tv_nice;
    private ImageView img_teacher;
    private TextView tv_teacher;
    private ImageView img_fun;
    private TextView tv_fun;
    private TextView tv_biography;
    private ImageView img_chat_button;
    private ImageView img_search_button;
    private ImageView img_profile_button;
    private ImageView img_edit;
    private Button btn_log_off;

    // Declaring current profile
    private Profile currentProfile;

    // Firebase authentication declaration
    private FireBaseWrapper fireBaseWrapper;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    // Declaring Firestore
    private FirebaseFirestore firestore;

    // Loading screen declaration
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // Initialising views
        img_profile = findViewById(R.id.img_profile_picture);
        tv_name = findViewById(R.id.lb_name);
        tv_age = findViewById(R.id.tv_age);
        tv_nativeLanguage = findViewById(R.id.tv_native_language);
        tv_learningLanguage = findViewById(R.id.tv_learningLanguage);
        tv_define = findViewById(R.id.tv_define);
        img_nice = findViewById(R.id.img_nice);
        tv_nice = findViewById(R.id.tv_nice);
        img_teacher = findViewById(R.id.img_teacher);
        tv_teacher = findViewById(R.id.tv_teacher);
        img_fun = findViewById(R.id.img_fun);
        tv_fun = findViewById(R.id.tv_fun);
        tv_biography = findViewById(R.id.tv_biography);
        img_chat_button = findViewById(R.id.img_chat_button);
        img_search_button = findViewById(R.id.img_search_button);
        img_profile_button = findViewById(R.id.img_profile_button);
        img_edit = findViewById(R.id.img_edit);
        btn_log_off = findViewById(R.id.btn_log_off);

        // Setting event listeners
        img_nice.setOnClickListener(this);
        img_teacher.setOnClickListener(this);
        img_chat_button.setOnClickListener(this);
        img_search_button.setOnClickListener(this);
        img_profile_button.setOnClickListener(this);
        img_edit.setOnClickListener(this);
        btn_log_off.setOnClickListener(this);

        // Initialising Firebase authentication
        fireBaseWrapper = new FireBaseWrapper(loadingDialog);
        mAuth = fireBaseWrapper.getMAuth();
        firebaseUser = mAuth.getCurrentUser();

        // Initialising current profile
        currentProfile = new Profile();

        // Initialising firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialising loading dialog
        loadingDialog = new LoadingDialog(MyProfile.this);

        // Catch intent extras

        if (getIntent().getExtras() != null && getIntent().getSerializableExtra(Constants.CURRENT_PROFILE) != null) {
            // Getting the profile
            currentProfile = (Profile) getIntent().getSerializableExtra(Constants.CURRENT_PROFILE);
            Map<String, Object> profile = new HashMap<>();
            profile.put("uid", currentProfile.getUid());
            firestore.collection("profiles").document(currentProfile.getUid())
                    .update(profile);
            // Setting the fields according to the profile's information
            fillFields(currentProfile);
        } else {
            currentProfile = new Profile();
            loadingDialog.startDialog();
            if (firestore != null) {
                firestore
                        .collection("profiles")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Map<String, Object> data = document.getData();
                                        String uid = (String) data.get("uid");
                                        if (uid.equals(document.getId()) && data.get("email").equals(mAuth.getCurrentUser().getEmail())) {
                                            currentProfile.setUid((String) data.get("uid"));
                                            currentProfile.setEmail((String) data.get("email"));
                                            currentProfile.setName((String) data.get("name"));
                                            currentProfile.setAge((String) data.get("age"));
                                            currentProfile.setGender((String) data.get("gender"));
                                            currentProfile.setCountryOfOrigin((String) data.get("country"));
                                            currentProfile.setNativeLanguage((String) data.get("native"));
                                            currentProfile.setLearningLanguage((String) data.get("learning"));
                                            currentProfile.setDefine((String) data.get("define"));
                                            currentProfile.setBiography((String) data.get("biography"));
                                        }
                                    }
                                    if (currentProfile.getName() == null || currentProfile.getName().trim().equals("")) {
                                        Intent intent = new Intent(MyProfile.this, ProfileCreation.class);
                                        startActivity(intent);
                                    } else {
                                        fillFields(currentProfile);
                                    }

                                    loadingDialog.dismissDialog();
                                }
                            }
                        });
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Get menu inflater
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_profile_menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == img_edit.getId()) {
            // EDIT BUTTON
            firestore
                    .collection("profiles")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(MyProfile.this, EditBiography.class);
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> data = document.getData();
                                    String uid = (String) data.get("uid");
                                    if (uid.equals(document.getId()) && data.get("email").equals(mAuth.getCurrentUser().getEmail())) {
                                        currentProfile.setUid(document.getId());
                                        currentProfile.setEmail((String) data.get("email"));
                                        currentProfile.setName((String) data.get("name"));
                                        currentProfile.setAge((String) data.get("age"));
                                        currentProfile.setGender((String) data.get("gender"));
                                        currentProfile.setCountryOfOrigin((String) data.get("country"));
                                        currentProfile.setNativeLanguage((String) data.get("native"));
                                        currentProfile.setLearningLanguage((String) data.get("learning"));
                                        currentProfile.setDefine((String) data.get("define"));
                                        currentProfile.setBiography((String) data.get("biography"));
                                    }
                                }
                                fillFields(currentProfile);
                                loadingDialog.dismissDialog();
                                intent.putExtra(Constants.CURRENT_PROFILE, currentProfile);
                                startActivity(intent);
                            }
                        }
                    });

        }

        if (v.getId() == btn_log_off.getId()) {
            // LOG OFF BUTTON
            if (fireBaseWrapper.getMAuth().getCurrentUser() != null) {
                fireBaseWrapper.getMAuth().signOut();
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
            }
        }

        // SEARCH BUTTON
        if (v.getId() == img_search_button.getId()) {
            Intent intent = new Intent(this, ProfileList.class);
            intent.putExtra(Constants.CURRENT_PROFILE, currentProfile);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        // CONVERSATIONS BUTTON
        if (v.getId() == img_chat_button.getId()) {
            Intent intent = new Intent(this, Conversations.class);
            intent.putExtra(Constants.CURRENT_PROFILE, currentProfile);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

    }

    /**
     * Fills all the fields with the information of the profile passed through the parameter
     * @param profile
     */
    public void fillFields(Profile profile) {
        tv_name.setText(profile.getName());
        tv_age.setText(String.valueOf(profile.getAge()));
        tv_nativeLanguage.setText(profile.getNativeLanguage());
        tv_learningLanguage.setText(profile.getLearningLanguage());
        tv_define.setText(profile.getDefine());
        tv_nice.setText(String.valueOf(profile.getPoints()[Constants.POINTS_NICE]));
        tv_teacher.setText(String.valueOf(profile.getPoints()[Constants.POINTS_TEACHER]));
        tv_fun.setText(String.valueOf(profile.getPoints()[Constants.POINTS_FUN]));
        tv_biography.setText(profile.getBiography());
    }

    @Override
    public void onBackPressed() {

    }
}

