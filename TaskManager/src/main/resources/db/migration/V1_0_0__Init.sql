CREATE TABLE task_t
(
    id UUID NOT NULL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    user_id BIGINT NOT NULL
);