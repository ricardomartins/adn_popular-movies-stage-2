package pt.rikmartins.adn.popularmoviesstage2.ui.detail;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;
import pt.rikmartins.adn.popularmoviesstage2.AppComponent;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.Video;
import pt.rikmartins.adn.popularmoviesstage2.domain.usecase.movie.IsMovieFavorite;
import pt.rikmartins.adn.popularmoviesstage2.domain.usecase.movie.SetMovieAsFavorite;
import pt.rikmartins.adn.popularmoviesstage2.domain.usecase.movie.SetMovieAsNotFavorite;
import pt.rikmartins.adn.popularmoviesstage2.domain.usecase.poster.GetPosterUrlOfMovieWithWidth;
import pt.rikmartins.adn.popularmoviesstage2.domain.usecase.poster.WatchPosterUrlsAvailability;
import pt.rikmartins.adn.popularmoviesstage2.domain.usecase.video.GetPreviewImageUrlOfVideo;
import pt.rikmartins.adn.popularmoviesstage2.domain.usecase.video.GetUrlOfVideo;
import pt.rikmartins.adn.popularmoviesstage2.domain.usecase.video.GetVideoListOfMovie;
import pt.rikmartins.adn.popularmoviesstage2.ui.BaseViewModel;

public class DetailsViewModel extends BaseViewModel {

    @Inject
    IsMovieFavorite isMovieFavorite;

    @Inject
    SetMovieAsFavorite setMovieAsFavorite;

    @Inject
    SetMovieAsNotFavorite setMovieAsNotFavorite;

    @Inject
    GetPosterUrlOfMovieWithWidth getPosterUrlOfMovieWithWidth;

    @Inject
    WatchPosterUrlsAvailability watchPosterUrlsAvailability;

    @Inject
    GetPreviewImageUrlOfVideo getPreviewImageUrlOfVideo;

    @Inject
    GetUrlOfVideo getUrlOfVideo;

    @Inject
    GetVideoListOfMovie getVideoListOfMovie;

    private MovieInfo movie;

    private LiveData<Boolean> postersAvailable;

    private Maybe<List<Video>> videoListOfMovie;

    private LiveData<Boolean> isFavorite;

    public DetailsViewModel(@NonNull Application application) {
        super(application);
        ((AppComponent.ComponentProvider) application).getComponent().inject(this);
    }

    public void setMovieAsFavorite() {
        ioUseCaseExecutor.executeAsync(setMovieAsFavorite, movie).subscribe();
    }

    public void setMovieAsNotFavorite() {
        ioUseCaseExecutor.executeAsync(setMovieAsNotFavorite, movie).subscribe();
    }

    public LiveData<Boolean> watchPostersAvailable() {
        return postersAvailable;
    }

    public Single<String> getPosterUrlOfMovieWithWidth(int width) {
        return ioUseCaseExecutor.executeAsync(getPosterUrlOfMovieWithWidth,
                new Pair<>(movie, width)).toSingle();
    }

    public String getPreviewImageUrlOfVideo(Video video) {
        return useCaseExecutor.execute(getPreviewImageUrlOfVideo, video);
    }

    public String getUrlOfVideo(Video video) {
        return useCaseExecutor.execute(getUrlOfVideo, video);
    }

    public Maybe<List<Video>> getVideoListOfMovie() {
        return videoListOfMovie;
    }

    public LiveData<Boolean> isMovieFavorite() {
        return isFavorite;
    }

    public void setMovie(MovieInfo movie) {
        this.movie = movie;

        videoListOfMovie = ioUseCaseExecutor.executeAsync(getVideoListOfMovie, movie).cache();
        isFavorite = watchCaseExposer.expose(isMovieFavorite, movie);
        postersAvailable = watchCaseExposer.expose(watchPosterUrlsAvailability);
    }

    public MovieInfo getMovie() {
        return movie;
    }
}
