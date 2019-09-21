package pt.rikmartins.adn.popularmoviesstage2.themoviedb3api.model;

import pt.rikmartins.adn.popularmoviesstage2.domain.model.Video;

public class VideoImpl implements Video {

    private String id;

    private String key;

    private String name;

    private String site;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSite() {
        return site;
    }
}
