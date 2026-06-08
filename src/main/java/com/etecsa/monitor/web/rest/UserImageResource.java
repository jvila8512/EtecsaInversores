package com.etecsa.monitor.web.rest;

import com.etecsa.monitor.domain.User;
import com.etecsa.monitor.repository.UserRepository;
import com.etecsa.monitor.security.SecurityUtils;
import com.etecsa.monitor.service.UserService;
import java.io.IOException;
import java.nio.file.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class UserImageResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserImageResource.class);
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final String[] ALLOWED_TYPES = { "image/jpeg", "image/png", "image/gif", "image/webp" };

    private final UserRepository userRepository;
    private final UserService userService;

    @Value("${app.upload.dir:uploads/profile-images}")
    private String uploadDir;

    public UserImageResource(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping(value = "/account/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfileImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.badRequest().body("File too large (max 5MB)");
        }
        String contentType = file.getContentType();
        if (contentType == null || !isValidType(contentType)) {
            return ResponseEntity.badRequest().body("Invalid file type. Allowed: JPEG, PNG, GIF, WebP");
        }

        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow();
        User user = userRepository.findOneByLogin(userLogin).orElseThrow();

        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            String extension = getExtension(contentType);
            String filename = "user-" + user.getId() + "-" + System.currentTimeMillis() + extension;
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String imageUrl = "/content/profile-images/" + filename;

            userService.updateUser(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getLangKey(),
                imageUrl
            );

            LOG.info("Profile image uploaded for user {}: {}", userLogin, imageUrl);
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            LOG.error("Failed to upload profile image for user {}", userLogin, e);
            return ResponseEntity.internalServerError().body("Upload failed");
        }
    }

    @DeleteMapping("/account/image")
    public ResponseEntity<Void> deleteProfileImage() {
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow();
        User user = userRepository.findOneByLogin(userLogin).orElseThrow();

        String oldImageUrl = user.getImageUrl();
        if (oldImageUrl != null && oldImageUrl.startsWith("/content/profile-images/")) {
            try {
                String oldFilename = oldImageUrl.substring(oldImageUrl.lastIndexOf('/') + 1);
                Path oldFile = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(oldFilename);
                Files.deleteIfExists(oldFile);
            } catch (IOException e) {
                LOG.warn("Could not delete old profile image for user {}", userLogin, e);
            }
        }

        userService.updateUser(
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getLangKey(),
            null
        );

        LOG.info("Profile image deleted for user {}", userLogin);
        return ResponseEntity.noContent().build();
    }

    private boolean isValidType(String contentType) {
        for (String allowed : ALLOWED_TYPES) {
            if (allowed.equalsIgnoreCase(contentType)) return true;
        }
        return false;
    }

    private String getExtension(String contentType) {
        return switch (contentType.toLowerCase()) {
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            default -> ".jpg";
        };
    }
}
