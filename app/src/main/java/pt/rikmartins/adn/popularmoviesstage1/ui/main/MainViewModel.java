package pt.rikmartins.adn.popularmoviesstage1.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import pt.rikmartins.adn.popularmoviesstage1.Component;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MovieListItem;
import pt.rikmartins.adn.popularmoviesstage1.data.Repository;

public class MainViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<MovieListItem>> movieListLiveData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        inject();

        movieListLiveData = repository.getMovieListLiveData();
    }

    public LiveData<List<MovieListItem>> getMovieListLiveData() {
        return movieListLiveData;
    }

    public void requestMoreData() {
        repository.requestMoreData();
    }

    private void inject() {
        Component component = ((Component.ComponentProvider) getApplication()).getComponent();
        repository = component.repository();
    }
}
