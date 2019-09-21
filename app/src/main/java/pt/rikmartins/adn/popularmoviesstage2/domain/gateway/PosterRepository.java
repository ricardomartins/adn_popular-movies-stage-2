package pt.rikmartins.adn.popularmoviesstage2.domain.gateway;

import androidx.lifecycle.LiveData;

public interface PosterRepository extends Repository {

    LiveData<Boolean> getPosterUrlReadyStatus();

    String getPosterUrlOfMovieWithWidth(String posterPath, int width);

    String getPosterUrlOfMovieWithHeight(String posterPath, int height);
}
