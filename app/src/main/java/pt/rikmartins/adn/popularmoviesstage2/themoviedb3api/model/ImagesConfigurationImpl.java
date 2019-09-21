package pt.rikmartins.adn.popularmoviesstage2.themoviedb3api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import pt.rikmartins.adn.popularmoviesstage2.themoviedb3.model.ImagesConfiguration;

public class ImagesConfigurationImpl implements ImagesConfiguration {

    @SerializedName("base_url")
    private String baseUrl;

    @SerializedName("secure_base_url")
    private String secureBaseUrl;

    @SerializedName("poster_sizes")
    private List<String> posterSizes;

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public String getSecureBaseUrl() {
        return secureBaseUrl;
    }

    @Override
    public List<String> getPosterSizes() {
        return posterSizes;
    }
}
