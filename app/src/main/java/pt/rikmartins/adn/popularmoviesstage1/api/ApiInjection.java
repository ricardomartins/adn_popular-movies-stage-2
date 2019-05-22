package pt.rikmartins.adn.popularmoviesstage1.api;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import pt.rikmartins.adn.popularmoviesstage1.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiInjection {

    @NotNull
    public String provideTheMovieDbApiV3Url() {
        return "https://api.themoviedb.org/3/";
    }

    @NotNull
    public String provideTheMovieDbApiV3Key() {
        return BuildConfig.TheMovieDbApiKey;
    }

    // Singleton
    @NotNull
    public Gson provideGson() {
        return new Gson();
    }

    // Singleton
    @NotNull
    public Retrofit provideTheMovieDatabaseApi3Retrofit(@NotNull String theMovieDbApiV3Url, @NotNull Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(theMovieDbApiV3Url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    // Singleton
    @NotNull
    public TheMovieDatabaseApi3 provideTheMovieDatabaseApi3(@NotNull Retrofit retrofit) {
        return retrofit.create(TheMovieDatabaseApi3.class);
    }

    // Singleton
    @NotNull
    public TheMovieDb3Service provideTheMovieDb3Service(@NotNull TheMovieDatabaseApi3 theMovieDatabaseApi3, @NotNull String theMovieDbApiV3Key) {
        return new TheMovieDb3Service(theMovieDatabaseApi3, theMovieDbApiV3Key);
    }

    public int provideApiStartPage() {
        return 1; // From the Api documentation
    }
}
