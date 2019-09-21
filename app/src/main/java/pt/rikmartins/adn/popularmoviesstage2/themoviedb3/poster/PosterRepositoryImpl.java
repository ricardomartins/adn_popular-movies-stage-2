package pt.rikmartins.adn.popularmoviesstage2.themoviedb3.poster;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import pt.rikmartins.adn.popularmoviesstage2.domain.gateway.PosterRepository;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3.model.ImagesConfiguration;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3.service.TheMovieDb3Service;

@Singleton
public class PosterRepositoryImpl implements PosterRepository, PosterManager {
    private static final String TAG = PosterRepositoryImpl.class.getSimpleName();

    private static final String SP_CONFIGURATION_IMAGES_BASE_URL_KEY = "ConfigurationImagesBaseUrl";
    private static final String SP_CONFIGURATION_IMAGES_SECURE_BASE_URL_KEY = "ConfigurationImagesSecureBaseUrl";
    private static final String SP_CONFIGURATION_IMAGES_POSTER_SIZES_KEY = "ConfigurationImagesPosterSizes";

    private final Map<String, String> cache = new HashMap<>();

    private MutableLiveData<Boolean> posterUrlReadyStatus;

    private final SharedPreferences sharedPreferences;
    private final Gson gson;
    private final WorkManager workManager;
    private final TheMovieDb3Service theMovieDb3Service;

    private final MutableLiveData<Boolean> workStatus = new MutableLiveData<>(false);

    @Inject
    public PosterRepositoryImpl(SharedPreferences sharedPreferences, Gson gson,
                                WorkManager workManager, TheMovieDb3Service theMovieDb3Service) {
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
        this.workManager = workManager;
        this.theMovieDb3Service = theMovieDb3Service;

        updatePosterConfigurationEvery(5L, TimeUnit.DAYS); // update every 5 days
    }

    private void postImageUrlGenerator() {
        posterUrlReadyStatus.postValue(getImagesSecureBaseUrl() != null);
    }

    private String getImagesBaseUrl() {
        return sharedPreferences.getString(SP_CONFIGURATION_IMAGES_BASE_URL_KEY, null);
    }

    private String getImagesSecureBaseUrl() {
        return sharedPreferences.getString(SP_CONFIGURATION_IMAGES_SECURE_BASE_URL_KEY, null);
    }

    private List<String> getImagesPosterSizes() {
        String posterSizesString = sharedPreferences.getString(SP_CONFIGURATION_IMAGES_POSTER_SIZES_KEY, null);
        return posterSizesString != null ? gson.fromJson(posterSizesString, new TypeToken<List<String>>() {
        }.getType()) : null;
    }

