package com.cyanshift.popularmovies;


import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
 * A simple {@link Fragment} subclass.
 * Use the {@link PostersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostersFragment extends Fragment {

    ArrayList<Movie> savedMovies;
    GridView gridView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostersFragment newInstance(String param1, String param2) {
        PostersFragment fragment = new PostersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PostersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if (savedInstanceState != null) {
            savedMovies = savedInstanceState.getParcelableArrayList("myKey");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("myKey", savedMovies);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_posters, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridView_posters);
        this.gridView = gridView;
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) parent.getItemAtPosition(position);
                DataHolder.getInstance().setValueForKey(movie, "currentMovie");
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        updatePosters();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            savedMovies = null;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updatePosters() {
        if (savedMovies != null) {
            gridView.setAdapter(new PosterAdapter(getActivity(), savedMovies));
        }
        else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortParam = prefs.getString(getString(R.string.pref_sort_key),
                    getString(R.string.pref_sort_popular));

            FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
            fetchMoviesTask.execute(sortParam);
        }
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private final String API_KEY = ""; // PUT YOUR API KEY HERE!
        private final String BASE_PATH = "http://api.themoviedb.org/3/discover/movie?";
        private final String SORT_PARAM_KEY = "sort_by";
        private final String KEY_PARAM_KEY = "api_key";

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {


            String sortParam = getString(R.string.pref_sort_popular);
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
            if (movies != null)
                savedMovies = movies;
                gridView.setAdapter(new PosterAdapter(getActivity(), movies));
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
    }

}
