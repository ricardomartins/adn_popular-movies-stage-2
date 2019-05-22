package pt.rikmartins.adn.popularmoviesstage1.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import pt.rikmartins.adn.popularmoviesstage1.api.TheMovieDb3Service;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MovieListItem;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MoviePage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class Repository {
    private static final String TAG = Repository.class.getSimpleName();

    private final static int NO_MODE = -1;
    public final static int POPULAR_MODE = 1;
    public final static int TOP_RATED_MODE = 2;

    private final static int NO_TOTAL_PAGES = -1;

    private int mode = NO_MODE;

    private final TheMovieDb3Service theMovieDb3Service;
    private final int startPage;

    private List<MovieListItem> movieListItems;

    private int nextPage;
    private int totalPages;

    private final MutableLiveData<List<MovieListItem>> movieListLiveData;

    public Repository(TheMovieDb3Service theMovieDb3Service, int startPage) {
        this.theMovieDb3Service = theMovieDb3Service;
        this.startPage = startPage;
        movieListLiveData = new MutableLiveData<>();

        switchMode(POPULAR_MODE);
    }

    public void switchMode(int mode) {
        if (this.mode != mode) {
            this.mode = mode;
            movieListItems = new ArrayList<>();
            nextPage = startPage;
            totalPages = NO_TOTAL_PAGES;
            movieListLiveData.setValue(movieListItems);
        }
    }

    public void requestMoreData() {
        if (totalPages == NO_TOTAL_PAGES || nextPage <= totalPages) {
            Call<MoviePage> moviesPageCall;
            if (mode == TOP_RATED_MODE) {
                moviesPageCall = theMovieDb3Service.getTopRatedMovies(nextPage);
            } else {
                moviesPageCall = theMovieDb3Service.getPopularMovies(nextPage); // This is default if weird values get into `mode`
            }
            moviesPageCall.enqueue(new Callback<MoviePage>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<MoviePage> call, Response<MoviePage> response) {
                    if (response.isSuccessful()) {
                        MoviePage moviePage = response.body();
                        if (moviePage != null) {
                            List<MovieListItem> results = moviePage.getResults();
                            if (results != null && !results.isEmpty()) {
                                movieListItems.addAll(results);
                                movieListLiveData.setValue(movieListItems);
                                totalPages = moviePage.getTotalPages();
                                nextPage = moviePage.getPage() + 1;
                            }
                        }
                    }
                    // TODO: Signal and deal with failure
                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<MoviePage> call, Throwable t) {
                    // TODO: Signal and deal with failure
                    Log.w(TAG, t.getMessage());
                }
            });
        }
    }

    public LiveData<List<MovieListItem>> getMovieListLiveData() {
        return movieListLiveData;
    }
}