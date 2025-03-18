from flask import Flask, request, jsonify
import os
from main_v2 import process_and_compare_videos  # MediaPipe 분석 함수 가져오기

app = Flask(__name__)

@app.route('/analyze', methods=['POST'])
def analyze():
    data = request.json
    expert_video_path = data.get("expert_video_path")  # 전문가 영상 경로
    amateur_video_path = data.get("amateur_video_path")  # 사용자 영상 경로

    if not expert_video_path or not amateur_video_path:
        return jsonify({"error": "Missing video paths"}), 400

    if not os.path.exists(expert_video_path) or not os.path.exists(amateur_video_path):
        return jsonify({"error": "Invalid video path(s)"}), 400

    # MediaPipe 분석 실행
    accuracy_score, feedback = process_and_compare_videos(expert_video_path, amateur_video_path)

    return jsonify({
        "accuracy_score": accuracy_score,
        "feedback": feedback
    })

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)  # 자바 백엔드와 연결하려면 포트 5000 사용

