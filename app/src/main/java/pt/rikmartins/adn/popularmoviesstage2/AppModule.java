package pt.rikmartins.adn.popularmoviesstage2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.work.WorkManager;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

@Module
public abstract class AppModule {
    @Provides
    @Reusable
    public static WorkManager provideWorkManager(Context appContext) {
        return WorkManager.getInstance(appContext);
    }

    @Provides
    @Reusable
    public static SharedPreferences provideSharedPreferences(Context appContext) {
        return PreferenceManager.getDefaultSharedPreferences(appContext);
    }
}
