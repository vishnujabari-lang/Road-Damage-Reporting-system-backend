package com.example.roaddamage;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Report {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 1000)
    private String description;
    private double latitude;
    private double longitude;
    @Enumerated(EnumType.STRING)
    private Severity severity = Severity.MEDIUM;
    @Enumerated(EnumType.STRING)
    private Status status = Status.REPORTED;
    private String photoFileName;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double v) { this.latitude = v; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double v) { this.longitude = v; }
    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity s) { this.severity = s; }
    public Status getStatus() { return status; }
    public void setStatus(Status s) { this.status = s; }
    public String getPhotoFileName() { return photoFileName; }
    public void setPhotoFileName(String f) { this.photoFileName = f; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
