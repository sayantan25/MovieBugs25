package com.example.sayantan.moviebugs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import  android.widget.ListView ;

import com.example.sayantan.moviebugs.adapter.MoviesAdapter;
//import com.example.sayantan.moviebugs.api.Client;
import com.example.sayantan.moviebugs.api.Client;
import com.example.sayantan.moviebugs.api.Service;
import com.example.sayantan.moviebugs.model.Movie;
import com.example.sayantan.moviebugs.model.MoviesResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.PUT;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
    public static final String LOG_TAG = MoviesAdapter.class.getName();
    private final static String API_KEY = "f48e0ffb5529f75f4d1c11f966fd71e8";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

    }


   public Activity getActivity() {

       Context context = this;
        while (context instanceof ContextWrapper) {

            if (context instanceof Activity) {
                return (Activity) context;
           }

            context = ((ContextWrapper) context).getBaseContext();

        }

        return null;
   }

    private void initViews() {


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        movieList = new ArrayList<>();
        adapter = new MoviesAdapter(this, movieList);

       if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        } else {
           recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        }


        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        checkSortOrder();


    }

    private void loadJSON(){

        try{
            if (API_KEY.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please obtain API Key firstly from themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }

            Client Client = new Client();

            Service service = Client.getClient().create(Service.class) ;

            Call<MoviesResponse> call =service.getPopularMovies(API_KEY);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    if(response.code()==200) {
                        List<Movie> movies = response.body().getResults();

                        Collections.sort(movies, Movie.BY_NAME_ALPHABETICAL);
                        recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                        recyclerView.smoothScrollToPosition(0);
                    }else
                   {
                       Toast.makeText(MainActivity.this, "ERROR_FETCHING_DATA", Toast.LENGTH_SHORT).show();
                   }

                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {

                    Log.d("Error", t.getMessage());

                    Toast.makeText(MainActivity.this, "ERROR_FETCHING_DATA", Toast.LENGTH_SHORT).show();


                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();

        }
    }
    private void loadJSON1(){

        try{
            if (API_KEY.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please obtain API Key firstly from themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }

            Client Client = new Client();

            Service service = Client.getClient().create(Service.class) ;

            Call<MoviesResponse> call =service.getTopRatedMovies(API_KEY);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    if(response.code()==200) {
                        List<Movie> movies = response.body().getResults();

                        Collections.sort(movies, Movie.BY_NAME_ALPHABETICAL);
                        recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                        recyclerView.smoothScrollToPosition(0);
                  }else
                    {
                        Toast.makeText(MainActivity.this, "ERROR_FETCHING_DATA", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {

                    Log.d("Error", t.getMessage());

                    Toast.makeText(MainActivity.this, "ERROR_FETCHING_DATA", Toast.LENGTH_SHORT).show();


                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();

        }
    }




    @Override
    public  boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)

    {
        switch (item.getItemId())
        {
            case R.id.menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true ;

                default:
                    return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences , String s)
    {
        Log.d(LOG_TAG, "preferences updated") ;
        checkSortOrder();

    }

    private void checkSortOrder(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder = preferences.getString(
                this.getString(R.string.pref_sort_order_key),
                this.getString(R.string.pref_most_popular)
        );

            if (sortOrder.equals(this.getString(R.string.pref_most_popular)))

            {
                Log.d(LOG_TAG, "sorting by most popular");
                loadJSON();
            }else
            {
                Log.d(LOG_TAG, "Sorting by vote average");
                loadJSON1();
            }

    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (movieList.isEmpty())
        {
          checkSortOrder();
        }else
        {
            checkSortOrder();
        }
    }



}
