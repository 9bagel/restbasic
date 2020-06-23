CREATE TABLE IF NOT EXISTS tags(
	id bigserial PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE);

CREATE TABLE IF NOT EXISTS certificates(
	id bigserial PRIMARY KEY,
	name varchar(255) NOT NULL,
	description varchar(255) NOT NULL,
	price decimal NOT NULL,
	created_at timestamp NOT NULL,
	updated_at timestamp NOT NULL,
	duration bigint NOT NULL);

CREATE TABLE IF NOT EXISTS certificate_tags(
	id bigserial PRIMARY KEY,
	tag_id bigint REFERENCES tags (id),
	certificate_id bigint REFERENCES certificates (id) ON DELETE CASCADE);