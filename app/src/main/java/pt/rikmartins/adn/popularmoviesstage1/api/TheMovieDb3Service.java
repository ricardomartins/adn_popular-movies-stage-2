package pt.rikmartins.adn.popularmoviesstage1.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import pt.rikmartins.adn.popularmoviesstage1.api.model.MoviePage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class TheMovieDb3Service {
    private static final String TAG = TheMovieDb3Service.class.getSimpleName();

    private final TheMovieDatabaseApi3 theMovieDatabaseApi3;
    private final String theMovieDbApiV3Key;

    public TheMovieDb3Service(TheMovieDatabaseApi3 theMovieDatabaseApi3, String theMovieDbApiV3Key) {
        this.theMovieDatabaseApi3 = theMovieDatabaseApi3;
        this.theMovieDbApiV3Key = theMovieDbApiV3Key;
    }

    public MutableLiveData<MoviePage>  getPopularMovies(Integer page) {
        final MutableLiveData<MoviePage> pageMutableLiveData = new MutableLiveData<>();

        theMovieDatabaseApi3.getPopularMovies(theMovieDbApiV3Key, null, page, null)
                .enqueue(new PageCallback(pageMutableLiveData));

        return pageMutableLiveData;
    }

    public MutableLiveData<MoviePage>  getTopRatedMovies(Integer page) {
        final MutableLiveData<MoviePage> pageMutableLiveData = new MutableLiveData<>();

        theMovieDatabaseApi3.getTopRatedMovies(theMovieDbApiV3Key, null, page, null)
                .enqueue(new PageCallback(pageMutableLiveData));

        return pageMutableLiveData;
    }

    private class PageCallback implements Callback<MoviePage> {
        private final MutableLiveData<MoviePage> pageMutableLiveData;

        PageCallback(MutableLiveData<MoviePage> pageMutableLiveData) {
            this.pageMutableLiveData = pageMutableLiveData;
        }

        @Override
        @EverythingIsNonNull
        public void onResponse(Call<MoviePage> call, Response<MoviePage> response) {
            if (response.isSuccessful()) pageMutableLiveData.setValue(response.body());
            else pageMutableLiveData.setValue(null);
        }

        @Override
        @EverythingIsNonNull
        public void onFailure(Call<MoviePage> call, Throwable t) {
            pageMutableLiveData.setValue(null);
            Log.w(TAG, t.getMessage());
        }

    }
}
