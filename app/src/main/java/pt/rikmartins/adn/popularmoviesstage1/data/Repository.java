package pt.rikmartins.adn.popularmoviesstage1.data;

import android.content.SharedPreferences;
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

import dagger.Reusable;
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

    private final MutableLiveData<Integer> mode = new MutableLiveData<>();

    private final TheMovieDb3Service theMovieDb3Service;
    private final SharedPreferencesUtils sharedPreferencesUtils;

    private final LiveData<PagedList<MovieListItem>> movieListLiveData = new LivePagedListBuilder<>(new MovieListItemDataSourceFactory(), 20).build();
    private final MutableLiveData<Boolean> workStatusLiveData = new MutableLiveData<>();

    private PositionalDataSource<MovieListItem> movieListItemPositionalDataSource = null;

    private final MutableLiveData<ImageUrlGenerator> imageUrlGenerator = new MutableLiveData<>();

    @Inject
    public Repository(TheMovieDb3Service theMovieDb3Service,
                      final SharedPreferencesUtils sharedPreferencesUtils) {
        this.theMovieDb3Service = theMovieDb3Service;
        this.sharedPreferencesUtils = sharedPreferencesUtils;

        mode.setValue(POPULAR_MODE);

        if (isConfigurationUpdateRequired()) updateConfiguration();

        if (sharedPreferencesUtils.getImagesSecureBaseUrl() != null) postImageUrlGenerator();

        sharedPreferencesUtils.getSharedPreferences().registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (SharedPreferencesUtils.SP_CONFIGURATION_IMAGES_SECURE_BASE_URL_KEY.equals(key)) {
                    postImageUrlGenerator();
                }
            }
        });
    }

    private void postImageUrlGenerator() {
        final String imagesPreferredBaseUrl = sharedPreferencesUtils.getImagesPreferredBaseUrl();
        final List<String> imagesPosterSizes = sharedPreferencesUtils.getImagesPosterSizes();
        final ImageUrlGenerator imageUrlGenerator = new ImageUrlGenerator(imagesPreferredBaseUrl, imagesPosterSizes);
        Repository.this.imageUrlGenerator.postValue(imageUrlGenerator);
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
                final MoviePage moviePage = request((params.requestedStartPosition / params.pageSize) + theMovieDb3Service.startPage);

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
            // What do you mean "unhandled error"!!?
        }

        @Override
        public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<MovieListItem> callback) {
            try {
                final MoviePage moviePage = request((params.startPosition / params.loadSize) + theMovieDb3Service.startPage);

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
            try {
                workStatusLiveData.postValue(true);
                Log.i(TAG, "Retrieving page " + page);
                final Call<MoviePage> moviesPageCall = getModeUnboxed() == TOP_RATED_MODE
                        ? theMovieDb3Service.getTopRatedMovies(page)
                        : theMovieDb3Service.getPopularMovies(page);

                final Response<MoviePage> response = moviesPageCall.execute();
                if (response.isSuccessful()) return response.body();

                return null;
            } finally {
                workStatusLiveData.postValue(false);
            }
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
                }
                // TODO: Signal and deal with failure
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

    public LiveData<MovieListItem> getMovie(int movieId) {
        final MutableLiveData<MovieListItem> liveResult = new MutableLiveData<>();

        theMovieDb3Service.getMovie(movieId).enqueue(new Callback<MovieListItem>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<MovieListItem> call, Response<MovieListItem> response) {
                if (response.isSuccessful()) {
                    final MovieListItem movie = response.body();
                    if (movie != null) liveResult.postValue(movie);
                }
                // TODO: Signal and deal with failure
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<MovieListItem> call, Throwable t) {
                // TODO: Signal and deal with failure
                Log.w(TAG, t.getMessage());
            }
        });

        return liveResult;
    }

    public LiveData<ImageUrlGenerator> getImageUrlGenerator() {
        return imageUrlGenerator;
    }
}
