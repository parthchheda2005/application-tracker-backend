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
    private AzureBlobService blobService;

    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository, AzureBlobService blobService) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.blobService = blobService;
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
        blobService.deleteBlob(resume.getAzureBlobPath());
        resumeRepository.delete(resume);
    }

    public Resume updateResume(Long id, UpdateResumeDto dto) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No resume found with given id"));

        if (dto.getAzureBlobPath() != null) {
            blobService.deleteBlob(resume.getAzureBlobPath());
            resume.setAzureBlobPath(dto.getAzureBlobPath());
        }

        if (dto.getName() != null) {
            resume.setName(dto.getName());
        }
        return resumeRepository.save(resume);
    }
}
