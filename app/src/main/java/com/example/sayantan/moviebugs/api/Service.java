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


    @GET("movie/popular?api_key=")
    Call<MoviesResponse> getPopularMovies(@Query("f48e0ffb5529f75f4d1c11f966fd71e8") String apikey) ;

    @GET("movie/top_rated?api_key=")
    Call<MoviesResponse> getTopRatedMovies(@Query("f48e0ffb5529f75f4d1c11f966fd71e8") String apikey) ;

}
