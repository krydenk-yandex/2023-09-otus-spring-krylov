--liquibase formatted sql

--changeset krydenk:2023-11-11-002-data
insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, cover_url, author_id)
values
    ('BookTitle_1', '/book-cover-1.png', 1),
    ('BookTitle_2', '/book-cover-2.png', 2),
    ('BookTitle_3', '/book-cover-3.png', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);
