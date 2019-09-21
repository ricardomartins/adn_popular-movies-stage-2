package pt.rikmartins.adn.popularmoviesstage2.domain.model;


public interface MovieInfo {

    Integer getId();

    String getTitle();

    String getOverview();

    String getPosterPath();

    String getReleaseDate();

    Float getVoteAverage();
}
