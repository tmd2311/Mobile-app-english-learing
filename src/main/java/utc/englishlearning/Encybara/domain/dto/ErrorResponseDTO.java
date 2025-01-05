package utc.englishlearning.Encybara.domain.dto;

import lombok.Getter;
import lombok.Setter;

public class ErrorResponseDTO {
    @Getter
    @Setter
    private String message;

    // Constructor
    public ErrorResponseDTO(String message) {
        this.message = message;
    }
}
