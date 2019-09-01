package pt.rikmartins.adn.popularmoviesstage1.ui.detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import javax.inject.Inject;

import pt.rikmartins.adn.popularmoviesstage1.AppComponent;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MovieListItem;
import pt.rikmartins.adn.popularmoviesstage1.data.ImageUrlGenerator;
import pt.rikmartins.adn.popularmoviesstage1.data.Repository;

public class DetailsViewModel extends AndroidViewModel {

    @Inject Repository repository;

    private final MutableLiveData<Integer> movieId;
    public final LiveData<MovieListItem> movie;

    public DetailsViewModel(@NonNull Application application) {
        super(application);
        ((AppComponent.ComponentProvider) application).getComponent().inject(this);

        movieId = new MutableLiveData<>();
        movie = Transformations.switchMap(movieId, new Function<Integer, LiveData<MovieListItem>>() {
            @Override
            public LiveData<MovieListItem> apply(Integer input) {
                return repository.getMovie(input);
            }
        });
    }

    void setMovieId(int movieId) {
        this.movieId.setValue(movieId);
    }

    LiveData<ImageUrlGenerator> getImageUrlGenerator() {
        return repository.getImageUrlGenerator();
    }
}
