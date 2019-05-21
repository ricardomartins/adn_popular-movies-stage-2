package pt.rikmartins.adn.popularmoviesstage1.api;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import pt.rikmartins.adn.popularmoviesstage1.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Injection {

    private String theMovieDbApiV3Url;
    private Gson gson;
    private Retrofit theMovieDatabaseApi3Retrofit;
    private TheMovieDatabaseApi3 theMovieDatabaseApi3;
    private TheMovieDb3Service theMovieDb3Service;

    @NotNull
    public String provideTheMovieDbApiUrl() {
        return "https://api.themoviedb.org/";
    }

    @NotNull
    public String provideTheMovieDbApiV3Url() {
        if (theMovieDbApiV3Url == null) theMovieDbApiV3Url = provideTheMovieDbApiUrl() + "3/";
        return theMovieDbApiV3Url;
    }

    @NotNull
    public String provideTheMovieDbApiV3Key() {
        return BuildConfig.TheMovieDbApiKey;
    }

    @NotNull
    public Gson provideGson() {
        if (gson == null) gson = new Gson();
        return gson;
    }

    @NotNull
    public Retrofit provideTheMovieDatabaseApi3Retrofit(@NotNull String theMovieDbApiV3Url, @NotNull Gson gson) {
        if (theMovieDatabaseApi3Retrofit == null)
            theMovieDatabaseApi3Retrofit = new Retrofit.Builder()
                    .baseUrl(theMovieDbApiV3Url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        return theMovieDatabaseApi3Retrofit;
    }

    @NotNull
    public TheMovieDatabaseApi3 provideTheMovieDatabaseApi3(@NotNull Retrofit retrofit) {
        if (theMovieDatabaseApi3 == null)
            theMovieDatabaseApi3 = retrofit.create(TheMovieDatabaseApi3.class);
        return theMovieDatabaseApi3;
    }

    @NotNull
    public TheMovieDb3Service provideTheMovieDb3Service(@NotNull TheMovieDatabaseApi3 theMovieDatabaseApi3, @NotNull String theMovieDbApiV3Key) {
        if (theMovieDb3Service == null)
            theMovieDb3Service = new TheMovieDb3Service(theMovieDatabaseApi3, theMovieDbApiV3Key);
        return theMovieDb3Service;
    }
}
