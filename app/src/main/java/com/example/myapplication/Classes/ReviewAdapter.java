package com.example.myapplication.Classes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;


public class ReviewAdapter extends ArrayAdapter<Review> {
    private Activity context;
    private ArrayList<Review> reviews;
    RecyclerView.ViewHolder viewHolder;

    public ReviewAdapter(Activity context, ArrayList<Review> reviews) {
        super(context, 0, reviews);
        this.context = context;
        this.reviews = reviews;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            LayoutInflater inflater = context.getLayoutInflater();
            @SuppressLint("ViewHolder") View listViewItem = inflater.inflate(R.layout.rating_layout, null, true);

            TextView textViewServiceTitle = listViewItem.findViewById(R.id.textViewServiceTitle);
            TextView textViewUsername = listViewItem.findViewById(R.id.textViewUsername);
            TextView textViewReview = listViewItem.findViewById(R.id.textViewReview);
            RatingBar ratingBar = listViewItem.findViewById(R.id.simpleRatingBar);

            textViewServiceTitle.setText(reviews.get(position).getServiceTitle());
            textViewUsername.setText(reviews.get(position).getUsername());
            textViewReview.setText(reviews.get(position).getReview());
            ratingBar.setRating(reviews.get(position).getRating());

            return listViewItem;
        } else {
            viewHolder = (RecyclerView.ViewHolder)convertView.getTag();
        }
        return convertView;

    }
}
