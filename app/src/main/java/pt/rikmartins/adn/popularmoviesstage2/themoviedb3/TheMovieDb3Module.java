package pt.rikmartins.adn.popularmoviesstage2.themoviedb3;

import dagger.Binds;
import dagger.Module;
import pt.rikmartins.adn.popularmoviesstage2.domain.gateway.MovieRepository;
import pt.rikmartins.adn.popularmoviesstage2.domain.gateway.PosterRepository;
import pt.rikmartins.adn.popularmoviesstage2.domain.gateway.ReviewRepository;
import pt.rikmartins.adn.popularmoviesstage2.domain.gateway.VideoRepository;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3.movie.MovieRepositoryImpl;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3.poster.PosterManager;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3.poster.PosterRepositoryImpl;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3.review.ReviewRepositoryImpl;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3.video.VideoRepositoryImpl;

@Module
public abstract class TheMovieDb3Module {
    @Binds
    public abstract MovieRepository provideMovieRepository(MovieRepositoryImpl repository);

    @Binds
    public abstract PosterRepository providePosterRepository(PosterRepositoryImpl repository);

    @Binds
    public abstract ReviewRepository provideReviewRepository(ReviewRepositoryImpl repository);

    @Binds
    public abstract VideoRepository provideVideoRepository(VideoRepositoryImpl repository);

    @Binds
    public abstract PosterManager providePosterManager(PosterRepositoryImpl posterManager);
}
