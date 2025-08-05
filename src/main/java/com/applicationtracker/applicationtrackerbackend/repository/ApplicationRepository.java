package com.applicationtracker.applicationtrackerbackend.repository;

import com.applicationtracker.applicationtrackerbackend.model.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    public Page<Application> findByUserId(Long userId, Pageable pageable);
}
