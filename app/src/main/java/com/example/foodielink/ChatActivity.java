package com.example.foodielink;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FriendsAdapter adapter;
    private List<Friend> friendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        friendsList = new ArrayList<>();
        friendsList.add(new Friend("Ron Cohen", R.drawable.avatar1, "Tel Aviv", "16/02/2025"));
        friendsList.add(new Friend("Sarah Levi", R.drawable.avatar2, "Ramat Gan", "16/02/2025"));
        friendsList.add(new Friend("Jude Omer", R.drawable.avatar3, "Tel Aviv", "15/02/2025"));
        friendsList.add(new Friend("Dani Kampo", R.drawable.avatar4, "Ramat Gan", "16/02/2025"));
        friendsList.add(new Friend("Joe Hickman", R.drawable.avatar5, "Tel Aviv", "15/02/2025"));
        friendsList.add(new Friend("Bobby Pires", R.drawable.avatar6, "Ramat Gan", "15/02/2025"));
        friendsList.add(new Friend("Naor Feldman", R.drawable.avatar7, "Tel Aviv", "15/02/2025"));
        friendsList.add(new Friend("Amit Gross", R.drawable.avatar8, "Ramat Gan", "13/02/2025"));

        adapter = new FriendsAdapter(friendsList);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeDeleteFriend(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        ImageView chefHat = findViewById(R.id.nav_search);
        chefHat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, SwipePage.class);
                startActivity(intent);
            }
        });

    }
}
