package me.silvana23.todo.entities;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "notes",
        indexes = {
                @Index(name = "idx_notes_owner", columnList = "owner_id")
        }
)
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Note extends PanacheEntityBase implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false, updatable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "finished", nullable = false)
    private Boolean finished = false;

    @ManyToOne
    @JoinColumn(
            name = "owner_id",
            referencedColumnName = "id",
            columnDefinition = "UUID",
            foreignKey = @ForeignKey(name = "fk_notes_owner"),
            updatable = false,
            nullable = false
    )
    private User owner;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private Instant lastUpdatedOn;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdOn;

    public static Uni<List<Note>> findAllByOwnerPagedAndSorted(UUID ownerId, Page page, Sort sort) {
        return find("owner.id", sort, ownerId)
                .page(page)
                .list();
    }

    public static Uni<Note> findByIdAndUserId(UUID userId, UUID id) {
        return find("from Note where id = ?1 and owner.id = ?2", id, userId)
                .firstResult();
    }
}
