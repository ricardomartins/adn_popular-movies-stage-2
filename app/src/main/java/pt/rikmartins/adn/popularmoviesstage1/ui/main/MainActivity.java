package pt.rikmartins.adn.popularmoviesstage1.ui.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import pt.rikmartins.adn.popularmoviesstage1.AppComponent;
import pt.rikmartins.adn.popularmoviesstage1.R;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MovieListItem;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private TextView placeholderTextView;
    private RecyclerView movieListRecyclerView;
    private ProgressBar progressBar;

    private MainViewModel viewModel; // TODO: Find a way to inject this through Dagger as well
                                     // https://medium.com/chili-labs/android-viewmodel-injection-with-dagger-f0061d3402ff
                                     // is a very complex example, but may be the only way or in the
                                     // very least contain clues to a simpler/better solution

    @Inject Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Injection
        ((AppComponent.ComponentProvider) getApplication()).getComponent().inject(this);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        movieListRecyclerView = findViewById(R.id.rv_movie_list);
        progressBar = findViewById(R.id.pb_movie_list);
        placeholderTextView = findViewById(R.id.tv_placeholder);

        final MovieAdapter adapter = new MovieAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        movieListRecyclerView.setAdapter(adapter);
        movieListRecyclerView.setLayoutManager(layoutManager);

        viewModel.getMovieListLiveData().observe(this, new Observer<List<MovieListItem>>() {
            @Override
            public void onChanged(List<MovieListItem> movieListItems) {
                if (movieListItems == null || movieListItems.isEmpty()) showPlaceholder();
                else {
                    adapter.setMovieListItems(movieListItems);
                    showList();
                }
            }
        });

        viewModel.getWorkStatusLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean workStatus) {
                if (workStatus) showProgress();
                else hideProgress();
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
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    private void showList() {
        movieListRecyclerView.setVisibility(View.VISIBLE);
        placeholderTextView.setVisibility(View.INVISIBLE);
    }

    private void showPlaceholder() {
        placeholderTextView.setVisibility(View.VISIBLE);
        movieListRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(MovieListItem movieListItem) {
        // TODO
    }
}
