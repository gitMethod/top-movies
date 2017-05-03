package com.example.android.moviesudacity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    public interface ListItemClickListener {
        void onListItemClick(Movie movie, int position);
    }

    final private ListItemClickListener mOnClickListener;
    private List<Movie> moviesArray;
    private static final String TAG = MoviesAdapter.class.getSimpleName();
    private Context mainContext;

    public MoviesAdapter( List<Movie> moviesArray, ListItemClickListener listener, Context context) {
        this.moviesArray = moviesArray;
        this.mOnClickListener = listener;
        this.mainContext = context;
    }

    public void clear(){
        moviesArray.clear();
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
        holder.bindView(position);
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

        public void bindView(int i){
            Picasso.with(mainContext).load(moviesArray.get(i).getPosterPath()).into(posterImage);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Movie movie = moviesArray.get(clickedPosition);
            mOnClickListener.onListItemClick(movie, clickedPosition);
        }
    }
}
