CREATE TABLE notes
(
    id         UUID NOT NULL,
    content    TEXT NOT NULL,
    owner_id   UUID NOT NULL,
    updated_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_notes PRIMARY KEY (id)
);

CREATE TABLE users
(
    id         UUID         NOT NULL,
    username   VARCHAR(32)  NOT NULL,
    password   VARCHAR(255) NOT NULL,
    updated_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT unq_users_username UNIQUE (username);

CREATE UNIQUE INDEX idx_unq_users_username ON users (username);

ALTER TABLE notes
    ADD CONSTRAINT FK_NOTES_OWNER FOREIGN KEY (owner_id) REFERENCES users (id);

CREATE INDEX idx_notes_owner ON notes (owner_id);