package pt.rikmartins.adn.popularmoviesstage2.themoviedb3api.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;

public class MovieListItem implements MovieInfo { // referred to in the API docs as {Movie List Result Object}

    @SerializedName("poster_path")
    @Nullable
    private String posterPath;

    private String overview;

    @SerializedName("release_date")
    private String releaseDate;

    private Integer id;

    private String title;

    @SerializedName("vote_average")
    private Float voteAverage;

    @Override
    @Nullable
    public String getPosterPath() {
        return posterPath;
    }

    @Override
    public String getOverview() {
        return overview;
    }

    @Override
    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Float getVoteAverage() {
        return voteAverage;
    }
}
