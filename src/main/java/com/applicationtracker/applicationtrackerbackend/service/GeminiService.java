package com.applicationtracker.applicationtrackerbackend.service;

import com.applicationtracker.applicationtrackerbackend.dto.ResumeRatingResponseDto;
import com.applicationtracker.applicationtrackerbackend.model.Application;
import com.applicationtracker.applicationtrackerbackend.model.User;
import com.applicationtracker.applicationtrackerbackend.repository.ApplicationRepository;
import com.applicationtracker.applicationtrackerbackend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    private final Client client;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final AzureBlobService azureBlobService;

    public GeminiService(@Value("${gemini.api.key}") String apiKey,
                         ApplicationRepository applicationRepository,
                         UserRepository userRepository,
                         AzureBlobService azureBlobService) {
        this.client = Client.builder().apiKey(apiKey).build();
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.azureBlobService = azureBlobService;
    }

    public ResumeRatingResponseDto getResumeRating() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Application mostRecentApplication = applicationRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId())
                .orElseThrow(() -> new RuntimeException("No applications found for user"));

        try {
            String resumeContent = azureBlobService.downloadBlobAsText(
                    mostRecentApplication.getResume().getAzureBlobPath()
            );

            System.out.println("Resume Content: " + resumeContent);

            String rawResponse = client.models.generateContent("gemini-2.0-flash",
                            "Rate the following resume on a scale of 0 to 100: \n\n" + resumeContent +
                                    "\n\nPlease do not make any reference to personal details on the resume at all." +
                                    "Refrain from giving feedback on links, if they are mentioned that means that it is probably clickable." +
                            "Please provide your response as a JSON object with a key 'rating' with an integer value, " +
                                    "which would be the resume rating and 2 arrays of strings with keys 'pros' " +
                                    "and 'improvements' for feedback.",

                            null)
                    .text();

            String cleanedResponse = rawResponse
                    .replaceAll("^```json\\s*", "")
                    .replaceAll("```$", "")
                    .trim();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(cleanedResponse, ResumeRatingResponseDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get resume rating", e);
        }
    }

}
