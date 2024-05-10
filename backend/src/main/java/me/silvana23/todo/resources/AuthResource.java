package me.silvana23.todo.resources;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import me.silvana23.todo.entities.User;
import me.silvana23.todo.mappers.UserMapper;
import me.silvana23.todo.model.UserModel;
import me.silvana23.todo.services.AuthService;
import org.jboss.logging.Logger;

import java.util.Map;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/auth")
@Produces({APPLICATION_JSON})
@Consumes(APPLICATION_JSON)
@RequestScoped
@RequiredArgsConstructor
public class AuthResource {
    private final Logger logger;

    private final AuthService authService;
    private final UserMapper userMapper;

    @POST
    @Path("/signup")
    @PermitAll
    public Uni<Response> signUp(@Valid UserModel userModel) {
        var userEntity = userMapper.toEntity(userModel);
        userEntity.setRole(User.Role.USER);

        return authService.signUp(userEntity)
                .onItem()
                .ifNotNull()
                .transform(uuid -> Response.status(Response.Status.CREATED)
                        .entity(Map.of("id", uuid))
                        .build()
                );
    }

    @POST
    @Path("/signin")
    @PermitAll
    public Uni<Response> signIn(@Valid UserModel userModel) {
        var userEntity = userMapper.toEntity(userModel);

        return authService.signIn(userEntity)
                .onItem().ifNotNull()
                .transform(token -> Response.status(Response.Status.OK)
                        .header("x-auth-token", token)
                        .build()
                );
    }
}
