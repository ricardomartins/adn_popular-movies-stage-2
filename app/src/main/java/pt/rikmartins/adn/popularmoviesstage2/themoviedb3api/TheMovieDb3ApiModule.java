package pt.rikmartins.adn.popularmoviesstage2.themoviedb3api;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import pt.rikmartins.adn.popularmoviesstage2.BuildConfig;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3.service.TheMovieDb3Service;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public abstract class TheMovieDb3ApiModule {

    public static final String THE_MOVIE_DB_3_API = "TheMovieDbApiV3Api";

    @Provides
    @Named(THE_MOVIE_DB_3_API)
    public static String provideTheMovieDbApiV3Url() {
        return "https://api.themoviedb.org/3/";
    }

    @Provides
    @Reusable
    public static Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Reusable
    @Named(THE_MOVIE_DB_3_API)
    public static Retrofit provideTheMovieDatabaseApi3Retrofit(@Named(THE_MOVIE_DB_3_API) String theMovieDbApiV3Url, Gson gson) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                final Request request = chain.request();
                final HttpUrl url = request.url().newBuilder()
                        .addQueryParameter("api_key", BuildConfig.TheMovieDbApiKey)
                        .build();
                return chain.proceed(request.newBuilder().url(url).build());
            }
        }).addInterceptor(httpLoggingInterceptor).build();

        return new Retrofit.Builder()
                .baseUrl(theMovieDbApiV3Url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Reusable
    public static TheMovieDatabaseApi3 provideTheMovieDatabaseApi3(@Named(THE_MOVIE_DB_3_API) Retrofit retrofit) {
        return retrofit.create(TheMovieDatabaseApi3.class);
    }

    @Binds
    public abstract TheMovieDb3Service provideTheMovieDb3Service(TheMovieDb3ServiceImpl repository);
}
