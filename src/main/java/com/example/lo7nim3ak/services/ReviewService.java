package com.example.lo7nim3ak.services;

import com.example.lo7nim3ak.entities.Review;
import com.example.lo7nim3ak.repository.ReviewRepository;
import com.example.lo7nim3ak.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    public double averageNoteByUserId(Long userId) {
        List<Review> reviews = getReviewsByUserId(userId);
        return reviews.stream()
                .mapToInt(Review::getNote)
                .average()
                .orElse(0.0);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
