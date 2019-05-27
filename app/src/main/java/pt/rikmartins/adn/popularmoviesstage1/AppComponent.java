package pt.rikmartins.adn.popularmoviesstage1;

import dagger.Component;
import pt.rikmartins.adn.popularmoviesstage1.api.ApiModule;
import pt.rikmartins.adn.popularmoviesstage1.data.DataModule;
import pt.rikmartins.adn.popularmoviesstage1.ui.main.MainActivity;
import pt.rikmartins.adn.popularmoviesstage1.ui.main.MainViewModel;

@Component(modules = {ApplicationModule.class, ApiModule.class, DataModule.class})
public interface AppComponent {

    void inject(MainActivity activity);

    void inject(MainViewModel viewModel);

    interface ComponentProvider {
        AppComponent getComponent();
    }
}
