package utc.englishlearning.Encybara.domain.response.notification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResNotificationDTO {
    private long id;
    private String message;
    private boolean isRead;
    private Long userId;
    private String createdAt;
}
