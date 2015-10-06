package com.cyanshift.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Movie movie = (Movie) DataHolder.getInstance().valueForKey("currentMovie");

        // Set original Title
        TextView textView = (TextView) rootView.findViewById(R.id.detail_title_textview);
        textView.setText(movie.getOriginal_title());

        // Set release date (year)
        textView = (TextView) rootView.findViewById(R.id.detail_releasedate_textview);
        Date releaseDate = movie.getRelease_date();
        if (releaseDate != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(releaseDate);
            Integer year = cal.get(Calendar.YEAR);
            textView.setText(year.toString());
        }
        else {
            textView.setText("???");
        }

        // Set rating
        textView = (TextView) rootView.findViewById(R.id.detail_rating_textview);
        textView.setText(movie.getVote_average().toString());

        // Set plot
        textView = (TextView) rootView.findViewById(R.id.detail_plot_textview);
        textView.setText(movie.getOverview());

        // Set poster
        ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_poster_imageview);
        //Picasso.with(getActivity()).load(Movie.BASE_POSTER_URL + movie.getPoster_path()).into(imageView);
        Picasso.with(getActivity()).load(Movie.BASE_POSTER_URL + movie.getPoster_path()).placeholder(R.drawable.image_placeholder).error(R.drawable.image_error).into(imageView);

        return rootView;
    }
}
