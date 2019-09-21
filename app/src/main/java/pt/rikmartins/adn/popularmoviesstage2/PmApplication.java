package pt.rikmartins.adn.popularmoviesstage2;

import android.app.Application;

public class PmApplication extends Application implements AppComponent.ComponentProvider {
    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.factory().create(this);
    }

    @Override
    public AppComponent getComponent() {
        return component;
    }
}
