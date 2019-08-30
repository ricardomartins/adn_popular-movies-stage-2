package pt.rikmartins.adn.popularmoviesstage1.ui.main;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import javax.inject.Inject;

import pt.rikmartins.adn.popularmoviesstage1.AppComponent;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MovieListItem;
import pt.rikmartins.adn.popularmoviesstage1.data.Repository;

public class MainViewModel extends AndroidViewModel {

    @Inject Repository repository;

    private final MutableLiveData<String> desiredPosterSize = new MutableLiveData<>();
    private final MediatorLiveData<Uri> imagesUrl = new MediatorLiveData<>();

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

    public Uri getImagesUrl(String desiredPosterSize) throws Repository.RequirementsMissingException {
        return repository.getImagesUrl(desiredPosterSize);
    }

    public void setDesiredPosterSize(String desiredPosterSize) {
        this.desiredPosterSize.setValue(desiredPosterSize);
    }

    public MediatorLiveData<Uri> getImagesUrl() {
        return imagesUrl;
    }
}
