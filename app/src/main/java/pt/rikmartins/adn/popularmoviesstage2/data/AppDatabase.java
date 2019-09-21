package pt.rikmartins.adn.popularmoviesstage2.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import pt.rikmartins.adn.popularmoviesstage2.data.model.Favorite;

@Database(entities = {Favorite.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavoriteDao favoriteDao();
}
