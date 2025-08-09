package com.applicationtracker.applicationtrackerbackend.service;

import com.applicationtracker.applicationtrackerbackend.dto.CreateResumeDto;
import com.applicationtracker.applicationtrackerbackend.dto.UpdateResumeDto;
import com.applicationtracker.applicationtrackerbackend.model.Resume;
import com.applicationtracker.applicationtrackerbackend.model.User;
import com.applicationtracker.applicationtrackerbackend.repository.ResumeRepository;
import com.applicationtracker.applicationtrackerbackend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeService {

    private ResumeRepository resumeRepository;
    private UserRepository userRepository;

    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }

    public Resume createResume(CreateResumeDto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Resume resume = new Resume(user, dto.getAzureBlobPath(), dto.getName());
        return resumeRepository.save(resume);
    }

    public List<Resume> getAllResumes() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return resumeRepository.findAllByUserOrderByCreatedAtDesc(user);
    }

    public void deleteResume(Long id) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resume not found, can't delete"));
        // TODO: handle deleting resume from firebase
        resumeRepository.delete(resume);
    }

    public Resume updateResume(Long id, UpdateResumeDto dto) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No resume found with given id"));

        resume.setName(dto.getName());
        resume.setAzureBlobPath(dto.getAzureBlobPath());

        return resumeRepository.save(resume);
    }
}
