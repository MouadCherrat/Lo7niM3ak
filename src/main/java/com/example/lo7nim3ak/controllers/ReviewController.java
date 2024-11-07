package com.example.lo7nim3ak.controllers;

import com.example.lo7nim3ak.entities.Reservation;
import com.example.lo7nim3ak.entities.Review;
import com.example.lo7nim3ak.services.ReservationService;
import com.example.lo7nim3ak.services.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;


    @PostMapping
    public Review createReview(@RequestBody Review review) {
        return reviewService.saveReview(review);
    }
    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }
    @GetMapping("/user/{userId}")
    public List<Review> getReviewsByUserId(@PathVariable Long userId) {
        return reviewService.getReviewsByUserId(userId);
    }
    @GetMapping("/user/{userId}/average-note")
    public ResponseEntity<Double> getAverageNoteByUserId(@PathVariable Long userId) {
        double averageNote = reviewService.averageNoteByUserId(userId);
        return ResponseEntity.ok(averageNote);
    }
    @DeleteMapping("/deleteReview/{id}")
    public void deleteReview(@PathVariable Long id){
        reviewService.deleteReview(id);
    }

}
