package pt.rikmartins.adn.popularmoviesstage2.data;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3.service.FavoriteService;

@Module
public abstract class DataModule {

    @Provides
    @Singleton
    public static AppDatabase provideAppDatabase(Context applicationContext) {
        return Room.databaseBuilder(applicationContext, AppDatabase.class, "database")
                .build();
    }

    @Provides
    @Singleton
    public static FavoriteDao provideFavoriteDao(AppDatabase appDatabase) {
        return appDatabase.favoriteDao();
    }

    @Binds
    public abstract FavoriteService provideFavoriteService(FavoriteServiceImpl favoriteService);
}
