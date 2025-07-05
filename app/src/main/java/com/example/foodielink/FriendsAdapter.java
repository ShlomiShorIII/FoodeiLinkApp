package com.example.foodielink;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// Adapter for displaying a list of friends in RecyclerView
public class FriendsAdapter extends RecyclerView.Adapter<FriendsViewHolder> {

    // List of friends
    private List<Friend> friends;

    // Constructor to initialize friends list
    public FriendsAdapter(List<Friend> friendsList) {
        this.friends = friendsList;
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the friend card layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend, parent, false);
        return new FriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
        // Get the friend at the current position
        Friend contact = friends.get(position);

        // Bind friend data to UI components
        holder.avatar_name.setText(contact.getName());
        holder.avatar_location.setText(contact.getLocation());
        holder.avatar_image.setImageResource(contact.getAvatar());
        holder.last_seen_time.setText(contact.getLastSeenTime());

        // Opening FriendActivity when the card is clicked
        holder.card.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Create intent to open friend details screen
                Intent intent = new Intent(v.getContext(), FriendActivity.class);
                intent.putExtra("contact", contact);

                // Create transition animation
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        (Activity) v.getContext(),
                        holder.card,
                        "cardTransition"
                );

                // Start FriendActivity with animation
                v.getContext().startActivity(intent, options.toBundle());
            }

        });
    }

    // Get friend at a specific position
    public Friend getFriendAt(int position) {
        return friends.get(position);
    }

    // Restore a previously deleted friend at a specific position
    public void restoreFriend(Friend friend, int position) {
        friends.add(position, friend);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        // Return number of friends
        return friends.size();
    }

    // Removing a friend from the list and updating the view
    public void DeleteFriend(int pos) {
        friends.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, friends.size());
    }

}