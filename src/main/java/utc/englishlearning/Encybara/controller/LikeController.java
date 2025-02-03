package utc.englishlearning.Encybara.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utc.englishlearning.Encybara.service.LikeService;
import utc.englishlearning.Encybara.domain.response.RestResponse;

@RestController
@RequestMapping("/api/v1/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping
    public ResponseEntity<RestResponse<Void>> likeReview(@RequestParam Long userId, @RequestParam Long reviewId) {
        likeService.likeReview(userId, reviewId);
        RestResponse<Void> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Review liked successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<RestResponse<Void>> unlikeReview(@RequestParam Long userId, @RequestParam Long reviewId) {
        likeService.unlikeReview(userId, reviewId);
        RestResponse<Void> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Review unliked successfully");
        return ResponseEntity.ok(response);
    }
}