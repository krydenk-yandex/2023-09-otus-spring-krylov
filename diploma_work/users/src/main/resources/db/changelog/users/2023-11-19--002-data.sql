--liquibase formatted sql

--changeset krydenk:2024-01-21-users-data
insert into users(username, password, enabled)
values ('a', '$2a$10$Fg5cJJei89zt7.yFyEgxjO7p0C48k9w3KsEkOF7icaxsrLN.byOgK', true);
-- plain pass - 1
insert into users(username, password, enabled)
values ('u', '$2a$10$Fg5cJJei89zt7.yFyEgxjO7p0C48k9w3KsEkOF7icaxsrLN.byOgK', true);
-- plain pass - 1

insert into authorities(user_id, authority)
values (1, 'ROLE_ADMIN');
values (2, 'ROLE_USER');