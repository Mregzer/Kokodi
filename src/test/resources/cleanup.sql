DELETE FROM users_sessions;
DELETE FROM turns;
DELETE FROM player_scores;
DELETE FROM cards_sessions;
DELETE FROM sessions;
DELETE FROM users;

ALTER TABLE sessions ALTER COLUMN id RESTART WITH 1;
