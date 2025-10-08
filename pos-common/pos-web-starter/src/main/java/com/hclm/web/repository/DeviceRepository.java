package com.hclm.web.repository;

import com.hclm.web.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

/**
 * Device实体的Repository接口
 * 提供设备相关的数据访问方法
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, String>, JpaSpecificationExecutor<Device> {
    /**
     * 按设备id查找
     *
     * @param id id
     * @return {@link Optional }<{@link Device }>
     */
    Optional<Device> findByDeviceId(String id);

    /**
     * 更新状态
     *
     * @param id     id
     * @param status 状态
     */
    @Modifying
    @Query("UPDATE Device device SET device.status = :status WHERE device.deviceId = :id")
    int updateStatus(@Param("id") String id, @Param("status") String status);

    @Modifying
    @Query("UPDATE Device device SET device.status = :status,device.lastLoginAt = :lastLoginAt WHERE device.deviceId = :id")
    int updateStatusAndLastLoginAt(@Param("id") String id, @Param("status") String status, @Param("lastLoginAt") Instant lastLoginAt);

    /**
     * 更新状态和上次联机时间
     *
     * @param id     id
     * @param status 状态
     */
    @Modifying
    @Query("UPDATE Device device SET device.status = :status,device.lastOnline = :lastOnline WHERE device.deviceId = :id")
    int updateStatusAndLastOnline(@Param("id") String id, @Param("status") String status, @Param("lastOnline") Instant lastOnline);

    /**
     * 按id进行软删除
     *
     * @param deviceId 设备id
     */
    @Modifying
    @Query("UPDATE Device d SET d.isDeleted = true WHERE d.deviceId = :deviceId")
    int softDeleteById(@Param("deviceId") String deviceId);
}
