DROP TABLE votes IF EXISTS;
DROP TABLE menu_history IF EXISTS;
DROP TABLE users IF EXISTS;
DROP TABLE restaurants IF EXISTS;
DROP SEQUENCE global_seq IF EXISTS;

CREATE SEQUENCE global_seq AS INTEGER START WITH 1;

CREATE TABLE users
(
    id       INTEGER GENERATED BY DEFAULT AS SEQUENCE global_seq PRIMARY KEY,
    name     VARCHAR(255)          NOT NULL,
    email    VARCHAR(255)          NOT NULL,
    password VARCHAR(255)          NOT NULL,
    is_admin BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE UNIQUE INDEX users_unique_email_idx
    ON USERS (email);

CREATE TABLE restaurants
(
    id   INTEGER GENERATED BY DEFAULT AS SEQUENCE global_seq PRIMARY KEY,
    name VARCHAR(255)  NOT NULL
);

CREATE UNIQUE INDEX restaurants_unique_name_idx
    ON restaurants (name);

CREATE TABLE votes
(
    restaurant_id INTEGER NOT NULL,
    date          DATE    NOT NULL,
    user_id       INTEGER NOT NULL,

    FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX votes_unique_idx
    ON votes (user_id, date);

CREATE TABLE menu_history
(
    restaurant_id INTEGER NOT NULL,
    date DATE NOT NULL,
    menu VARCHAR(1024) NOT NULL,

    FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX menu_history_unique_idx
    ON menu_history (restaurant_id, date);