package pt.rikmartins.adn.popularmoviesstage1;

import android.app.Application;

public class PmApplication extends Application implements Component.ComponentHolder {
    private Component component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = new Component();
    }

    @Override
    public Component getComponent() {
        return component;
    }

}
