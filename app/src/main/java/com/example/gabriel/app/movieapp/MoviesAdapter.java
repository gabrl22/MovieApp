package com.example.gabriel.app.movieapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Gabriel on 10/07/16.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private final String imageBaseUrl = "https://image.tmdb.org/t/p/w185";
    private ArrayList<Movie> mMovies;
    private Context mContext;
    public String hola = "hola";

    // Define listener member variable
    private OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public MoviesAdapter(Context context, ArrayList<Movie> movies){
        mContext = context;
        mMovies = movies;
    }

    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(mContext).load(imageBaseUrl + mMovies.get(position).getGridImageUrl()).into(holder.movieImageView);

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView movieImageView;



        public ViewHolder(final View itemView) {
            super(itemView);
            movieImageView = (ImageView)itemView.findViewById(R.id.grid_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Movie movie = mMovies.get(position);
                    Intent intent = new Intent(mContext, MovieDetailActivity.class);
                    intent.putExtra("MOVIE", movie);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    final void swap(ArrayList<Movie> movies){
        mMovies.clear();
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }


}
