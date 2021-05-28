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
import com.wika.wikachat.activities.Chat;
import com.wika.wikachat.templates.Profile;
import com.wika.wikachat.utils.Constants;

import java.util.List;

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ConversationsHolder> {

    private Context context;
    private List<Profile> profiles;
    private Profile currentProfile;

    public ConversationsAdapter (Context context, List<Profile> profiles, Profile currentProfile) {
        this.context = context;
        this.profiles = profiles;
        this.currentProfile = currentProfile;
    }

    @NonNull
    @Override
    public ConversationsAdapter.ConversationsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.conversation_row, parent, false);
        return new ConversationsAdapter.ConversationsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationsAdapter.ConversationsHolder holder, final int position) {
        holder.name.setText(profiles.get(position).getName());
        holder.age.setText(profiles.get(position).getAge());
        // Action that executes when the user selects a conversation
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chat.class);
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

    public class ConversationsHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView age;
        ConstraintLayout constraintLayout;

        public ConversationsHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            age = itemView.findViewById(R.id.tv_age);
            constraintLayout = itemView.findViewById(R.id.clayout_profile);
        }

    }

}
