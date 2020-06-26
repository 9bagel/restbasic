CREATE TABLE IF NOT EXISTS tags(
	id bigserial PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE);

CREATE TABLE IF NOT EXISTS certificates(
	id bigserial PRIMARY KEY,
	name varchar(255) NOT NULL UNIQUE,
	description varchar(255) NOT NULL,
	price decimal NOT NULL,
	created_at timestamp NOT NULL,
	updated_at timestamp NOT NULL,
	duration bigint NOT NULL);

CREATE TABLE IF NOT EXISTS certificate_tags(
	id bigserial PRIMARY KEY,
	tag_id bigint REFERENCES tags (id) ON DELETE CASCADE,
	certificate_id bigint REFERENCES certificates (id) ON DELETE CASCADE);

	CREATE TABLE IF NOT EXISTS orders(
	id bigserial PRIMARY KEY,
	cost decimal NOT NULL,
	purchaseDate timestamp NOT NULL);

	CREATE TABLE IF NOT EXISTS ordered_certificates(
	id bigserial PRIMARY KEY,
	order_id bigint REFERENCES orders (id) ON  DELETE CASCADE,
	certificate_id bigint REFERENCES certificates (id) ON DELETE CASCADE);

	CREATE TABLE IF NOT EXISTS users(
	id bigserial PRIMARY KEY,
	first_name varchar(255) NOT NULL,
	second_name varchar(255) NOT NULL);

	CREATE TABLE IF NOT EXISTS user_orders(
	id bigserial PRIMARY KEY,
	user_id bigint REFERENCES users (id) ON  DELETE CASCADE,
	order_id bigint REFERENCES orders (id) ON DELETE CASCADE);

CREATE OR REPLACE FUNCTION search(IN word CHARACTER varying) RETURNS certificates AS $$
CREATE OR REPLACE FUNCTION search(IN word character varying) RETURNS SETOF certificates AS $$
SELECT * FROM certificates
WHERE UPPER(description) LIKE UPPER('%\'||word||'%')
	OR UPPER(name) LIKE UPPER('%\'||word||'%')
$$ LANGUAGE SQL;