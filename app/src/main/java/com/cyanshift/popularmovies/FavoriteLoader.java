package com.cyanshift.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.cyanshift.popularmovies.data.MovieContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by michael on 08/10/15.
 */
public class FavoriteLoader {

    public static final String LOG_TAG = FavoriteLoader.class.getSimpleName();

    private Context mContext;

    public FavoriteLoader(Context c) {
        this.mContext = c;
        this.loadMovies();
    }

    public ArrayList<Movie> loadMovies() {
        ArrayList<Movie> movies = new ArrayList<Movie>();

        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        movieCursor.moveToFirst();
        do {
            Integer movieId = movieCursor.getInt(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
            String title = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
            boolean adult = movieCursor.getInt(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ADULT)) != 0 ? true : false;
            String backDropPath = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH));
            String genreIdsString = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_GENRE_IDS));
            String[] genreStrings = genreIdsString.split(",");
            ArrayList<Integer> genreIds = new ArrayList<Integer>();
            for (String s:genreStrings) {
                genreIds.add(Integer.parseInt(s));
            }
            String orgLanguage = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE));
            String orgTitle = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE));
            String overview = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW));
            String releasString = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
            Date releaseDate = new Date();
            try {
                releaseDate = sdf.parse(releasString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String posterPath = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
            Double popularity = movieCursor.getDouble(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POPULARITY));
            boolean isVideo = movieCursor.getInt(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VIDEO)) != 0 ? true : false;
            Double voteAverage = movieCursor.getDouble(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE));
            Integer voteCount = movieCursor.getInt(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_COUNT));

            Movie movie = new Movie();
            movie.setId(movieId);
            movie.setTitle(title);
            movie.setAdult(adult);
            movie.setBackdrop_path(backDropPath);
            movie.setOriginal_language(orgLanguage);
            movie.setOriginal_title(orgTitle);
            movie.setOverview(overview);
            movie.setRelease_date(releaseDate);
            movie.setPoster_path(posterPath);
            movie.setPopularity(popularity);
            movie.setVideo(isVideo);
            movie.setVote_average(voteAverage);
            movie.setVote_count(voteCount);
            movies.add(movie);
        }
        while(movieCursor.moveToNext());

        return movies;
    }
}
