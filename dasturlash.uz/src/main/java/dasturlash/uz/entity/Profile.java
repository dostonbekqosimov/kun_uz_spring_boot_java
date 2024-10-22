package dasturlash.uz.entity;

import dasturlash.uz.enums.Status;
import dasturlash.uz.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String password;
    private Status status;
    private Boolean visible;
    private Role role;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "photo_id")  // Store reference to the image in Attach
    private Attach photo;
}