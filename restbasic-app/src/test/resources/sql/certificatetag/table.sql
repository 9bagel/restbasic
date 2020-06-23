CREATE TABLE IF NOT EXISTS certificate_tags(
	id bigserial PRIMARY KEY,
	tag_id bigint REFERENCES tags (id) ON DELETE CASCADE,
	certificate_id bigint REFERENCES certificates (id) ON DELETE CASCADE);