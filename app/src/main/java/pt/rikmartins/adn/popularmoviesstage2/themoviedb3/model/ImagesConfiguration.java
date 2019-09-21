package pt.rikmartins.adn.popularmoviesstage2.themoviedb3.model;

import java.util.List;

public interface ImagesConfiguration {
    String getBaseUrl();

    String getSecureBaseUrl();

    List<String> getPosterSizes();
}
