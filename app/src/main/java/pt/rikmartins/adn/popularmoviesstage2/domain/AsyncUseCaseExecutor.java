package pt.rikmartins.adn.popularmoviesstage2.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface AsyncUseCaseExecutor {
    <RequestDto, ResponseDto, Request, Response> Maybe<ResponseDto> executeAsync(
            @NonNull UseCase<Request, Response> useCase,
            @Nullable RequestDto requestDto,
            @NonNull Function<RequestDto, Request> requestConverter,
            @NonNull Function<Response, ResponseDto> responseConverter);

    <Request, Response> Maybe<Response> executeAsync(
            @NonNull UseCase<Request, Response> useCase,
            @Nullable Request request);

    <RequestDto, Request> Completable executeAsync(
            @NonNull UseCase<Request, Void> useCase,
            @Nullable RequestDto requestDto,
            @NonNull Function<RequestDto, Request> requestConverter);

    <Response> Single<Response> executeAsync(
            @NonNull UseCase<Void, Response> useCase);
}
