package pt.rikmartins.adn.popularmoviesstage2.domain.usecase.poster;

import androidx.lifecycle.LiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

import pt.rikmartins.adn.popularmoviesstage2.domain.gateway.PosterRepository;
import pt.rikmartins.adn.popularmoviesstage2.domain.WatchCase;

@Singleton
public class WatchPosterUrlsAvailability implements WatchCase<Void, Boolean> {

    private final PosterRepository posterRepository;

    @Inject
    public WatchPosterUrlsAvailability(PosterRepository posterRepository) {
        this.posterRepository = posterRepository;
    }

    @Override
    public LiveData<Boolean> expose(Void aVoid) {
        return posterRepository.getPosterUrlReadyStatus();
    }
}
