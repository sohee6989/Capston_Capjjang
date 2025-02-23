(수정 : )이라고 써놓은 부분만 수정하시면 됩니다

## 📂 1. ChallengeSessionController.java 변경 사항

### 🔹 변경된 점
- **메서드 이름 변경**
  - `getChallengeSessionsByUser(Long userId)` → `getByUserId(Long userId)`
  - 간결하게 바꿈

- **메서드 삭제**
  - `getChallengeSessionsBySong(Long songId)`

- **메서드 추가**
  - `getByUserAndSong(Long userId, Long songId)`
---

## 📂 2. PracticeSessionController.java 변경 사항

### 🔹 변경된 점
- **메서드 삭제**
  - `getPracticeModeSongs()`

- **메서드 이름 변경**
  - `getPracticeSessionsByUser(Long userId)` → `getByUserId(Long userId)`
  - 간결하게 바꿈

- **메서드 변경**
  - `getPracticeSessionsBySong(Long songId)` → `getBySongAndUser(Long songId, Long userId)`

---

## 📂 3. SongController.java 변경 사항

### 🔹 변경된 점
- **메서드 내부 최적화**
  - `getAllSongs()`에서 직접 `songService.getAllSongs()`를 반환.

- **TODO 주석 추가**
  - `saveSong(SongDto songDto)` 메서드를 **"ADMIN 권한으로 변경하기"** 필요.

---

## 📂 4. ChallengeSessionService.java 변경 사항

### 🔹 변경된 점
- **중복 코드 제거 및 최적화**
  - `getUserById(Long userId)`, `getSongById(Long songId)` 추가.

- **메서드 이름 변경**
  - `getChallengeSessionsByUser(Long userId)` → `getByUserId(Long userId)`
  - 간결하게 바꿈

- **메서드 삭제**
  - `getChallengeSessionsBySong(Long songId)`

- **메서드 추가**
  - `getByUserAndSong(Long userId, Long songId)`


---

## 📂 5. PracticeSessionService.java 변경 사항

### 🔹 변경된 점
- **중복 코드 제거**
  - `getUserById(Long userId)`, `getSongById(Long songId)` 추가.

- **예외 처리 강화**
  - `UserNotFoundException`, `SongNotFoundException` 추가.
  - exception파일에 이름만 똑같이 해서 추가해주시면 됩니다

- **메서드 이름 변경**
  - `getPracticeSessionsByUser(Long userId)` → `getByUserId(Long userId)`
  - `getPracticeSessionsBySong(Long songId)` → `getBySongAndUser(Long songId, Long userId)`
  - 간결하게 바꿈

- **메서드 추가**
  - `getById(Long sessionId)`

---

## 📂 6. SongService.java 변경 사항

### 🔹 변경된 점
- **메서드 최적화**
  - `getAllSongs()` 직접 반환.

- **메서드 최적화**
  - `saveSong(SongDto dto)`에서 `dtoToEntity` 메서드 분리.

---

## 📂 7. AccuracySessionService.java 변경 사항

### 🔹 변경된 점
- **메서드 추가**
  - `getUserById(Long userId)`, `getSongById(Long songId)`

- **메서드 이름 변경**
  - `getByUserId(Long userId)`
  - 간결하게 바꿈

- **메서드 삭제**
  - `getAccuracyHistory(Long userId)`

- **메서드 추가**
  - `getByUserAndSong(Long userId, Long songId)`

---

## 📂 8. AccuracySessionController.java 변경 사항

### 🔹 변경된 점
- **메서드 이름 변경**
  - `getAccuracySessionsByUser(Long userId)` → `getByUserId(Long userId)`
  - 간결하게 바꿈

- **메서드 삭제**
  - `getAccuracySessionsBySong(Long songId)`

- **메서드 추가**
  - `getByUserAndSong(Long userId, Long songId)`


---


---

##  **요약**
###  주요 변경 사항
- **메서드 이름 변경 및 간결화** (`getChallengeSessionsByUser` → `getByUserId`)
- **중복 코드 제거 및 최적화** (`getUserById`, `getSongById` 추가)
- **예외 처리 강화** (`UserNotFoundException`, `SongNotFoundException` 추가)

