package pt.rikmartins.adn.popularmoviesstage2.domain.usecase.review;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import javax.inject.Inject;

import pt.rikmartins.adn.popularmoviesstage2.domain.gateway.ReviewRepository;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.Review;
import pt.rikmartins.adn.popularmoviesstage2.domain.WatchCase;

public class WatchPagedReviewListOfMovie implements WatchCase<MovieInfo, PagedList<Review>> {

    private ReviewRepository reviewRepository;

    @Inject
    public WatchPagedReviewListOfMovie(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public LiveData<PagedList<Review>> expose(MovieInfo movieInfo) {
        return reviewRepository.getReviewsOfMovie(movieInfo);
    }
}
