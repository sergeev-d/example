package com.example.portal.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AssessmentList {
    private List<AssessmentPreview> assessments;
    private AtomicInteger assessmentsCount;

    public AssessmentList() {
        this.assessments = new ArrayList<>();
        this.assessmentsCount = new AtomicInteger(0);
    }

    public List<AssessmentPreview> getAssessments() {
        return assessments;
    }

    public int getAssessmentsCount() {
        return assessmentsCount.get();
    }

    public void addAssessmentPreview(AssessmentPreview assessmentPreview){
        assessments.add(assessmentPreview);
        assessmentsCount.addAndGet(1);
    }
}


