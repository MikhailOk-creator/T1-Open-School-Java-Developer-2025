CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TABLE task_t
(
    id UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    user_id BIGINT NOT NULL
);