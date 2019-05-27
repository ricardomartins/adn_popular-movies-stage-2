package pt.rikmartins.adn.popularmoviesstage1.ui.main;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import pt.rikmartins.adn.popularmoviesstage1.AppComponent;
import pt.rikmartins.adn.popularmoviesstage1.R;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MovieListItem;
import pt.rikmartins.adn.popularmoviesstage1.data.SharedPreferencesUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    SharedPreferencesUtils sharedPreferencesUtils;

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
        ((AppComponent.ComponentProvider) getApplication()).getComponent().inject(this);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        movieListRecyclerView = findViewById(R.id.rv_movie_list);
        progressBar = findViewById(R.id.pb_movie_list);
        placeholderTextView = findViewById(R.id.tv_placeholder);

        columnQuant = getResources().getInteger(R.integer.column_quantity);

        adapter = new MovieAdapter(this);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, columnQuant);

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

        sharedPreferencesUtils.getSharedPreferences().registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                switch (key) {
                    case SharedPreferencesUtils.SP_CONFIGURATION_IMAGES_BASE_URL_KEY:
                    case SharedPreferencesUtils.SP_CONFIGURATION_IMAGES_SECURE_BASE_URL_KEY:
                    case SharedPreferencesUtils.SP_CONFIGURATION_IMAGES_POSTER_SIZES_KEY:
                        try {
                            adapter.setImagesUrl(getImagesUrl());
                        } catch (RequirementsMissingException rme) {
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
                adapter.setImagesUrl(getImagesUrl());
            } catch (RequirementsMissingException rme) {
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

    private static class RequirementsMissingException extends Exception {}

    private Uri getImagesUrl() throws RequirementsMissingException {
        String baseUrl = sharedPreferencesUtils.getImagesPreferredBaseUrl();
        if (baseUrl == null) throw new RequirementsMissingException();

        String posterSize = getPosterSize(desiredPosterSize);

        return getImagesUrl(Uri.parse(baseUrl), posterSize);
    }

    private Uri getImagesUrl(Uri baseUrl, String posterSize) {
        return baseUrl.buildUpon().appendPath(posterSize).build();
    }

    private String getPosterSize(String desiredPosterSize) throws RequirementsMissingException {
        List<String> imagesPosterSizes = sharedPreferencesUtils.getImagesPosterSizes();
        if (imagesPosterSizes == null) throw new RequirementsMissingException();

        if (desiredPosterSize == null) {
            Log.w(TAG, "No poster size selected, building URL with worst quality"); // TODO: Create a const with the message
            return imagesPosterSizes.get(0);
        }

        String prefix = desiredPosterSize.substring(0, 1);
        int size = Integer.parseInt(desiredPosterSize.substring(1));

        String posterSize = null;

        boolean prefixFound = false;
        boolean adequateSizeFound = false;
        for (String ips : imagesPosterSizes) {
            posterSize = ips;
            if (ips.startsWith(prefix)) {
                prefixFound = true;
                if (size <= Integer.parseInt(ips.substring(1))) {
                    adequateSizeFound = true;
                    break;
                }
            }
        }
        if (!prefixFound)
            Log.w(TAG, "No image size with width as dimension was found, defaulting to \"" + posterSize + "\"."); // TODO: Create a const with the message
        else if (!adequateSizeFound)
            Log.i(TAG, "No image size with appropriate size was found, defaulting to \"" + posterSize + "\"."); // TODO: Create a const with the message

        return posterSize;
    }
}
