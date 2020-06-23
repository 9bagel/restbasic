CREATE TABLE IF NOT EXISTS certificates(
	id bigserial PRIMARY KEY,
	name varchar(255) NOT NULL,
	description varchar(255) NOT NULL,
	price decimal NOT NULL,
	created_at timestamp NOT NULL,
	updated_at timestamp NOT NULL,
	duration bigint NOT NULL);