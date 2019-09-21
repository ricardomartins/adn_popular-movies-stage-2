package pt.rikmartins.adn.popularmoviesstage2;

import android.content.Context;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import pt.rikmartins.adn.popularmoviesstage2.data.DataModule;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3.TheMovieDb3Module;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3.poster.PosterConfigurationUpdateWorker;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3api.TheMovieDb3ApiModule;
import pt.rikmartins.adn.popularmoviesstage2.ui.UiModule;
import pt.rikmartins.adn.popularmoviesstage2.ui.review.ReviewsActivity;
import pt.rikmartins.adn.popularmoviesstage2.ui.detail.DetailsActivity;
import pt.rikmartins.adn.popularmoviesstage2.ui.detail.DetailsViewModel;
import pt.rikmartins.adn.popularmoviesstage2.ui.main.MainActivity;
import pt.rikmartins.adn.popularmoviesstage2.ui.main.MainViewModel;
import pt.rikmartins.adn.popularmoviesstage2.ui.review.ReviewsViewModel;

@Singleton
@Component(modules = {AppModule.class, TheMovieDb3Module.class, DataModule.class, UiModule.class,
        TheMovieDb3ApiModule.class})
public interface AppComponent {

    void inject(MainActivity activity);

    void inject(DetailsActivity activity);

    void inject(MainViewModel viewModel);

    void inject(DetailsViewModel viewModel);

    void inject(PosterConfigurationUpdateWorker posterConfigurationUpdateWorker);

    void inject(ReviewsActivity reviewsActivity);

    void inject(ReviewsViewModel reviewsViewModel);

    @Component.Factory
    interface Factory {
        AppComponent create(@BindsInstance Context applicationContext);
    }

    interface ComponentProvider {
        AppComponent getComponent();
    }
}
