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
import com.wika.wikachat.activities.ForeignProfile;
import com.wika.wikachat.templates.Profile;
import com.wika.wikachat.utils.Constants;

import java.util.List;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.ProfileListHolder> {

    private Context context;
    private List<Profile> profiles;
    private Profile currentProfile;

    public ProfileListAdapter (Context context, List<Profile> profiles, Profile currentProfile) {
        this.context = context;
        this.profiles = profiles;
        this.currentProfile = currentProfile;
    }

    @NonNull
    @Override
    public ProfileListAdapter.ProfileListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.profile_row, parent, false);
        return new ProfileListAdapter.ProfileListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileListAdapter.ProfileListHolder holder, final int position) {
        holder.name.setText(profiles.get(position).getName());
        holder.age.setText(profiles.get(position).getAge());
        holder.nativeLanguage.setText(profiles.get(position).getNativeLanguage());
        holder.learningLanguage.setText(profiles.get(position).getLearningLanguage());
        holder.extract.setText(profiles.get(position).getDefine());

        // Action that executes when the user selects a country
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ForeignProfile.class);
                intent.putExtra(Constants.CURRENT_PROFILE, currentProfile);
                intent.putExtra(Constants.FOCUS_PROFILE, profiles.get(position));
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public class ProfileListHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView age;
        TextView nativeLanguage;
        TextView learningLanguage;
        TextView extract;
        ConstraintLayout constraintLayout;

        public ProfileListHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            age = itemView.findViewById(R.id.tv_age);
            nativeLanguage = itemView.findViewById(R.id.tv_native_language);
            learningLanguage = itemView.findViewById(R.id.tv_learning_language);
            extract = itemView.findViewById(R.id.tv_extract);
            constraintLayout = itemView.findViewById(R.id.clayout_profile);
        }

    }

}
