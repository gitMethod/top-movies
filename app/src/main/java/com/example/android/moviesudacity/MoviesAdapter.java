package com.example.android.moviesudacity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private ArrayList<Movie> moviesArray;
    private static final String TAG = MoviesAdapter.class.getSimpleName();
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String BASE_IMAGE_SIZE = "w185/";
    Context mContext;

    public MoviesAdapter( ArrayList<Movie> moviesArray, Context context) {
        this.moviesArray = moviesArray;
        this.mContext = context;
    }

    public void swap(List<Movie> data){
        moviesArray.clear();
        moviesArray.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MoviesViewHolder viewHolder = new MoviesViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        Movie movie = moviesArray.get(position);
        String imgUrl = BASE_IMAGE_URL + BASE_IMAGE_SIZE + movie.getPosterUrl();
        Picasso.with(mContext).load(imgUrl).into(holder.posterImage);
    }

    @Override
    public int getItemCount() {
        return moviesArray.size();
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder {
        private ImageView posterImage;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            posterImage = (ImageView) itemView.findViewById(R.id.list_poster);
        }
    }
}
