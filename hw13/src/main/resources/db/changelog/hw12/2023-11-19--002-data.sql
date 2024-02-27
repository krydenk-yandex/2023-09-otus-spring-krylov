--liquibase formatted sql

--changeset krydenk:2024-01-21-users-data
insert into users(username, password, enabled)
values ('a', '{bcrypt}$2y$10$.9pN4cHZrL37f7oynFDHw.p5GmpKX9pUJQYdY4FEXeBkOPDYpxUtW', true);
-- plain pass - 1
insert into users(username, password, enabled)
values ('u', '{bcrypt}$2y$10$.9pN4cHZrL37f7oynFDHw.p5GmpKX9pUJQYdY4FEXeBkOPDYpxUtW', true);
-- plain pass - 1

insert into authorities(user_id, authority)
values (1, 'ROLE_ADMIN');
values (2, 'ROLE_USER');