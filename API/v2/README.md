# spring boot
수정 사항
- 사용자 객체 불러올 때  @AuthenticationPrincipal User user -> @AuthenticationPrincipal CustomUserDetails user
- AppUser entity name필드 삭제 및 username필드 추가 -> UserRepository : Optional<AppUser> findByUsername(String username); 추가 / Service files : findByName -> findByUsername 변경
- Song entity 연습 모드 관련 필드 타입 변경 : int (primitive type (int)이라서 Hibernate가 값을 주입할 수 없어서 터지는 오류) -> Integer
# flask
# redis
