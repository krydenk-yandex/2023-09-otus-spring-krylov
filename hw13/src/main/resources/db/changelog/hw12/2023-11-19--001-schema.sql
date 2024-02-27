--liquibase formatted sql

--changeset krydenk:2024-01-21-users-schema
create table users(
    id bigserial,
    username varchar(50) not null,
    password varchar(500) not null,
    enabled boolean not null,
    primary key (id)
);

create table authorities (
    id bigserial,
    authority varchar(50) not null,
    user_id bigint references users (id) on delete cascade,
    primary key (id)
);

create unique index ix_authority_user_id on authorities (user_id,authority);
