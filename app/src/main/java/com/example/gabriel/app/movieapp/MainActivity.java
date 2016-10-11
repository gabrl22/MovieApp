package com.example.gabriel.app.movieapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private GridView mGridView;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MoviesAdapter mAdapter;
    public static final String MOVIEDB_URL = "https://api.themoviedb.org/3/movie";
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new GridLayoutManager(this, 2);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Avengers", "Avengers", "Avengers", "Avengers", "Avengers", 1.2));
        movies.add(new Movie("Avengers", "Avengers", "Avengers", "Avengers", "Avengers", 1.2));
        movies.add(new Movie("Avengers", "Avengers", "Avengers", "Avengers", "Avengers", 1.2));

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(4));
        mAdapter = new MoviesAdapter(this, movies);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        updateMovieList();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    private void updateMovieList() {
        String sortBy = mSharedPreferences.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default_value));
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new FetchMoviesTask().execute(sortBy);
        } else {
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
        }
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            if (movies != null) {
                mAdapter.swap(movies);
            }
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... strings) {
            ArrayList<Movie> movies = new ArrayList<>();
            String sortBy = strings[0];

            Uri uri = Uri.parse(MOVIEDB_URL).buildUpon()
                    .appendPath(sortBy)
                    .appendQueryParameter("page", "1")
                    .appendQueryParameter("api_key", "")
                    .appendQueryParameter("language", "en-US")
                    .build();

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJSONStr;
            try {
                URL url = new URL(uri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                if (inputStream == null) {
                    moviesJSONStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }
                if (stringBuffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    moviesJSONStr = null;
                }
                moviesJSONStr = stringBuffer.toString();
                movies = getMoviesFromJSON(moviesJSONStr);

            } catch (IOException ex) {
                Log.e(MainActivity.class.getSimpleName(), "Error" + ex);
            } catch (JSONException ex) {
                Log.e(MainActivity.class.getSimpleName(), "Error " + ex);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return movies;
        }

        private ArrayList<Movie> getMoviesFromJSON(String jsonStr) throws JSONException {

            ArrayList<Movie> movies = new ArrayList<>();

            JSONObject moviesJSON = new JSONObject(jsonStr);
            JSONArray moviesArray = moviesJSON.getJSONArray("results");
            JSONObject firstMovie = moviesArray.getJSONObject(0);
            String movieTitle = firstMovie.getString("title");

            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject jsonMovie = moviesArray.getJSONObject(i);

                String title = jsonMovie.getString("title");
                String overview = jsonMovie.getString("overview");
                String gridImageUrl = jsonMovie.getString("poster_path");
                String detailImageUrl = jsonMovie.getString("backdrop_path");

                String releaseDate = jsonMovie.getString("release_date");
                double rating = jsonMovie.getDouble("vote_average");
                Movie movie = new Movie(title, overview, gridImageUrl, detailImageUrl, releaseDate, rating);
                movies.add(movie);
            }

            return movies;
        }
    }
}
