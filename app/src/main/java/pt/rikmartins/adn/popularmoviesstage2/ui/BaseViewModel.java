package pt.rikmartins.adn.popularmoviesstage2.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import javax.inject.Inject;

import pt.rikmartins.adn.popularmoviesstage2.domain.AsyncUseCaseExecutor;
import pt.rikmartins.adn.popularmoviesstage2.domain.UseCaseExecutor;
import pt.rikmartins.adn.popularmoviesstage2.domain.WatchCaseExposer;


public abstract class BaseViewModel extends AndroidViewModel {

    @Inject
    public AsyncUseCaseExecutor ioUseCaseExecutor;

    @Inject
    public UseCaseExecutor useCaseExecutor;

    @Inject
    public WatchCaseExposer watchCaseExposer;

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }
}
