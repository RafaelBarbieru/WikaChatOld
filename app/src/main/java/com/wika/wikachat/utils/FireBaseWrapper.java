package com.wika.wikachat.utils;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wika.wikachat.R;
import com.wika.wikachat.activities.Login;
import com.wika.wikachat.activities.MyProfile;
import com.wika.wikachat.activities.ProfileCreation;
import com.wika.wikachat.activities.ProfileCreation2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FireBaseWrapper implements Serializable {

    // Firebase authentication declaration
    private FirebaseAuth mAuth;

    // Firebase Firestore
    private FirebaseFirestore firestore;

    // Loading dialog
    private LoadingDialog loadingDialog;

    // Constructor
    public FireBaseWrapper(LoadingDialog loadingDialog) {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        this.loadingDialog = loadingDialog;
    }

    /**
     * Signs in a user according to the values of the Email and Password fields
     *
     * @param email
     * @param password
     * @param origin
     */
    public void signInUser(String email, String password, final Activity origin) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(origin, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Check if user is verified
                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(origin, MyProfile.class);
                                intent.putExtra("user", user);
                                origin.startActivity(intent);
                            } else {
                                Toast.makeText(origin, R.string.auth_verify_email, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(origin, R.string.auth_login_failed, Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismissDialog();
                    }
                });


    }

    /**
     * Signs up a user with an email and a password
     *
     * @param email
     * @param password
     * @param origin
     */
    public void signUpUser(final String email, final String password, final Activity origin) {


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(origin, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Send email verification
                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(origin, R.string.auth_registration_successful, Toast.LENGTH_LONG).show();
                                        origin.startActivity(new Intent(origin, ProfileCreation.class));
                                    } else {
                                        Toast.makeText(origin, R.string.auth_error_sending_email, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(origin, R.string.auth_signup_failed, Toast.LENGTH_LONG).show();
                        }
                        loadingDialog.dismissDialog();
                    }
                });


    }

    public void resetPassword(final String email, final Activity origin) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(origin, R.string.reset_pwd_email, Toast.LENGTH_LONG).show();
                            origin.startActivity(new Intent(origin, Login.class));
                        } else {
                            Toast.makeText(origin, R.string.reset_pwd_error_sending_email, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * Getter for mAuth
     *
     * @return
     */
    public FirebaseAuth getMAuth() {
        return this.mAuth;
    }

    public FirebaseFirestore getFirestore() {
        return firestore;
    }
}
