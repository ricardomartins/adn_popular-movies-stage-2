package pt.rikmartins.adn.popularmoviesstage1;

import android.content.Context;

import dagger.BindsInstance;
import dagger.Component;
import pt.rikmartins.adn.popularmoviesstage1.api.ApiModule;
import pt.rikmartins.adn.popularmoviesstage1.data.DataModule;
import pt.rikmartins.adn.popularmoviesstage1.ui.detail.DetailsActivity;
import pt.rikmartins.adn.popularmoviesstage1.ui.detail.DetailsViewModel;
import pt.rikmartins.adn.popularmoviesstage1.ui.main.MainActivity;
import pt.rikmartins.adn.popularmoviesstage1.ui.main.MainViewModel;

@Component(modules = {ApiModule.class, DataModule.class})
public interface AppComponent {

    void inject(MainActivity activity);

    void inject(DetailsActivity activity);

    void inject(MainViewModel viewModel);

    void inject(DetailsViewModel viewModel);

    @Component.Factory
    interface Factory {
        AppComponent create(@BindsInstance Context applicationContext);
    }

    interface ComponentProvider {
        AppComponent getComponent();
    }
}
