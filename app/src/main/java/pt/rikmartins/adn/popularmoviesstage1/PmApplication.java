package pt.rikmartins.adn.popularmoviesstage1;

import android.app.Application;

public class PmApplication extends Application implements AppComponent.ComponentProvider {
    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder().build();
    }

    @Override
    public AppComponent getComponent() {
        return component;
    }

}
