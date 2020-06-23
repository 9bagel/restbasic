INSERT INTO tags(id, name) VALUES (1, 'books');
INSERT INTO tags(id, name) VALUES (2, 'tools');
INSERT INTO tags(id, name) VALUES (3, 'news');
SELECT setval('tags_id_seq', (SELECT MAX(id) from "tags"));