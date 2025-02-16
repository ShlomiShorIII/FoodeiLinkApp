package com.example.foodielink;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.yalantis.library.Koloda;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class SwipePage extends AppCompatActivity {

    private SwipeAdapter adapter;
    private List<Integer> list;
    private Koloda koloda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_page);

        koloda = findViewById(R.id.koloda);

        list = new ArrayList<>();
        list.add(R.drawable.dish1);
        list.add(R.drawable.dish2);
        list.add(R.drawable.dish3);
        list.add(R.drawable.dish4);
        list.add(R.drawable.dish5);
        list.add(R.drawable.dish6);
        list.add(R.drawable.dish7);
        list.add(R.drawable.dish8);
        list.add(R.drawable.dish9);
        list.add(R.drawable.dish10);
        list.add(R.drawable.dish11);
        list.add(R.drawable.dish12);
        list.add(R.drawable.dish13);
        list.add(R.drawable.dish14);
        list.add(R.drawable.dish15);
        list.add(R.drawable.dish16);
        list.add(R.drawable.dish17);
        list.add(R.drawable.dish18);

        Collections.shuffle(list);

        adapter = new SwipeAdapter(this, list);
        koloda.setAdapter(adapter);

        ImageView chatButton = findViewById(R.id.nav_chat);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SwipePage.this, ChatActivity.class);
                startActivity(intent);
            }
        });

    }

}
