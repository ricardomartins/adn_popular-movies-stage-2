package pt.rikmartins.adn.popularmoviesstage2.themoviedb3api;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import java.util.ArrayList;
import java.util.List;

import pt.rikmartins.adn.popularmoviesstage2.themoviedb3api.model.Page;

abstract class PagedDataSource<T, P extends Page<? extends T>> extends PositionalDataSource<T> {

    @Override
    public void loadInitial(@NonNull final LoadInitialParams params,
                            @NonNull final LoadInitialCallback<T> callback) {
        final P page = request((params.requestedStartPosition / params.pageSize) + TheMovieDb3ServiceImpl.FIRST_PAGE);

        if (page != null) {
            final List<? extends T> results = page.getResults();
            if (results != null) {
                final int totalResults = page.getTotalResults();
                final int position = computeInitialLoadPosition(params, totalResults);
                callback.onResult(new ArrayList<>(results), position, totalResults);
            }
        }
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, final @NonNull LoadRangeCallback<T> callback) {
        final P page = request((params.startPosition / params.loadSize) + TheMovieDb3ServiceImpl.FIRST_PAGE);

        if (page != null) {
            final List<? extends T> results = page.getResults();
            if (results != null) callback.onResult(new ArrayList<>(results));
        }
    }

    abstract P request(int page);
}
