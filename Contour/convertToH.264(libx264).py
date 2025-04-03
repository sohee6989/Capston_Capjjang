import subprocess
import os

# 입력 및 출력 파일 경로
input_path = "/mnt/data/Mantra_silhouette.mp4"
output_path = "/mnt/data/Mantra_silhouette_h264.mp4"

# FFmpeg 명령어: 비디오만 libx264로 인코딩, 오디오는 그대로(copy)
command = [
    "ffmpeg",
    "-i", input_path,
    "-c:v", "libx264",  # 비디오 코덱: H.264
    "-c:a", "copy",     # 오디오는 원본 그대로
    output_path
]

# FFmpeg 실행
result = subprocess.run(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)

# 변환 결과 확인
if result.returncode == 0 and os.path.exists(output_path):
    print("변환 성공:", output_path)
else:
    print("변환 실패:", result.stderr)
