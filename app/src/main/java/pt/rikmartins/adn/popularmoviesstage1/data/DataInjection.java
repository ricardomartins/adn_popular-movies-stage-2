package pt.rikmartins.adn.popularmoviesstage1.data;

import org.jetbrains.annotations.NotNull;

import pt.rikmartins.adn.popularmoviesstage1.api.TheMovieDb3Service;

public class DataInjection {

    // Singleton
    @NotNull
    public Repository provideRepository(TheMovieDb3Service theMovieDb3Service, int apiStartPage) {
        return new Repository(theMovieDb3Service, apiStartPage);
    }
}
