package com.example.sayantan.moviebugs.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import  android.widget.ListView ;

import retrofit2.Response;

/**
 * Created by Sayantan on 11/30/2017.
 */

public class MoviesResponse {

    @SerializedName("results")
    private List<Movie> results;



    public List<Movie> getResults() {
        return results;
    }



    public void setMovies(List<Movie> results) {
        this.results = results;
    }

}

