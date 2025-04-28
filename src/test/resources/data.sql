INSERT INTO users (id, username, login, role, password) VALUES
(1,'user1', 'user1', 'USER', 'Pass111)'),
(2, 'user2', 'user2', 'USER', 'Pass222)'),
(3, 'user3', 'user3', 'USER', 'Pass333)');

INSERT INTO sessions (status, created_by) VALUES
('WAIT_FOR_PLAYERS', 1);

INSERT INTO users_sessions (user_id, session_id) VALUES
(1, 1),
(2, 1);
