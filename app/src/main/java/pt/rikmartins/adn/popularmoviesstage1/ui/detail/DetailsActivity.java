package pt.rikmartins.adn.popularmoviesstage1.ui.detail;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import pt.rikmartins.adn.popularmoviesstage1.AppComponent;
import pt.rikmartins.adn.popularmoviesstage1.R;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MovieListItem;
import pt.rikmartins.adn.popularmoviesstage1.data.ImageUrlGenerator;
import pt.rikmartins.adn.popularmoviesstage1.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity {

    public static final String ARG_MOVIE_ID = "MOVIE_ID";

    public static Bundle buildBundle(int movieId) {
        final Bundle bundle = new Bundle(1);
        bundle.putInt(ARG_MOVIE_ID, movieId);
        return bundle;
    }

    @Inject
    Picasso picasso;

    private final MutableLiveData<Integer> desiredPosterWidth = new MutableLiveData<>();

    private ActivityDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        // Injection
        ((AppComponent.ComponentProvider) getApplication()).getComponent().inject(this);
        final DetailsViewModel viewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);

        binding.setViewModel(viewModel);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            final int movieId = bundle.getInt(ARG_MOVIE_ID);
            viewModel.setMovieId(movieId);
        }

        binding.setLifecycleOwner(this);

        viewModel.getImageUrlGenerator().observe(this, new Observer<ImageUrlGenerator>() {
            @Override
            public void onChanged(ImageUrlGenerator imageUrlGenerator) {

            }
        });

        new ImageUlrLiveData(desiredPosterWidth, viewModel.movie, viewModel.getImageUrlGenerator()).observe(this, new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                picasso.load(uri).into(binding.moviePoster);
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            final int widthInPx = binding.moviePoster.getWidth();
            final float density = getResources().getDisplayMetrics().density;
            final float widthInDp = widthInPx / density;
            desiredPosterWidth.setValue((int) widthInDp);
        }
    }

    private static class ImageUlrLiveData extends MediatorLiveData<Uri> {
        private Integer lastDesiredPosterWidth;
        private MovieListItem lastMovieListItem;
        private ImageUrlGenerator lastImageUrlGenerator;

        private ImageUlrLiveData(MutableLiveData<Integer> desiredPosterWidth, LiveData<MovieListItem> movieListItem, LiveData<ImageUrlGenerator> imageUrlGenerator) {
            addSource(desiredPosterWidth, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    lastDesiredPosterWidth = integer;
                    if (lastImageUrlGenerator != null) {
                        lastImageUrlGenerator.setPosterSizeByWidth(lastDesiredPosterWidth);
                        trigger();
                    }
                }
            });
            addSource(movieListItem, new Observer<MovieListItem>() {
                @Override
                public void onChanged(MovieListItem movieListItem) {
                    lastMovieListItem = movieListItem;
                    trigger();
                }
            });
            addSource(imageUrlGenerator, new Observer<ImageUrlGenerator>() {
                @Override
                public void onChanged(ImageUrlGenerator imageUrlGenerator) {
                    lastImageUrlGenerator = imageUrlGenerator;
                    trigger();
                }
            });
        }

        private void trigger() {
            if (lastMovieListItem == null || lastImageUrlGenerator == null) return;

            final String posterPath = lastMovieListItem.getPosterPath();
            final Uri imageUrl = lastImageUrlGenerator.getImageUrl(posterPath);
            setValue(imageUrl);
        }
    }
}
