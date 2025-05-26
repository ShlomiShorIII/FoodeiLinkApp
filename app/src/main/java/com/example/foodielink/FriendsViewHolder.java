package com.example.foodielink;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

// ViewHolder for an item in the friends list
public class FriendsViewHolder extends RecyclerView.ViewHolder {

    public ImageView avatar_image; // Friend's avatar image
    public TextView avatar_name; // Friend's name
    public TextView avatar_location; // Friend's location
    public TextView last_seen_time; // Last seen time
    public CardView card; // Friend's card layout

    // Constructor that initializes the UI components
    public FriendsViewHolder(@NonNull View itemView) {
        super(itemView);
        this.avatar_image = itemView.findViewById(R.id.avatar_image);
        this.avatar_name = itemView.findViewById(R.id.avatar_name);
        this.avatar_location = itemView.findViewById(R.id.avatar_location);
        last_seen_time = itemView.findViewById(R.id.last_seen_time);
        this.card = itemView.findViewById(R.id.card);
    }
}
