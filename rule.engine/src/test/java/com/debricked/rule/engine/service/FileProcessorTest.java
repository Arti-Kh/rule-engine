package com.debricked.rule.engine.service;

import com.debricked.rule.engine.exception.FileValidationException;
import com.debricked.rule.engine.model.DependencyFile;
import com.debricked.rule.engine.utils.FileConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FileProcessorTest {

    @InjectMocks
    private FileProcessor fileProcessor;

    @Mock
    private DebrickedScanService scanService;

    @Mock
    private MultipartFile multipartFile;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessFile_FileIsEmpty() {
        // Arrange
        when(multipartFile.isEmpty()).thenReturn(true);

        // Act & Assert
        FileValidationException exception = assertThrows(FileValidationException.class, () -> {
            fileProcessor.processFile(multipartFile);
        });
        assertEquals("Uploaded File is empty", exception.getMessage());
    }
}