package pt.rikmartins.adn.popularmoviesstage2.themoviedb3.video;

import androidx.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import pt.rikmartins.adn.popularmoviesstage2.domain.gateway.VideoRepository;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.Video;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3api.TheMovieDb3ServiceImpl;

@Singleton
public class VideoRepositoryImpl implements VideoRepository {

    private final TheMovieDb3ServiceImpl theMovieDb3Service;

    private final LiveData<Boolean> workStatus;

    @Inject
    public VideoRepositoryImpl(TheMovieDb3ServiceImpl theMovieDb3Service) {
        this.theMovieDb3Service = theMovieDb3Service;

        workStatus = theMovieDb3Service.getWorkStatus();
    }

    @Override
    public List<Video> getVideosOfMovie(MovieInfo movieInfo) {
        return theMovieDb3Service.getMovieVideos(movieInfo.getId());
    }

    @Override
    public LiveData<Boolean> getWorkStatus() {
        return workStatus;
    }
}
