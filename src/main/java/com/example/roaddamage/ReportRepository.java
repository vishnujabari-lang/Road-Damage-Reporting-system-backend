package com.example.roaddamage;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByStatusOrderByCreatedAtDesc(Status status);
    List<Report> findAllByOrderByCreatedAtDesc();
}
