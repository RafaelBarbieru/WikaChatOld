package com.wika.wikachat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.wika.wikachat.R;
import com.wika.wikachat.activities.ProfileCreation2;
import com.wika.wikachat.templates.Profile;
import com.wika.wikachat.utils.Constants;

public class LanguagesAdapter extends RecyclerView.Adapter<LanguagesAdapter.LanguagesViewHolder> {

    private Context context;
    private String[] languages;
    private String typeOfLanguage;
    private Profile profile;

    public LanguagesAdapter (Context context, String[] languages, Profile profile, String typeOfLanguage) {
        this.context = context;
        this.languages = languages;
        this.typeOfLanguage = typeOfLanguage;
        this.profile = profile;
    }

    @NonNull
    @Override
    public LanguagesAdapter.LanguagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.language_row, parent, false);
        return new LanguagesAdapter.LanguagesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguagesAdapter.LanguagesViewHolder holder, final int position) {
        holder.language.setText(languages[position]);

        // Action that executes when the user selects a country
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileCreation2.class);

                // According to the type of language the intent brings, it is assigned to either
                // native or learning language
                if (typeOfLanguage.equals(Constants.LANGUAGE_NATIVE)) {
                    profile.setNativeLanguage(languages[position]);
                } else if (typeOfLanguage.equals(Constants.LANGUAGE_LEARNING)) {
                    profile.setLearningLanguage(languages[position]);
                }
                intent.putExtra(Constants.TYPE, typeOfLanguage);
                intent.putExtra(Constants.CURRENT_PROFILE, profile);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return languages.length;
    }

    public class LanguagesViewHolder extends RecyclerView.ViewHolder {

        TextView language;
        ConstraintLayout constraintLayout;

        public LanguagesViewHolder(@NonNull View itemView) {
            super(itemView);
            language = itemView.findViewById(R.id.lb_language);
            constraintLayout = itemView.findViewById(R.id.clayout_language);
        }

    }

}
