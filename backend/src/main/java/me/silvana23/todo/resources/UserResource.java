package me.silvana23.todo.resources;

import io.quarkus.panache.common.Page;
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
import me.silvana23.todo.entities.User;
import me.silvana23.todo.mappers.UserMapper;
import me.silvana23.todo.model.UserModel;
import me.silvana23.todo.services.UserService;
import org.eclipse.microprofile.jwt.Claim;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestPath;

import java.util.UUID;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/users")
@Produces({APPLICATION_JSON})
@Consumes(APPLICATION_JSON)
@RequestScoped
@RequiredArgsConstructor
public class UserResource {
    private final Logger logger;

    private final UserService userService;
    private final UserMapper userMapper;

    @Claim("userId")
    String userId;

    @POST
    @RolesAllowed("Admin")
    public Uni<UserModel> create(@Valid UserModel requestModel) {
        var requestEntity = userMapper.toEntity(requestModel);

        return userService.create(requestEntity)
                .onItem().ifNotNull().transform(userMapper::toModel);
    }

    @GET
    @RolesAllowed("Admin")
    public Multi<UserModel> getAllPaged(
            @QueryParam(value = "page") @DefaultValue("0") Integer pageIndex,
            @QueryParam(value = "pageSize") @DefaultValue("16") Integer pageSize
    ) {
        Page page = Page.of(pageIndex, pageSize);
        return userService.getAllPaged(page)
                .onFailure().call(throwable -> {
                    logger.error(throwable.getMessage(), throwable);
                    return Uni.createFrom().failure(new InternalServerErrorException(throwable));
                })
                .onItem().disjoint()
                .onItem().castTo(User.class)
                .map(entity -> userMapper.toModel(entity).withPassword(""));
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Admin", "User"})
    public Uni<UserModel> getById(@Context SecurityContext securityContext, @RestPath UUID id) {
        UUID userId = UUID.fromString(this.userId);

        if (!userId.equals(id) || !securityContext.isUserInRole("Admin")) {
            return Uni.createFrom().failure(new AuthorizationException());
        }

        return userService.getById(id)
                .onFailure().transform(InternalServerErrorException::new)
                .onItem().ifNull().failWith(NotAcceptableException::new)
                .map(entity -> userMapper.toModel(entity).withPassword(""));
    }

    @PATCH
    @Path("/{id}")
    @RolesAllowed({"Admin", "User"})
    public Uni<UserModel> update(@Context SecurityContext securityContext, @RestPath UUID id, UserModel requestModel) {
        UUID userId = UUID.fromString(this.userId);

        if (!userId.equals(id) || !securityContext.isUserInRole("Admin")) {
            return Uni.createFrom().failure(new AuthorizationException());
        }

        var entity = userMapper.toEntity(requestModel);
        return userService.update(id, entity)
                .map(entity1 -> userMapper.toModel(entity1).withPassword(""));
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("Admin")
    public Uni<Void> delete(@RestPath UUID id) {
        return userService.delete(id);
    }
}
