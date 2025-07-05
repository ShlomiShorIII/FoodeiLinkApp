package com.example.foodielink;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

// ViewHolder for an item in the friends list
public class FriendsViewHolder extends RecyclerView.ViewHolder {

    // Friend's avatar image
    public ImageView avatar_image;

    // Friend's name
    public TextView avatar_name;

    // Friend's location
    public TextView avatar_location;

    // Last seen time
    public TextView last_seen_time;

    // Friend's card layout
    public CardView card;

    // Constructor that initializes the UI components
    public FriendsViewHolder(@NonNull View itemView) {
        super(itemView);

        // Bind UI components by their IDs
        this.avatar_image = itemView.findViewById(R.id.avatar_image);
        this.avatar_name = itemView.findViewById(R.id.avatar_name);
        this.avatar_location = itemView.findViewById(R.id.avatar_location);
        this.last_seen_time = itemView.findViewById(R.id.last_seen_time);
        this.card = itemView.findViewById(R.id.card);
    }
}
