package pt.rikmartins.adn.popularmoviesstage2.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;

import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;
import pt.rikmartins.adn.popularmoviesstage2.domain.AsyncUseCaseExecutor;
import pt.rikmartins.adn.popularmoviesstage2.domain.AsyncUseCaseExecutorImpl;
import pt.rikmartins.adn.popularmoviesstage2.domain.UseCase;
import pt.rikmartins.adn.popularmoviesstage2.domain.UseCaseExecutor;
import pt.rikmartins.adn.popularmoviesstage2.domain.UseCaseExecutorImpl;
import pt.rikmartins.adn.popularmoviesstage2.domain.WatchCaseExposer;
import pt.rikmartins.adn.popularmoviesstage2.domain.WatchCaseExposerImpl;

@Module
public abstract class UiModule {
    @Provides
    @Reusable
    public static Picasso providePicasso() {
        return Picasso.get();
    }

    @Provides
    @Reusable
    public static UseCaseExecutor provideUseCaseExecutor() {
        return new UseCaseExecutorImpl();
    }

    @Provides
    @Reusable
    public static AsyncUseCaseExecutor provideAsyncUseCaseExecutor(UseCaseExecutor useCaseExecutor) {
        return new AsyncUseCaseExecutorImpl(useCaseExecutor) {
            @Override
            public <RequestDto, ResponseDto, Request, Response> Maybe<ResponseDto> executeAsync(
                    @NonNull UseCase<Request, Response> useCase,
                    @Nullable RequestDto requestDto,
                    @NonNull Function<RequestDto, Request> requestConverter,
                    @NonNull Function<Response, ResponseDto> responseConverter) {
                return super.executeAsync(useCase, requestDto, requestConverter, responseConverter)
                        .subscribeOn(Schedulers.io());
            }
        };
    }

    @Provides
    @Reusable
    public static WatchCaseExposer provideWatchCaseExposer() {
        return new WatchCaseExposerImpl();
    }
}
