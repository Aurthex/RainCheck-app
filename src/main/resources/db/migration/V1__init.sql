DROP TABLE IF EXISTS activities;

CREATE TABLE activities (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    description VARCHAR(255) NOT NULL
    );

DROP TABLE IF EXISTS conditions;

CREATE TABLE conditions(
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    weather_code INT NOT NULL
    );

DROP TABLE IF EXISTS activity_condition_join;

CREATE TABLE activity_condition_join (
    activity_id INT REFERENCES activities(id),
    condition_id INT REFERENCES conditions(id),
    CONSTRAINT activity_condition_join_pk PRIMARY KEY(activity_id, condition_id)
    );
