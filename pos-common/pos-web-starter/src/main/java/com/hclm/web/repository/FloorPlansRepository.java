package com.hclm.web.repository;

import com.hclm.web.entity.FloorPlans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 平面图存储库
 *
 * @author hanhua
 * @since 2025/10/13
 */
@Repository
public interface FloorPlansRepository extends JpaRepository<FloorPlans, Long> {
}
