package me.silvana23.todo.mappers;

import me.silvana23.todo.entities.User;
import me.silvana23.todo.model.UserModel;
import org.mapstruct.*;

@Mapper(
        componentModel = "cdi",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper extends DefaultMappings {
    @Mappings({
            @Mapping(source = "lastUpdatedOn", target = "lastUpdatedOn"),
            @Mapping(source = "createdOn", target = "createdOn"),
            @Mapping(source = "role", target = "role")
    })
    UserModel toModel(User entity);

    @InheritInverseConfiguration
    User toEntity(UserModel model);
}
