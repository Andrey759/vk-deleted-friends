
CREATE TABLE "user"
(
    id BIGINT PRIMARY KEY,
    interval_type VARCHAR(50) NOT NULL,
    last_update TIMESTAMP
);

CREATE TABLE abstract_data
(
    dtype VARCHAR(50) NOT NULL,
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES "user"(id),
    remote_id BIGINT NOT NULL,
    last_update TIMESTAMP,
    last_entry TIMESTAMP NOT NULL,
    removed BOOLEAN,
    first_name VARCHAR(256) NOT NULL,
    last_name VARCHAR(256) NOT NULL,
    b_date TIMESTAMP,
    occupation VARCHAR(256),
    nickname VARCHAR(256),
    sex_type VARCHAR(50),
    deactivated_type VARCHAR(50),
    domain VARCHAR(256),
    photo50 VARCHAR(256),
    relation_type VARCHAR(50),
    relation_partner_data_id BIGINT REFERENCES abstract_data(id) ON DELETE CASCADE,
    UNIQUE (user_id, remote_id)
);

CREATE TABLE friend_change
(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES "user"(id),
    friend_data_id BIGINT NOT NULL REFERENCES abstract_data(id) ON DELETE CASCADE,
    detect_time_min TIMESTAMP NOT NULL,
    detect_time_max TIMESTAMP NOT NULL,
    change_type VARCHAR(50) NOT NULL
);

CREATE TABLE data_change
(
    id BIGSERIAL PRIMARY KEY,
    data_id BIGINT NOT NULL REFERENCES abstract_data(id) ON DELETE CASCADE,
    detect_time_min TIMESTAMP NOT NULL,
    detect_time_max TIMESTAMP NOT NULL,
    field_name VARCHAR(50),
    old_value VARCHAR(256),
    new_value VARCHAR(256)
);

INSERT INTO "user" VALUES(1974730, 'EVERY_MINUTE', null);
