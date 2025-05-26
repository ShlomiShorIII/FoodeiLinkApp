package com.example.foodielink;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

// Class that enables deleting a friend via swipe
public class SwipeDeleteFriend extends ItemTouchHelper.SimpleCallback {

    FriendsAdapter adapter; // Friends list adapter

    public SwipeDeleteFriend(FriendsAdapter a) {
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        adapter = a;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    // Function triggered when an item is swiped
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.RIGHT) {
            adapter.DeleteFriend(pos);}
        else {
            adapter.notifyItemChanged(pos);
        }
    }
}
