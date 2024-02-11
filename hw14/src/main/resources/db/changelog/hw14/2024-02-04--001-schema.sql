--liquibase formatted sql

--changeset krydenk:2024-02-04--001-schema
create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    genre_id bigint references genres (id) on delete cascade,
    primary key (id)
);

create table comments (
    id bigserial,
    text text,
    author_name varchar(255),
    book_id bigint references books (id) on delete cascade,
    primary key (id)
);