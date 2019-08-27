package pt.rikmartins.adn.popularmoviesstage1.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import javax.inject.Inject;

import pt.rikmartins.adn.popularmoviesstage1.AppComponent;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MovieListItem;
import pt.rikmartins.adn.popularmoviesstage1.data.Repository;

public class MainViewModel extends AndroidViewModel {

    @Inject Repository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        ((AppComponent.ComponentProvider) application).getComponent().inject(this);
    }

    LiveData<PagedList<MovieListItem>> getMovieListLiveData() {
        return repository.getMovieListLiveData();
    }

    LiveData<Boolean> getWorkStatusLiveData() {
        return repository.getWorkStatusLiveData();
    }

    LiveData<Integer> getMode() {
        return repository.getMode();
    }

    void switchMode(int mode) {
        repository.switchMode(mode);
    }

    void updateConfiguration() {
        repository.updateConfiguration();
    }
}
