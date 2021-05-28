package com.wika.wikachat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wika.wikachat.R;
import com.wika.wikachat.adapters.ChatAdapter;
import com.wika.wikachat.templates.Message;
import com.wika.wikachat.templates.Profile;
import com.wika.wikachat.utils.Constants;
import com.wika.wikachat.utils.FireBaseWrapper;
import com.wika.wikachat.utils.LoadingDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat extends AppCompatActivity implements View.OnClickListener {

    // Declaring views
    private TextView tv_contact_name;
    //private ImageButton btn_back_arrow;
    private ImageView img_send;
    private EditText tx_message;

    // Declaring profiles
    private Profile currentProfile;
    private Profile focusProfile;

    // Declaring loading dialog
    private LoadingDialog loadingDialog;

    // Declaring firebase components
    private FireBaseWrapper fireBaseWrapper;

    // Declaring message list
    private List<Message> messages;

    // Declaring chat adapter
    private ChatAdapter chatAdapter;

    // Declaring recycler view
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //btn_back_arrow = findViewById(R.id.btn_back_arrow);
        img_send = findViewById(R.id.img_send);
        tx_message = findViewById(R.id.tx_message);

        // Setting event listeners
        //btn_back_arrow.setOnClickListener(this);
        img_send.setOnClickListener(this);

        // Initialising dialog log
        loadingDialog = new LoadingDialog(this);

        // Initialising firebase components
        fireBaseWrapper = new FireBaseWrapper(loadingDialog);

        // Initialising recycler view
        recyclerView = findViewById(R.id.recyclerview_chat);
        recyclerView.setHasFixedSize(true);


        // Getting intent extras
        if (getIntent().getExtras() != null && !getIntent().getExtras().isEmpty()) {
            currentProfile = (Profile) getIntent().getSerializableExtra(Constants.CURRENT_PROFILE);
            focusProfile = (Profile) getIntent().getSerializableExtra(Constants.FOCUS_PROFILE);
            //tv_contact_name.setText(focusProfile.getName());
            loadingDialog.startDialog();

        }

        readMessages(currentProfile, focusProfile);
        Object[] taskParams = {
                messages,
                fireBaseWrapper,
                currentProfile,
                focusProfile,
                recyclerView,
                loadingDialog,
                currentProfile,
                focusProfile,
                this
        };
        new ChatTask().execute(taskParams);
    }

    @Override
    public void onClick(View v) {

        // BACK ARROW
        /*
        if (v.getId() == btn_back_arrow.getId()) {
            Intent intent = new Intent(this, ForeignProfile.class);
            intent.putExtra(Constants.CURRENT_PROFILE, currentProfile);
            intent.putExtra(Constants.FOCUS_PROFILE, focusProfile);
            startActivity(intent);
        }

         */

        // SEND BUTTON
        if (v.getId() == img_send.getId()) {
            sendMessage(currentProfile, focusProfile, tx_message.getText().toString());
        }

    }

    private void readMessages(final Profile sender, final Profile receiver) {
        messages = new ArrayList<>();
        fireBaseWrapper.getFirestore()
                .collection("messages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                Message msg = new Message();
                                if (data.get("sender").equals(sender.getEmail()) && data.get("receiver").equals(receiver.getEmail())) {
                                    msg.setSender(sender);
                                    msg.setReceiver(receiver);
                                    msg.setMessage((String) data.get("message"));
                                    msg.setOrder(Integer.parseInt((String) data.get("order")));
                                    messages.add(msg);
                                } else if (data.get("sender").equals(receiver.getEmail()) && data.get("receiver").equals(sender.getEmail())) {
                                    msg.setSender(receiver);
                                    msg.setReceiver(sender);
                                    msg.setMessage((String) data.get("message"));
                                    msg.setOrder(Integer.parseInt((String) data.get("order")));
                                    messages.add(msg);
                                }

                            }
                            Collections.sort(messages);
                            chatAdapter = new ChatAdapter(Chat.this, currentProfile, focusProfile, messages);
                            recyclerView.setAdapter(chatAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(Chat.this));
                            recyclerView.scrollToPosition(chatAdapter.getItemCount()-1);
                            loadingDialog.dismissDialog();
                        }
                    }
                });
    }

    private void sendMessage(final Profile sender, final Profile receiver, final String message) {
        fireBaseWrapper.getFirestore().collection("messages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Message> filteredMessages = new ArrayList<>();
                            if (task.getResult().size() > 0) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // If there are messages, get the last order
                                    Map<String, Object> data = document.getData();
                                    // Filter for sender and receiver only
                                    if (data.get("sender").equals(sender.getEmail()) && data.get("receiver").equals(receiver.getEmail())
                                            || data.get("sender").equals(receiver.getEmail()) && data.get("receiver").equals(sender.getEmail())) {
                                        Message msg = new Message();
                                        msg.setReceiver(receiver);
                                        msg.setSender(sender);
                                        msg.setMessage((String) data.get("message"));
                                        msg.setOrder(Integer.parseInt((String) data.get("order")));
                                        filteredMessages.add(msg);
                                    }
                                }
                                // Order filtered messages
                                Collections.sort(filteredMessages);
                                // Get the last order
                                int lastOrder = filteredMessages.get(filteredMessages.size() - 1).getOrder();
                                // Stringify new order
                                String newOrderString = String.valueOf(lastOrder + 1);
                                // Call auxiliary method
                                sendMessageAux(sender, receiver, message, newOrderString);
                            } else {
                                // If there are no messages, return 0
                                sendMessageAux(sender, receiver, message, "0");
                            }

                        }
                    }
                });
    }

    private void sendMessageAux(final Profile sender, final Profile receiver, String message, String order) {
        if (message != null && !message.trim().equals("")) {
            final Map<String, String> msg = new HashMap<>();
            msg.put("sender", sender.getEmail());
            msg.put("receiver", receiver.getEmail());
            msg.put("message", message);
            msg.put("order", order);

            fireBaseWrapper.getFirestore().collection("messages")
                    .add(msg)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            tx_message.setText("");
                            readMessages(currentProfile, focusProfile);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }


    }
}

