DROP TABLE IF EXISTS users, categories, events, requests, compilations, event_compilations, comments, reports CASCADE ;

CREATE TABLE IF NOT EXISTS users (
    user_id              BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email                VARCHAR(255) NOT NULL,
    name                 VARCHAR(255) NOT NULL,
    are_comments_blocked BOOLEAN DEFAULT false,
    CONSTRAINT uq_email UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS categories (
    category_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    CONSTRAINT uq_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events (
    event_id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation         VARCHAR(2000) NOT NULL,
    category_id        BIGINT REFERENCES categories (category_id) ON DELETE RESTRICT,
    description        VARCHAR(7000) NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    lat                REAL NOT NULL,
    lon                REAL NOT NULL,
    paid               BOOLEAN DEFAULT FALSE,
    participant_limit  INTEGER DEFAULT 0,
    request_moderation BOOLEAN DEFAULT TRUE,
    title              VARCHAR(120) NOT NULL,
    confirmed_requests INTEGER DEFAULT 0,
    created_on         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator          BIGINT REFERENCES users (user_id) ON DELETE RESTRICT,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    state              VARCHAR,
    views              BIGINT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS requests (
    request_id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_id     BIGINT REFERENCES events (event_id) ON DELETE RESTRICT,
    requester_id BIGINT REFERENCES users (user_id) ON DELETE RESTRICT,
    status       VARCHAR,
    CONSTRAINT uq_request UNIQUE (event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS compilations (
    compilation_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pinned         BOOLEAN DEFAULT FALSE,
    title          VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS event_compilations (
    compilation_id BIGINT REFERENCES compilations(compilation_id) ON DELETE RESTRICT,
    event_id BIGINT REFERENCES events(event_id) ON DELETE RESTRICT,
    PRIMARY KEY (compilation_id, event_id)
);

CREATE TABLE IF NOT EXISTS comments (
    comment_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text       VARCHAR(7000) NOT NULL,
    state      VARCHAR DEFAULT 'NOT_EDIT',
    created    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    author_id  BIGINT REFERENCES users(user_id) ON DELETE RESTRICT,
    event_id   BIGINT REFERENCES events(event_id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS reports (
    report_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    reported_user BIGINT REFERENCES users(user_id) ON DELETE RESTRICT,
    reported_message VARCHAR(7000) NOT NULL,
    CONSTRAINT uq_report UNIQUE (reported_user, reported_message)
);

