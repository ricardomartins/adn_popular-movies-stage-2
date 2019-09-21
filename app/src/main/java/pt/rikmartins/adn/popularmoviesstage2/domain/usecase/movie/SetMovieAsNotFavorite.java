package pt.rikmartins.adn.popularmoviesstage2.domain.usecase.movie;

import javax.inject.Inject;
import javax.inject.Singleton;

import pt.rikmartins.adn.popularmoviesstage2.domain.UseCase;
import pt.rikmartins.adn.popularmoviesstage2.domain.gateway.MovieRepository;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;

@Singleton
public class SetMovieAsNotFavorite implements UseCase<MovieInfo, Void> {

    private final MovieRepository movieRepository;

    @Inject
    public SetMovieAsNotFavorite(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Void execute(MovieInfo movieInfo) {
        movieRepository.setMovieFavoriteStatus(movieInfo, false);
        return null;
    }
}