class ChatTask extends AsyncTask {

    private List<Message> messages;
    private FireBaseWrapper fireBaseWrapper;
    private Profile sender;
    private Profile receiver;
    private ChatAdapter chatAdapter;
    private RecyclerView recyclerView;
    private LoadingDialog loadingDialog;
    private Profile currentProfile;
    private Profile focusProfile;
    private Context context;

    @Override
    protected Object doInBackground(Object[] objects) {

        messages = (List<Message>) objects[0];
        fireBaseWrapper = (FireBaseWrapper) objects[1];
        sender = (Profile) objects[2];
        receiver = (Profile) objects[3];
        recyclerView = (RecyclerView) objects[4];
        loadingDialog = (LoadingDialog) objects[5];
        currentProfile = (Profile) objects[6];
        focusProfile = (Profile) objects[7];
        context = (Context) objects[8];

        while (true) {

            messages = new ArrayList<>();
            fireBaseWrapper.getFirestore()
                    .collection("messages")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> data = document.getData();
                                    Message msg = new Message();
                                    if (data.get("sender").equals(sender.getEmail()) && data.get("receiver").equals(receiver.getEmail())) {
                                        msg.setSender(sender);
                                        msg.setReceiver(receiver);
                                        msg.setMessage((String) data.get("message"));
                                        msg.setOrder(Integer.parseInt((String) data.get("order")));
                                        messages.add(msg);
                                    } else if (data.get("sender").equals(receiver.getEmail()) && data.get("receiver").equals(sender.getEmail())) {
                                        msg.setSender(receiver);
                                        msg.setReceiver(sender);
                                        msg.setMessage((String) data.get("message"));
                                        msg.setOrder(Integer.parseInt((String) data.get("order")));
                                        messages.add(msg);
                                    }

                                }
                                Collections.sort(messages);
                                chatAdapter = new ChatAdapter(context, currentProfile, focusProfile, messages);
                                recyclerView.setAdapter(chatAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                recyclerView.scrollToPosition(chatAdapter.getItemCount()-1);
                                loadingDialog.dismissDialog();
                            }

                        }
                    });
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {

            }
        }
    }
}
