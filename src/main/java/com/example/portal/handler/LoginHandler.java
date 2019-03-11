package com.example.portal.handler;

import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.apache.http.HttpStatus;

public class LoginHandler implements Handler<RoutingContext> {
    @Override
    public void handle(RoutingContext ctx) {
        ctx.response()
                .setStatusCode(HttpStatus.SC_CREATED)
                .end(Json.encode("successfully logged"));
    }
}
