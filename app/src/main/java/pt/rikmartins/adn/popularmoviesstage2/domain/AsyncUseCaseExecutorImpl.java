package pt.rikmartins.adn.popularmoviesstage2.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Single;

public class AsyncUseCaseExecutorImpl implements AsyncUseCaseExecutor {

    private final UseCaseExecutor useCaseExecutor;

    public AsyncUseCaseExecutorImpl(UseCaseExecutor useCaseExecutor) {
        this.useCaseExecutor = useCaseExecutor;
    }

    @Override
    public <RequestDto, ResponseDto, Request, Response> Maybe<ResponseDto> executeAsync(
            @NonNull final UseCase<Request, Response> useCase,
            @Nullable RequestDto requestDto,
            @NonNull Function<RequestDto, Request> requestConverter,
            @NonNull Function<Response, ResponseDto> responseConverter) {
        final Request request = requestConverter.apply(requestDto);

        final Maybe<Response> responseSingle = Maybe.create(new MaybeOnSubscribe<Response>() {
            @Override
            public void subscribe(MaybeEmitter<Response> emitter) throws Exception {
                try {
                    final Response response = AsyncUseCaseExecutorImpl.this.getUseCaseExecutor().execute(useCase, request);
                    if (response != null) emitter.onSuccess(response);
                    else emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });

        return responseSingle.map(Utils.toXFunction(responseConverter));
    }

    @Override
    public final <Request, Response> Maybe<Response> executeAsync(
            @NonNull final UseCase<Request, Response> useCase,
            @Nullable final Request request) {
        return executeAsync(useCase, request, Utils.<Request>forward(), Utils.<Response>forward());
    }

    @Override
    public final <RequestDto, Request> Completable executeAsync(
            @NonNull UseCase<Request, Void> useCase,
            @Nullable RequestDto requestDto,
            @NonNull Function<RequestDto, Request> requestConverter) {
        return executeAsync(useCase, requestDto, requestConverter, Utils.<Void>forward()).ignoreElement();
    }

    @Override
    public final <Response> Single<Response> executeAsync(
            @NonNull UseCase<Void, Response> useCase) {
        return executeAsync(useCase, null, Utils.<Void>forward(), Utils.<Response>forward()).toSingle();
    }

    private UseCaseExecutor getUseCaseExecutor() {
        return useCaseExecutor;
    }
}
