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
    activity_id INT REFERENCES activities(id) ON DELETE CASCADE,
    condition_id INT REFERENCES conditions(id) ON DELETE CASCADE,
    constraint fk_activity_conditions_activity foreign key(activity_id) references activities(id),
    constraint fk_activity_conditions_condition foreign key(condition_id) references conditions(id)
);