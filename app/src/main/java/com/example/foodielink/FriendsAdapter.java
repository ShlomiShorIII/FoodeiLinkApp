package com.example.foodielink;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsViewHolder> {

    private List<Friend> friends;

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
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void DeleteFriend(int pos) {
        friends.remove(pos);
        notifyDataSetChanged();
    }
}