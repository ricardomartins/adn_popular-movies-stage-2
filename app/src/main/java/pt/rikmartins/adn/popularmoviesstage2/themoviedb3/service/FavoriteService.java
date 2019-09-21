package pt.rikmartins.adn.popularmoviesstage2.themoviedb3.service;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;

public interface FavoriteService {
    LiveData<PagedList<MovieInfo>> getAll();

    LiveData<? extends MovieInfo> getFavorite(MovieInfo movieInfo);

    void insert(MovieInfo favorite);

    void delete(MovieInfo favorite);
}
