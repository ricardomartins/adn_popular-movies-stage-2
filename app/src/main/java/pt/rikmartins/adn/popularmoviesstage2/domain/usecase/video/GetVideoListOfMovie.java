package pt.rikmartins.adn.popularmoviesstage2.domain.usecase.video;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import pt.rikmartins.adn.popularmoviesstage2.domain.UseCase;
import pt.rikmartins.adn.popularmoviesstage2.domain.gateway.VideoRepository;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.Video;

@Singleton
public class GetVideoListOfMovie implements UseCase<MovieInfo, List<Video>> {

    private final VideoRepository videoRepository;

    @Inject
    public GetVideoListOfMovie(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Override
    public List<Video> execute(MovieInfo movieInfo) {
        return videoRepository.getVideosOfMovie(movieInfo);
    }
}
