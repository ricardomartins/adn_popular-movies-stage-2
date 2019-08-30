package pt.rikmartins.adn.popularmoviesstage1.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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

import pt.rikmartins.adn.popularmoviesstage1.R;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MovieListItem;
import pt.rikmartins.adn.popularmoviesstage1.data.Repository;
import pt.rikmartins.adn.popularmoviesstage1.data.SharedPreferencesUtils;
import pt.rikmartins.adn.popularmoviesstage1.ui.detail.DetailsActivity;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView placeholderTextView;
    private RecyclerView movieListRecyclerView;
    private ProgressBar progressBar;

    private int columnQuant;

    private String desiredPosterSize;

    private MovieAdapter adapter;

    private MainViewModel viewModel; // TODO: Find a way to inject this through Dagger as well
    // https://medium.com/chili-labs/android-viewmodel-injection-with-dagger-f0061d3402ff
    // is a very complex example, but may be the only way or in the
    // very least it may contain clues to a simpler/better solution

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Injection
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        movieListRecyclerView = findViewById(R.id.rv_movie_list);
        progressBar = findViewById(R.id.pb_movie_list);
        placeholderTextView = findViewById(R.id.tv_placeholder);

        columnQuant = getResources().getInteger(R.integer.column_quantity);

        adapter = new MovieAdapter(this);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, columnQuant);

        movieListRecyclerView.setAdapter(adapter);
        movieListRecyclerView.setLayoutManager(layoutManager);

        viewModel.getMovieListLiveData().observe(this, new Observer<PagedList<MovieListItem>>() {
            @Override
            public void onChanged(PagedList<MovieListItem> movieListItems) {
                adapter.submitList(movieListItems);
            }
        });

        viewModel.getWorkStatusLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean workStatus) {
                if (workStatus) showProgress();
                else hideProgress();
            }
        });

        sharedPreferencesUtils.getSharedPreferences().registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                switch (key) {
                    case SharedPreferencesUtils.SP_CONFIGURATION_IMAGES_BASE_URL_KEY:
                    case SharedPreferencesUtils.SP_CONFIGURATION_IMAGES_SECURE_BASE_URL_KEY:
                    case SharedPreferencesUtils.SP_CONFIGURATION_IMAGES_POSTER_SIZES_KEY:
                        try {
                            adapter.setImagesUrl(viewModel.getImagesUrl(desiredPosterSize));
                        } catch (Repository.RequirementsMissingException rme) {
                            viewModel.updateConfiguration();
                        }
                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            setPosterSizeByWidth((int) ((float) movieListRecyclerView.getWidth() / (float) columnQuant / displayMetrics.density));
            try {
                adapter.setImagesUrl(viewModel.getImagesUrl(desiredPosterSize));
            } catch (Repository.RequirementsMissingException rme) {
                viewModel.updateConfiguration();
            }
        }
    }

    private void setPosterSizeByWidth(int width) {
        desiredPosterSize = "w" + width; // TODO: Create a const with the message
    }

    private void setPosterSizeByHeight(int height) {
        desiredPosterSize = "h" + height; // TODO: Create a const with the message
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
        switch (item.getItemId()) {
            case R.id.order_by_popular:
                viewModel.switchMode(Repository.POPULAR_MODE);
                return true;
            case R.id.order_by_rate:
                viewModel.switchMode(Repository.TOP_RATED_MODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

        final Bundle bundle = DetailsActivity.buildBundle(movieListItem.getId());
        intent.putExtras(bundle);

        startActivity(intent);
    }
}
