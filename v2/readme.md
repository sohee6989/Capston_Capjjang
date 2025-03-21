
# 파이썬 코드 설명



- **SAM2**: 실시간 실루엣 분할
- **MediaPipe**: 3D 포즈 키포인트 추출
- **OpenCV**: 웹캠 영상 처리 및 시각화
- **FastDTW**: 사용자 자세와 기준 자세의 유사도 계산

---

### 1. `flask_final_v2.py`

Flask 서버의 메인 코드


### 2. `functions.py`

실루엣 시각화를 위한 유틸 함수 정의 파일임


### 3. SAM2 모델 연동 (꼭 파일 위치 변경해서 사용해주세요)

SAM2는 `build_sam2_video_predictor()`를 통해 초기화되며  
다음 모델 파일과 설정 파일이 필요함:

- `checkpoints/sam2.1_hiera_large.pt` (모델 가중치)
- `configs/sam2.1/sam2.1_hiera_l.yaml` (구성 설정)
