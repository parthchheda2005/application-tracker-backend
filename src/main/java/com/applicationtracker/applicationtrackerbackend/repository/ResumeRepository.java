package com.applicationtracker.applicationtrackerbackend.repository;

import com.applicationtracker.applicationtrackerbackend.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

}
