CREATE TABLE user_t
(
    id         UUID         NOT NULL,
    username   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user_t PRIMARY KEY (id)
);

CREATE TABLE task_t
(
    id          UUID         NOT NULL,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    user_id     UUID         NOT NULL,
    CONSTRAINT pk_task_t PRIMARY KEY (id)
);

ALTER TABLE user_t
    ADD CONSTRAINT uc_user_t_email UNIQUE (email);

ALTER TABLE user_t
    ADD CONSTRAINT uc_user_t_username UNIQUE (username);

ALTER TABLE task_t
    ADD CONSTRAINT FK_TASK_T_ON_USER FOREIGN KEY (user_id) REFERENCES user_t (id);