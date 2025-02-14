package capston.capston_spring.repository;

import capston.capston_spring.entity.RecordedVideo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordedVideoRepository extends JpaRepository<RecordedVideo, Long> {

    List<RecordedVideo> findByUserId(Long userId);

    List<RecordedVideo> findByUserIdAndMode(Long userId, String practice);

    void deleteByUserId(Long id);
}
