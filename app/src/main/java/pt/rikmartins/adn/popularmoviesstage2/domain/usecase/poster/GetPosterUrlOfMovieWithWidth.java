package pt.rikmartins.adn.popularmoviesstage2.domain.usecase.poster;

import android.util.Pair;

import javax.inject.Inject;
import javax.inject.Singleton;

import pt.rikmartins.adn.popularmoviesstage2.domain.gateway.PosterRepository;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;
import pt.rikmartins.adn.popularmoviesstage2.domain.UseCase;

@Singleton
public class GetPosterUrlOfMovieWithWidth implements UseCase<Pair<MovieInfo, Integer>, String> {

    private final PosterRepository posterRepository;

    @Inject
    public GetPosterUrlOfMovieWithWidth(PosterRepository posterRepository) {
        this.posterRepository = posterRepository;
    }

    @Override
    public String execute(Pair<MovieInfo, Integer> movieInfoStringPair) {
        final String posterPath = movieInfoStringPair.first.getPosterPath();
        final Integer width = movieInfoStringPair.second;

        return posterRepository.getPosterUrlOfMovieWithWidth(posterPath, width);
    }
}
