package com.example.portal.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AssessmentList {
    private List<AssessmentPreview> assessments;
    private AtomicInteger assessmentCount;

    public AssessmentList() {
        this.assessments = new ArrayList<>();
        this.assessmentCount = new AtomicInteger(0);
    }

    public List<AssessmentPreview> getAssessments() {
        return assessments;
    }

    public int getAssessmentCount() {
        return assessmentCount.get();
    }

    public void addAssessmentPreview(AssessmentPreview assessmentPreview){
        assessments.add(assessmentPreview);
        assessmentCount.addAndGet(1);
    }
}


