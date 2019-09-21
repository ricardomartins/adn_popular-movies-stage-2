package pt.rikmartins.adn.popularmoviesstage2.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

public class WatchCaseExposerImpl implements WatchCaseExposer {

    @Override
    public <RequestDto, ResponseDto, Request, Response> LiveData<ResponseDto> expose(
            @NonNull WatchCase<Request, Response> watchCase,
            @Nullable RequestDto requestDto,
            @NonNull Function<RequestDto, Request> requestConverter,
            @NonNull Function<Response, ResponseDto> responseConverter) {
        final Request request = requestConverter.apply(requestDto);
        final LiveData<Response> response = watchCase.expose(request);
        return Transformations.map(response, responseConverter);
    }

    @Override
    public final <Request, Response> LiveData<Response> expose(
            @NonNull WatchCase<Request, Response> watchCase,
            @Nullable Request request) {
        return expose(watchCase, request, Utils.<Request>forward(), Utils.<Response>forward());
    }

    @Override
    public final <Response> LiveData<Response> expose(
            @NonNull WatchCase<Void, Response> watchCase) {
        return expose(watchCase, null, Utils.<Void>forward(), Utils.<Response>forward());
    }
}
