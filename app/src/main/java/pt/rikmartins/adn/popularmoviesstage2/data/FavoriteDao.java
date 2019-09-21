package pt.rikmartins.adn.popularmoviesstage2.data;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import pt.rikmartins.adn.popularmoviesstage2.data.model.Favorite;

@Dao
public interface FavoriteDao {
    @Query("SELECT * FROM favorite")
    DataSource.Factory<Integer, Favorite> getAll();

    @Query("SELECT * from favorite WHERE id = :id")
    LiveData<Favorite> getById(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Favorite favorite);

    @Delete
    void delete(Favorite user);
}
