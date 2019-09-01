package pt.rikmartins.adn.popularmoviesstage1.ui.detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import javax.inject.Inject;

import pt.rikmartins.adn.popularmoviesstage1.AppComponent;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MovieListItem;
import pt.rikmartins.adn.popularmoviesstage1.data.ImageUrlGenerator;
import pt.rikmartins.adn.popularmoviesstage1.data.Repository;

public class DetailsViewModel extends AndroidViewModel {

    @Inject Repository repository;

    private MovieListItem movie;

    public DetailsViewModel(@NonNull Application application) {
        super(application);
        ((AppComponent.ComponentProvider) application).getComponent().inject(this);
    }

    LiveData<ImageUrlGenerator> getImageUrlGenerator() {
        return repository.getImageUrlGenerator();
    }

    void setMovie(MovieListItem movie) {
        this.movie = movie;
    }

    public MovieListItem getMovie() {
        return movie;
    }
}
