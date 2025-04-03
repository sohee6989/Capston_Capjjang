INSERT INTO recorded_video (
    user_id, mode, practice_session_id, challenge_session_id, accuracy_session_id,
    video_path, recorded_at, duration
) VALUES
(2, 'PRACTICE', 4, NULL, NULL,
'https://danzle-s3-bucket.s3.ap-northeast-2.amazonaws.com/videos/2/PRACTICE/4.mp4', '2025-04-01 14:00:00', 80),
(2, 'CHALLENGE', NULL, 1, NULL,
'https://danzle-s3-bucket.s3.ap-northeast-2.amazonaws.com/videos/2/CHALLENGE/1.mp4', '2025-04-01 14:10:00', 36),
(2, 'ACCURACY', NULL, NULL, 3,
'https://danzle-s3-bucket.s3.ap-northeast-2.amazonaws.com/videos/2/ACCURACY/3.mp4', '2025-04-01 14:20:00', 57),

(9, 'PRACTICE', 7, NULL, NULL,
'https://danzle-s3-bucket.s3.ap-northeast-2.amazonaws.com/videos/9/PRACTICE/7.mp4', '2025-04-01 14:00:00', 47),
(9, 'CHALLENGE', NULL, 4, NULL,
'https://danzle-s3-bucket.s3.ap-northeast-2.amazonaws.com/videos/9/CHALLENGE/4.mp4', '2025-04-01 14:10:00', 81),
(9, 'ACCURACY', NULL, NULL, 6,
'https://danzle-s3-bucket.s3.ap-northeast-2.amazonaws.com/videos/9/ACCURACY/6.mp4', '2025-04-01 14:20:00', 44),

(10, 'PRACTICE', 10, NULL, NULL,
'https://danzle-s3-bucket.s3.ap-northeast-2.amazonaws.com/videos/10/PRACTICE/10.mp4', '2025-04-01 14:00:00', 23),
(10, 'CHALLENGE', NULL, 7, NULL,
'https://danzle-s3-bucket.s3.ap-northeast-2.amazonaws.com/videos/10/CHALLENGE/7.mp4', '2025-04-01 14:10:00', 20),
(10, 'ACCURACY', NULL, NULL, 9,
'https://danzle-s3-bucket.s3.ap-northeast-2.amazonaws.com/videos/10/ACCURACY/9.mp4', '2025-04-01 14:20:00', 70),

(11, 'PRACTICE', 13, NULL, NULL,
'https://danzle-s3-bucket.s3.ap-northeast-2.amazonaws.com/videos/11/PRACTICE/13.mp4', '2025-04-01 14:00:00', 75),
(11, 'CHALLENGE', NULL, 10, NULL,
'https://danzle-s3-bucket.s3.ap-northeast-2.amazonaws.com/videos/11/CHALLENGE/10.mp4', '2025-04-01 14:10:00', 21),
(11, 'ACCURACY', NULL, NULL, 12,
'https://danzle-s3-bucket.s3.ap-northeast-2.amazonaws.com/videos/11/ACCURACY/12.mp4', '2025-04-01 14:20:00', 38);
