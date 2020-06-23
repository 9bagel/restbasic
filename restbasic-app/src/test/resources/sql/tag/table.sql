CREATE TABLE IF NOT EXISTS tags(
	id bigserial PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE);