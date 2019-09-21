package pt.rikmartins.adn.popularmoviesstage2.domain;


public interface UseCase<Request, Response> {
    Response execute(Request request);
}

