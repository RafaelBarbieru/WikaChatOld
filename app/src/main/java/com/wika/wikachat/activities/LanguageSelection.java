package com.wika.wikachat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.wika.wikachat.R;
import com.wika.wikachat.utils.Constants;
import com.wika.wikachat.adapters.LanguagesAdapter;
import com.wika.wikachat.templates.Profile;

public class LanguageSelection extends AppCompatActivity implements View.OnClickListener {

    // Declaring languages array
    private String[] languages;

    // Declaring Recycler View Adapter
    LanguagesAdapter languagesAdapter;

    // Declaring views
    private ImageView btn_back_arrow;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_selection);

        // Initialising views
        btn_back_arrow = findViewById(R.id.btn_back_arrow);
        recyclerView = findViewById(R.id.recyclerViewCountries);

        // Initialising countries array
        languages = getResources().getStringArray(R.array.languages);

        // Initialising countries adapter
        languagesAdapter = new LanguagesAdapter(
                this,
                languages,
                (Profile) getIntent().getSerializableExtra(Constants.CURRENT_PROFILE),
                getIntent().getStringExtra(Constants.TYPE)
        );
        recyclerView.setAdapter(languagesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setting event listener of views
        btn_back_arrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btn_back_arrow.getId()) {
            // Back arrow button
            startActivity(new Intent(LanguageSelection.this, ProfileCreation2.class));
        }
    }
}
