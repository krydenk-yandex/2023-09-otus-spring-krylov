--liquibase formatted sql

--changeset krydenk:2024-03-24--002-data
INSERT INTO bookmarks (type, chapter_uuid, user_id) VALUES ('MANUAL', '31db786a-2448-4b3a-ad2b-de4d80efd295', 1);
INSERT INTO bookmarks (type, chapter_uuid, user_id) VALUES ('AUTO', '9718afff-6348-45c2-aa48-f0eaa183687d', 1);
INSERT INTO bookmarks (type, chapter_uuid, user_id) VALUES ('MANUAL', '38b09b5a-ace1-4688-9676-35455dcba123', 2);
INSERT INTO bookmarks (type, chapter_uuid, user_id) VALUES ('AUTO', '9718afff-6348-45c2-aa48-f0eaa183687d', 2);

