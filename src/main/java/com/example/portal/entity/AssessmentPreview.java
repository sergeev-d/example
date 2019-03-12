package com.example.portal.entity;

import java.util.List;

public class AssessmentPreview {
    private long id;
    private String name;
    private String description;
    private List<String> applicationFields;


    public AssessmentPreview() {
    }

    public AssessmentPreview(long id, String name, String description, List<String> applicationFields) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.applicationFields = applicationFields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getApplicationFields() {
        return applicationFields;
    }

    public void setApplicationFields(List<String> applicationFields) {
        this.applicationFields = applicationFields;
    }
}
