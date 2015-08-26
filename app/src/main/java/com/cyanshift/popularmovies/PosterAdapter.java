package com.cyanshift.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by michael on Aug/26/2015.
 */
public class PosterAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Movie> movies;

    public PosterAdapter(Context c, ArrayList<Movie> movies) {
        mContext = c;
        this.movies = movies;
    }

    public int getCount() {
        return movies.size();
    }

    public Object getItem(int position) {
        return movies.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }


    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        
        View view;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            view = LayoutInflater.from(mContext).inflate(R.layout.grid_item_poster, null);
        } else {
            view = convertView;
        }

        TextView text = (TextView)view.findViewById(R.id.grid_item_poster_text_view);
        ImageView imageView = (ImageView)view.findViewById(R.id.grid_item_poster_imageview);

        Movie movie = movies.get(position);
        Picasso.with(mContext).load(Movie.BASE_POSTER_URL + movie.getPoster_path()).into(imageView);
        text.setText(movie.getTitle());

        return view;

    }
}
