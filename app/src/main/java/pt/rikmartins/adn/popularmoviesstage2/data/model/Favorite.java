package pt.rikmartins.adn.popularmoviesstage2.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.auto.value.AutoValue;

import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;

@AutoValue
@Entity(tableName = "favorite")
public abstract class Favorite implements MovieInfo {

    @AutoValue.CopyAnnotations
    @Override
    @PrimaryKey
    public abstract Integer getId();

    public static Favorite create(Integer id, String title, String overview, String posterPath,
                                  String releaseDate, Float voteAverage) {
        return new AutoValue_Favorite(title, overview, posterPath, releaseDate, voteAverage, id);
    }
}
