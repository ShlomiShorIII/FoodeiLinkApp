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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend, parent, false);
        return new FriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
        Friend contact = friends.get(position);
        holder.avatar_name.setText(contact.getName());
        holder.avatar_location.setText(contact.getLocation());
        holder.avatar_image.setImageResource(contact.getAvatar());
        holder.last_seen_time.setText(contact.getLastSeenTime());

        // Opening FriendActivity when the card is clicked
        holder.card.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FriendActivity.class);
                intent.putExtra("contact", contact);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        (Activity) v.getContext(),
                        holder.card,
                        "cardTransition"
                );
                v.getContext().startActivity(intent, options.toBundle());
            }

        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    // Removing a friend from the list and updating the view
    public void DeleteFriend(int pos) {
        friends.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, friends.size());
    }

}