package pt.rikmartins.adn.popularmoviesstage2.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;

public interface UseCaseExecutor {
    <RequestDto, ResponseDto, Request, Response> ResponseDto execute(
            @NonNull UseCase<Request, Response> useCase,
            @Nullable RequestDto requestDto,
            @NonNull Function<RequestDto, Request> requestConverter,
            @NonNull Function<Response, ResponseDto> responseConverter);

    <Request, Response> Response execute(
            @NonNull UseCase<Request, Response> useCase,
            @Nullable Request request);

    <RequestDto, Request> void execute(
            @NonNull UseCase<Request, Void> useCase,
            @Nullable RequestDto requestDto,
            @NonNull Function<RequestDto, Request> requestConverter);

    <Response> Response execute(
            @NonNull UseCase<Void, Response> useCase);
}
