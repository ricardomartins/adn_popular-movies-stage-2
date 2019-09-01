package pt.rikmartins.adn.popularmoviesstage1.ui.main;

import android.net.Uri;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import pt.rikmartins.adn.popularmoviesstage1.R;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MovieListItem;
import pt.rikmartins.adn.popularmoviesstage1.data.ImageUrlGenerator;

class MovieAdapter extends PagedListAdapter<MovieListItem, MovieAdapter.MoviePosterViewHolder> {

    private final Picasso picasso;

    private int desiredPosterWidth = 0;

    private MovieAdapterOnClickHandler clickHandler;
    private ImageUrlGenerator imageUrlGenerator;

    private final SparseArray<Uri> urlCache = new SparseArray<>();

    private View referenceView; // used solely to calculate view width

    @Inject
    MovieAdapter(Picasso picasso) {
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
        this.picasso = picasso;
    }

    void setMovieAdapterOnClickHandler(MovieAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public void onCurrentListChanged(@Nullable PagedList<MovieListItem> previousList, @Nullable PagedList<MovieListItem> currentList) {
        super.onCurrentListChanged(previousList, currentList);
        urlCache.clear();
    }

    @NonNull
    @Override
    public MoviePosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.item_movie_grid, parent, false);

        setReferenceView(view);

        return new MoviePosterViewHolder(view);
    }

    private synchronized void setReferenceView(View view) {
        if (referenceView == null && desiredPosterWidth == 0) {
            referenceView = view;
            referenceView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (referenceView.getVisibility() == View.VISIBLE) {
                        final int widthInPx = referenceView.getWidth();
                        final float density = referenceView.getResources().getDisplayMetrics().density;
                        final float widthInDp = widthInPx / density;
                        setDesiredPosterWidth((int) widthInDp);
                        referenceView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        referenceView = null;
                    }
                }
            });
        }
    }

    private void setDesiredPosterWidth(int width) {
        desiredPosterWidth = width;
        imageUrlGenerator.setPosterSizeByWidth(desiredPosterWidth);
        urlCache.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MoviePosterViewHolder holder, int position) {
        final MovieListItem movieListItem = getItem(position);

        if (movieListItem != null) {
            Uri uri = urlCache.get(position);
            if (uri == null) {
                uri = imageUrlGenerator.getImageUrl(movieListItem.getPosterPath());
                urlCache.append(position, uri);
            }
            holder.bind(movieListItem, uri);
        }
    }

    public class MoviePosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView itemText;

        private final ImageView posterImage;

        MoviePosterViewHolder(View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.tv_item_text);
            posterImage = itemView.findViewById(R.id.iv_poster);

            if (clickHandler != null) itemView.setOnClickListener(this);
        }

        private void bind(MovieListItem movieListItem, Uri imageUrl) {
            itemText.setText(movieListItem.getTitle());
            if (imageUrlGenerator != null)
                picasso.load(imageUrl).into(posterImage);
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

    void setImageUrlGenerator(@NonNull ImageUrlGenerator imageUrlGenerator) {
        this.imageUrlGenerator = imageUrlGenerator;
        imageUrlGenerator.setPosterSizeByWidth(desiredPosterWidth);
        urlCache.clear();
        notifyDataSetChanged();

        // TODO: Refresh all views or something of the kind
    }
}
