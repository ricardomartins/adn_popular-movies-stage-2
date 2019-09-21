package pt.rikmartins.adn.popularmoviesstage2.ui.main;

import android.app.Application;
import android.util.Pair;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.PagedList;

import javax.inject.Inject;

import io.reactivex.Single;
import pt.rikmartins.adn.popularmoviesstage2.AppComponent;
import pt.rikmartins.adn.popularmoviesstage2.R;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;
import pt.rikmartins.adn.popularmoviesstage2.domain.usecase.movie.WatchFavoriteMovies;
import pt.rikmartins.adn.popularmoviesstage2.domain.usecase.movie.WatchPagedMoviesByHighestRated;
import pt.rikmartins.adn.popularmoviesstage2.domain.usecase.movie.WatchPagedMoviesByMostPopular;
import pt.rikmartins.adn.popularmoviesstage2.domain.usecase.poster.GetPosterUrlOfMovieWithWidth;
import pt.rikmartins.adn.popularmoviesstage2.domain.usecase.poster.WatchPosterUrlsAvailability;
import pt.rikmartins.adn.popularmoviesstage2.ui.BaseViewModel;

public class MainViewModel extends BaseViewModel {

    private static final int FAVORITE_MODE = 1001;
    private static final int TOP_RATED_MODE = 1011;
    private static final int MOST_POPULAR_MODE = 1012;

    final SparseArray<String> modes;

    @Inject
    WatchPosterUrlsAvailability watchPosterUrlsAvailability;

    @Inject
    GetPosterUrlOfMovieWithWidth getPosterUrlOfMovieWithWidth;

    @Inject
    WatchFavoriteMovies watchFavoriteMovies;

    @Inject
    WatchPagedMoviesByHighestRated watchPagedMoviesByHighestRated;

    @Inject
    WatchPagedMoviesByMostPopular watchPagedMoviesByMostPopular;

    private LiveData<Boolean> postersAvailable;

    private LiveData<PagedList<MovieInfo>> favoriteMovies;

    private LiveData<PagedList<MovieInfo>> highestRatedMovies;

    private LiveData<PagedList<MovieInfo>> mostPopularMovies;

    private final MutableLiveData<Integer> mode = new MutableLiveData<>(FAVORITE_MODE);

    final LiveData<PagedList<MovieInfo>> movies = Transformations.switchMap(mode, new Function<Integer, LiveData<PagedList<MovieInfo>>>() {
        @Override
        public LiveData<PagedList<MovieInfo>> apply(Integer input) {
            switch (input) {
                default:
                case FAVORITE_MODE:
                    return MainViewModel.this.watchFavoriteMovies();
                case TOP_RATED_MODE:
                    return MainViewModel.this.watchHighestRatedMovies();
                case MOST_POPULAR_MODE:
                    return MainViewModel.this.watchMostPopularMovies();
            }
        }
    });

    public MainViewModel(@NonNull Application application) {
        super(application);
        ((AppComponent.ComponentProvider) application).getComponent().inject(this);

        modes = new SparseArray<>(3);
        modes.put(FAVORITE_MODE, application.getString(R.string.main_show_favorite));
        modes.put(TOP_RATED_MODE, application.getString(R.string.main_order_by_rate));
        modes.put(MOST_POPULAR_MODE, application.getString(R.string.main_order_by_popular));
    }

    private LiveData<PagedList<MovieInfo>> watchFavoriteMovies() {
        if (favoriteMovies == null)
            favoriteMovies = watchCaseExposer.expose(watchFavoriteMovies);
        return favoriteMovies;
    }

    private LiveData<PagedList<MovieInfo>> watchHighestRatedMovies() {
        if (highestRatedMovies == null)
            highestRatedMovies = watchCaseExposer.expose(watchPagedMoviesByHighestRated);
        return highestRatedMovies;
    }

    private LiveData<PagedList<MovieInfo>> watchMostPopularMovies() {
        if (mostPopularMovies == null)
            mostPopularMovies = watchCaseExposer.expose(watchPagedMoviesByMostPopular);
        return mostPopularMovies;
    }

    LiveData<Boolean> watchPostersAvailable() {
        if (postersAvailable == null)
            postersAvailable = watchCaseExposer.expose(watchPosterUrlsAvailability);
        return postersAvailable;
    }

    Single<String> getPosterUrlOfMovieWithWidth(MovieInfo movieInfo, int width) {
        return ioUseCaseExecutor.executeAsync(getPosterUrlOfMovieWithWidth,
                new Pair<>(movieInfo, width)).toSingle();
    }

    void setMode(int mode) {
        switch (mode) {
            case FAVORITE_MODE:
            case TOP_RATED_MODE:
            case MOST_POPULAR_MODE:
                this.mode.setValue(mode);
        }
    }

    LiveData<Integer> getMode() {
        return mode;
    }
}
