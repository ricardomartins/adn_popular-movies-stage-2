package pt.rikmartins.adn.popularmoviesstage2.domain.gateway;

import androidx.lifecycle.LiveData;

public interface Repository {
    LiveData<Boolean> getWorkStatus();
}
