package pt.rikmartins.adn.popularmoviesstage1.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.rikmartins.adn.popularmoviesstage1.R;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MovieListItem;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MoviePosterViewHolder> {

    private final MovieAdapterOnClickHandler clickHandler;
    private List<MovieListItem> movieListItems;

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MoviePosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_movie_grid, parent, false);
        return new MoviePosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviePosterViewHolder holder, int position) {
        holder.itemText.setText(movieListItems.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return movieListItems == null ? 0 : movieListItems.size();
    }

    public class MoviePosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView itemText;

        public MoviePosterViewHolder(View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.tv_item_text);
            if (clickHandler != null) itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            MovieListItem movieListItem = movieListItems.get(getAdapterPosition());
            clickHandler.onClick(movieListItem);
        }
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(MovieListItem movieListItem);
    }

    public void setMovieListItems(List<MovieListItem> movieListItems) {
        this.movieListItems = movieListItems;
        notifyDataSetChanged();
    }

}
