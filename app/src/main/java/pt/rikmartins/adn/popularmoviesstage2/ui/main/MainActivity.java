package pt.rikmartins.adn.popularmoviesstage2.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Single;
import pt.rikmartins.adn.popularmoviesstage2.AppComponent;
import pt.rikmartins.adn.popularmoviesstage2.R;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;
import pt.rikmartins.adn.popularmoviesstage2.ui.detail.DetailsActivity;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler,
        MovieAdapter.MovieAdapterImageUrlProvider {

    private TextView placeholderTextView;
    private RecyclerView movieListRecyclerView;
    private ProgressBar progressBar;

    @Inject
    MovieAdapter adapter;

    private MainViewModel viewModel;

    private final Set<MenuItem> menuItems = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Injection
        ((AppComponent.ComponentProvider) getApplication()).getComponent().inject(this);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        movieListRecyclerView = findViewById(R.id.rv_movie_list);
        progressBar = findViewById(R.id.pb_movie_list);
        placeholderTextView = findViewById(R.id.tv_placeholder);

        adapter.setMovieAdapterOnClickHandler(this);
        movieListRecyclerView.setAdapter(adapter);

        final int columnQuant = getResources().getInteger(R.integer.column_quantity);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, columnQuant);
        movieListRecyclerView.setLayoutManager(layoutManager);

        viewModel.movies.observe(this, new Observer<PagedList<MovieInfo>>() {
            @Override
            public void onChanged(PagedList<MovieInfo> movieListItems) {
                if (movieListItems == null || movieListItems.isEmpty())
                    MainActivity.this.showPlaceholder();
                else MainActivity.this.showList();

                adapter.submitList(movieListItems, new Runnable() {
                    @Override
                    public void run() {
                        movieListRecyclerView.scrollToPosition(0);
                    }
                });
            }
        });

        viewModel.watchPostersAvailable().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                adapter.setMovieAdapterImageUrlProvider(aBoolean ? MainActivity.this : null);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final int modesSize = viewModel.modes.size();

        for (int i = 0; i < modesSize; i++) {
            final MenuItem menuItem = menu.add(Menu.NONE, viewModel.modes.keyAt(i), Menu.NONE, viewModel.modes.valueAt(i));
            menuItems.add(menuItem);
        }

        viewModel.getMode().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer mode) {
                for (MenuItem menuItem : menuItems)
                    menuItem.setEnabled(mode != menuItem.getItemId());
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        viewModel.setMode(item.getItemId());

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
    public void onClick(MovieInfo movieInfo) {
        final Intent intent = new Intent(this, DetailsActivity.class);

        final Bundle bundle = DetailsActivity.buildBundle(movieInfo);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public Single<String> getPosterUrlOfMovieWithWidth(MovieInfo movieInfo, int width) {
        return viewModel.getPosterUrlOfMovieWithWidth(movieInfo, width);
    }
}
