package pt.rikmartins.adn.popularmoviesstage2.domain.gateway;

import java.util.List;

import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.Video;

public interface VideoRepository extends Repository {
    List<Video> getVideosOfMovie(MovieInfo movieInfo);
}
