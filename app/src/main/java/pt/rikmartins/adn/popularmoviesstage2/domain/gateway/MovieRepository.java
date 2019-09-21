package pt.rikmartins.adn.popularmoviesstage2.domain.gateway;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;


public interface MovieRepository extends Repository {

    LiveData<PagedList<MovieInfo>> getAllFavoriteMovies();

    LiveData<PagedList<MovieInfo>> getAllMoviesByHighestRated();

    LiveData<PagedList<MovieInfo>> getAllMoviesByMostPopular();

    void setMovieFavoriteStatus(MovieInfo movieInfo, boolean isFavorite);

    LiveData<MovieInfo> getFavorite(MovieInfo movieInfo);
}
