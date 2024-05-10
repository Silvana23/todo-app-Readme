package me.silvana23.todo.services;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import me.silvana23.todo.entities.Note;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class NoteService {
    @WithTransaction
    public Uni<Note> create(Note note) {
        note.setFinished(false);
        return note.persistAndFlush();
    }

    @WithSession
    public Uni<List<Note>> getAllPagedAndSorted(UUID userId, Page page, Sort sort) {
        return Note.findAllByOwnerPagedAndSorted(userId, page, sort);
    }

    @WithSession
    public Uni<Note> getById(UUID userId, UUID noteId) {
        return Note.findByIdAndUserId(userId, noteId);
    }

    @WithTransaction
    public Uni<Note> update(UUID userId, UUID noteId, Note update) {
        return Note.findByIdAndUserId(userId, noteId)
                .onItem().ifNotNull().call(note -> {
                    if (Objects.nonNull(update.getContent())) {
                        note.setContent(update.getContent());
                    }

                    if (Objects.nonNull(update.getFinished())) {
                        note.setFinished(update.getFinished());
                    }

                    return note.flush().replaceWith(note);
                });
    }

    @WithTransaction
    public Uni<Boolean> delete(UUID userId, UUID noteId) {
        return Note.findByIdAndUserId(userId, noteId)
                .onItem().ifNotNull()
                .transformToUni(note -> {
                    var notes = note.getOwner().getNotes();

                    var deleted = notes.removeIf(n -> n.getId().equals(note.getId()));
                    if (deleted) {
                        note.getOwner().setNotes(notes);

                        return Uni.createFrom().item(true);
                    }

                    return Uni.createFrom().item(false);
                });
    }
}
