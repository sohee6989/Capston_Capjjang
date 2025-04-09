import cv2
import mediapipe as mp
import json
import math
import time
from tqdm import tqdm

# ───────────────────────
# 유틸 함수 정의
def extract_pose_keypoints(results):
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
    l_hip = data["left_hip"]
    r_hip = data["right_hip"]
    data["mid_hip"] = {
        "x": (l_hip["x"] + r_hip["x"]) / 2,
        "y": (l_hip["y"] + r_hip["y"]) / 2
    }
    return data

def body_direction_angle(pose):
    dx = pose['mid_hip']['x'] - pose['left_shoulder']['x']
    dy = pose['mid_hip']['y'] - pose['left_shoulder']['y']
    return math.degrees(math.atan2(dy, dx))

def part_diff(user, ref, keys):
    total = 0
    for k in keys:
        total += abs(user[k]['x'] - ref[k]['x']) + abs(user[k]['y'] - ref[k]['y'])
    return (total / len(keys)) * 100

def compare_pose_directional(user, ref):
    breakdown = {}
    user_angle = body_direction_angle(user)
    ref_angle = body_direction_angle(ref)
    diff_angle = abs(user_angle - ref_angle)
    breakdown['body_direction'] = {"diff": round(diff_angle, 2), "score": round(max(0, 100 - diff_angle), 2)}

    limbs = {
        "left_arm_diff": ["left_shoulder", "left_elbow", "left_wrist"],
        "right_arm_diff": ["right_shoulder", "right_elbow", "right_wrist"],
        "left_leg_diff": ["left_hip", "left_knee", "left_ankle"],
        "right_leg_diff": ["right_hip", "right_knee", "right_ankle"]
    }
    for key, part in limbs.items():
        diff = part_diff(user, ref, part)
        breakdown[key] = {"diff": round(diff, 2), "score": round(max(0, 100 - diff), 2)}

    weights = {
        "body_direction": 10.0,
        "left_arm_diff": 3.0, "right_arm_diff": 3.0,
        "left_leg_diff": 1.0, "right_leg_diff": 1.0
    }

    total_score, total_weight = 0, 0
    for key, item in breakdown.items():
        w = weights[key]
        total_score += item['score'] * w
        total_weight += w

    return round(total_score / total_weight, 2)

# ───────────────────────
# 기준 포즈 불러오기
with open('ref_pose_filtered_1sec.json', 'r') as f:
    ref_data = json.load(f)

ref_pose_by_frame = {entry['frame']: entry['keypoints'] for entry in ref_data}

# ───────────────────────
# 사용자 영상 분석
user_path = 'D:/LearningPython/capston/amateur_dance.mp4'
cap = cv2.VideoCapture(user_path)
fps = cap.get(cv2.CAP_PROP_FPS)
fps = 30

mp_pose = mp.solutions.pose
mp_draw = mp.solutions.drawing_utils

frame_id = 0
latest_score = None
## start_time = time.time()  # 시작 시간 기록

# 비디오 저장 설정
fourcc = cv2.VideoWriter_fourcc(*'mp4v')  # MP4 파일 형식 설정
out = cv2.VideoWriter('output_video3.mp4', fourcc, fps, (int(cap.get(3)), int(cap.get(4))))  # 저장할 비디오 파일 경로 설정

with mp_pose.Pose() as pose:
    total_frames = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))
    pbar = tqdm(total=total_frames)

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        result = pose.process(rgb)

        if frame_id % int(fps) == 0:
            if frame_id in ref_pose_by_frame:
                user_pose = extract_pose_keypoints(result)
                ref_pose = ref_pose_by_frame[frame_id]
                latest_score = compare_pose_directional(user_pose, ref_pose)
                cv2.putText(frame, f'Score: {latest_score}', (30, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (0,255,0), 2)

        out.write(frame)  # 결과 프레임 저장
        frame_id += 1
        pbar.update(1)

pbar.close()
cap.release()
out.release() 
