--liquibase formatted sql

--changeset krydenk:2024-03-10--001-schema
create table if not exists chapters (
    uuid uuid,
    title varchar(255),
    text text,
    book_id bigint references books (id) on delete cascade,
    next_chapter_uuid uuid references chapters (uuid) default null,
    prev_chapter_uuid uuid references chapters (uuid) default null,
    primary key (uuid)
);