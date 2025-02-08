package utc.englishlearning.Encybara.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utc.englishlearning.Encybara.service.NotificationService;
import utc.englishlearning.Encybara.domain.Notification;
import utc.englishlearning.Encybara.domain.request.notification.ReqNotificationDTO;
import utc.englishlearning.Encybara.domain.response.RestResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<RestResponse<Notification>> createNotification(
            @RequestBody ReqNotificationDTO requestDTO) {
        Notification notification = notificationService.createNotification(requestDTO);
        RestResponse<Notification> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Notification created successfully");
        response.setData(notification);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<RestResponse<List<Notification>>> getAllNotificationsByUserId(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getAllNotificationsByUserId(userId);
        RestResponse<List<Notification>> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Notifications retrieved successfully");
        response.setData(notifications);
        return ResponseEntity.ok(response);
    }
}