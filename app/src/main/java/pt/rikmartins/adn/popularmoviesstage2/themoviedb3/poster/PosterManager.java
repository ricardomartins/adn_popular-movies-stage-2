package pt.rikmartins.adn.popularmoviesstage2.themoviedb3.poster;

import pt.rikmartins.adn.popularmoviesstage2.themoviedb3.model.ImagesConfiguration;

public interface PosterManager {

    ImagesConfiguration downloadPosterConfiguration();

    void setPosterConfiguration(ImagesConfiguration imagesConfiguration);
}
