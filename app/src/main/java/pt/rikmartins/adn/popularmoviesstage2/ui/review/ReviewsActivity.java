package pt.rikmartins.adn.popularmoviesstage2.ui.review;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import pt.rikmartins.adn.popularmoviesstage2.AppComponent;
import pt.rikmartins.adn.popularmoviesstage2.R;
import pt.rikmartins.adn.popularmoviesstage2.databinding.ReviewsActivityBinding;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.Review;
import pt.rikmartins.adn.popularmoviesstage2.ui.model.MovieInfoImpl;

public class ReviewsActivity extends AppCompatActivity {

    public static final String ARG_MOVIE = "MOVIE";

    public static Bundle buildBundle(MovieInfo movieInfo) {
        final Bundle bundle = new Bundle(1);
        bundle.putParcelable(ARG_MOVIE, new MovieInfoImpl(movieInfo));
        return bundle;
    }

    @Inject
    ReviewsAdapter reviewsAdapter;

    private ReviewsActivityBinding binding;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.reviews_activity);

        // Injection
        ((AppComponent.ComponentProvider) getApplication()).getComponent().inject(this);
        final ReviewsViewModel viewModel = ViewModelProviders.of(this).get(ReviewsViewModel.class);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) viewModel.setMovie((MovieInfo) bundle.getParcelable(ARG_MOVIE));

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.reviewList.setAdapter(reviewsAdapter);
        binding.reviewList.setLayoutManager(layoutManager);
        binding.reviewList.addItemDecoration(dividerItemDecoration);
        viewModel.getMovieReviews().observe(this, new Observer<PagedList<Review>>() {
            @Override
            public void onChanged(PagedList<Review> reviews) {
                reviewsAdapter.submitList(reviews);
            }
        });


        binding.setViewModel(viewModel);

        binding.setLifecycleOwner(this);
    }

    private void showList() {
        binding.reviewList.setVisibility(View.VISIBLE);
        binding.emptyPlaceholder.setVisibility(View.INVISIBLE);
    }

    private void showPlaceholder() {
        binding.emptyPlaceholder.setVisibility(View.VISIBLE);
        binding.reviewList.setVisibility(View.INVISIBLE);
    }
}
