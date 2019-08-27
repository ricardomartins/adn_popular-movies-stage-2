package pt.rikmartins.adn.popularmoviesstage1.ui.main;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import pt.rikmartins.adn.popularmoviesstage1.R;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MovieListItem;

class MovieAdapter extends PagedListAdapter<MovieListItem, MovieAdapter.MoviePosterViewHolder> {

    private final MovieAdapterOnClickHandler clickHandler;

    private Uri imagesUrl;

    MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        super(new DiffUtil.ItemCallback<MovieListItem>() {
            @Override
            public boolean areItemsTheSame(@NonNull MovieListItem oldItem, @NonNull MovieListItem newItem) {
                if (oldItem.getId() != null) return oldItem.getId().equals(newItem.getId());
                return newItem.getId() == null;
            }

            @Override
            public boolean areContentsTheSame(@NonNull MovieListItem oldItem, @NonNull MovieListItem newItem) {
                return areItemsTheSame(oldItem, newItem); // It will have to do for now
            }
        });

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
        MovieListItem movieListItem = getItem(position);
        if (movieListItem == null) return;

        holder.itemText.setText(movieListItem.getTitle());
        if (imagesUrl != null)
            Picasso.get()
                    .load(imagesUrl.buildUpon().appendEncodedPath(movieListItem.getPosterPath()).build())
                    .into(holder.posterImage);
    }

    public class MoviePosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView itemText;

        private ImageView posterImage;

        MoviePosterViewHolder(View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.tv_item_text);
            posterImage = itemView.findViewById(R.id.iv_poster);

            if (clickHandler != null) itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            MovieListItem movieListItem = getItem(getAdapterPosition());
            clickHandler.onClick(movieListItem);
        }
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(MovieListItem movieListItem);
    }

    void setImagesUrl(Uri imagesUrl) {
        this.imagesUrl = imagesUrl;

        notifyDataSetChanged();

        // TODO: Refresh all views or something of the kind
    }
}
