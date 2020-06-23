INSERT INTO certificates(id ,name, description, price, created_at, updated_at, duration)
VALUES(1, 'certificate1', 'desc', 10, '2020-06-20 16:22:09.936', '2020-06-20 16:22:09.936', 5),
(2, 'certificate2', 'desc', 10, '2020-06-20 16:22:09.936', '2020-06-20 16:22:09.936', 5),
(3, 'certificate3', 'test', 10, '2020-06-20 16:22:09.936', '2020-06-20 16:22:09.936', 5);
SELECT setval('certificates_id_seq', (SELECT MAX(id) from "certificates"));