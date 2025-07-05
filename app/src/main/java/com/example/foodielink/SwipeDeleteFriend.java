package com.example.foodielink;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

// Class that enables deleting a friend via swipe with Undo option
public class SwipeDeleteFriend extends ItemTouchHelper.SimpleCallback {

    private FriendsAdapter adapter;

    public SwipeDeleteFriend(FriendsAdapter adapter) {
        super(0, ItemTouchHelper.RIGHT); // Swipe only to the right
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();
        Friend deletedFriend = adapter.getFriendAt(pos);
        adapter.DeleteFriend(pos);

        // Show Undo option with Snackbar
        Snackbar.make(viewHolder.itemView, "Friend deleted", Snackbar.LENGTH_LONG)
                .setAction("Undo", v -> adapter.restoreFriend(deletedFriend, pos))
                .show();
    }
}
