package com.applicationtracker.applicationtrackerbackend.repository;

import com.applicationtracker.applicationtrackerbackend.model.Application;
import com.applicationtracker.applicationtrackerbackend.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Page<Application> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT FUNCTION('DATE', a.createdAt) AS day, COUNT(a) AS count " +
            "FROM Application a " +
            "WHERE a.user.id = :userId " +
            "AND a.createdAt >= :fromDate " +
            "GROUP BY FUNCTION('DATE', a.createdAt) " +
            "ORDER BY day ASC")
    List<Object[]> countApplicationsPerDayLast7Days(@Param("userId") Long userId,
                                                    @Param("fromDate") LocalDateTime fromDate);

    @Query("SELECT a.resume.name, COUNT(a) " +
            "FROM Application a " +
            "WHERE a.user.id = :userId " +
            "AND a.status IN (:statuses) " +
            "GROUP BY a.resume.name")
    List<Object[]> countApplicationsByResumeNameWithStatuses(@Param("userId") Long userId,
                                                             @Param("statuses") List<Status> statuses);

    @Query("SELECT a.status, COUNT(a) " +
            "FROM Application a " +
            "WHERE a.user.id = :userId " +
            "GROUP BY a.status")
    List<Object[]> countApplicationsByStatus(@Param("userId") Long userId);
}
