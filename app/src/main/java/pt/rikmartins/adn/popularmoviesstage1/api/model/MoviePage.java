package pt.rikmartins.adn.popularmoviesstage1.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviePage {

    public MoviePage() {}

    private Integer page;

    private List<MovieListItem> results;

    @SerializedName("total_results")
    private Integer totalResults;

    @SerializedName("total_pages")
    private Integer totalPages;
}
