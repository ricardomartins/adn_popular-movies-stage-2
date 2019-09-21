package pt.rikmartins.adn.popularmoviesstage2.domain.gateway;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.Review;

public interface ReviewRepository extends Repository {
    LiveData<PagedList<Review>> getReviewsOfMovie(MovieInfo movieInfo);
}
