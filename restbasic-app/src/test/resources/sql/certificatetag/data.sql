INSERT INTO certificate_tags(id ,tag_id, certificate_id)
VALUES(1, 1, 1),
(2, 2, 1),
(3, 3, 2);
SELECT setval('certificate_tags_id_seq', (SELECT MAX(id) from "certificate_tags"));