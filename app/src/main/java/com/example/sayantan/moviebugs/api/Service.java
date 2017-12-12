package com.example.sayantan.moviebugs.api;

import android.util.JsonReader;

import com.example.sayantan.moviebugs.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Sayantan on 11/30/2017.
 */

public interface Service {


    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apikey) ;

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apikey) ;

}
