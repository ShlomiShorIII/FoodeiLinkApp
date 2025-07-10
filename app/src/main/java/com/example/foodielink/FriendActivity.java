package com.example.foodielink;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FriendActivity extends AppCompatActivity {

    // Chat
    private RecyclerView chatRecyclerView;
    private EditText messageInput;
    private Button sendButton;

    // Messages List
    private MessageAdapter messageAdapter;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display
        EdgeToEdge.enable(this);

        // Set the layout for this activity
        setContentView(R.layout.activity_friend);

        // Handle system window insets for full screen experience
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieving selected friend data
        Friend contact = (Friend) getIntent().getSerializableExtra("contact");

        // Find UI elements by ID
        ImageView iv = findViewById(R.id.avatar_image);
        TextView tvName = findViewById(R.id.avatar_name);
        TextView tvLocation = findViewById(R.id.avatar_location);
        TextView tvLastSeen = findViewById(R.id.last_seen_time);

        // Displaying friend details if available
        if (contact != null) {
            iv.setImageResource(contact.getAvatar());
            tvName.setText(contact.getName());
            tvLocation.setText(contact.getLocation());
            tvLastSeen.setText(contact.getLastSeenTime());
        }

        // Back button to return to chat screen
        ImageView backArrow = findViewById(R.id.backArrow);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendActivity.this, ChatActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Initializing the chat components
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);

        chatRecyclerView.setAdapter(messageAdapter);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = messageInput.getText().toString().trim();
                if (!text.isEmpty()) {
                    // Create a message with text and current time
                    Message message = new Message(text);
                    messageList.add(message);

                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    chatRecyclerView.scrollToPosition(messageList.size() - 1);

                    // Clearing the input field
                    messageInput.setText("");
                }
            }
        });
    }
}
