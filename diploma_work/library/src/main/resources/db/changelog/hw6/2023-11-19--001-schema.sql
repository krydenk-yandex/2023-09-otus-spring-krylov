--liquibase formatted sql

--changeset krydenk:2023-11-19-001-schema
create table comments (
    id bigserial,
    text text,
    author_name varchar(255),
    book_id bigint references books (id) on delete cascade,
    primary key (id)
);