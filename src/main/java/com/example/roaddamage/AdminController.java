package com.example.roaddamage;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    // Used by the web page to check admin credentials.
    @GetMapping("/api/admin/me")
    public Map<String, String> me() { return Map.of("role", "ADMIN"); }
}
