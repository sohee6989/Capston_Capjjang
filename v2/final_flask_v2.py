import sys
sys.path.append(r"C:\Users\sam2-main")  # SAM2 설치 경로

from flask import Flask, jsonify
import os
import cv2
import numpy as np
import mediapipe as mp
from fastdtw import fastdtw
from scipy.spatial.distance import euclidean
from sam2.build_sam import build_sam2_video_predictor
from functions import draw_neon_contours  # 네온 외곽선 함수 사용

# Flask 서버 초기화
app = Flask(__name__)

# SAM2 모델 불러오기
sam2_checkpoint = r"C:\Users\sam2-main\checkpoints\sam2.1_hiera_large.pt"
model_cfg = r"C:\Users\sam2-main\configs\sam2.1\sam2.1_hiera_l.yaml"
predictor = build_sam2_video_predictor(model_cfg, sam2_checkpoint, device="cuda")

# Mediapipe 포즈 추적 초기화
mp_pose = mp.solutions.pose

def normalize_keypoints(keypoints):
    pelvis = keypoints[24]
    return np.nan_to_num(keypoints - pelvis)

def calculate_similarity(expert_keypoints, user_keypoints):
    distances = np.linalg.norm(expert_keypoints - user_keypoints, axis=1)
    mean_distance = np.mean(distances)
    max_distance = 0.5
    score = max(100 - (mean_distance / max_distance) * 100, 0)
    return score

# 1. 연습 모드: 실시간 네온 실루엣만 출력
@app.route("/practice-session", methods=["GET"])
def practice_session():
    cap = cv2.VideoCapture(0)
    if not cap.isOpened():
        return jsonify({"error": "웹캠을 열 수 없습니다."}), 500

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        # SAM2 실루엣 추출
        frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        state = predictor.init_state(video_frame=frame_rgb)
        predictor.reset_state(state)
        mask_logits = predictor.predict_single_frame(state, frame_rgb)
        mask = (mask_logits[0] > 0.0).cpu().numpy()

        # 네온 외곽선 적용
        mask_binary = mask.astype(np.uint8)
        neon_frame = draw_neon_contours(frame, mask_binary)

        cv2.imshow("Practice Mode", neon_frame)
        if cv2.waitKey(1) & 0xFF == ord("q"):
            break

    cap.release()
    cv2.destroyAllWindows()
    return jsonify({"message": "연습 세션 종료"})

# 2. 정확도 모드: 실시간 네온 실루엣 + 점수 + 피드백
@app.route("/accuracy-session", methods=["GET"])
def accuracy_session():
    cap = cv2.VideoCapture(0)
    if not cap.isOpened():
        return jsonify({"error": "웹캠을 열 수 없습니다."}), 500

    score_list = []

    with mp_pose.Pose(min_detection_confidence=0.5, min_tracking_confidence=0.5) as pose:
        while True:
            ret, frame = cap.read()
            if not ret:
                break

            # SAM2 실루엣 추출
            frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
            state = predictor.init_state(video_frame=frame_rgb)
            predictor.reset_state(state)
            mask_logits = predictor.predict_single_frame(state, frame_rgb)
            mask = (mask_logits[0] > 0.0).cpu().numpy()

            # 네온 외곽선 적용
            mask_binary = mask.astype(np.uint8)
            neon_frame = draw_neon_contours(frame, mask_binary)

            # Mediapipe 포즈 분석
            result = pose.process(frame_rgb)
            if result.pose_landmarks:
                keypoints = np.array([[lmk.x, lmk.y, lmk.z] for lmk in result.pose_landmarks.landmark])
                normalized = normalize_keypoints(keypoints)

                # 점수 계산 
                score = calculate_similarity(normalized, normalized)
                score_list.append(score)

                # 점수 & 피드백 시각화
                cv2.putText(neon_frame, f"Score: {score:.2f}", (30, 50),
                            cv2.FONT_HERSHEY_SIMPLEX, 1.2, (0, 255, 0), 2)
                cv2.putText(neon_frame, "Feedback: Keep going!", (30, 100),
                            cv2.FONT_HERSHEY_SIMPLEX, 1.0, (0, 255, 0), 2)

            cv2.imshow("Accuracy Mode", neon_frame)
            if cv2.waitKey(1) & 0xFF == ord("q"):
                break

    cap.release()
    cv2.destroyAllWindows()

    avg_score = np.mean(score_list) if score_list else 0
    return jsonify({
        "accuracy_score": avg_score,
        "feedback": "______________",   # 상의 후 업뎃하겠습니다
        "accuracy_details": "+++++++++++++++"   # 이하 동일
    })

# 서버 실행
if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
