package pt.rikmartins.adn.popularmoviesstage2.domain.usecase.movie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import javax.inject.Inject;
import javax.inject.Singleton;

import pt.rikmartins.adn.popularmoviesstage2.domain.WatchCase;
import pt.rikmartins.adn.popularmoviesstage2.domain.gateway.MovieRepository;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;

@Singleton
public class IsMovieFavorite implements WatchCase<MovieInfo, Boolean> {

    private final MovieRepository movieRepository;

    @Inject
    public IsMovieFavorite(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public LiveData<Boolean> expose(MovieInfo movieInfo) {
        return Transformations.map(movieRepository.getFavorite(movieInfo), input -> input != null);
    }
}
