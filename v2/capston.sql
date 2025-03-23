USE 0124_db;

DROP TABLE IF EXISTS songs;

CREATE TABLE songs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    artist VARCHAR(100) NOT NULL,
    genre VARCHAR(50),
    duration INT,
    
    verseStartTime INT DEFAULT 0,
    verseEndTime INT DEFAULT 0,
    highlightStartTime INT DEFAULT 0,
    highlightEndTime INT DEFAULT 0,
    challengeStartTime INT DEFAULT 0,
    challengeEndTime INT DEFAULT 0,
    
    audioFilePath VARCHAR(255),    
    silhouetteVideoPath  VARCHAR(255),
    danceGuidePath VARCHAR(255),
    avatarVideoWithAudioPath VARCHAR(255),
    coverImagePath VARCHAR(255),
    createdBy VARCHAR(100) NOT NULL
);

INSERT INTO songs (
    title, artist, duration, genre,
    silhouetteVideoPath, danceGuidePath,
    verseStartTime, verseEndTime,
    highlightStartTime, highlightEndTime,
    challengeStartTime, challengeEndTime,
    audioFilePath, avatarVideoWithAudioPath, coverImagePath,
    createdBy
) VALUES
('Mantra', 'Jennie', 69, 'Pop',
 'Mantra_silhouette.mp4', 'Mantra_expert.mp4',
 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 'admin'),
 
('Vividiva', 'Hoshimachi Suisei', 19, 'Dance',
 'Vividiva_silhouette.mp4', 'Vividiva_expert.mp4',
  0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 'admin'),

('Hype Boy', 'NewJeans', 78, 'Dance',
 NULL, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 'admin'),
('Ditto', 'NewJeans', 80, 'Dance',
 NULL, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 'admin'),
('Super Shy', 'NewJeans', 82, 'Dance',
 NULL, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 'admin'),
 
('Bouncy', 'ATEEZ', 85, 'Dance',
 NULL, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 'admin'),
('Wonderland', 'ATEEZ', 90, 'Dance',
 NULL, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 'admin'),
('Say My Name', 'ATEEZ', 87, 'Dance',
 NULL, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 'admin');
 
SELECT * FROM songs;
