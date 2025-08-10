package com.applicationtracker.applicationtrackerbackend.service;

import com.applicationtracker.applicationtrackerbackend.dto.GetApplicationsDto;
import com.applicationtracker.applicationtrackerbackend.dto.SaveApplicationDto;
import com.applicationtracker.applicationtrackerbackend.model.Application;
import com.applicationtracker.applicationtrackerbackend.model.Resume;
import com.applicationtracker.applicationtrackerbackend.model.User;
import com.applicationtracker.applicationtrackerbackend.model.enums.Status;
import com.applicationtracker.applicationtrackerbackend.repository.ApplicationRepository;
import com.applicationtracker.applicationtrackerbackend.repository.ResumeRepository;
import com.applicationtracker.applicationtrackerbackend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                              ResumeRepository resumeRepository,
                              UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }

    public GetApplicationsDto getApplications(int page, int pageSize, String sortBy) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable;
        if (sortBy == null || sortBy.isBlank()) {
            pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        } else if (sortBy.equals("createdAt") || sortBy.equals("updatedAt")) {
            pageable = PageRequest.of(page, pageSize, Sort.by(sortBy).descending());
        } else {
            pageable = PageRequest.of(page, pageSize, Sort.by(sortBy).ascending());
        }
        Page<Application> apps = applicationRepository.findByUserId(user.getId(), pageable);

        List<Application> content = apps.getContent();
        long totalElements = apps.getTotalElements();
        int totalPages = apps.getTotalPages();

        return new GetApplicationsDto(totalElements, totalPages, content);
    }

    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Application createApplication(SaveApplicationDto req) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Resume resume = resumeRepository.findById(req.getResumeId())
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        Application app = new Application(user, resume, req.getCompany(), req.getPosition());

        return applicationRepository.save(app);
    }

    public Application updateStatus(Long id, String status) throws RuntimeException {
        Application application = applicationRepository.findById(id).orElse(null);
        if (application == null) throw new RuntimeException("No application with the id");

        Status newStatus;
        try {
            newStatus = Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new RuntimeException(status + " is not a type of status");
        }
        application.setStatus(newStatus);

        return applicationRepository.save(application);
    }

    public Application updateApplication(Long id, SaveApplicationDto dto) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
        Resume resume = resumeRepository.findById(dto.getResumeId())
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        application.setResume(resume);
        application.setCompany(dto.getCompany());
        application.setPosition(dto.getPosition());

        return applicationRepository.save(application);
    }

    public void deleteApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
        applicationRepository.delete(application);
    }
}
