INSERT INTO accuracy_session (
    user_id, song_id, start_time, end_time,
    score, feedback, accuracy_details, mode, created_at
) VALUES
(2, 10, '2025-04-01 14:15:00', '2025-04-01 14:15:55', 94.56,
 'Great job! Try to improve the pose at 22s.',
 '{"arm": 0.87, "leg": 0.87, "body": 0.93}', 'ACCURACY', '2025-04-01 14:15:00'),

(2, 12, '2025-04-01 14:52:00', '2025-04-01 14:52:36', 88.30,
 'Great job! Try to improve the pose at 27s.',
 '{"arm": 0.74, "leg": 0.8, "body": 0.82}', 'ACCURACY', '2025-04-01 14:52:00'),

(2, 10, '2025-04-01 14:58:00', '2025-04-01 14:58:53', 73.64,
 'Great job! Try to improve the pose at 22s.',
 '{"arm": 0.8, "leg": 0.68, "body": 0.94}', 'ACCURACY', '2025-04-01 14:58:00'),

(9, 12, '2025-04-01 14:28:00', '2025-04-01 14:28:33', 96.79,
 'Great job! Try to improve the pose at 17s.',
 '{"arm": 0.63, "leg": 0.67, "body": 0.72}', 'ACCURACY', '2025-04-01 14:28:00'),

(9, 9, '2025-04-01 14:20:00', '2025-04-01 14:20:36', 74.47,
 'Great job! Try to improve the pose at 10s.',
 '{"arm": 0.71, "leg": 0.61, "body": 0.72}', 'ACCURACY', '2025-04-01 14:20:00'),

(9, 12, '2025-04-01 14:53:00', '2025-04-01 14:53:34', 82.96,
 'Great job! Try to improve the pose at 41s.',
 '{"arm": 0.92, "leg": 0.65, "body": 0.83}', 'ACCURACY', '2025-04-01 14:53:00'),

(10, 12, '2025-04-01 14:51:00', '2025-04-01 14:51:46', 68.35,
 'Great job! Try to improve the pose at 33s.',
 '{"arm": 0.63, "leg": 0.79, "body": 0.98}', 'ACCURACY', '2025-04-01 14:51:00'),

(10, 10, '2025-04-01 14:41:00', '2025-04-01 14:41:23', 83.63,
 'Great job! Try to improve the pose at 34s.',
 '{"arm": 0.92, "leg": 0.62, "body": 0.75}', 'ACCURACY', '2025-04-01 14:41:00'),

(10, 11, '2025-04-01 14:33:00', '2025-04-01 14:33:43', 61.11,
 'Great job! Try to improve the pose at 44s.',
 '{"arm": 0.9, "leg": 0.62, "body": 0.63}', 'ACCURACY', '2025-04-01 14:33:00'),

(11, 10, '2025-04-01 14:16:00', '2025-04-01 14:16:59', 80.48,
 'Great job! Try to improve the pose at 17s.',
 '{"arm": 0.66, "leg": 0.63, "body": 1.0}', 'ACCURACY', '2025-04-01 14:16:00'),

(11, 9, '2025-04-01 14:37:00', '2025-04-01 14:37:51', 75.52,
 'Great job! Try to improve the pose at 15s.',
 '{"arm": 0.61, "leg": 0.79, "body": 0.79}', 'ACCURACY', '2025-04-01 14:37:00'),

(11, 11, '2025-04-01 14:38:00', '2025-04-01 14:38:54', 87.58,
 'Great job! Try to improve the pose at 29s.',
 '{"arm": 0.76, "leg": 0.64, "body": 0.89}', 'ACCURACY', '2025-04-01 14:38:00');
