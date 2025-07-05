package com.example.foodielink;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

// Adapter for displaying images in swipe cards
public class SwipeAdapter extends BaseAdapter {

    private Context context; // Application context
    private List<Integer> list; // List of image resource IDs

    // Constructor to initialize the adapter with image list and context
    public SwipeAdapter(Context context, List<Integer> list) {
        this.context = context;
        this.list = list;
    }

    // Returns a fixed number of items
    @Override
    public int getCount() {
        return 50;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position % list.size());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_koloda, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageResource(list.get(position % list.size()));
        return convertView;
    }

    // ViewHolder inner class
    static class ViewHolder {
        ImageView imageView;
    }
}
