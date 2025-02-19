## 1. AccuracySessionController
- 사용자의 정확도 세션 조회 (/user/{userId})
- 특정 곡의 정확도 세션 조회 (/song/{songId})
- 정확도 세션 저장 (POST /)
- AI 분석 기반 세션 저장 (POST /analyze)

## 2. AuthController
- 회원가입 (POST /join)
- 로그인 (POST /login)
- OAuth 로그인 (GET /loginNaver, /loginGoogle)
- 로그아웃 (GET /logout)

## 3. ChallengeSessionController
- 특정 사용자의 챌린지 세션 조회 (/user/{userId})
- 특정 곡의 챌린지 세션 조회 (/song/{songId})
- 챌린지 세션 저장 (POST /)
- 배경 및 하이라이트 부분 제공 (GET /background/{songId}, /highlight/{songId})
  
## 4. PasswordResetController
- 비밀번호 재설정 요청 (POST /request)
- 인증 코드 검증 (POST /verify)
- 새 비밀번호 설정 (POST /new-password)
  
## 5. PracticeSessionController
- 연습 모드 곡 리스트 조회 (GET /songs)
- 특정 사용자의 연습 세션 조회 (GET /user/{userId})
- 특정 곡의 연습 세션 조회 (GET /song/{songId})
- 연습 세션 시작 (POST /)
  
## 6. RecordedVideoController  -> VideoService와 통합 필요
- 사용자의 녹화 영상 조회 (/user/{userId})
- 특정 세션(연습/챌린지)의 녹화 영상 조회 (/practice/{sessionId}, /challenge/{sessionId})
- 녹화 영상 저장 (POST /)
  
## 7. SongController
- 모든 곡 조회 (GET /all)
- 특정 제목/아티스트 기반 곡 조회 (GET /title/{title}, /artist/{artist})
- 곡 저장 (POST /)
  
## 8. TestController -> 나중에 제거
- 인증 테스트 (POST /test)
- 루트 페이지 출력 (GET /)

## 9. UserController
- 마이페이지 조회 (GET /home, /profile)
- 프로필 수정 (이름, 비밀번호, 이미지)
- 계정 삭제 (DELETE /profile/deleteAccount)
- 내 비디오 조회 및 수정 (GET /myVideo, POST /myVideo/{videoId}/edit)
- 연습 기록 조회 (GET /history)
