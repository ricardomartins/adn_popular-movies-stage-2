package pt.rikmartins.adn.popularmoviesstage1.api;

import pt.rikmartins.adn.popularmoviesstage1.api.model.Configuration;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MovieListItem;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MoviePage;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDatabaseApi3 {
    @GET("movie/popular")
    Call<MoviePage> getPopularMovies(@Query("api_key") String apiKey,
                                     @Query("language") String language,
                                     @Query("page") Integer page,
                                     @Query("region") String region);

    @GET("movie/top_rated")
    Call<MoviePage> getTopRatedMovies(@Query("api_key") String apiKey,
                                      @Query("language") String language,
                                      @Query("page") Integer page,
                                      @Query("region") String region);

    @GET("movie/{movie_id}")
    Call<MovieListItem> getMovie(@Path("movie_id") int movieId,
                                 @Query("api_key") String apiKey,
                                 @Query("language") String language);

    @GET("configuration")
    Call<Configuration> getConfiguration(@Query("api_key") String apiKey);
}
