package me.silvana23.todo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@Value
@With
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoteModel {
    String id;

    @NotNull
    @NotEmpty
    @Size(min = 1)
    String content;

    Boolean finished;

    String ownerId;

    Long lastUpdatedOn;
    Long createdOn;
}
