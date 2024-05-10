package me.silvana23.todo.mappers;

import me.silvana23.todo.entities.Note;
import me.silvana23.todo.model.NoteModel;
import org.mapstruct.*;

@Mapper(
        componentModel = "cdi",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface NoteMapper extends DefaultMappings {
    @Mappings({
            @Mapping(source = "lastUpdatedOn", target = "lastUpdatedOn"),
            @Mapping(source = "createdOn", target = "createdOn"),
            @Mapping(source = "owner.id", target = "ownerId")
    })
    NoteModel toModel(Note entity);

    @InheritInverseConfiguration
    Note toEntity(NoteModel model);
}
