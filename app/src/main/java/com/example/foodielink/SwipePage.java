package com.example.foodielink;

import android.os.Bundle;
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

        Collections.shuffle(list);

        adapter = new SwipeAdapter(this, list);
        koloda.setAdapter(adapter);
    }
}
