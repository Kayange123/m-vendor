package dev.kayange.Multivendor.E.commerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@SuperBuilder
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 1)
    private Long id;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @CreatedBy
    private Long createdBy;
    private Long updatedBy;
    @Column(name = "is_deleted")
    private boolean isDeleted;
}
