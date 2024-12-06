package com.example.lo7nim3ak.services;

import com.example.lo7nim3ak.dto.ReviewDto;
import com.example.lo7nim3ak.entities.Review;
import com.example.lo7nim3ak.repository.ReviewRepository;
import com.example.lo7nim3ak.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<ReviewDto> getReviewsByUser(Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);
        return reviews.stream().map(review -> {
            ReviewDto dto = new ReviewDto();
            dto.setId(review.getId());
            dto.setRating(review.getNote());
            dto.setComment(review.getMessage());
            dto.setUserId(review.getUser().getId());
            dto.setClientName(review.getUser().getFirstName() + " " + review.getUser().getName()); // Include name
            return dto;
        }).collect(Collectors.toList());
    }

}
