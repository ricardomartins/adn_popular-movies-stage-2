package pt.rikmartins.adn.popularmoviesstage2.domain.usecase.poster;

import android.util.Pair;

import javax.inject.Inject;
import javax.inject.Singleton;

import pt.rikmartins.adn.popularmoviesstage2.domain.gateway.PosterRepository;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;
import pt.rikmartins.adn.popularmoviesstage2.domain.UseCase;

@Singleton
public class GetPosterUrlOfMovieWithHeight implements UseCase<Pair<MovieInfo, Integer>, String> {

    private final PosterRepository posterRepository;

    @Inject
    public GetPosterUrlOfMovieWithHeight(PosterRepository posterRepository) {
        this.posterRepository = posterRepository;
    }

    @Override
    public String execute(Pair<MovieInfo, Integer> movieInfoStringPair) {
        final String posterPath = movieInfoStringPair.first.getPosterPath();
        final Integer height = movieInfoStringPair.second;

        return posterRepository.getPosterUrlOfMovieWithHeight(posterPath, height);
    }
}
