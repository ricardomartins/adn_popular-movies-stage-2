package pt.rikmartins.adn.popularmoviesstage1;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import pt.rikmartins.adn.popularmoviesstage1.api.ApiInjection;
import pt.rikmartins.adn.popularmoviesstage1.api.TheMovieDatabaseApi3;
import pt.rikmartins.adn.popularmoviesstage1.api.TheMovieDb3Service;
import pt.rikmartins.adn.popularmoviesstage1.data.DataInjection;
import pt.rikmartins.adn.popularmoviesstage1.data.Repository;
import retrofit2.Retrofit;

public class Component {
    private ApiInjection apiModule;
    private DataInjection dataModule;

    private Graph graph;

    public Component() {
        apiModule = new ApiInjection();
        dataModule = new DataInjection();

        graph = new Graph();
    }

    @NonNull
    public Gson gson() { // TODO: Remove when no longer needed
        return apiModule.provideGson();
    }

    @NonNull
    public Repository repository() {
        return graph.resolveRepository();
    }

    private class Graph {
        private Gson gson;
        private Retrofit theMovieDatabaseApi3Retrofit;
        private TheMovieDatabaseApi3 theMovieDatabaseApi3;
        private TheMovieDb3Service theMovieDb3Service;
        private Repository repository;

        private Gson resolveGson() {
            if (gson == null) gson = apiModule.provideGson();
            return gson;
        }

        private Retrofit resolveTheMovieDatabaseApi3Retrofit() {
            if (theMovieDatabaseApi3Retrofit == null)
                theMovieDatabaseApi3Retrofit = apiModule.provideTheMovieDatabaseApi3Retrofit(apiModule.provideTheMovieDbApiV3Url(), resolveGson());
            return theMovieDatabaseApi3Retrofit;
        }

        private TheMovieDatabaseApi3 resolveTheMovieDatabaseApi3() {
            if (theMovieDatabaseApi3 == null)
                theMovieDatabaseApi3 = apiModule.provideTheMovieDatabaseApi3(resolveTheMovieDatabaseApi3Retrofit());
            return theMovieDatabaseApi3;
        }

        private TheMovieDb3Service resolveTheMovieDb3Service() {
            if (theMovieDb3Service == null)
                theMovieDb3Service = apiModule.provideTheMovieDb3Service(resolveTheMovieDatabaseApi3(), apiModule.provideTheMovieDbApiV3Key());
            return theMovieDb3Service;
        }

        private Repository resolveRepository() {
            if (repository == null)
                repository = dataModule.provideRepository(resolveTheMovieDb3Service(), apiModule.provideApiStartPage());
            return repository;
        }
    }

    public interface ComponentProvider {
        Component getComponent();
    }
}
