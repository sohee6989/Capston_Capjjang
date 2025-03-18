# 정확도 모드 : 점수+피드백 실시간 웹캠에서 추출

from flask import Flask, request, jsonify
import os
import cv2
import mediapipe as mp
import numpy as np
from fastdtw import fastdtw
from scipy.spatial.distance import euclidean
import time

# Flask 서버 초기화 (추가된 부분)
app = Flask(__name__)

# Mediapipe Pose 모델 초기화
mp_pose = mp.solutions.pose
mp_drawing = mp.solutions.drawing_utils

# OpenCV 최적화 설정
cv2.setUseOptimized(True)
cv2.setNumThreads(4)

# 고정된 크기로 프레임 리사이즈 함수
def resize_frame_fixed_size(frame1, frame2, width=640, height=480):
    frame1_resized = cv2.resize(frame1, (width, height))
    frame2_resized = cv2.resize(frame2, (width, height))
    return frame1_resized, frame2_resized

def process_and_compare_videos(expert_video_path, amateur_video_path):
    cap_expert = cv2.VideoCapture(expert_video_path)
    cap_amateur = cv2.VideoCapture(0)
    # cap_amateur = cv2.VideoCapture(amateur_video_path)  (사용자 영상을 준비하여 사용할 때)

    frame_idx = 0  # frame_idx 초기화

    # 점수 계산 간격 설정
    score_interval = 1.5  # 초 단위
    last_score_time = time.time()
    last_score = None      # 마지막 점수 저장
    last_feedback = None   # 마지막 피드백 저장
    display_duration = 1.5 # 점수 표시 유지 시간 (초)
    score_display_time = time.time()  # 점수 표시 시작 시간
    
    final_score = 0  # 최종 점수 (Flask 응답으로 반환)
    final_feedback = "No feedback"  # 최종 피드백 (Flask 응답으로 반환)

    with mp_pose.Pose(static_image_mode=False, min_detection_confidence=0.5, min_tracking_confidence=0.5) as pose:
        while cap_expert.isOpened() and cap_amateur.isOpened():
            ret_expert, frame_expert = cap_expert.read()
            ret_amateur, frame_amateur = cap_amateur.read()

            if not ret_expert or not ret_amateur:
                break

            # 프레임 리사이즈
            frame_expert_resized, frame_amateur_resized = resize_frame_fixed_size(frame_expert, frame_amateur)

            # 포즈 추출
            frame_expert_rgb = cv2.cvtColor(frame_expert_resized, cv2.COLOR_BGR2RGB)
            frame_amateur_rgb = cv2.cvtColor(frame_amateur_resized, cv2.COLOR_BGR2RGB)

            result_expert = pose.process(frame_expert_rgb)
            result_amateur = pose.process(frame_amateur_rgb)

            
            # 현재 시간 확인
            current_time = time.time()

            # 초 간격의 점수 계산
            if current_time - last_score_time >= score_interval:
                if result_expert.pose_landmarks and result_amateur.pose_landmarks:
                    # 전문가와 사용자 키포인트 추출
                    expert_keypoints = np.array([[lmk.x, lmk.y, lmk.z] for lmk in result_expert.pose_landmarks.landmark])
                    amateur_keypoints = np.array([[lmk.x, lmk.y, lmk.z] for lmk in result_amateur.pose_landmarks.landmark])
                
                    # 1차원 벡터로 변환
                    expert_keypoints_flat = expert_keypoints.flatten()
                    amateur_keypoints_flat = amateur_keypoints.flatten()
                
                    # DTW 거리 계산
                    distance, _ = fastdtw(expert_keypoints_flat[:, np.newaxis], amateur_keypoints_flat[:, np.newaxis], dist=euclidean)
                    
                    # 점수 계산
                    max_distance = 5.5
                    score = max(100 - (distance / max_distance) * 100, 0)

                    # 점수에 따른 피드백
                    if score >= 95:
                        feedback = "Perfect! Great job!"
                    elif score >= 85:
                        feedback = "Good! You're almost there!"
                    elif score >= 75:
                        feedback = "Normal! Keep going!"
                    elif score >= 60:
                        feedback = "Nice try! You're getting there!"
                    elif score > 15:
                        feedback = "Good effort! Keep it up!"
                    else:
                        feedback = "It's Okay... Don't give up!"

                    final_score = score  # 최종 점수 저장
                    final_feedback = feedback  # 최종 피드백 저장
                    last_score = score  # 마지막 점수 업데이트 (화면에 일정 시간 표시됨)
                    last_feedback = feedback  # 마지막 피드백 업데이트 (화면에 일정 시간 표시됨)
                    last_score_time = current_time  # 마지막 점수 계산 시간 업데이트
                    score_display_time = current_time

                    # 점수와 피드백 출력
                    cv2.putText(frame_amateur_resized, f"Score: {score:.2f}", (50, 50),
                                cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)
                    cv2.putText(frame_amateur_resized, feedback, (50, 100),
                                cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)

            # 마지막 점수와 피드백을 일정 시간 동안 표시
            if last_score is not None and last_feedback is not None and current_time - score_display_time <= display_duration:
                cv2.putText(frame_amateur_resized, f"Score: {last_score:.2f}", (50, 50),
                            cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)
                cv2.putText(frame_amateur_resized, last_feedback, (50, 100),
                            cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)
                
            
            # 두 영상 나란히 보기
            combined_frame = cv2.hconcat([frame_expert_resized, frame_amateur_resized])

            # 결합된 프레임 출력
            cv2.imshow("Expert vs Amateur Comparison", combined_frame)

            # 종료 조건 (q 키를 누르면 종료)
            if cv2.waitKey(1) & 0xFF == ord('q'):
                break

            frame_idx += 1

    cap_expert.release()
    cap_amateur.release()
    cv2.destroyAllWindows()

    return final_score, final_feedback

# Flask 엔드포인트 추가 (이 API는 Java(Spring Boot) 백엔드에서 호출해야 함)
@app.route('/analyze', methods=['POST'])
def analyze():
    data = request.json
    expert_video_path = data.get("expert_video_path")
    amateur_video_path = data.get("amateur_video_path")

    if not expert_video_path or not amateur_video_path:
        return jsonify({"error": "Missing video paths"}), 400

    if not os.path.exists(expert_video_path) or not os.path.exists(amateur_video_path):
        return jsonify({"error": "Invalid video path(s)"}), 400

    # Java(Spring Boot) 백엔드가 이 API를 호출하여 비디오를 분석하고 점수를 받음(프론트X)
    accuracy_score, feedback = process_and_compare_videos(expert_video_path, amateur_video_path)

    return jsonify({
        "accuracy_score": accuracy_score,
        "feedback": feedback
    })

# Flask 서버 실행
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)   # Flask는 Java(Spring Boot) 백엔드에서 호출됨
