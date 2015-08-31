package com.cyanshift.popularmovies;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostersFragment extends Fragment implements FetchMoviesTask.FetchMovieTaskInterface {

    ArrayList<Movie> savedMovies;
    String latestSortParam;
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
        // Reuse the old list if sortParams have not changed
        if (savedInstanceState != null) {
            savedMovies = savedInstanceState.getParcelableArrayList("myKey");
            latestSortParam = savedInstanceState.getString("latestParams");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("myKey", savedMovies);
        outState.putString("latestParams", getCurrentSortParam());
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
            //savedMovies = null;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getCurrentSortParam() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular));
    }

    private boolean paramsChanged() {
        if (latestSortParam == null)
            return true;

        return !latestSortParam.equals(getCurrentSortParam());
    }

    private void updatePosters() {
        if (savedMovies != null && !paramsChanged()) {
            gridView.setAdapter(new PosterAdapter(getActivity(), savedMovies));
        }
        else {
            if (isNetworkAvailable()) {
                String sortParam = getCurrentSortParam();
                FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(this, getString(R.string.pref_sort_popular));
                fetchMoviesTask.execute(sortParam);
            }
            else {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.network_error)
                        .setMessage(R.string.network_error_dialogue)
                        .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                updatePosters();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void fetchMovieTaskFinished(ArrayList<Movie> movies) {
        if (movies != null)
            savedMovies = movies;
        latestSortParam = getCurrentSortParam();
        gridView.setAdapter(new PosterAdapter(getActivity(), movies));
    }
}
