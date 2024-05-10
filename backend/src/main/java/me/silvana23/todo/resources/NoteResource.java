package me.silvana23.todo.resources;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import lombok.RequiredArgsConstructor;
import me.silvana23.todo.core.exceptions.AuthorizationException;
import me.silvana23.todo.core.exceptions.NotFoundException;
import me.silvana23.todo.entities.Note;
import me.silvana23.todo.entities.User;
import me.silvana23.todo.mappers.NoteMapper;
import me.silvana23.todo.model.NoteModel;
import me.silvana23.todo.services.NoteService;
import org.eclipse.microprofile.jwt.Claim;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.Optional;
import java.util.UUID;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/notes")
@Produces({APPLICATION_JSON})
@Consumes(APPLICATION_JSON)
@RequestScoped
@RequiredArgsConstructor
public class NoteResource {
    private final NoteMapper noteMapper;
    private final NoteService noteService;

    @Claim("userId")
    String userId;

    @POST
    @RolesAllowed({"Admin", "User"})
    public Uni<NoteModel> create(@Valid NoteModel model) {
        UUID userId = UUID.fromString(this.userId);

        var entity = noteMapper.toEntity(model);
        entity.setOwner(User.builder().id(userId).build());

        return noteService.create(entity)
                .map(noteMapper::toModel);
    }

    @GET
    @RolesAllowed({"Admin", "User"})
    public Multi<NoteModel> getAllPagedAndSorted(
            @RestQuery @DefaultValue("0") Integer pageIndex,
            @RestQuery @DefaultValue("10") Integer pageSize,
            @RestQuery @DefaultValue("createdOn") String sortBy,
            @RestQuery @DefaultValue("Descending") Sort.Direction sortDirection
    ) {
        Sort sort = Sort.by(sortBy).direction(sortDirection);
        Page page = Page.of(pageIndex, pageSize);
        UUID userId = UUID.fromString(this.userId);

        return noteService.getAllPagedAndSorted(userId, page, sort)
                .onItem().disjoint()
                .onItem().castTo(Note.class)
                .map(noteMapper::toModel);
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Admin", "User"})
    public Uni<NoteModel> getById(
            @Context SecurityContext securityContext,
            @RestPath UUID id,
            @RestQuery("userId") Optional<UUID> otherUserId
    ) {
        UUID userId = UUID.fromString(this.userId);

        if (otherUserId.isPresent() && (!otherUserId.get().equals(userId) || !securityContext.isUserInRole("Admin"))) {
            return Uni.createFrom().failure(AuthorizationException::new);
        }

        return noteService.getById(otherUserId.orElse(userId), id)
                .onItem().ifNull().failWith(NotFoundException::new)
                .map(noteMapper::toModel);
    }

    @PATCH
    @Path("/{id}")
    @RolesAllowed({"Admin", "User"})
    public Uni<NoteModel> updateById(
            @Context SecurityContext securityContext,
            @RestPath UUID id,
            @RestQuery("userId") Optional<UUID> otherUserId,
            NoteModel update
    ) {
        UUID userId = UUID.fromString(this.userId);

        if (otherUserId.isPresent() && (!otherUserId.get().equals(userId) || !securityContext.isUserInRole("Admin"))) {
            return Uni.createFrom().failure(AuthorizationException::new);
        }

        return noteService.update(otherUserId.orElse(userId), id, noteMapper.toEntity(update))
                .onItem().ifNull().failWith(NotFoundException::new)
                .map(noteMapper::toModel);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"Admin", "User"})
    public Uni<Void> deleteById(
            @Context SecurityContext securityContext,
            @RestPath UUID id,
            @RestQuery("userId") Optional<UUID> otherUserId
    ) {
        UUID userId = UUID.fromString(this.userId);

        if (otherUserId.isPresent() && (!otherUserId.get().equals(userId) || !securityContext.isUserInRole("Admin"))) {
            return Uni.createFrom().failure(AuthorizationException::new);
        }

        return noteService.delete(userId, id)
                .onItem().ifNull()
                .failWith(NotFoundException::new)
                .onItem().ifNotNull()
                .transformToUni(aBoolean -> Uni.createFrom().voidItem());
    }
}
