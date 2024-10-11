package com.debricked.rule.engine.service;

import com.debricked.rule.engine.exception.FileValidationException;
import com.debricked.rule.engine.model.DependencyFile;
import com.debricked.rule.engine.utils.FileConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * This class performs following tasks :
 * 1. Validates the input file
 * 2. If validation is success, send the file to Debricked for scanning
 */
@Service
public class FileProcessor {

    @Autowired
    DebrickedScanService scanService;

    public void processFile(MultipartFile file) throws IOException {

        // Step 1 : Validate File
        if (file.isEmpty()) {
            throw new FileValidationException("Uploaded File is empty");
        }

        // Step 2 : Create DependencyFile object to be sent to scan
        DependencyFile fileForScan = createFileForScan(file);

        //Step 3 : Invoke API to scan the uploaded file
        scanService.sendFileForScan(fileForScan);

    }

    private DependencyFile createFileForScan(MultipartFile multipartFile) throws IOException {
        File file = FileConverter.convertToFile(multipartFile);
        return new DependencyFile("id", file.getName(), "IN_PROGRESS", file);
    }



}
