package com.wika.wikachat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.wika.wikachat.R;
import com.wika.wikachat.utils.FireBaseWrapper;
import com.wika.wikachat.utils.LoadingDialog;

public class Register extends AppCompatActivity implements ImageView.OnClickListener {

    // Declaring the views
    private ImageButton btn_back_arrow;
    private EditText tx_email;
    private EditText tx_password;
    private EditText tx_password2;
    private Button btn_signup;

    // Firebase authentication declaration
    private FireBaseWrapper fireBaseWrapper;
    private FirebaseAuth mAuth;

    // Loading screen declaration
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialising views
        btn_back_arrow = findViewById(R.id.btn_back_arrow);
        tx_email = findViewById(R.id.tx_email);
        tx_password = findViewById(R.id.tx_password);
        tx_password2 = findViewById(R.id.tx_password2);
        btn_signup = findViewById(R.id.btn_signin);


        // Setting views listeners
        btn_back_arrow.setOnClickListener(this);
        btn_signup.setOnClickListener(this);


        // Initialising loading dialog
        loadingDialog = new LoadingDialog(Register.this);

        // Initialising Firebase components
        fireBaseWrapper = new FireBaseWrapper(loadingDialog);
        mAuth = fireBaseWrapper.getMAuth();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btn_back_arrow.getId()) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        } else if (v.getId() == btn_signup.getId()) {
            String email = tx_email.getText().toString();
            String password = tx_password.getText().toString();
            String password2 = tx_password2.getText().toString();

            // Checking if everything is in order before signing up

            // Checking if all fields are filled
            if (!"".equals(email) && !"".equals(password) && !"".equals(password2)) {
                // Checking if email is correctly formatted
                String emailRegex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
                if (email.matches(emailRegex)) {
                    // Checking if password is at least 6 characters long
                    if (password.length() >= 6) {
                        // Checking if passwords match
                        if (password.equals(password2)) {
                            // Signing up
                            loadingDialog.startDialog();
                            fireBaseWrapper.signUpUser(email, password, this);
                        } else {
                            Toast.makeText(this, R.string.auth_passwords_dont_match, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, R.string.auth_password_too_short, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, R.string.auth_email_bad, Toast.LENGTH_LONG).show();
                }                
                
            } else {
                Toast.makeText(this, R.string.auth_all_fields_required, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
