package pt.rikmartins.adn.popularmoviesstage2.themoviedb3api;

import pt.rikmartins.adn.popularmoviesstage2.themoviedb3api.model.Configuration;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3api.model.MovieListItem;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3api.model.MoviePage;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3api.model.ReviewPage;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3api.model.VideoResults;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDatabaseApi3 {

    @GET("movie/popular")
    Call<MoviePage> getPopularMovies(@Query("page") Integer page);

    @GET("movie/top_rated")
    Call<MoviePage> getTopRatedMovies(@Query("page") Integer page);

    @GET("movie/{movie_id}")
    Call<MovieListItem> getMovie(@Path("movie_id") int movieId);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewPage> getMovieReviews(@Path("movie_id") int movieId,
                                       @Query("page") Integer page);

    @GET("movie/{movie_id}/videos")
    Call<VideoResults> getMovieVideos(@Path("movie_id") int movieId);

    @GET("configuration")
    Call<Configuration> getConfiguration();
}
