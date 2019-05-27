package pt.rikmartins.adn.popularmoviesstage1.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
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

    private final static int NO_MODE = -1;
    public final static int POPULAR_MODE = 1;
    public final static int TOP_RATED_MODE = 2;

    private final static int NO_TOTAL_PAGES = -1;

    // Defines how frequently the configuration will be updated
    private final static long CONFIGURATION_UPDATE_DURATION = 5 * 86400000; // 5 days of milliseconds

    private int mode = NO_MODE;

    private final TheMovieDb3Service theMovieDb3Service;
    private final int startPage;
    private final SharedPreferencesUtils sharedPreferencesUtils;

    private List<MovieListItem> movieListItems;

    private int nextPage;
    private int totalPages;

    private final MutableLiveData<List<MovieListItem>> movieListLiveData;
    private final MutableLiveData<Boolean> workStatusLiveData;

    @Inject
    public Repository(TheMovieDb3Service theMovieDb3Service,
                      @Named(ApiModule.THE_MOVIE_DB_API_START_PAGE_NAME) int startPage,
                      SharedPreferencesUtils sharedPreferencesUtils) {
        this.theMovieDb3Service = theMovieDb3Service;
        this.startPage = startPage;
        this.sharedPreferencesUtils = sharedPreferencesUtils;

        movieListLiveData = new MutableLiveData<>();
        workStatusLiveData = new MutableLiveData<>();

        switchMode(POPULAR_MODE);

        if (isConfigurationUpdateRequired()) updateConfiguration();
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

            workStatusLiveData.setValue(true);
            moviesPageCall.enqueue(new Callback<MoviePage>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<MoviePage> call, Response<MoviePage> response) {
                    onFinished();
                    if (response.isSuccessful()) {
                        MoviePage moviePage = response.body();
                        if (moviePage != null) {
                            List<MovieListItem> results = moviePage.getResults();
                            totalPages = moviePage.getTotalPages();
                            nextPage = moviePage.getPage() + 1;
                            if (results != null && !results.isEmpty()) {
                                movieListItems.addAll(results);
                                movieListLiveData.setValue(movieListItems);
                            }
                        }
                    }
                    // TODO: Signal and deal with failure
                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<MoviePage> call, Throwable t) {
                    onFinished();
                    // TODO: Signal and deal with failure
                    Log.w(TAG, t.getMessage());
                }

                private void onFinished() {
                    workStatusLiveData.setValue(false);
                }
            });
        }
    }

    public void updateConfiguration() {
        theMovieDb3Service.getConfiguration().enqueue(new Callback<Configuration>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Configuration> call, Response<Configuration> response) {
                if (response.isSuccessful()) {
                    Configuration configuration = response.body();

                    if (configuration != null) {
                        ImagesConfiguration imagesConfiguration = configuration.getImages();

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

        long configurationUpdateDate = sharedPreferencesUtils.getUpdateDate();
        if (configurationUpdateDate == SharedPreferencesUtils.CONFIGURATION_UPDATE_DATE_MISSING) return true;
        else {
            long currentTimeMillis = System.currentTimeMillis();
            // How long has it been since the last update to configuration
            long elapsedTime = currentTimeMillis - configurationUpdateDate;
            if (elapsedTime >= CONFIGURATION_UPDATE_DURATION) {
                // It's over the threshold, so ask for the update
                return true;
            }
        }

        String configurationImagesBaseUrl = sharedPreferencesUtils.getImagesBaseUrl();
        String configurationImagesSecureBaseUrl = sharedPreferencesUtils.getImagesSecureBaseUrl();
        List<String> configurationImagesPosterSizes = sharedPreferencesUtils.getImagesPosterSizes();

        // Not sure about this conditions, but I do think they make sense
        return (configurationImagesBaseUrl == null && configurationImagesSecureBaseUrl == null) ||
                configurationImagesPosterSizes == null;
    }

    public LiveData<List<MovieListItem>> getMovieListLiveData() {
        return movieListLiveData;
    }

    public LiveData<Boolean> getWorkStatusLiveData() {
        return workStatusLiveData;
    }
}
