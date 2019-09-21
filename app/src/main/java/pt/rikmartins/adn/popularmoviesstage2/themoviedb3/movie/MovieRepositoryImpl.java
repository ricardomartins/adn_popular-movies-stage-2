package pt.rikmartins.adn.popularmoviesstage2.themoviedb3.movie;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import javax.inject.Inject;
import javax.inject.Singleton;

import pt.rikmartins.adn.popularmoviesstage2.domain.gateway.MovieRepository;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3.service.FavoriteService;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3.service.TheMovieDb3Service;

@Singleton
public class MovieRepositoryImpl implements MovieRepository {

    private final FavoriteService favoriteService;
    private final TheMovieDb3Service theMovieDb3Service;

    @Inject
    public MovieRepositoryImpl(
            FavoriteService favoriteService,
            TheMovieDb3Service theMovieDb3Service) {
        this.favoriteService = favoriteService;
        this.theMovieDb3Service = theMovieDb3Service;
    }

    @Override
    public LiveData<PagedList<MovieInfo>> getAllFavoriteMovies() {
        return favoriteService.getAll();
    }

    @Override
    public LiveData<PagedList<MovieInfo>> getAllMoviesByHighestRated() {
        return theMovieDb3Service.getTopRatedMovies();
    }

    @Override
    public LiveData<PagedList<MovieInfo>> getAllMoviesByMostPopular() {
        return theMovieDb3Service.getPopularMovies();
    }

    @Override
    public void setMovieFavoriteStatus(MovieInfo movieInfo, boolean isFavorite) {
        if (isFavorite) favoriteService.insert(movieInfo);
        else favoriteService.delete(movieInfo);
    }

    @Override
    public LiveData<MovieInfo> getFavorite(MovieInfo movieInfo) {
        return (LiveData<MovieInfo>) favoriteService.getFavorite(movieInfo);
    }

    @Override
    public LiveData<Boolean> getWorkStatus() {
        return theMovieDb3Service.getWorkStatus();
    }
}
