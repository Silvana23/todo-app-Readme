package me.silvana23.todo.services;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import me.silvana23.todo.core.exceptions.AlreadyExistsException;
import me.silvana23.todo.core.security.PasswordUtil;
import me.silvana23.todo.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class UserService {
    private final PasswordUtil passwordUtil;

    @WithTransaction
    public Uni<User> create(User user) {
        user.setPassword(passwordUtil.encode(user.getPassword()));
        user.setNotes(new ArrayList<>());

        return User.findByUsername(user.getUsername())
                .onItem().ifNotNull().failWith(AlreadyExistsException::new)
                .onItem().ifNull().switchTo(user::persistAndFlush);
    }

    @WithSession
    public Uni<List<User>> getAllPaged(Page page) {
        return User.findAll(Sort.by("createdOn").descending())
                .page(page)
                .list();
    }

    @WithSession
    public Uni<User> getById(UUID id) {
        return User.findById(id);
    }

    @WithSession
    public Uni<User> getByUsername(String username) {
        return User.findByUsername(username);
    }

    @WithTransaction
    public Uni<User> update(UUID id, User update) {
        return User.findById(id)
                .onItem()
                .ifNotNull()
                .transform(panacheEntityBase -> (User) panacheEntityBase)
                .call((entity) -> {
                    if (Objects.nonNull(update.getPassword())) {
                        var hashed = passwordUtil.encode(update.getPassword());
                        entity.setPassword(hashed);
                    }

                    if (Objects.nonNull(update.getRole())) {
                        entity.setRole(update.getRole());
                    }

                    return entity.flush().replaceWith(entity);
                });
    }

    @WithTransaction
    public Uni<Void> delete(UUID id) {
        return User.deleteById(id)
                .onItem()
                .ignore()
                .andContinueWithNull();
    }
}
