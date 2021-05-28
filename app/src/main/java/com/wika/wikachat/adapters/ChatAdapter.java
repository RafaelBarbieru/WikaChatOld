package com.wika.wikachat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wika.wikachat.R;
import com.wika.wikachat.templates.Message;
import com.wika.wikachat.templates.Profile;
import com.wika.wikachat.utils.Constants;

import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Context context;
    private Profile currentProfile;
    private Profile focusProfile;
    private List<Message> messages;

    public ChatAdapter (Context context, Profile currentProfile, Profile focusProfile, List<Message> messages) {
        this.context = context;
        this.currentProfile = currentProfile;
        this.focusProfile = focusProfile;
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v;
        if (viewType == Constants.MSG_TYPE_RIGHT) {
            v = inflater.inflate(R.layout.chat_item_right, parent, false);
        } else {
            v = inflater.inflate(R.layout.chat_item_left, parent, false);
        }
        return new ChatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, final int position) {

        Message message = messages.get(position);

        holder.show_message.setText(message.getMessage());

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView show_message;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.tv_show_message);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getSender().getEmail().equals(currentProfile.getEmail())) {
            return Constants.MSG_TYPE_RIGHT;
        } else {
            return Constants.MSG_TYPE_LEFT;
        }
    }
}
