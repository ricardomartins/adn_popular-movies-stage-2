package pt.rikmartins.adn.popularmoviesstage2.themoviedb3api.model;

import pt.rikmartins.adn.popularmoviesstage2.domain.model.Review;

public class ReviewImpl implements Review {

    private String id;

    private String author;

    private String content;

    private String url;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
