package com.cyanshift.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cyanshift.popularmovies.data.MovieContract;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clickedButton(View view) {

        //TODO: Check if already exist, if so, delete it from the database

        Movie movie = (Movie) DataHolder.getInstance().valueForKey("currentMovie");


        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        movieValues.put(MovieContract.MovieEntry.COLUMN_ADULT, movie.isAdult());
        movieValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdrop_path());
        // Store genre Id's as a comma separated String
        String genreIds = "";
        for (int i = 0; i < movie.getGenre_ids().size(); i++) {
            genreIds += movie.getGenre_ids().get(i);
            if (i < movie.getGenre_ids().size() -1)
                genreIds += ",";
        }
        movieValues.put(MovieContract.MovieEntry.COLUMN_GENRE_IDS, genreIds);
        movieValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, movie.getOriginal_language());
        movieValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginal_title());
        movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getRelease_date().toString());
        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPoster_path());
        movieValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
        movieValues.put(MovieContract.MovieEntry.COLUMN_VIDEO, movie.isVideo());
        movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVote_average());
        movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVote_count());


        Uri insertedUri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);
        long movieId = ContentUris.parseId(insertedUri);


        Log.d("BAJS", "Inserting into database" + movieId);
    }
}
