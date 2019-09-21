package pt.rikmartins.adn.popularmoviesstage2.ui.detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import pt.rikmartins.adn.popularmoviesstage2.AppComponent;
import pt.rikmartins.adn.popularmoviesstage2.R;
import pt.rikmartins.adn.popularmoviesstage2.databinding.DetailsActivityBinding;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.Video;
import pt.rikmartins.adn.popularmoviesstage2.ui.model.MovieInfoImpl;
import pt.rikmartins.adn.popularmoviesstage2.ui.review.ReviewsActivity;

public class DetailsActivity extends AppCompatActivity implements
        VideosAdapter.VideoAdapterOnClickHandler, VideosAdapter.VideoAdapterImageUrlProvider {

    public static final String ARG_MOVIE = "MOVIE";

    public static Bundle buildBundle(MovieInfo movieInfo) {
        final Bundle bundle = new Bundle(1);
        bundle.putParcelable(ARG_MOVIE, new MovieInfoImpl(movieInfo));
        return bundle;
    }

    @Inject
    Picasso picasso;

    @Inject
    VideosAdapter videosAdapter;

    private int desiredPosterWidth = 0;

    private DetailsActivityBinding binding;

    private DetailsViewModel viewModel;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.details_activity);

        // Injection
        ((AppComponent.ComponentProvider) getApplication()).getComponent().inject(this);
        viewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) viewModel.setMovie((MovieInfo) bundle.getParcelable(ARG_MOVIE));

        binding.setViewModel(viewModel);
        binding.setActions(new Actions());

        binding.favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) viewModel.setMovieAsFavorite();
                else viewModel.setMovieAsNotFavorite();
            }
        });

        videosAdapter.setVideoAdapterImageUrlProvider(this);
        videosAdapter.setVideoAdapterOnClickHandler(this);
        viewModel.getVideoListOfMovie()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        binding.videoGroup.setVisibility(View.GONE);
                        videosAdapter.setData(null);
                    }
                })
                .subscribe(new Consumer<List<Video>>() {
                    @Override
                    public void accept(List<Video> videos) throws Exception {
                        if (!videos.isEmpty()) binding.videoGroup.setVisibility(View.VISIBLE);
                        videosAdapter.setData(videos);
                    }
                });
        binding.videos.setAdapter(videosAdapter);

        binding.videos.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        binding.setLifecycleOwner(this);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus && desiredPosterWidth == 0) {
            final int widthInPx = binding.moviePoster.getWidth();
            final float density = getResources().getDisplayMetrics().density;
            final float widthInDp = widthInPx / density;
            desiredPosterWidth = (int) widthInDp;

            viewModel.watchPostersAvailable().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (aBoolean)
                        viewModel.getPosterUrlOfMovieWithWidth(desiredPosterWidth)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String url) throws Exception {
                                        picasso.load(url).into(binding.moviePoster);
                                    }
                                });
                }
            });
        }
    }

    @Override
    public void onClick(Video video) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.getUrlOfVideo(video)));
        startActivity(intent);
    }

    @Override
    public String getImageUrlOfVideo(Video video) {
        return viewModel.getPreviewImageUrlOfVideo(video);
    }

    public class Actions {
        public void seeReviews(View ignored) {
            final Intent intent = new Intent(DetailsActivity.this, ReviewsActivity.class);

            final Bundle bundle = ReviewsActivity.buildBundle(viewModel.getMovie());
            intent.putExtras(bundle);

            startActivity(intent);
        }
    }
}
