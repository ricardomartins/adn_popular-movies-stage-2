package pt.rikmartins.adn.popularmoviesstage1.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import pt.rikmartins.adn.popularmoviesstage1.AppComponent;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MovieListItem;
import pt.rikmartins.adn.popularmoviesstage1.data.Repository;

public class MainViewModel extends AndroidViewModel {

    @Inject Repository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        ((AppComponent.ComponentProvider) getApplication()).getComponent().inject(this);
    }

    LiveData<List<MovieListItem>> getMovieListLiveData() {
        return repository.getMovieListLiveData();
    }

    LiveData<Boolean> getWorkStatusLiveData() {
        return repository.getWorkStatusLiveData();
    }

    void requestMoreData() {
        repository.requestMoreData();
    }

    void switchMode(int mode) {
        repository.switchMode(mode);
    }
}
