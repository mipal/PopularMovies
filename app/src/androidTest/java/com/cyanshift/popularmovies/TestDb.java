package com.cyanshift.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.cyanshift.popularmovies.data.MovieContract;
import com.cyanshift.popularmovies.data.MovieDbHelper;

import java.util.HashSet;

/**
 * Created by michael on 21/09/15.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    public void setUp() {

    }

    public void testDatabaseFunctionality() {
        deleteDatabase();
        try {
            createDb();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        insertToDb();
        queryDb();
        updateDb();
        queryDb();
        deleteFromDb();
    }


    void deleteDatabase() {
        Log.d(LOG_TAG, "deleting database");
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    void createDb() throws Throwable {
        Log.d(LOG_TAG, "Creating database");
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieContract.MovieEntry.TABLE_NAME);

        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // All tables Created?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: Database not created correctly", c.moveToFirst());

        c = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieEntry.TABLE_NAME + ")",
                null);
        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(MovieContract.MovieEntry._ID);
        locationColumnHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        locationColumnHashSet.add(MovieContract.MovieEntry.COLUMN_ADULT);
        locationColumnHashSet.add(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH);
        locationColumnHashSet.add(MovieContract.MovieEntry.COLUMN_GENRE_IDS);
        locationColumnHashSet.add(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE);
        locationColumnHashSet.add(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE);
        locationColumnHashSet.add(MovieContract.MovieEntry.COLUMN_OVERVIEW);
        locationColumnHashSet.add(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
        locationColumnHashSet.add(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
        locationColumnHashSet.add(MovieContract.MovieEntry.COLUMN_POPULARITY);
        locationColumnHashSet.add(MovieContract.MovieEntry.COLUMN_TITLE);
        locationColumnHashSet.add(MovieContract.MovieEntry.COLUMN_VIDEO);
        locationColumnHashSet.add(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
        locationColumnHashSet.add(MovieContract.MovieEntry.COLUMN_VOTE_COUNT);


        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while(c.moveToNext());
        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                locationColumnHashSet.isEmpty());


        db.close();
    }

    void insertToDb() {
        Log.d(LOG_TAG, "Inserting into database");
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 76341);
        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "Mad Max: Fury Road");

        Uri insertedUri = mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);
        long movieId = ContentUris.parseId(insertedUri);
        assertTrue("id was " + movieId, movieId > -1);
        Log.d(LOG_TAG, "Inserted with id: " + movieId);

    }

    void queryDb() {
        Log.d(LOG_TAG, "Querying database");
        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        if (movieCursor.moveToFirst()) {
            String title = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
            int movieID = movieCursor.getInt(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
            Log.d(LOG_TAG, "title: "+ title);
            Log.d(LOG_TAG, "Movie_id: "+ movieID);
        }
    }

    void updateDb() {
        Log.d(LOG_TAG, "Updating database");
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 666);
        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "Mad Max: Fury Road");

        int count = mContext.getContentResolver().update(
                MovieContract.MovieEntry.CONTENT_URI,
                movieValues,
                MovieContract.MovieEntry._ID + " = ?",
                new String[]{"1"});
        Log.d(LOG_TAG, "number of rows updated: " + count);
    }

    void deleteFromDb() {
        Log.d(LOG_TAG, "Deleting from database");
        int rowsDeleted = getContext().getContentResolver().delete(
                MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.COLUMN_TITLE + "= ?",
                new String[]{"Mad Max: Fury Road"});
        Log.d(LOG_TAG, "delete rows: " + rowsDeleted);
    }
}
