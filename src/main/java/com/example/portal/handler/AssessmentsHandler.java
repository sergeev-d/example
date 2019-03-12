package com.example.portal.handler;

import com.example.portal.entity.AssessmentList;
import com.example.portal.entity.AssessmentPreview;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.apache.http.HttpStatus;

import java.util.Arrays;

public class AssessmentsHandler implements Handler<RoutingContext> {
    @Override
    public void handle(RoutingContext ctx) {
        final AssessmentList assessmentList = new AssessmentList();
        final AssessmentPreview ap1 = new AssessmentPreview();
        ap1.setId(1l);
        ap1.setName("Test efficiency");
        ap1.setDescription("Test efficiency for small company");
        ap1.setApplicationFields(Arrays.asList("Filed area2","Filed area2"));

        final AssessmentPreview ap2 = new AssessmentPreview();
        ap2.setId(2l);
        ap2.setName("Test efficiency2");
        ap2.setDescription("Test efficiency for small company");
        ap2.setApplicationFields(Arrays.asList("Filed area1","Filed area2","Filed area3"));

        assessmentList.addAssessmentPreview(ap1);
        assessmentList.addAssessmentPreview(ap2);

        ctx.response()
                .setStatusCode(HttpStatus.SC_OK)
                .end(Json.encode(assessmentList));
    }
}
