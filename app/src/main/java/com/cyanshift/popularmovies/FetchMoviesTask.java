package com.cyanshift.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by michael on Aug/31/2015.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private final String API_KEY = "4881ef723ce5e7be9be793d66b3e2afe"; // PUT YOUR API KEY HERE!
    private final String BASE_PATH = "http://api.themoviedb.org/3/discover/movie?";
    private final String SORT_PARAM_KEY = "sort_by";
    private final String KEY_PARAM_KEY = "api_key";

    private FetchMovieTaskInterface delegate;
    private String sortParams;

    public FetchMoviesTask(FetchMovieTaskInterface delegate, String sortParams ) {
        this.delegate = delegate;
        this.sortParams = sortParams;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {


        //String sortParam = getString(R.string.pref_sort_popular);
        String sortParam = sortParams;
        if (params.length > 0) {
            sortParam = params[0];
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonStr = null;

        try {
            Uri builtUri = Uri.parse(BASE_PATH).buildUpon()
                    .appendQueryParameter(SORT_PARAM_KEY, sortParam)
                    .appendQueryParameter(KEY_PARAM_KEY, API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.e(LOG_TAG, builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                // Adding new line not necessary, but makes debuging easier.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            jsonStr = buffer.toString();
            //Log.e(LOG_TAG, jsonStr);

        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getMovieDataFromJson(jsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }


        return null;
    }


    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        
        delegate.fetchMovieTaskFinished(movies);
    }

    private ArrayList<Movie> getMovieDataFromJson(String jsonString) throws JSONException  {

        if (jsonString == null)
            return null;

        JSONObject resultJson = new JSONObject(jsonString);
        JSONArray resultArray = resultJson.getJSONArray("results");

        ArrayList<Movie> movies = new ArrayList<Movie>();

        for (int i = 0; i < resultArray.length(); i++) {
            JSONObject result = resultArray.getJSONObject(i);
            Movie movie = new Movie();
            movie.setAdult(result.getBoolean("adult"));
            movie.setBackdrop_path(result.getString("backdrop_path"));
            JSONArray genreIds = result.getJSONArray("genre_ids");
            ArrayList<Integer>genreIdArrayList = new ArrayList<Integer>();
            for (int j = 0; j < genreIds.length(); j++) {
                Integer genreId = genreIds.getInt(j);
                genreIdArrayList.add(genreId);
            }
            movie.setGenre_ids(genreIdArrayList);
            movie.setId(result.getInt("id"));
            movie.setOriginal_language(result.getString("original_language"));
            movie.setOriginal_title(result.getString("original_title"));
            movie.setOverview(result.getString("overview"));
            String dateStr = result.getString("release_date");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                movie.setRelease_date(df.parse(dateStr));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            movie.setPoster_path(result.getString("poster_path"));
            movie.setPopularity(result.getDouble("popularity"));
            movie.setTitle(result.getString("title"));
            movie.setVideo(result.getBoolean("video"));
            movie.setVote_average(result.getDouble("vote_average"));
            movie.setVote_count(result.getInt("vote_count"));

            movies.add(movie);
        }

        return movies;
    }

    public interface FetchMovieTaskInterface {

        public void fetchMovieTaskFinished(ArrayList<Movie> movies);
    }
}
