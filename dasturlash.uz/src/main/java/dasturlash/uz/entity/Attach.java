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
    private String id;

    @Column(name = "path")
    private String path;

    @Column(name = "extension")
    private String extension;

    @Column(name = "origen_name")
    private String origenName;
    @Column(name = "size")
    private Long size;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "visible")
    private Boolean visible = true;


}


