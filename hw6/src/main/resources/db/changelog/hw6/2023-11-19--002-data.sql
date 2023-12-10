--liquibase formatted sql

--changeset krydenk:2023-11-19-002-data
insert into comments(text, author_name, book_id)
values ('Comment #1', 'Person #1', 1),
('Comment #2', 'Person #2', 1),
('Comment #3', 'Person #3', 2),
('Comment #4', 'Person #4', 3);
