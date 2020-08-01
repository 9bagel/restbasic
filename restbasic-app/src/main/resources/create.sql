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
	user_id bigint REFERENCES users (id) ON  DELETE CASCADE,
	purchase_at timestamp with time zone NOT NULL);

	CREATE TABLE IF NOT EXISTS ordered_certificates(
	id bigserial PRIMARY KEY,
	order_id bigint REFERENCES orders (id) ON  DELETE CASCADE,
	certificate_id bigint REFERENCES certificates (id) ON DELETE CASCADE);

	CREATE TABLE IF NOT EXISTS users(
	id bigserial PRIMARY KEY,
	username varchar(255) NOT NULL UNIQUE,
	first_name varchar(255) NOT NULL,
	last_name varchar(255) NOT NULL,
	password varchar(255) NOT NULL,
	role varchar(255) NOT NULL,
	email varchar(255) NOT NULL UNIQUE);


CREATE OR REPLACE FUNCTION search(IN word character varying) RETURNS SETOF certificates AS $$
SELECT * FROM certificates
WHERE UPPER(description) LIKE UPPER('%\'||word||'%')
	OR UPPER(name) LIKE UPPER('%\'||word||'%')
$$ LANGUAGE SQL;