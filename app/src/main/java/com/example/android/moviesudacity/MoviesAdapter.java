package com.example.android.moviesudacity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    public interface ListItemClickListener {
        void onListItemClick(Movie movie);
    }

    final private ListItemClickListener mOnClickListener;
    private List<Movie> moviesArray;
    private static final String TAG = MoviesAdapter.class.getSimpleName();

    public MoviesAdapter( List<Movie> moviesArray, ListItemClickListener listener) {
        this.moviesArray = moviesArray;
        this.mOnClickListener = listener;
    }

    public void clear(){
        moviesArray.clear();
    }

    public void swap(List<Movie> data){
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
        holder.posterImage.setImageBitmap(movie.getBitmapImg());
    }

    @Override
    public int getItemCount() {
        return moviesArray.size();
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView posterImage;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            posterImage = (ImageView) itemView.findViewById(R.id.list_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Movie movie = moviesArray.get(clickedPosition);
            mOnClickListener.onListItemClick(movie);
        }
    }
}
