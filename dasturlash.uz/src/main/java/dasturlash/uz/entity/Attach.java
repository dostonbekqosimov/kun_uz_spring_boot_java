package dasturlash.uz.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "attaches")
public class Attach {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;  // UUID for file uniqueness

    private String originalName;  // Original name of the uploaded file
    private String path;          // Path where the file is stored (e.g., "/uploads/images/")
    private Long size;            // File size in bytes
    private String extension;     // File extension (e.g., ".jpg", ".png")
    private LocalDateTime createdDate;  // When the file was uploaded

    // Constructors, Getters, Setters
}

