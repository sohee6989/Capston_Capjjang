// 챌린지 모드에서 사용할 가상 배경을 데이터베이스에서 불러오거나 저장

package com.example._4.repository;

import com.example._4.entity.ChallengeBackground;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeBackgroundRepository extends JpaRepository<ChallengeBackground, Long> {
}
