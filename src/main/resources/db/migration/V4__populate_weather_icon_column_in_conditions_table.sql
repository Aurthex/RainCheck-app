-- Clear
UPDATE conditions SET weather_icon = 'https://i.ibb.co/3mCLrXb/001.png' WHERE id=1;

-- Mainly clear, partly cloudy
UPDATE conditions SET weather_icon = 'https://i.ibb.co/NxvkRz8/002.png' WHERE id IN (2, 3);

-- Overcast
UPDATE conditions SET weather_icon = 'https://i.ibb.co/rsSLvGP/003.png' WHERE id=4;

-- Fog, Freezing fog
UPDATE conditions SET weather_icon = 'https://i.ibb.co/g61N864/004.png' WHERE id IN (5, 6);

-- Drizzle: Light, moderate, and dense intensity
UPDATE conditions SET weather_icon = 'https://i.ibb.co/h90XFPh/005.png' WHERE id IN (7, 8, 9);

-- Freezing Drizzle: Light and dense intensity + Freezing Rain: Light and heavy intensity
UPDATE conditions SET weather_icon = 'https://i.ibb.co/MMfxLWC/006.png' WHERE id IN (10, 11, 15, 16);

-- Rain: Slight, moderate and heavy intensity + Rain showers: Slight, moderate, and violent
UPDATE conditions SET weather_icon = 'https://i.ibb.co/bKfcgJY/007.png' WHERE id IN (12, 13, 14, 21, 22, 23);

-- Snow fall: Slight, moderate, and heavy intensity + Snow showers slight and heavy
UPDATE conditions SET weather_icon = 'https://i.ibb.co/SydwpgK/008.png' WHERE id IN (17, 18, 19, 24, 25);

-- Sleet
UPDATE conditions SET weather_icon = 'https://i.ibb.co/SXyBT0H/009.png' WHERE id=20;

-- Thunderstorm: Slight or moderate + Thunderstorm with slight and heavy hail
UPDATE conditions SET weather_icon = 'https://i.ibb.co/Ydw9qK9/010.png' WHERE id IN (26, 27, 28);