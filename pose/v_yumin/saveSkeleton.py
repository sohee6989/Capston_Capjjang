import cv2
import mediapipe as mp
import json

def extract_reduced_keypoints(results):
    landmarks = results.pose_landmarks.landmark
    indices = {
        "left_shoulder": 11, "right_shoulder": 12, "left_elbow": 13, "right_elbow": 14,
        "left_wrist": 15, "right_wrist": 16, "left_hip": 23, "right_hip": 24,
        "left_knee": 25, "right_knee": 26, "left_ankle": 27, "right_ankle": 28
    }

    data = {}
    for k in indices:
        l = landmarks[indices[k]]
        data[k] = {"x": l.x, "y": l.y}
    # mid_hip 계산
    l_hip = data["left_hip"]
    r_hip = data["right_hip"]
    data["mid_hip"] = {
        "x": (l_hip["x"] + r_hip["x"]) / 2,
        "y": (l_hip["y"] + r_hip["y"]) / 2
    }
    return data

# 경로 설정
ref_path = 'D:/LearningPython/capston/mantra.mp4'
cap = cv2.VideoCapture(ref_path)
fps = cap.get(cv2.CAP_PROP_FPS)  # FPS 값

# FPS 값을 기준으로 1초마다 저장 (FPS를 1초마다 저장)
interval = int(fps)  # 1초에 해당하는 프레임 수로 설정

mp_pose = mp.solutions.pose
saved_data = []
frame_id = 0

with mp_pose.Pose() as pose:
    while True:
        ret, frame = cap.read()
        if not ret:
            break

        # 1초마다 저장 (fps에 맞춰 계산된 간격에 맞는 프레임을 저장)
        if frame_id % interval == 0:  # interval 간격마다 처리
            image = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
            result = pose.process(image)
            if result.pose_landmarks:
                keypoints_filtered = extract_reduced_keypoints(result)
                saved_data.append({
                    "frame": frame_id,
                    "video": ref_path,
                    "keypoints": keypoints_filtered
                })

        frame_id += 1

cap.release()

# 저장된 키포인트를 JSON 파일로 저장
with open('ref_pose_filtered_1sec.json', 'w') as f:
    json.dump(saved_data, f, indent=2)
