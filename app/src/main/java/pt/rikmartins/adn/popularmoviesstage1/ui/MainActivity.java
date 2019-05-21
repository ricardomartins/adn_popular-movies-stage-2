package pt.rikmartins.adn.popularmoviesstage1.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;

import pt.rikmartins.adn.popularmoviesstage1.Component;
import pt.rikmartins.adn.popularmoviesstage1.R;
import pt.rikmartins.adn.popularmoviesstage1.api.TheMovieDb3Service;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MoviePage;

public class MainActivity extends AppCompatActivity {

    private TheMovieDb3Service theMovieDb3Service;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        injectVariables();

        final TextView tv = findViewById(R.id.tv_output);

        MutableLiveData<MoviePage> popularMovies = theMovieDb3Service.getPopularMovies(1);

        popularMovies.observeForever(new Observer<MoviePage>() {
            @Override
            public void onChanged(MoviePage movieListItemPage) {
                tv.setText(gson.toJson(movieListItemPage));
            }
        });
    }

    private void injectVariables() {
        Component component = ((Component.ComponentHolder) getApplication()).getComponent();
        theMovieDb3Service = component.service();
        gson = component.gson();
    }
}
