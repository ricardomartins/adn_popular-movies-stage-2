package pt.rikmartins.adn.popularmoviesstage2.data;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import javax.inject.Inject;
import javax.inject.Singleton;

import pt.rikmartins.adn.popularmoviesstage2.data.model.Favorite;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3.service.FavoriteService;

@Singleton
class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteDao favoriteDao;

    @Inject
    FavoriteServiceImpl(FavoriteDao favoriteDao) {
        this.favoriteDao = favoriteDao;
    }

    @Override
    public LiveData<PagedList<MovieInfo>> getAll() {
        final DataSource.Factory<Integer, MovieInfo> mappedDataSourceFactory =
                favoriteDao.getAll().map(new Function<Favorite, MovieInfo>() {
                    @Override
                    public MovieInfo apply(Favorite input) {
                        return input;
                    }
                });

        return new LivePagedListBuilder<>(mappedDataSourceFactory, 50).build();
    }

    @Override
    public LiveData<? extends MovieInfo> getFavorite(MovieInfo movieInfo) {
        return favoriteDao.getById(movieInfo.getId());
    }

    @Override
    public void insert(MovieInfo favorite) {
        favoriteDao.insert(toDataModel(favorite));
    }

    @Override
    public void delete(MovieInfo favorite) {
        favoriteDao.delete(toDataModel(favorite));
    }

    private static Favorite toDataModel(MovieInfo movieInfo) {
        return Favorite.create(movieInfo.getId(), movieInfo.getTitle(), movieInfo.getOverview(),
                movieInfo.getPosterPath(), movieInfo.getReleaseDate(), movieInfo.getVoteAverage());
    }
}
