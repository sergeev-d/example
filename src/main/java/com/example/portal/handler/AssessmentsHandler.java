package com.example.portal.handler;

import com.example.portal.entity.AssessmentList;
import com.example.portal.entity.AssessmentPreview;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.apache.http.HttpStatus;

public class AssessmentsHandler implements Handler<RoutingContext> {
    @Override
    public void handle(RoutingContext ctx) {
        final AssessmentList assessmentList = new AssessmentList();
        assessmentList.addAssessmentPreview(new AssessmentPreview());
        assessmentList.addAssessmentPreview(new AssessmentPreview());

        ctx.response()
                .setStatusCode(HttpStatus.SC_OK)
                .end(Json.encode(assessmentList));
    }
}
