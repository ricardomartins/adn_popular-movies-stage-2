package pt.rikmartins.adn.popularmoviesstage2.themoviedb3api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.Review;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.Video;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3.service.TheMovieDb3Service;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3.model.ImagesConfiguration;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3api.model.Configuration;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3api.model.MovieListItem;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3api.model.MoviePage;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3api.model.ReviewPage;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3api.model.VideoImpl;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3api.model.VideoResults;
import retrofit2.Response;

@Singleton
public class TheMovieDb3ServiceImpl implements TheMovieDb3Service {

    final static int PAGE_SIZE = 20; // Size of all full pages from the Api
    final static int FIRST_PAGE = 1; // Initial page of all paged content in the Api

    private final TheMovieDatabaseApi3 theMovieDatabaseApi3;

    private MutableLiveData<Boolean> workStatus = new MutableLiveData<>(false);

    @Inject
    public TheMovieDb3ServiceImpl(TheMovieDatabaseApi3 theMovieDatabaseApi3) {
        this.theMovieDatabaseApi3 = theMovieDatabaseApi3;
    }

    @Override
    public LiveData<PagedList<MovieInfo>> getPopularMovies() {
        return new LivePagedListBuilder<>(new PopularMovieDataSourceFactory(), PAGE_SIZE).build();
    }

    @Override
    public LiveData<PagedList<MovieInfo>> getTopRatedMovies() {
        return new LivePagedListBuilder<>(new TopRatedMovieDataSourceFactory(), PAGE_SIZE).build();
    }

    @Override
    @Nullable
    public MovieInfo getMovie(int movieId) {
        try {
            final Response<MovieListItem> response = theMovieDatabaseApi3.getMovie(movieId).execute();
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public LiveData<PagedList<Review>> getMovieReviews(int movieId) {
        return new LivePagedListBuilder<>(new MovieReviewsDataSourceFactory(movieId), PAGE_SIZE).build();
    }

    @Override
    public List<Video> getMovieVideos(int movieId) {
        try {
            final Response<VideoResults> response = theMovieDatabaseApi3.getMovieVideos(movieId).execute();
            final VideoResults videoResults = response.body();
            if (videoResults != null) {
                final List<VideoImpl> videos = videoResults.getResults();
                if (videos != null) return new ArrayList<Video>(videos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ImagesConfiguration getImagesConfiguration() {
        try {
            final Response<Configuration> response = theMovieDatabaseApi3.getConfiguration().execute();
            final Configuration configuration = response.body();
            if (configuration != null) return configuration.getImages();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public LiveData<Boolean> getWorkStatus() {
        return workStatus;
    }

    private class PopularMovieDataSourceFactory extends DataSource.Factory<Integer, MovieInfo> {
        @NonNull
        @Override
        public PagedDataSource<MovieInfo, MoviePage> create() {
            return new PagedDataSource<MovieInfo, MoviePage>() {
                @Override
                MoviePage request(int page) {
                    workStatus.postValue(true);
                    try {
                        final Response<MoviePage> response = theMovieDatabaseApi3.getPopularMovies(page).execute();
                        return response.body();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        workStatus.postValue(false);
                    }
                    return null;
                }
            };
        }
    }

    private class TopRatedMovieDataSourceFactory extends DataSource.Factory<Integer, MovieInfo> {
        @NonNull
        @Override
        public PagedDataSource<MovieInfo, MoviePage> create() {
            return new PagedDataSource<MovieInfo, MoviePage>() {
                @Override
                MoviePage request(int page) {
                    workStatus.postValue(true);
                    try {
                        final Response<MoviePage> response = theMovieDatabaseApi3.getTopRatedMovies(page).execute();
                        return response.body();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        workStatus.postValue(false);
                    }
                    return null;
                }
            };
        }
    }

    private class MovieReviewsDataSourceFactory extends DataSource.Factory<Integer, Review> {

        private int movieId;

        private MovieReviewsDataSourceFactory(int movieId) {
            this.movieId = movieId;
        }

        @NonNull
        @Override
        public PagedDataSource<Review, ReviewPage> create() {
            return new PagedDataSource<Review, ReviewPage>() {
                @Override
                ReviewPage request(int page) {
                    workStatus.postValue(true);
                    try {
                        final Response<ReviewPage> response = theMovieDatabaseApi3.getMovieReviews(movieId, page).execute();
                        return response.body();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        workStatus.postValue(false);
                    }
                    return null;
                }
            };
        }
    }
}
