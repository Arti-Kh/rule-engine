package com.debricked.rule.engine.model;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class DependencyFile {

    private String id;
    private String name;
    private String status;
    private File file;

    public DependencyFile(String id, String name, String status, File file) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.file = file;
    }
}
