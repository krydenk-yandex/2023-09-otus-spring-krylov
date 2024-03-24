--liquibase formatted sql

--changeset krydenk:2024-03-24--001-schema
CREATE TABLE bookmarks (
    id BIGSERIAL,
    type VARCHAR(255) NOT NULL,
    chapter_uuid UUID NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (chapter_uuid) REFERENCES chapters(uuid),
    primary key (id)
);