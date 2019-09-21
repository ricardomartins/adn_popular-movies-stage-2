package pt.rikmartins.adn.popularmoviesstage2.domain.usecase.movie;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import javax.inject.Inject;
import javax.inject.Singleton;

import pt.rikmartins.adn.popularmoviesstage2.domain.WatchCase;
import pt.rikmartins.adn.popularmoviesstage2.domain.gateway.MovieRepository;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;

@Singleton
public class WatchFavoriteMovies implements WatchCase<Void, PagedList<MovieInfo>> {

    private MovieRepository movieRepository;

    @Inject
    public WatchFavoriteMovies(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public LiveData<PagedList<MovieInfo>> expose(Void aVoid) {
        return movieRepository.getAllFavoriteMovies();
    }
}
