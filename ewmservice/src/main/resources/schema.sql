CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY,
    email VARCHAR(500) NOT NULL,
    name  VARCHAR(250) NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (id),
    CONSTRAINT unique_mail UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(250) NOT NULL,
    CONSTRAINT categoryId PRIMARY KEY (id),
    CONSTRAINT unique_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY,
    title              VARCHAR(250)  NOT NULL,
    annotation         VARCHAR(500)  NOT NULL,
    description        VARCHAR(2000) NOT NULL,
    category_id        BIGINT        NOT NULL,
    created            TIMESTAMP     NOT NULL,
    event_date         TIMESTAMP     NOT NULL,
    initiator_id       BIGINT        NOT NULL,
    paid               BOOLEAN       NOT NULL,
    participant_limit  INT           NOT NULL,
    published          TIMESTAMP,
    request_moderation BOOLEAN,
    state              VARCHAR,
    lat                NUMERIC       NOT NULL,
    lon                NUMERIC       NOT NULL,
    CONSTRAINT events_pk PRIMARY KEY (id),
    CONSTRAINT category FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT initiator FOREIGN KEY (initiator_id) REFERENCES users (id)
);


CREATE TABLE IF NOT EXISTS requests
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    requester_id BIGINT                                  NOT NULL,
    event_id     BIGINT                                  NOT NULL,
    created     TIMESTAMP,
    status      VARCHAR,
    CONSTRAINT pk_request PRIMARY KEY (id),
    UNIQUE (requester_id, event_id),
    CONSTRAINT REQUESTER FOREIGN KEY (requester_id) REFERENCES users (id),
    CONSTRAINT EVENT_FK FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title  VARCHAR(255)                            NOT NULL,
    pinned BOOLEAN,
    CONSTRAINT pk_compilation PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS event_compilations
(
    compilation_Id BIGINT NOT NULL,
    event_Id       BIGINT NOT NULL,
    CONSTRAINT COMPILATIONS FOREIGN KEY (compilation_Id) REFERENCES compilations (id),
    CONSTRAINT EVENT FOREIGN KEY (event_Id) REFERENCES events (id)
);