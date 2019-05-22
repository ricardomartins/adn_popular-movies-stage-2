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

    private LiveData<List<MovieListItem>> movieListLiveData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        ((AppComponent.ComponentProvider) getApplication()).getComponent().inject(this);

        movieListLiveData = repository.getMovieListLiveData();
    }

    public LiveData<List<MovieListItem>> getMovieListLiveData() {
        return movieListLiveData;
    }

    public void requestMoreData() {
        repository.requestMoreData();
    }

    public void switchMode(int mode) {
        repository.switchMode(mode);
    }
}
