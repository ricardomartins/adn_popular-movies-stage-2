package pt.rikmartins.adn.popularmoviesstage2.themoviedb3.poster;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import javax.inject.Inject;

import pt.rikmartins.adn.popularmoviesstage2.AppComponent;
import pt.rikmartins.adn.popularmoviesstage2.themoviedb3.model.ImagesConfiguration;

public class PosterConfigurationUpdateWorker extends Worker {

    @Inject
    PosterManager posterManager;

    public PosterConfigurationUpdateWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        ((AppComponent.ComponentProvider) getApplicationContext()).getComponent().inject(this);

        final ImagesConfiguration imagesConfiguration = posterManager.downloadPosterConfiguration();

        if (imagesConfiguration != null) {
            posterManager.setPosterConfiguration(imagesConfiguration);
            return Result.success();
        }
        return Result.retry();
    }
}
