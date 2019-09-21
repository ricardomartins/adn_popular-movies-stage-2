package pt.rikmartins.adn.popularmoviesstage2.themoviedb3api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public abstract class Page<T> {
    private Integer page;

    private List<T> results;

    @SerializedName("total_results")
    private Integer totalResults;

    @SerializedName("total_pages")
    private Integer totalPages;

    public Integer getPage() {
        return page;
    }

    public List<T> getResults() {
        return results;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }
}
