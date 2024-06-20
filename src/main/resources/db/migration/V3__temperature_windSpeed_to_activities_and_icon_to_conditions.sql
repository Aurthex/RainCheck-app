ALTER TABLE activities
  ADD COLUMN temperature INT,
  ADD COLUMN wind_speed INT;

 ALTER TABLE conditions
  ADD COLUMN weather_icon varchar(255);