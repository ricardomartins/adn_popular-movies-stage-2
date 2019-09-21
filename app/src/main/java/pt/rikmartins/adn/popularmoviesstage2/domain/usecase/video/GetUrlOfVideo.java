package pt.rikmartins.adn.popularmoviesstage2.domain.usecase.video;

import javax.inject.Inject;
import javax.inject.Singleton;

import pt.rikmartins.adn.popularmoviesstage2.domain.model.Video;
import pt.rikmartins.adn.popularmoviesstage2.domain.UseCase;

@Singleton
public class GetUrlOfVideo implements UseCase<Video, String> {

    @Inject
    public GetUrlOfVideo() {}

    @Override
    public String execute(Video video) {
        switch (video.getSite()) {
            case "YouTube":
                return String.format("https://www.youtube.com/watch?v=%s", video.getKey());
            default:
                return null;
        }
    }
}
