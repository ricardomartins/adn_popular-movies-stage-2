package pt.rikmartins.adn.popularmoviesstage2.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import pt.rikmartins.adn.popularmoviesstage2.R;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;

class MovieAdapter extends PagedListAdapter<MovieInfo, MovieAdapter.MoviePosterViewHolder> {

    private final Picasso picasso;

    private int desiredPosterWidth = 0;

    private MovieAdapterOnClickHandler clickHandler;

    private MovieAdapterImageUrlProvider movieAdapterImageUrlProvider;

    private View referenceView; // used solely to calculate view width

    @Inject
    MovieAdapter(Picasso picasso) {
        super(new DiffUtil.ItemCallback<MovieInfo>() {
            @Override
            public boolean areItemsTheSame(@NonNull MovieInfo oldItem, @NonNull MovieInfo newItem) {
                if (oldItem.getId() != null) return oldItem.getId().equals(newItem.getId());
                return newItem.getId() == null;
            }

            @Override
            public boolean areContentsTheSame(@NonNull MovieInfo oldItem, @NonNull MovieInfo newItem) {
                return areItemsTheSame(oldItem, newItem); // It will have to do for now
            }
        });
        this.picasso = picasso;
    }

    void setMovieAdapterOnClickHandler(MovieAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    void setMovieAdapterImageUrlProvider(MovieAdapterImageUrlProvider movieAdapterImageUrlProvider) {
        this.movieAdapterImageUrlProvider = movieAdapterImageUrlProvider;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MoviePosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.main_movie_item, parent, false);

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
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MoviePosterViewHolder holder, int position) {
        final MovieInfo movieInfo = getItem(position);

        if (movieInfo != null) holder.bind(movieInfo);
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

        private void bind(MovieInfo movieInfo) {
            itemText.setText(movieInfo.getTitle());
            if (movieAdapterImageUrlProvider != null)
                movieAdapterImageUrlProvider.getPosterUrlOfMovieWithWidth(movieInfo, desiredPosterWidth)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String url) throws Exception {
                                picasso.load(url).into(posterImage);
                            }
                        });
        }

        @Override
        public void onClick(View v) {
            MovieInfo movieListItem = getItem(getAdapterPosition());
            clickHandler.onClick(movieListItem);
        }
    }

    interface MovieAdapterOnClickHandler {
        void onClick(MovieInfo movieListItem);
    }

    interface MovieAdapterImageUrlProvider {
        Single<String> getPosterUrlOfMovieWithWidth(MovieInfo movieInfo, int width);
    }
}
