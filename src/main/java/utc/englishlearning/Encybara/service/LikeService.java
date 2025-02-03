package utc.englishlearning.Encybara.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utc.englishlearning.Encybara.domain.Like;
import utc.englishlearning.Encybara.domain.Review;
import utc.englishlearning.Encybara.exception.ResourceNotFoundException;
import utc.englishlearning.Encybara.repository.LikeRepository;
import utc.englishlearning.Encybara.repository.ReviewRepository;
import utc.englishlearning.Encybara.repository.UserRepository;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void likeReview(Long userId, Long reviewId) {
        if (likeRepository.existsByUserIdAndReviewId(userId, reviewId)) {
            throw new RuntimeException("User has already liked this review.");
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        Like like = new Like();
        like.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));
        like.setReview(review);
        likeRepository.save(like);

        review.setNumLike(review.getNumLike() + 1);
        reviewRepository.save(review);
    }

    @Transactional
    public void unlikeReview(Long userId, Long reviewId) {
        if (!likeRepository.existsByUserIdAndReviewId(userId, reviewId)) {
            throw new RuntimeException("User has not liked this review.");
        }

        likeRepository.deleteByUserIdAndReviewId(userId, reviewId);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        review.setNumLike(review.getNumLike() - 1);
        reviewRepository.save(review);
    }
}