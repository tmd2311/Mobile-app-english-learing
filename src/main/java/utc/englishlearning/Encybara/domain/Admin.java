package utc.englishlearning.Encybara.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "admins")
@Getter
@Setter
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String password;
    private Integer field;
    private String adminUsername;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY)
    private List<Message> messages;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY)
    private List<Role_Permission> rolePermissions;
}