package pt.rikmartins.adn.popularmoviesstage1.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieListItem { // referred to in the API docs as {Movie List Result Object}

    @SerializedName("poster_path")
    private String posterPath; // can be null

    private Boolean adult;

    private String overview;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("genre_ids")
    private List<Integer> genreIds;

    private Integer id;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("original_language")
    private String originalLanguage;

    private String title;

    @SerializedName("backdrop_path")
    private String backdropPath; // can be null

    private Float popularity;

    @SerializedName("vote_count")
    private Integer voteCount;

    private Boolean video;

    @SerializedName("vote_average")
    private Float voteAverage;

    public String getPosterPath() {
        return posterPath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public Integer getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Float getPopularity() {
        return popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public Boolean getVideo() {
        return video;
    }

    public Float getVoteAverage() {
        return voteAverage;
    }
}
