package pt.rikmartins.adn.popularmoviesstage2.domain;

import androidx.lifecycle.LiveData;

public interface WatchCase<Request, Response> {
     LiveData<Response> expose(Request request);
}
