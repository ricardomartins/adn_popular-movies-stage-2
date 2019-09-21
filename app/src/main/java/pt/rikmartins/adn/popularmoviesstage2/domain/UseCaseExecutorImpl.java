package pt.rikmartins.adn.popularmoviesstage2.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;

public class UseCaseExecutorImpl implements UseCaseExecutor {

    @Override
    public <RequestDto, ResponseDto, Request, Response> ResponseDto execute(
            @NonNull UseCase<Request, Response> useCase,
            @Nullable RequestDto requestDto,
            @NonNull Function<RequestDto, Request> requestConverter,
            @NonNull Function<Response, ResponseDto> responseConverter) {
        final Request request = requestConverter.apply(requestDto);
        final Response response = useCase.execute(request);
        return responseConverter.apply(response);
    }

    @Override
    public final <Request, Response> Response execute(
            @NonNull UseCase<Request, Response> useCase,
            @Nullable Request request) {
        return execute(useCase, request, Utils.<Request>forward(), Utils.<Response>forward());
    }

    @Override
    public final <RequestDto, Request> void execute(
            @NonNull UseCase<Request, Void> useCase,
            @Nullable RequestDto requestDto,
            @NonNull Function<RequestDto, Request> requestConverter) {
        execute(useCase, requestDto, requestConverter, Utils.<Void>forward());
    }

    @Override
    public final <Response> Response execute(
            @NonNull UseCase<Void, Response> useCase) {
        return execute(useCase, null, Utils.<Void>forward(), Utils.<Response>forward());
    }
}
