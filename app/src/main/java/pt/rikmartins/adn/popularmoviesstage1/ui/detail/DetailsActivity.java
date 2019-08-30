package pt.rikmartins.adn.popularmoviesstage1.ui.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import pt.rikmartins.adn.popularmoviesstage1.AppComponent;
import pt.rikmartins.adn.popularmoviesstage1.R;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MovieListItem;
import pt.rikmartins.adn.popularmoviesstage1.data.SharedPreferencesUtils;
import pt.rikmartins.adn.popularmoviesstage1.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity {

    public static final String ARG_MOVIE_ID = "MOVIE_ID";

    public static Bundle buildBundle(int movieId) {
        final Bundle bundle = new Bundle(1);
        bundle.putInt(ARG_MOVIE_ID, movieId);
        return bundle;
    }

    @Inject
    SharedPreferencesUtils sharedPreferencesUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_details);

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

        viewModel.movie.observe(this, new Observer<MovieListItem>() {
            @Override
            public void onChanged(MovieListItem movieListItem) {
                Picasso.get().load()
            }
        });

        Picasso.get()
                .load(imagesUrl.buildUpon().appendEncodedPath(movieListItem.getPosterPath()).build())
                .into(holder.posterImage);

    }
}
