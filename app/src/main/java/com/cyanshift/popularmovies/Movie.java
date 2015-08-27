package com.cyanshift.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by michael on Aug/26/2015.
 */

public class Movie implements Parcelable {

    public final static String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w342//";

    private boolean adult;
    private String backdrop_path;
    private ArrayList<Integer> genre_ids;
    private Integer id;
    private String original_language;
    private String original_title;
    private String overview;
    private Date release_date;
    private String poster_path;
    private Double popularity;
    private String title;
    private boolean video;
    private Double vote_average;
    private Integer vote_count;


    public Movie() {

    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public ArrayList<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(ArrayList<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public Integer getVote_count() {
        return vote_count;
    }

    public void setVote_count(Integer vote_count) {
        this.vote_count = vote_count;
    }

    private Movie (Parcel in) {
        this.adult = in.readByte() != 0;
        this.backdrop_path = in.readString();
        this.genre_ids = (ArrayList<Integer>) in.readSerializable();
        this.id = in.readInt();
        this.original_language = in.readString();
        this.original_title = in.readString();
        this.overview = in.readString();
        this.release_date = new Date(in.readLong());
        this.poster_path = in.readString();
        this.popularity = in.readDouble();
        this.title = in.readString();
        this.video = in.readByte() != 0;
        this.vote_average = in.readDouble();
        this.vote_count = in.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (adult ? 1: 0));
        dest.writeString(backdrop_path);
        dest.writeSerializable(genre_ids);
        dest.writeInt(id);
        dest.writeString(original_language);
        dest.writeString(original_title);
        dest.writeString(overview);
        if (release_date != null)
            dest.writeLong(release_date.getTime());
        dest.writeString(poster_path);
        dest.writeDouble(popularity);
        dest.writeString(title);
        dest.writeByte((byte) (video ? 1: 0));
        dest.writeDouble(vote_average);
        dest.writeInt(vote_count);

    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
