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

import pt.rikmartins.adn.popularmoviesstage1.Component;
import pt.rikmartins.adn.popularmoviesstage1.R;
import pt.rikmartins.adn.popularmoviesstage1.api.model.MovieListItem;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;

    private TextView tv;

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inject();

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

    private void inject() {
        Component component = ((Component.ComponentProvider) getApplication()).getComponent();
        gson = component.gson();
    }
}
