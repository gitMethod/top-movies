package com.example.android.moviesudacity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    public interface ListItemClickListener {
        void onListItemClick(String movieName);
    }

    final private ListItemClickListener mOnClickListener;
    private ArrayList<Movie> moviesArray;
    private static final String TAG = MoviesAdapter.class.getSimpleName();
    private Context mContext;

    public MoviesAdapter( ArrayList<Movie> moviesArray, Context context, ListItemClickListener listener) {
        this.moviesArray = moviesArray;
        this.mContext = context;
        this.mOnClickListener = listener;
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
            Movie movie = moviesArray.get(getAdapterPosition());
            String name = movie.getTitle();
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(name);
        }
    }
}
