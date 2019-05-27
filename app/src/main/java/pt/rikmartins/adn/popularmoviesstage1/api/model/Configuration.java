package pt.rikmartins.adn.popularmoviesstage1.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Configuration {
    private ImagesConfiguration images;

    @SerializedName("change_keys")
    private List<String > changeKeys;

    public ImagesConfiguration getImages() {
        return images;
    }

    public List<String> getChangeKeys() {
        return changeKeys;
    }
}
