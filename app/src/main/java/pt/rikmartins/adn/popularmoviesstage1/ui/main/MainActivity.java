package pt.rikmartins.adn.popularmoviesstage1.ui.main;

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

import javax.inject.Inject;

import pt.rikmartins.adn.popularmoviesstage1.AppComponent;
import pt.rikmartins.adn.popularmoviesstage1.R;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MovieListItem;
import pt.rikmartins.adn.popularmoviesstage1.data.ImageUrlGenerator;
import pt.rikmartins.adn.popularmoviesstage1.data.Repository;
import pt.rikmartins.adn.popularmoviesstage1.ui.detail.DetailsActivity;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private TextView placeholderTextView;
    private RecyclerView movieListRecyclerView;
    private ProgressBar progressBar;

    @Inject MovieAdapter adapter;

    private MainViewModel viewModel;

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

        adapter.setMovieAdapterOnClickHandler(this);
        movieListRecyclerView.setAdapter(adapter);

        final int columnQuant = getResources().getInteger(R.integer.column_quantity);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, columnQuant);
        movieListRecyclerView.setLayoutManager(layoutManager);

        viewModel.getMovieListLiveData().observe(this, new Observer<PagedList<MovieListItem>>() {
            @Override
            public void onChanged(PagedList<MovieListItem> movieListItems) {
                if (movieListItems == null || movieListItems.isEmpty()) showPlaceholder();
                else showList();

                adapter.submitList(movieListItems, new Runnable() {
                    @Override
                    public void run() {
                        movieListRecyclerView.scrollToPosition(0);
                    }
                });
            }
        });

        viewModel.getWorkStatusLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean workStatus) {
                if (workStatus) showProgress();
                else hideProgress();
            }
        });

        viewModel.getImageUrlGenerator().observe(this, new Observer<ImageUrlGenerator>() {
            @Override
            public void onChanged(ImageUrlGenerator imageUrlGenerator) {
                adapter.setImageUrlGenerator(imageUrlGenerator);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem orderByPopular = menu.findItem(R.id.order_by_popular);
        final MenuItem orderByRate = menu.findItem(R.id.order_by_rate);

        viewModel.getMode().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer boxedMode) {
                if (boxedMode != null) {
                    boolean isPopularMode = boxedMode == Repository.POPULAR_MODE;
                    orderByPopular.setVisible(!isPopularMode);
                    orderByRate.setVisible(isPopularMode);
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean modeSwapped = false;

        switch (item.getItemId()) {
            case R.id.order_by_popular:
                viewModel.switchMode(Repository.POPULAR_MODE);
                modeSwapped = true;
                break;
            case R.id.order_by_rate:
                viewModel.switchMode(Repository.TOP_RATED_MODE);
                modeSwapped = true;
                break;
        }

        if (modeSwapped) return true;
        else return super.onOptionsItemSelected(item);
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
        final Intent intent = new Intent(this, DetailsActivity.class);

        final Bundle bundle = DetailsActivity.buildBundle(movieListItem);
        intent.putExtras(bundle);

        startActivity(intent);
    }
}
