package com.example.roaddamage;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportRepository repo;
    private final Path uploadDir;

    public ReportController(ReportRepository repo, @Value("${app.upload-dir}") String dir) throws IOException {
        this.repo = repo;
        this.uploadDir = Paths.get(dir).toAbsolutePath();
        Files.createDirectories(uploadDir);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Report create(@RequestParam String description,
                         @RequestParam double latitude,
                         @RequestParam double longitude,
                         @RequestParam(defaultValue = "MEDIUM") Severity severity,
                         @RequestParam(required = false) MultipartFile photo) throws IOException {
        if (description.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description required");
        Report r = new Report();
        r.setDescription(description);
        r.setLatitude(latitude);
        r.setLongitude(longitude);
        r.setSeverity(severity);
        if (photo != null && !photo.isEmpty()) {
            String type = photo.getContentType();
            if (type == null || !type.startsWith("image/"))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Photo must be an image");
            String name = UUID.randomUUID() + ".img";
            Files.copy(photo.getInputStream(), uploadDir.resolve(name));
            r.setPhotoFileName(name);
        }
        return repo.save(r);
    }

    @GetMapping
    public List<Report> list(@RequestParam(required = false) Status status) {
        return status == null ? repo.findAllByOrderByCreatedAtDesc()
                              : repo.findByStatusOrderByCreatedAtDesc(status);
    }

    @GetMapping("/{id}")
    public Report get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}/status")
    public Report updateStatus(@PathVariable Long id, @RequestParam Status status) {
        Report r = get(id);
        r.setStatus(status);
        return repo.save(r);
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<Resource> photo(@PathVariable Long id) {
        Report r = get(id);
        if (r.getPhotoFileName() == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .body(new FileSystemResource(uploadDir.resolve(r.getPhotoFileName())));
    }
}
