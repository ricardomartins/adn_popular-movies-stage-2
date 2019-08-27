package pt.rikmartins.adn.popularmoviesstage1.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.PositionalDataSource;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Reusable;
import pt.rikmartins.adn.popularmoviesstage1.api.ApiModule;
import pt.rikmartins.adn.popularmoviesstage1.api.TheMovieDb3Service;
import pt.rikmartins.adn.popularmoviesstage1.api.model.Configuration;
import pt.rikmartins.adn.popularmoviesstage1.api.model.ImagesConfiguration;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MovieListItem;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MoviePage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

@Reusable
public class Repository {
    private static final String TAG = Repository.class.getSimpleName();

    public final static int POPULAR_MODE = 1;
    public final static int TOP_RATED_MODE = 2;

    // Defines how frequently the configuration will be updated
    private final static long CONFIGURATION_UPDATE_DURATION = 5 * 86400000; // 5 days of milliseconds

    private MutableLiveData<Integer> mode = new MutableLiveData<>();

    private final TheMovieDb3Service theMovieDb3Service;
    private final int startPage;
    private final SharedPreferencesUtils sharedPreferencesUtils;

    private final LiveData<PagedList<MovieListItem>> movieListLiveData;
    private final MutableLiveData<Boolean> workStatusLiveData;

    private PositionalDataSource<MovieListItem> movieListItemPositionalDataSource = null;

    @Inject
    public Repository(TheMovieDb3Service theMovieDb3Service,
                      @Named(ApiModule.THE_MOVIE_DB_API_START_PAGE_NAME) int startPage,
                      SharedPreferencesUtils sharedPreferencesUtils) {
        this.theMovieDb3Service = theMovieDb3Service;
        this.startPage = startPage;
        this.sharedPreferencesUtils = sharedPreferencesUtils;

        movieListLiveData = new LivePagedListBuilder<>(new MovieListItemDataSourceFactory(), 20).build();
        workStatusLiveData = new MutableLiveData<>();

        mode.setValue(POPULAR_MODE);

        if (isConfigurationUpdateRequired()) updateConfiguration();
    }

    private class MovieListItemDataSourceFactory extends DataSource.Factory<Integer, MovieListItem> {
        @NonNull
        @Override
        public DataSource<Integer, MovieListItem> create() {
            movieListItemPositionalDataSource = new MovieListItemDataSource();

            return movieListItemPositionalDataSource;
        }
    }

    private class MovieListItemDataSource extends PositionalDataSource<MovieListItem> {
        @Override
        public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<MovieListItem> callback) {
            try {
                final MoviePage moviePage = request((params.requestedStartPosition / params.pageSize) + startPage);

                final List<MovieListItem> results;
                if (moviePage != null && (results = moviePage.getResults()) != null) {
                    final Integer totalResults = moviePage.getTotalResults();
                    final int position = computeInitialLoadPosition(params, totalResults);
                    callback.onResult(results, position, totalResults);
                    return;
                }
            } catch (IOException e) {
                Log.w(TAG, e.getMessage());
            }
            // TODO: Signal and deal with failure
        }

        @Override
        public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<MovieListItem> callback) {
            try {
                final MoviePage moviePage = request((params.startPosition / params.loadSize) + startPage);

                final List<MovieListItem> results;
                if (moviePage != null && (results = moviePage.getResults()) != null){
                    callback.onResult(results);
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // TODO: Signal and deal with failure
        }

        private MoviePage request(Integer page) throws IOException {
            workStatusLiveData.postValue(true);
            Log.i(TAG, "Retrieving page " + page);
            final Call<MoviePage> moviesPageCall = getModeUnboxed() == TOP_RATED_MODE
                    ? theMovieDb3Service.getTopRatedMovies(page)
                    : theMovieDb3Service.getPopularMovies(page);

            final Response<MoviePage> response = moviesPageCall.execute();
            workStatusLiveData.postValue(false);
            if (response.isSuccessful()) return response.body();

            return null;
        }
    }

    private int getModeUnboxed() {
        final Integer value = mode.getValue();
        return value != null ? value : POPULAR_MODE;
    }

    public void switchMode(int mode) {
        final int oldMode = getModeUnboxed();
        if (oldMode != mode && (mode == POPULAR_MODE || mode == TOP_RATED_MODE)) {
            this.mode.setValue(mode);
            movieListItemPositionalDataSource.invalidate();
        }
    }

    public LiveData<Integer> getMode() {
        return mode;
    }

    public void updateConfiguration() {
        theMovieDb3Service.getConfiguration().enqueue(new Callback<Configuration>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Configuration> call, Response<Configuration> response) {
                if (response.isSuccessful()) {
                    final Configuration configuration = response.body();

                    if (configuration != null) {
                        final ImagesConfiguration imagesConfiguration = configuration.getImages();

                        if (imagesConfiguration != null) {
                            // Not checking each individual value
                            sharedPreferencesUtils.update(imagesConfiguration.getBaseUrl(),
                                    imagesConfiguration.getSecureBaseUrl(),
                                    imagesConfiguration.getPosterSizes());
                        }
                    }
                    // TODO: Signal and deal with failure
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Configuration> call, Throwable t) {
                // TODO: Signal and deal with failure
                Log.w(TAG, t.getMessage());
            }
        });
    }

    private boolean isConfigurationUpdateRequired() {

        final long configurationUpdateDate = sharedPreferencesUtils.getUpdateDate();
        if (configurationUpdateDate == SharedPreferencesUtils.CONFIGURATION_UPDATE_DATE_MISSING)
            return true;
        else {
            final long currentTimeMillis = System.currentTimeMillis();
            // How long has it been since the last update to configuration
            final long elapsedTime = currentTimeMillis - configurationUpdateDate;
            if (elapsedTime >= CONFIGURATION_UPDATE_DURATION) {
                // It's over the threshold, so ask for the update
                return true;
            }
        }

        final String configurationImagesBaseUrl = sharedPreferencesUtils.getImagesBaseUrl();
        final String configurationImagesSecureBaseUrl = sharedPreferencesUtils.getImagesSecureBaseUrl();
        final List<String> configurationImagesPosterSizes = sharedPreferencesUtils.getImagesPosterSizes();

        // Not sure about this conditions, but I do think they make sense
        return (configurationImagesBaseUrl == null && configurationImagesSecureBaseUrl == null) ||
                configurationImagesPosterSizes == null;
    }

    public LiveData<PagedList<MovieListItem>> getMovieListLiveData() {
        return movieListLiveData;
    }

    public LiveData<Boolean> getWorkStatusLiveData() {
        return workStatusLiveData;
    }
}
