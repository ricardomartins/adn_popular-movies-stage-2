package pt.rikmartins.adn.popularmoviesstage2.themoviedb3.service;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import java.util.List;

import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.Review;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.Video;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3.model.ImagesConfiguration;

public interface TheMovieDb3Service {
    LiveData<PagedList<MovieInfo>> getPopularMovies();

    LiveData<PagedList<MovieInfo>> getTopRatedMovies();

    MovieInfo getMovie(int movieId);

    LiveData<PagedList<Review>> getMovieReviews(int movieId);

    List<Video> getMovieVideos(int movieId);

    ImagesConfiguration getImagesConfiguration();

    LiveData<Boolean> getWorkStatus();
}
