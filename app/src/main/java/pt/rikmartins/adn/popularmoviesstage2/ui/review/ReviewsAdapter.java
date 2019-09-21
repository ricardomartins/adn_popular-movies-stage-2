package pt.rikmartins.adn.popularmoviesstage2.ui.review;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import pt.rikmartins.adn.popularmoviesstage2.databinding.ReviewsReviewItemBinding;
import pt.rikmartins.adn.popularmoviesstage2.domain.model.Review;

class ReviewsAdapter extends PagedListAdapter<Review, ReviewsAdapter.ReviewViewHolder> {

    @Inject
    ReviewsAdapter() {
        super(new DiffUtil.ItemCallback<Review>() {
            @Override
            public boolean areItemsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
                if (oldItem.getId() != null) return oldItem.getId().equals(newItem.getId());
                return newItem.getId() == null;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
                return areItemsTheSame(oldItem, newItem); // It will have to do for now
            }
        });
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ReviewsReviewItemBinding binding = ReviewsReviewItemBinding.inflate(inflater, parent, false);
        return new ReviewViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        final Review review = getItem(position);

        if (review != null) holder.bind(review);
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        private ReviewsReviewItemBinding binding;

        ReviewViewHolder(ReviewsReviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(Review review) {
            binding.setReview(review);
            binding.executePendingBindings();
        }
    }
}
