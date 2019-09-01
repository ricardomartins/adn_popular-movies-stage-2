package pt.rikmartins.adn.popularmoviesstage1.data;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

public class ImageUrlGenerator {
    private static final String TAG = ImageUrlGenerator.class.getSimpleName();

    private final Uri baseUrl;
    private final List<String> imagesPosterSizes;

    private String desiredPosterSize;
    private String posterSize ;
    private Uri imagesUrl;

    ImageUrlGenerator(@NonNull String baseUrl, @NonNull List<String> imagesPosterSizes) {
        this.baseUrl = Uri.parse(baseUrl);
        this.imagesPosterSizes = imagesPosterSizes;

        this.setPosterSize(desiredPosterSize);
    }

    public Uri getImageUrl(String imagePath) {
        return imagesUrl.buildUpon().appendEncodedPath(imagePath).build();
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getDesiredPosterSize() {
        return desiredPosterSize;
    }

    /**
     *
     * @param width is the width in pixels that should be used to set the poster size
     * @return the posterSize
     */
    public void setPosterSizeByWidth(int width) {
        setPosterSize("w" + width);
    }

    /**
     *
     * @param height is the height in pixels that should be used to set the poster size
     */
    public void setPosterSizeByHeight(int height) {
        setPosterSize("h" + height);
    }

    private void setPosterSize(String desiredPosterSize) {
        this.desiredPosterSize = desiredPosterSize;
        this.posterSize = calculatePosterSize(desiredPosterSize, imagesPosterSizes);
        this.imagesUrl = baseUrl.buildUpon().appendPath(posterSize).build();
    }

    private static String calculatePosterSize(final String desiredPosterSize, final List<String> imagesPosterSizes) {
        if (desiredPosterSize == null) {
            Log.w(TAG, "No poster size selected, building URL with worst quality"); // TODO: Create a const with the message
            return imagesPosterSizes.get(0);
        }

        String prefix = desiredPosterSize.substring(0, 1);
        int size = Integer.parseInt(desiredPosterSize.substring(1));

        String posterSize = null;

        boolean prefixFound = false;
        boolean adequateSizeFound = false;
        for (String ips : imagesPosterSizes) {
            posterSize = ips;
            if (ips.startsWith(prefix)) {
                prefixFound = true;
                if (size <= Integer.parseInt(ips.substring(1))) {
                    adequateSizeFound = true;
                    break;
                }
            }
        }

        if (!prefixFound)
            Log.w(TAG, "No image size with width as dimension was found, defaulting to \"" + posterSize + "\"."); // TODO: Create a const with the message
        else if (!adequateSizeFound)
            Log.i(TAG, "No image size with appropriate size was found, defaulting to \"" + posterSize + "\"."); // TODO: Create a const with the message

        return posterSize;
    }
}
