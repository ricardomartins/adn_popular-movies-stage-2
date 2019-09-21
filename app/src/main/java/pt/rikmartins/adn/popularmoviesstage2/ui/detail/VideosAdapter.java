package pt.rikmartins.adn.popularmoviesstage2.ui.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import dagger.Reusable;
import pt.rikmartins.adn.popularmoviesstage2.R;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.Video;

@Reusable
class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    private final Picasso picasso;

    private VideoAdapterOnClickHandler clickHandler;

    private VideoAdapterImageUrlProvider videoAdapterImageUrlProvider;

    private List<Video> data;

    @Inject
    VideosAdapter(Picasso picasso) {
        this.picasso = picasso;
    }

    void setData(List<Video> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.details_videos_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        final Video video = data.get(position);
        if (video != null) holder.bind(video);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    void setVideoAdapterOnClickHandler(VideoAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    void setVideoAdapterImageUrlProvider(VideoAdapterImageUrlProvider videoAdapterImageUrlProvider) {
        this.videoAdapterImageUrlProvider = videoAdapterImageUrlProvider;
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView videoImg;

        VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            videoImg = itemView.findViewById(R.id.videoImg);
        }

        private void bind(Video video) {
            final String url = videoAdapterImageUrlProvider.getImageUrlOfVideo(video);
            picasso.load(url).error(R.drawable.video_placeholder)
                    .placeholder(R.drawable.video_placeholder).into(videoImg);
        }

        @Override
        public void onClick(View v) {
            final Video video = data.get(getAdapterPosition());
            clickHandler.onClick(video);
        }
    }

    interface VideoAdapterOnClickHandler {
        void onClick(Video video);
    }

    interface VideoAdapterImageUrlProvider {
        String getImageUrlOfVideo(Video video);
    }
}
