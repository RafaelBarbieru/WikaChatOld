package com.wika.wikachat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wika.wikachat.R;
import com.wika.wikachat.adapters.ConversationsAdapter;
import com.wika.wikachat.adapters.ProfileListAdapter;
import com.wika.wikachat.templates.Profile;
import com.wika.wikachat.utils.Constants;
import com.wika.wikachat.utils.FireBaseWrapper;
import com.wika.wikachat.utils.LoadingDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Conversations extends AppCompatActivity implements View.OnClickListener {

    // Declaring views
    private ImageView img_chat_button;
    private ImageView img_search_button;
    private ImageView img_profile_button;
    private RecyclerView recyclerView;

    // Profiles
    private Profile currentProfile;

    // Others
    private LoadingDialog loadingDialog;
    private FireBaseWrapper fireBaseWrapper;
    private FirebaseFirestore firestore;
    private ConversationsAdapter conversationsAdapter;
    private List<Profile> profiles;
    private List<String> profile_emails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);

        // Initialising views
        img_chat_button = findViewById(R.id.img_chat_button);
        img_search_button = findViewById(R.id.img_search_button);
        img_profile_button = findViewById(R.id.img_profile_button);
        recyclerView = findViewById(R.id.recyclerView_conversations);

        // Event listeners
        img_chat_button.setOnClickListener(this);
        img_search_button.setOnClickListener(this);
        img_profile_button.setOnClickListener(this);

        // Other
        loadingDialog = new LoadingDialog(this);
        fireBaseWrapper = new FireBaseWrapper(loadingDialog);
        firestore = fireBaseWrapper.getFirestore();
        profiles = new ArrayList<>();
        profile_emails = new ArrayList<>();

        // Get intent extras
        if (getIntent().getExtras() != null && getIntent().getSerializableExtra(Constants.CURRENT_PROFILE) != null) {
            // Getting the profile
            currentProfile = (Profile) getIntent().getSerializableExtra(Constants.CURRENT_PROFILE);
        }

        // Adding all the profiles from Firestore
        loadingDialog.startDialog();
        firestore.collection("messages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                if (data.get("sender").equals(currentProfile.getEmail()) || data.get("receiver").equals(currentProfile.getEmail())) {
                                    final String foreign_email;
                                    if (data.get("sender").equals(currentProfile.getEmail())) {
                                        foreign_email = (String) data.get("receiver");
                                    } else {
                                        foreign_email = (String) data.get("sender");
                                    }
                                    if (!profile_emails.contains(foreign_email)) {
                                        profile_emails.add(foreign_email);
                                        firestore.collection("profiles")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                Map<String, Object> data = document.getData();
                                                                if (data.get("email").equals(foreign_email)) {
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
                                                        conversationsAdapter = new ConversationsAdapter(
                                                                Conversations.this,
                                                                profiles,
                                                                currentProfile
                                                        );
                                                        recyclerView.setAdapter(conversationsAdapter);
                                                        recyclerView.setLayoutManager(new LinearLayoutManager(Conversations.this));
                                                        loadingDialog.dismissDialog();
                                                    }
                                                });
                                    }
                                }
                            }
                            loadingDialog.dismissDialog();
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {

        // SEARCH BUTTON
        if (v.getId() == img_search_button.getId()) {
            Intent intent = new Intent(this, ProfileList.class);
            intent.putExtra(Constants.CURRENT_PROFILE, currentProfile);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        // PROFILE BUTTON
        if (v.getId() == img_profile_button.getId()) {
            Intent intent = new Intent(this, MyProfile.class);
            intent.putExtra(Constants.CURRENT_PROFILE, currentProfile);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

    }
}
