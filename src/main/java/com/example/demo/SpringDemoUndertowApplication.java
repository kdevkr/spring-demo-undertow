package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@EnableScheduling
@Controller
@SpringBootApplication
public class SpringDemoUndertowApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDemoUndertowApplication.class, args);
    }

    @ResponseBody
    @PostMapping("/files")
    public ResponseEntity<String> uploadFileHandler(@RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(file.getOriginalFilename());
    }



}