    @Override
    public LiveData<Boolean> getPosterUrlReadyStatus() {
        if (posterUrlReadyStatus == null) {
            posterUrlReadyStatus = new MutableLiveData<>();

            if (getImagesSecureBaseUrl() != null) postImageUrlGenerator();

            sharedPreferences.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
                if (SP_CONFIGURATION_IMAGES_SECURE_BASE_URL_KEY.equals(key))
                    PosterRepositoryImpl.this.postImageUrlGenerator();
            });
        }
        return posterUrlReadyStatus;
    }

    @Override
    public String getPosterUrlOfMovieWithWidth(String posterPath, int width) {
        return getPosterUrlOfMovieWithSize(posterPath, "w" + width);
    }

    @Override
    public String getPosterUrlOfMovieWithHeight(String posterPath, int height) {
        return getPosterUrlOfMovieWithSize(posterPath, "h" + height);
    }

    private String getPosterUrlOfMovieWithSize(String posterPath, String sizeString) {
        String uriBuilder = cache.get(sizeString);
        if (uriBuilder == null) {
            final String imagesBaseUrl = getImagesBaseUrl();
            final String imagesSecureBaseUrl = getImagesSecureBaseUrl();
            final List<String> imagesPosterSizes = getImagesPosterSizes();

            if (minimalInfoIsAvailable(imagesBaseUrl, imagesSecureBaseUrl, imagesPosterSizes)) {
                uriBuilder = buildUriBuilder(sizeString, imagesBaseUrl, imagesSecureBaseUrl, imagesPosterSizes).toString();

                cache.put(sizeString, uriBuilder);
            }
        }

        return uriBuilder != null ? buildUri(uriBuilder, posterPath) : null;
    }

    private static String buildUri(String uriBuilder, String posterPath) {
        return Uri.parse(uriBuilder).buildUpon().appendEncodedPath(posterPath).toString();
    }

    private static boolean minimalInfoIsAvailable(String imagesBaseUrl, String imagesSecureBaseUrl, List<String> imagesPosterSizes) {
        return imagesPosterSizes != null && (imagesBaseUrl != null || imagesSecureBaseUrl != null);
    }

    private static Uri.Builder buildUriBuilder(String sizeString, String imagesBaseUrl, String imagesSecureBaseUrl, List<String> imagesPosterSizes) {
        final String imagesPreferedUrl = imagesSecureBaseUrl != null ? imagesSecureBaseUrl : imagesBaseUrl;

        final String calculatedPosterSize = calculatePosterSize(sizeString, imagesPosterSizes);

        return Uri.parse(imagesPreferedUrl).buildUpon().appendPath(calculatedPosterSize);
    }

    private static String calculatePosterSize(final String desiredPosterSize, final List<String> imagesPosterSizes) {
        if (desiredPosterSize == null) {
            Log.w(TAG, "No poster size selected, building URL with worst quality"); // TODO: Create a const with the message
            return imagesPosterSizes.get(0);
        }

        String prefix = desiredPosterSize.substring(0, 1);
        int size = Integer.parseInt(desiredPosterSize.substring(1));

        String posterSize = null;

        boolean prefixFound = false;
        boolean adequateSizeFound = false;
        for (String ips : imagesPosterSizes) {
            posterSize = ips;
            if (ips.startsWith(prefix)) {
                prefixFound = true;
                if (size <= Integer.parseInt(ips.substring(1))) {
                    adequateSizeFound = true;
                    break;
                }
            }
        }

        if (!prefixFound)
            Log.w(TAG, "No image size with width as dimension was found, defaulting to \"" + posterSize + "\"."); // TODO: Create a const with the message
        else if (!adequateSizeFound)
            Log.i(TAG, "No image size with appropriate size was found, defaulting to \"" + posterSize + "\"."); // TODO: Create a const with the message

        return posterSize;
    }

    private void updatePosterConfigurationEvery(Long repeatInterval, TimeUnit repeatIntervalTimeUnit) {
        if ((getImagesBaseUrl() == null && getImagesSecureBaseUrl() == null) || getImagesPosterSizes() == null) {
            Single.create((SingleOnSubscribe<ImagesConfiguration>) emitter -> {
                final ImagesConfiguration imagesConfiguration = downloadPosterConfiguration();
                emitter.onSuccess(imagesConfiguration);
            }).doOnSuccess(this::setPosterConfiguration).subscribeOn(Schedulers.io()).subscribe();

            final PeriodicWorkRequest periodicRequest =
                    new PeriodicWorkRequest.Builder(PosterConfigurationUpdateWorker.class, repeatInterval, repeatIntervalTimeUnit)
                            .build();

            workManager.enqueue(periodicRequest);
        }
    }

    @Override
    public ImagesConfiguration downloadPosterConfiguration() {
        return theMovieDb3Service.getImagesConfiguration();
    }

    @Override
    public void setPosterConfiguration(ImagesConfiguration imagesConfiguration) {
        sharedPreferences.edit()
                .putString(SP_CONFIGURATION_IMAGES_BASE_URL_KEY, imagesConfiguration.getBaseUrl())
                .putString(SP_CONFIGURATION_IMAGES_SECURE_BASE_URL_KEY, imagesConfiguration.getSecureBaseUrl())
                .putString(SP_CONFIGURATION_IMAGES_POSTER_SIZES_KEY, gson.toJson(imagesConfiguration.getPosterSizes()))
                .apply();
    }

    @Override
    public LiveData<Boolean> getWorkStatus() {
        return workStatus;
    }
}
