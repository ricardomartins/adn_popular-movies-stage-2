package pt.rikmartins.adn.popularmoviesstage1.data;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import javax.inject.Inject;

public class SharedPreferencesUtils {
    public static final long CONFIGURATION_UPDATE_DATE_MISSING = -100;

    public static final String SP_CONFIGURATION_IMAGES_BASE_URL_KEY = "ConfigurationImagesBaseUrl";
    public static final String SP_CONFIGURATION_IMAGES_SECURE_BASE_URL_KEY = "ConfigurationImagesSecureBaseUrl";
    public static final String SP_CONFIGURATION_IMAGES_POSTER_SIZES_KEY = "ConfigurationImagesPosterSizes";
    public static final String SP_CONFIGURATION_UPDATE_DATE_KEY = "ConfigurationUpdateDate";

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    @Inject
    public SharedPreferencesUtils(SharedPreferences sharedPreferences, Gson gson) {
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public String getImagesBaseUrl() {
        return sharedPreferences.getString(SP_CONFIGURATION_IMAGES_BASE_URL_KEY, null);
    }

    public String getImagesSecureBaseUrl() {
        return sharedPreferences.getString(SP_CONFIGURATION_IMAGES_SECURE_BASE_URL_KEY, null);
    }

    public String getImagesPreferredBaseUrl() {
        return sharedPreferences.getString(SP_CONFIGURATION_IMAGES_SECURE_BASE_URL_KEY,
                sharedPreferences.getString(SP_CONFIGURATION_IMAGES_BASE_URL_KEY,
                        null));
    }

    public List<String> getImagesPosterSizes() {
        String posterSizesString = sharedPreferences.getString(SP_CONFIGURATION_IMAGES_POSTER_SIZES_KEY, null);
        return posterSizesString != null ? gson.<List<String>>fromJson(posterSizesString, new TypeToken<List<String>>() {}.getType()) : null;
    }

    /**
     * @return The date of the last update or CONFIGURATION_UPDATE_DATE_MISSING if there's no date available
     */
    public long getUpdateDate() {
        return sharedPreferences.getLong(SP_CONFIGURATION_UPDATE_DATE_KEY, CONFIGURATION_UPDATE_DATE_MISSING);
    }

    public void update(String baseUrl, String secureBaseUrl, List<String> posterSizes) {
        sharedPreferences.edit()
                .putString(SharedPreferencesUtils.SP_CONFIGURATION_IMAGES_BASE_URL_KEY, baseUrl)
                .putString(SharedPreferencesUtils.SP_CONFIGURATION_IMAGES_SECURE_BASE_URL_KEY, secureBaseUrl)
                .putString(SharedPreferencesUtils.SP_CONFIGURATION_IMAGES_POSTER_SIZES_KEY, gson.toJson(posterSizes))
                .putLong(SharedPreferencesUtils.SP_CONFIGURATION_UPDATE_DATE_KEY, System.currentTimeMillis())
                .apply();

    }
}
