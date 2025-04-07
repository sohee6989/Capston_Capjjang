## 실행 방법

### cmd에 python flask_v2.py 를 입력  
### **연습모드** : 브라우저에 http://localhost:5000/practice-mode?songTitle=Mantra
### **연습모드** : 브라우저에 http://localhost:5000/practice-mode?songTitle=Vividiva

### **정확도모드** : 브라우저에 http://localhost:5000/accuracy-mode?songTitle=Mantra
### **정확도모드** : 브라우저에 http://localhost:5000/accuracy-mode?songTitle=Vividiva

---

## API 엔드포인트

### `/practice-mode` [GET]
- **설명:**  
  실루엣 영상을 오버레이한 웹캠 피드를 제공
- **쿼리 매개변수:**  
  - songTitle (문자열, 필수): 연습에 사용할 곡 제목 (예: "Mantra")
이 값에 따라 {songTitle}_expert.mp4 및 {songTitle}_silhouette.mp4 파일이 로드됨

---

### `/accuracy-mode` [GET]
- **설명:**  
  사용자의 춤 동작을 분석하고 실시간 피드백을 제공
- **쿼리 매개변수:**  
  - songTitle (문자열, 필수): 정확도 분석에 사용할 곡 제목 (예: "Mantra")

---

### 서버는 아래의 형식으로 영상 파일을 자동 로드:

전문가 영상 (안무가): videos1/{songTitle}_expert.mp4
실루엣 영상 (사용자 추출): static/output/{songTitle}_silhouette.mp4

예시:
songTitle = "Mantra"일 경우:
 전문가 영상 → videos1/Mantra_expert.mp4
 실루엣 영상 → static/output/Mantra_silhouette.mp4
