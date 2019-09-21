package pt.rikmartins.adn.popularmoviesstage2.themoviedb3.review;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import javax.inject.Inject;
import javax.inject.Singleton;

import pt.rikmartins.adn.popularmoviesstage2.domain.gateway.ReviewRepository;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.Review;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3.service.TheMovieDb3Service;

@Singleton
public class ReviewRepositoryImpl implements ReviewRepository {

    private final LiveData<Boolean> workStatus;

    private final TheMovieDb3Service theMovieDb3Service;

    @Inject
    public ReviewRepositoryImpl(TheMovieDb3Service theMovieDb3Service) {
        this.theMovieDb3Service = theMovieDb3Service;
        workStatus = theMovieDb3Service.getWorkStatus();
    }

    @Override
    public LiveData<PagedList<Review>> getReviewsOfMovie(MovieInfo movieInfo) {
        return theMovieDb3Service.getMovieReviews(movieInfo.getId());
    }

    @Override
    public LiveData<Boolean> getWorkStatus() {
        return workStatus;
    }
}
