package com.wika.wikachat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wika.wikachat.R;
import com.wika.wikachat.utils.FireBaseWrapper;
import com.wika.wikachat.utils.LoadingDialog;

public class Login extends AppCompatActivity implements TextView.OnClickListener {

    // Declaring the views
    private EditText tx_email;
    private EditText tx_password;
    private Button btn_signin;
    private TextView btn_forgot_password;
    private Button btn_no_account;

    // Firebase authentication declaration
    private FireBaseWrapper fireBaseWrapper;
    private FirebaseAuth mAuth;



    // Loading screen declaration
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialising views
        tx_email = findViewById(R.id.tx_email);
        tx_password = findViewById(R.id.tx_password);
        btn_signin = findViewById(R.id.btn_signin);
        btn_forgot_password = findViewById(R.id.lb_forgot_password);
        btn_no_account = findViewById(R.id.btn_no_account);

        // Setting views listeners
        btn_signin.setOnClickListener(this);
        btn_forgot_password.setOnClickListener(this);
        btn_no_account.setOnClickListener(this);

        // Initialising loading dialog
        loadingDialog = new LoadingDialog(Login.this);

        // Initialising Firebase components
        fireBaseWrapper = new FireBaseWrapper(loadingDialog);
        mAuth = fireBaseWrapper.getMAuth();
    }



    @Override
    protected void onStart() {
        super.onStart();

        // Checking if the user is already signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if (currentUser != null) {
            Intent intent = new Intent(this, MyProfile.class);
            startActivity(intent);
        }


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_no_account) {
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_signin) {

            // Sign in button

            // Check if email and password fields are not empty
            if (!"".equals(tx_email.getText().toString()) && !"".equals(tx_password.getText().toString())) {

                // Attempt to sign in user
                String email = tx_email.getText().toString();
                String password = tx_password.getText().toString();
                loadingDialog.startDialog();
                fireBaseWrapper.signInUser(email, password, this);

            } else {

                // Warn user to fill the fields
                Toast.makeText(this, R.string.auth_enter_fields, Toast.LENGTH_SHORT).show();
            }

        } else if (v.getId() == R.id.lb_forgot_password) {
            startActivity(new Intent(Login.this, ResetPassword.class));
        }
    }

}
