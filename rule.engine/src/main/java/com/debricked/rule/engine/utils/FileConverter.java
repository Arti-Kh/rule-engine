package com.debricked.rule.engine.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileConverter {

    public static File convertToFile(MultipartFile multipartFile) throws IOException {
        // Create a temporary file
        File tempFile = File.createTempFile("upload-", multipartFile.getOriginalFilename());

        // Transfer the data from the MultipartFile to the temporary file
        multipartFile.transferTo(tempFile);

        return tempFile;
    }
}
