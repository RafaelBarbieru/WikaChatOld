package com.wika.wikachat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

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

import java.util.HashMap;
import java.util.Map;

public class ForeignProfile extends AppCompatActivity implements View.OnClickListener {

    // Declaring views
    TextView tv_contact_name;
    ImageButton img_back_arrow;
    ImageView img_profile;
    TextView tv_name;
    TextView tv_age;
    TextView tv_nativeLanguage;
    TextView tv_learningLanguage;
    TextView tv_define;
    ImageView img_nice;
    TextView tv_nice;
    ImageView img_teacher;
    TextView tv_teacher;
    ImageView img_fun;
    TextView tv_fun;
    TextView tv_biography;
    ImageView img_chat_button;
    ImageView img_search_button;
    ImageView img_profile_button;
    Toolbar btn_chat;

    // Declaring profiles
    private Profile currentProfile;
    private Profile focusProfile;

    // Firebase components
    private FireBaseWrapper fireBaseWrapper;
    private FirebaseFirestore firestore;

    // Other
    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_profile);

        // Initialising views
        tv_contact_name = findViewById(R.id.tv_contact_name);
        img_back_arrow = findViewById(R.id.btn_back_arrow);
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
        btn_chat = findViewById(R.id.toolbar_chat);

        // Setting event listeners
        img_back_arrow.setOnClickListener(this);
        img_nice.setOnClickListener(this);
        img_teacher.setOnClickListener(this);
        img_fun.setOnClickListener(this);
        btn_chat.setOnClickListener(this);

        // Other
        loadingDialog = new LoadingDialog(this);

        // Firebase components
        fireBaseWrapper = new FireBaseWrapper(loadingDialog);
        firestore = fireBaseWrapper.getFirestore();

        // Getting intent extras
        if (getIntent().getExtras() != null && !getIntent().getExtras().isEmpty()) {
            // Initialising current profile
            currentProfile = (Profile) getIntent().getSerializableExtra(Constants.CURRENT_PROFILE);
            focusProfile = (Profile) getIntent().getSerializableExtra(Constants.FOCUS_PROFILE);
            refreshPointsOnUI();
            fillFields(focusProfile);
        }

    }

    /**
     * Fills all the fields with the information of the profile passed through the parameter
     * @param profile
     */
    public void fillFields(Profile profile) {
        tv_contact_name.setText(profile.getName());
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
    public void onClick(View v) {

        // BACK BUTTON
        if (v.getId() == img_back_arrow.getId()) {
            Intent intent = new Intent(this, ProfileList.class);
            intent.putExtra(Constants.CURRENT_PROFILE, currentProfile);
            startActivity(intent);
        }

        // CHAT BUTTON
        if (v.getId() == btn_chat.getId()) {
            Intent intent = new Intent(this, Chat.class);
            intent.putExtra(Constants.CURRENT_PROFILE, currentProfile);
            intent.putExtra(Constants.FOCUS_PROFILE, focusProfile);
            startActivity(intent);
        }

        // FUN BUTTON
        if (v.getId() == img_fun.getId()) {
            loadingDialog.startDialog();
            if (img_nice.getAlpha() == 1f) {
                voteProfile(Constants.VOTE_TYPE_NICE, true);
            } else if (img_teacher.getAlpha() == 1f) {
                voteProfile(Constants.VOTE_TYPE_TEACHER, true);
            }
            img_fun.setAlpha(1f);
            img_nice.setAlpha(0.2f);
            img_teacher.setAlpha(0.2f);
            voteProfile(Constants.VOTE_TYPE_FUN, false);
        }

        // NICE BUTTON
        if (v.getId() == img_nice.getId()) {
            loadingDialog.startDialog();
            if (img_teacher.getAlpha() == 1f) {
                voteProfile(Constants.VOTE_TYPE_TEACHER, true);
            } else if (img_fun.getAlpha() == 1f) {
                voteProfile(Constants.VOTE_TYPE_FUN, true);
            }
            img_nice.setAlpha(1f);
            img_teacher.setAlpha(0.2f);
            img_fun.setAlpha(0.2f);
            voteProfile(Constants.VOTE_TYPE_NICE, false);
        }

        // TEACHER BUTTON
        if (v.getId() == img_teacher.getId()) {
            loadingDialog.startDialog();
            if (img_nice.getAlpha() == 1f) {
                voteProfile(Constants.VOTE_TYPE_NICE, true);
            } else if (img_fun.getAlpha() == 1f) {
                voteProfile(Constants.VOTE_TYPE_FUN, true);
            }
            img_teacher.setAlpha(1f);
            img_nice.setAlpha(0.2f);
            img_fun.setAlpha(0.2f);
            voteProfile(Constants.VOTE_TYPE_TEACHER, false);
        }

    }

    private void voteProfile(final int typeOfVote, final boolean unvote) {
        firestore.collection("profiles")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        long nicePoints = 0;
                        long teacherPoints = 0;
                        long funPoints = 0;
                        if (task.isSuccessful()) {
                            if (task.getResult().size() > 0) {
                                for (QueryDocumentSnapshot document: task.getResult()) {
                                    Map<String, Object> data = document.getData();
                                    if (focusProfile.getEmail().equals(data.get("email"))) {
                                        nicePoints = (long) data.get("points_nice");
                                        teacherPoints = (long) data.get("points_serious");
                                        funPoints = (long) data.get("points_fun");
                                    }
                                }
                            }
                            Map<String, Object> points = new HashMap<>();
                            switch (typeOfVote) {
                                case Constants.VOTE_TYPE_NICE:
                                    if (!unvote) {
                                        points.put("points_nice", nicePoints + 1);
                                        firestore.collection("profiles").document(focusProfile.getUid())
                                                .update(points)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful())
                                                            refreshPointsOnUI();
                                                    }
                                                });
                                    } else {
                                        points.put("points_nice", nicePoints -1);
                                        firestore.collection("profiles").document(focusProfile.getUid())
                                                .update(points)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful())
                                                            refreshPointsOnUI();
                                                    }
                                                });
                                    }
                                    break;
                                case Constants.VOTE_TYPE_TEACHER:
                                    if (!unvote) {
                                        points.put("points_serious", teacherPoints + 1);
                                        firestore.collection("profiles").document(focusProfile.getUid())
                                                .update(points)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful())
                                                            refreshPointsOnUI();
                                                    }
                                                });
                                    } else {
                                        points.put("points_serious", teacherPoints - 1);
                                        firestore.collection("profiles").document(focusProfile.getUid())
                                                .update(points)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful())
                                                            refreshPointsOnUI();
                                                    }
                                                });
                                    }
                                    break;
                                case Constants.VOTE_TYPE_FUN:
                                    if (!unvote) {
                                        points.put("points_fun", funPoints + 1);
                                        firestore.collection("profiles").document(focusProfile.getUid())
                                                .update(points)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful())
                                                            refreshPointsOnUI();
                                                    }
                                                });
                                    } else {
                                        points.put("points_fun", funPoints - 1);
                                        firestore.collection("profiles").document(focusProfile.getUid())
                                                .update(points)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful())
                                                            refreshPointsOnUI();
                                                    }
                                                });
                                    }
                                    break;
                            }
                        }
                    }
                });

    }

    private void refreshPointsOnUI() {
        firestore.collection("profiles")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() > 0) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.getData().get("email").equals(focusProfile.getEmail())) {
                                        tv_nice.setText(String.valueOf((long) document.getData().get("points_nice")));
                                        tv_teacher.setText(String.valueOf((long) document.getData().get("points_serious")));
                                        tv_fun.setText(String.valueOf((long) document.getData().get("points_fun")));
                                    }
                                }
                            }
                        }
                        loadingDialog.dismissDialog();
                    }
                });
    }
}
