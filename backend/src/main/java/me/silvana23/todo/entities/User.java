package me.silvana23.todo.entities;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_unq_users_username", columnList = "username", unique = true)
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "unq_users_username", columnNames = {"username"})
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false, updatable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "username", nullable = false, unique = true, updatable = false, length = 32)
    private String username;

    @Column(name = "password", nullable = false, length = 256)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 16)
    private Role role = Role.USER;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "owner", orphanRemoval = true)
    private List<Note> notes;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private Instant lastUpdatedOn;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdOn;

    public static Uni<User> findByUsername(String username) {
        return find("username", username).firstResult();
    }

    public static Uni<Boolean> exists(UUID id) {
        return findById(id)
                .onItem().transform(Objects::nonNull);
    }

    @Getter
    @RequiredArgsConstructor
    public enum Role {
        ADMIN("Admin"), USER("User");

        private final String name;
    }
}
