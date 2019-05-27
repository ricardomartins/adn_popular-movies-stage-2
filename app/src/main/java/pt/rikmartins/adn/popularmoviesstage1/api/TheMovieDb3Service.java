package pt.rikmartins.adn.popularmoviesstage1.api;

import javax.inject.Inject;
import javax.inject.Named;

import pt.rikmartins.adn.popularmoviesstage1.api.model.Configuration;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MoviePage;
import retrofit2.Call;

public class TheMovieDb3Service {

    private final TheMovieDatabaseApi3 theMovieDatabaseApi3;
    private final String theMovieDbApiV3Key;

    @Inject
    public TheMovieDb3Service(TheMovieDatabaseApi3 theMovieDatabaseApi3, @Named(ApiModule.THE_MOVIE_DB_API_KEY_NAME) String theMovieDbApiV3Key) {
        this.theMovieDatabaseApi3 = theMovieDatabaseApi3;
        this.theMovieDbApiV3Key = theMovieDbApiV3Key;
    }

    public Call<MoviePage> getPopularMovies(Integer page) {
        return theMovieDatabaseApi3.getPopularMovies(theMovieDbApiV3Key, null, page, null);
    }

    public Call<MoviePage> getTopRatedMovies(Integer page) {
        return theMovieDatabaseApi3.getTopRatedMovies(theMovieDbApiV3Key, null, page, null);
    }

    public Call<Configuration> getConfiguration() {
        return theMovieDatabaseApi3.getConfiguration(theMovieDbApiV3Key);
    }
}
