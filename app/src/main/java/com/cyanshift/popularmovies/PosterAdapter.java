package com.cyanshift.popularmovies;

import android.content.Context;
import android.net.Uri;
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
        if (movies != null)
            return movies.size();
        return 0;
    }

    public Object getItem(int position) {
        return movies.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }


    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        PosterViewHolder viewHolder;

        View view;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            view = LayoutInflater.from(mContext).inflate(R.layout.grid_item_poster, null);

            viewHolder = new PosterViewHolder();
            viewHolder.textView = (TextView)view.findViewById(R.id.grid_item_poster_text_view);
            viewHolder.imageView = (ImageView)view.findViewById(R.id.grid_item_poster_imageview);

            view.setTag(viewHolder);

        } else {
            view = convertView;
            viewHolder = (PosterViewHolder) view.getTag();
        }


        Movie movie = movies.get(position);
        String urlString = Movie.BASE_POSTER_URL + movie.getPoster_path();
        Uri uri = Uri.parse(urlString);
        if (uri != null) {
            Picasso.with(mContext).load(urlString).into(viewHolder.imageView);
        }

        viewHolder.textView.setText(movie.getTitle());

        return view;

    }

    static class PosterViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
