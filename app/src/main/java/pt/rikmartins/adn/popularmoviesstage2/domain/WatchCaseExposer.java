package pt.rikmartins.adn.popularmoviesstage2.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;

public interface WatchCaseExposer {
    <RequestDto, ResponseDto, Request, Response> LiveData<ResponseDto> expose(
            @NonNull WatchCase<Request, Response> watchCase,
            @Nullable RequestDto requestDto,
            @NonNull Function<RequestDto, Request> requestConverter,
            @NonNull Function<Response, ResponseDto> responseConverter);

    <Request, Response> LiveData<Response> expose(
            @NonNull WatchCase<Request, Response> watchCase,
            @Nullable Request request);

    <Response> LiveData<Response> expose(
            @NonNull WatchCase<Void, Response> watchCase);
}
