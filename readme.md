## 실행 방법 (**실행 전 코드에서 꼭 파일 경로명 수정해주세요**)

### cmd에 python flask_v2.py 를 입력  
### **연습모드** : 브라우저에 http://localhost:5000/practice-mode?filename=contourwithaudio_x100.mp4  
### **정확도모드** : 브라우저에 http://localhost:5000/accuracy-mode?filename=contourwithaudio_x100.mp4

---

## API 엔드포인트

### `/practice-mode` [GET]
- **설명:**  
  실루엣 영상을 오버레이한 웹캠 피드를 제공
- **쿼리 매개변수:**  
  - `filename` (문자열): 오버레이할 실루엣 영상 파일명  
    *기본값:* `"contourwithaudio_x100.mp4"`

---

### `/accuracy-mode` [GET]
- **설명:**  
  사용자의 춤 동작을 분석하고 실시간 피드백을 제공
- **쿼리 매개변수:**  
  - `filename` (문자열): 비교할 실루엣 영상 파일명  
    *기본값:* `"contourwithaudio_x100.mp4"`

---

