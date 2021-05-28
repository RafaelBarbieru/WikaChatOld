package com.wika.wikachat.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.wika.wikachat.R;
import com.wika.wikachat.utils.FireBaseWrapper;
import com.wika.wikachat.utils.LoadingDialog;

public class ResetPassword extends AppCompatActivity implements Button.OnClickListener {

    // Declaring views
    private EditText tx_email;
    private Button btn_reset;
    private ImageView btn_back_arrow;

    // Firebase authentication declaration
    private FireBaseWrapper fireBaseWrapper;
    private FirebaseAuth mAuth;

    // Loading screen declaration
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Initialising views
        tx_email = findViewById(R.id.tx_email);
        btn_reset = findViewById(R.id.btn_signin);
        btn_back_arrow = findViewById(R.id.btn_back_arrow);

        // Setting views listeners
        btn_reset.setOnClickListener(this);
        btn_back_arrow.setOnClickListener(this);

        // Initialising loading dialog
        loadingDialog = new LoadingDialog(ResetPassword.this);

        // Initialising Firebase components
        fireBaseWrapper = new FireBaseWrapper(loadingDialog);
        mAuth = fireBaseWrapper.getMAuth();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == btn_reset.getId()) {

            // Reset button
            String email = tx_email.getText().toString();
            if (!"".equals(email) && email != null) {
                loadingDialog.startDialog();
                fireBaseWrapper.resetPassword(email, this);
            } else {
                Toast.makeText(this, R.string.reset_pwd_error_sending_email, Toast.LENGTH_LONG).show();
            }


        } else if (v.getId() == btn_back_arrow.getId()) {

            // Back arrow button
            startActivity(new Intent(ResetPassword.this, Login.class));
        }

    }
}
