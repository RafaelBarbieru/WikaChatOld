package com.wika.wikachat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wika.wikachat.R;
import com.wika.wikachat.templates.Profile;
import com.wika.wikachat.utils.Constants;
import com.wika.wikachat.utils.FireBaseWrapper;
import com.wika.wikachat.utils.LoadingDialog;
import com.wika.wikachat.adapters.ProfileListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfileList extends AppCompatActivity implements View.OnClickListener {

    // Declaring views
    private ImageView img_chat_button;
    private ImageView img_search_button;
    private ImageView img_profile_button;

    // Declaring loading dialog
    private LoadingDialog loadingDialog;

    // Declaring firebase components
    private FireBaseWrapper fireBaseWrapper;
    private FirebaseFirestore firestore;

    // Declaring profiles array
    private List<Profile> profiles;

    // Declaring Recycler View Adapter
    ProfileListAdapter profileListAdapter;

    // Declaring views
    private RecyclerView recyclerView;

    // Declaring current profile
    private Profile currentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);

        // Initialising views
        recyclerView = findViewById(R.id.recyclerViewProfile);
        img_chat_button = findViewById(R.id.img_chat_button);
        img_search_button = findViewById(R.id.img_search_button);
        img_profile_button = findViewById(R.id.img_profile_button);

        // Setting event listeners
        img_chat_button.setOnClickListener(this);
        img_search_button.setOnClickListener(this);
        img_profile_button.setOnClickListener(this);

        // Initialising loading dialog
        loadingDialog = new LoadingDialog(this);

        // Initialising firebase components
        fireBaseWrapper = new FireBaseWrapper(loadingDialog);
        firestore = fireBaseWrapper.getFirestore();

        // Initialising profile array
        profiles = new ArrayList<>();

        if (getIntent().getExtras() != null && getIntent().getSerializableExtra(Constants.CURRENT_PROFILE) != null) {
            // Getting the profile
            currentProfile = (Profile) getIntent().getSerializableExtra(Constants.CURRENT_PROFILE);
        }

        // Adding all the profiles from Firestore
        loadingDialog.startDialog();
        firestore.collection("profiles")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                // Filter for self
                                if (!data.get("uid").equals(currentProfile.getUid())) {
                                    // Filter for languages
                                    if (data.get("learning").equals(currentProfile.getNativeLanguage())
                                            && data.get("native").equals(currentProfile.getLearningLanguage())) {
                                        Profile profile = new Profile();
                                        profile.setUid((String) data.get("uid"));
                                        profile.setEmail((String) data.get("email"));
                                        profile.setName((String) data.get("name"));
                                        profile.setAge((String) data.get("age"));
                                        profile.setCountryOfOrigin((String) data.get("country"));
                                        profile.setNativeLanguage((String) data.get("native"));
                                        profile.setLearningLanguage((String) data.get("learning"));
                                        profile.setDefine((String) data.get("define"));
                                        profile.setBiography((String) data.get("biography"));
                                        int[] points = new int[3];
                                        points[0] = Integer.parseInt(String.valueOf(data.get("points_nice")));
                                        points[1] = Integer.parseInt(String.valueOf(data.get("points_serious")));
                                        points[2] = Integer.parseInt(String.valueOf(data.get("points_fun")));
                                        profile.setPoints(points);
                                        profiles.add(profile);
                                    }
                                }
                            }
                            // Initialising profiles adapter
                            profileListAdapter = new ProfileListAdapter(
                                    ProfileList.this,
                                    profiles,
                                    currentProfile
                            );
                            recyclerView.setAdapter(profileListAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ProfileList.this));
                            loadingDialog.dismissDialog();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {

        // MY PROFILE BUTTON
        if (v.getId() == img_profile_button.getId()) {
            Intent intent = new Intent(this, MyProfile.class);
            intent.putExtra(Constants.CURRENT_PROFILE, currentProfile);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        // CONVERSATIONS BUTTON
        if (v.getId() == img_chat_button.getId()) {
            Intent intent = new Intent(this, Conversations.class);
            intent.putExtra(Constants.CURRENT_PROFILE, currentProfile);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

    }
}
