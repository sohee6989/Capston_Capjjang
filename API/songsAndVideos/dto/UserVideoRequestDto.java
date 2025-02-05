// 사용자가 연습, 챌린지 모드에서 녹화한 영상을 데이터베이스에 저장하고 나중에 다시 볼 수 있도록 관리

package com.example._4.repository;

import com.example._4.entity.UserVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserVideoRepository extends JpaRepository<UserVideo, Long> {
    // 사용자의 저장된 비디오 목록 가져오기
    List<UserVideo> findByUserId(Long userId);
}
