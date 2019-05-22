package pt.rikmartins.adn.popularmoviesstage1.ui.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import pt.rikmartins.adn.popularmoviesstage1.AppComponent;
import pt.rikmartins.adn.popularmoviesstage1.R;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MovieListItem;

public class MainActivity extends AppCompatActivity {

    private TextView tv;

    private MainViewModel viewModel; // TODO: Find a way to inject this through Dagger as well
                                     // https://medium.com/chili-labs/android-viewmodel-injection-with-dagger-f0061d3402ff
                                     // is a very complex example, but may be the only way or in the
                                     // very least contain clues to a simpler/better solution

    @Inject Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((AppComponent.ComponentProvider) getApplication()).getComponent().inject(this);

        tv = findViewById(R.id.tv_output);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovieListLiveData().observe(this, new Observer<List<MovieListItem>>() {
            @Override
            public void onChanged(List<MovieListItem> movieListItems) {
                tv.setText(gson.toJson(movieListItems));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("get").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                viewModel.requestMoreData();
                return true;
            }
        });
        return true;
    }
}
