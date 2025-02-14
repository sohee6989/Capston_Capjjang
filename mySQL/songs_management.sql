USE 0124_db;
TRUNCATE TABLE songs;

SELECT * FROM songs;
INSERT INTO songs (title, artist, genre, duration) 
VALUES ('Mantra', 'Jennie', 'Pop', 66);
SELECT * FROM songs;

INSERT INTO songs (title, artist, genre, duration) 
VALUES ('Hype Boy', 'NewJeans', 'Dance', 78),
('Ditto', 'NewJeans', 'Dance', 80),
('Super Shy', 'NewJeans', 'Dance', 82),

('Bouncy', 'ATEEZ', 'Dance', 85),
('Wonderland', 'ATEEZ', 'Dance', 90),
('Say My Name', 'ATEEZ', 'Dance', 87),

('I AM', 'IVE', 'Dance', 83),
('After LIKE', 'IVE', 'Dance', 88),
('Baddie', 'IVE', 'Dance', 86),

('Kick It', 'NCT127', 'Dance', 92),
('Baggy Jeans', 'NCT U', 'Dance', 91),
('WISH', 'NCT WISH', 'Dance', 89);
SELECT * FROM songs;

USE your_database_name;
SELECT * FROM accuracy_results WHERE songId = 1;

