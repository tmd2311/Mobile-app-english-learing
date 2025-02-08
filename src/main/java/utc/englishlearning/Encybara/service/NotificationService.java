package utc.englishlearning.Encybara.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utc.englishlearning.Encybara.domain.Notification;
import utc.englishlearning.Encybara.domain.request.notification.ReqNotificationDTO;
import utc.englishlearning.Encybara.repository.NotificationRepository;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public Notification createNotification(ReqNotificationDTO requestDTO) {
        Notification notification = new Notification();
        notification.setMessage(requestDTO.getMessage());
        notification.setUserId(requestDTO.getUserId());
        notification.setRead(false); // Mặc định là chưa đọc
        notification.setCreatedAt(String.valueOf(System.currentTimeMillis())); // Thời gian tạo
        return notificationRepository.save(notification);
    }

    public List<Notification> getAllNotificationsByUserId(Long userId) {
        return notificationRepository.findAll().stream()
                .filter(notification -> notification.getUserId().equals(userId))
                .toList();
    }
}