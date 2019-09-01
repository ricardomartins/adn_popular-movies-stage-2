package pt.rikmartins.adn.popularmoviesstage1.api;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import pt.rikmartins.adn.popularmoviesstage1.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public abstract class ApiModule {

    public static final String THE_MOVIE_DB_API_URL_NAME = "TheMovieDbApiV3Url";
    public static final String THE_MOVIE_DB_API_KEY_NAME = "TheMovieDbApiV3Key";

    @Provides
    @Named(THE_MOVIE_DB_API_URL_NAME)
    public static String provideTheMovieDbApiV3Url() {
        return "https://api.themoviedb.org/3/";
    }

    @Provides
    @Named(THE_MOVIE_DB_API_KEY_NAME)
    public static String provideTheMovieDbApiV3Key() {
        return BuildConfig.TheMovieDbApiKey;
    }

    @Provides
    @Reusable
    public static Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Reusable
    public static Retrofit provideTheMovieDatabaseApi3Retrofit(@Named(THE_MOVIE_DB_API_URL_NAME) String theMovieDbApiV3Url, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(theMovieDbApiV3Url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Reusable
    public static TheMovieDatabaseApi3 provideTheMovieDatabaseApi3(Retrofit retrofit) {
        return retrofit.create(TheMovieDatabaseApi3.class);
    }

    @Provides
    @Reusable
    public static Picasso providePicasso() {
        return Picasso.get();
    }
}
