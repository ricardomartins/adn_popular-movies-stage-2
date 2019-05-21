package pt.rikmartins.adn.popularmoviesstage1;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import pt.rikmartins.adn.popularmoviesstage1.api.Injection;
import pt.rikmartins.adn.popularmoviesstage1.api.TheMovieDatabaseApi3;
import pt.rikmartins.adn.popularmoviesstage1.api.TheMovieDb3Service;
import retrofit2.Retrofit;

public class Component {
    private Injection apiModule;

    public Component() {
        this.apiModule = new Injection();
    }

    @NonNull
    public Gson gson() { // TODO: Remove when no longer needed
        return apiModule.provideGson();
    }

    @NonNull
    public TheMovieDb3Service service() {
        Gson gson = apiModule.provideGson();
        String theMovieDbApiV3Url = apiModule.provideTheMovieDbApiV3Url();
        Retrofit retrofit = apiModule.provideTheMovieDatabaseApi3Retrofit(theMovieDbApiV3Url, gson);
        TheMovieDatabaseApi3 theMovieDatabaseApi3 = apiModule.provideTheMovieDatabaseApi3(retrofit);
        String theMovieDbApiV3Key = apiModule.provideTheMovieDbApiV3Key();
        return apiModule.provideTheMovieDb3Service(theMovieDatabaseApi3, theMovieDbApiV3Key);
    }

    public interface ComponentHolder {
        Component getComponent();
    }
}
