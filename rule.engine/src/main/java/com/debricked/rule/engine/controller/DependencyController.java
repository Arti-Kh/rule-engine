package com.debricked.rule.engine.controller;

import com.debricked.rule.engine.service.FileProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Controller class to upload Dependency Files
 */
@RestController
@RequestMapping("/v1/upload")
public class DependencyController {

    @Autowired
    FileProcessor fileProcessor;

    @PostMapping
    public ResponseEntity<String> uploadFiles(@RequestParam("file") MultipartFile file) throws IOException {

        fileProcessor.processFile(file);
        return ResponseEntity.ok("Upload Success");

    }

}
