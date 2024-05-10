package me.silvana23.todo.mappers;

import java.time.Instant;
import java.util.Objects;

public interface DefaultMappings {
    default Long instantToLong(Instant instant) {
        return Objects.requireNonNullElse(instant, Instant.ofEpochMilli(0)).toEpochMilli();
    }

    default Instant longToInstant(Long timestamp) {
        return Objects.nonNull(timestamp) ? Instant.ofEpochMilli(timestamp) : Instant.ofEpochMilli(0);
    }
}
