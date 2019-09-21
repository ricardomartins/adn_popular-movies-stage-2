package pt.rikmartins.adn.popularmoviesstage2.ui.review;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import javax.inject.Inject;

import pt.rikmartins.adn.popularmoviesstage2.AppComponent;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.Review;
import pt.rikmartins.adn.popularmoviesstage2.domain.usecase.review.WatchPagedReviewListOfMovie;
import pt.rikmartins.adn.popularmoviesstage2.ui.BaseViewModel;

public class ReviewsViewModel extends BaseViewModel {

    @Inject
    WatchPagedReviewListOfMovie watchPagedReviewListOfMovie;

    private LiveData<PagedList<Review>> movieReviews;

    public ReviewsViewModel(@NonNull Application application) {
        super(application);
        ((AppComponent.ComponentProvider) application).getComponent().inject(this);
    }

    public void setMovie(MovieInfo movie) {
        movieReviews = watchCaseExposer.expose(watchPagedReviewListOfMovie, movie);
    }

    public LiveData<PagedList<Review>> getMovieReviews() {
        return movieReviews;
    }
}
