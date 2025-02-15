package com.example.foodielink;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class FriendsViewHolder extends RecyclerView.ViewHolder {

    public ImageView avatar_image;
    public TextView avatar_name;
    public TextView avatar_location;
    public CardView card;


    public FriendsViewHolder(@NonNull View itemView) {
        super(itemView);
        this.avatar_image = itemView.findViewById(R.id.avatar_image);
        this.avatar_name = itemView.findViewById(R.id.avatar_name);
        this.avatar_location = itemView.findViewById(R.id.avatar_location);
        this.card = itemView.findViewById(R.id.card);
    }
}
