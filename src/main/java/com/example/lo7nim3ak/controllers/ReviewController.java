package com.example.lo7nim3ak.controllers;

import com.example.lo7nim3ak.dto.ReviewDto;
import com.example.lo7nim3ak.entities.Review;
import com.example.lo7nim3ak.services.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        Review savedReview = reviewService.saveReview(review);
        return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable Long userId) {
        List<Review> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/{userId}/average-note")
    public ResponseEntity<Double> getAverageNoteByUserId(@PathVariable Long userId) {
        double averageNote = reviewService.averageNoteByUserId(userId);
        return ResponseEntity.ok(averageNote);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByUser(@PathVariable Long userId) {
        List<ReviewDto> reviews = reviewService.getReviewsByUser(userId);
        return ResponseEntity.ok(reviews);
    }
}
