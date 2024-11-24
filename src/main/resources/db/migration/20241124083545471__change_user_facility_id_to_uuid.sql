ALTER TABLE users RENAME facility_id to facility_uuid;
ALTER TABLE users ADD COLUMN facility_id BIGINT;
