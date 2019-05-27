package pt.rikmartins.adn.popularmoviesstage1.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {
    @Provides
    public SharedPreferences providesDefaultSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
