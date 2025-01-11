package utc.englishlearning.Encybara.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "history_admins")
@Getter
@Setter
public class History_Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String historyContent;
    private Instant historyTime;
    private String historyNote;

    @OneToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;
}
