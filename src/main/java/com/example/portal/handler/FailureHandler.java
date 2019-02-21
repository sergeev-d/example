package com.example.portal.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.api.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FailureHandler implements Handler<RoutingContext> {
    private static Logger logger = LoggerFactory.getLogger(FailureHandler.class);

    public static final FailureHandler INSTANCE = new FailureHandler();

    private FailureHandler() {
    }

    @Override
    public void handle(RoutingContext ctx) {
        final Throwable failure = ctx.failure();
        if (failure instanceof ValidationException) {
            logger.warn("Request validation exception: {}", failure.getMessage());
            ctx.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end(Json.encode(failure.getMessage()));
        } else {
            logger.error("Unexpected error occurred during handling the request '{}' : '{}'",
                    ctx.request().path(),
                    String.valueOf(ctx.failure()));
            logger.debug("Stacktrace", ctx.failure());
            ctx.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end(Json.encode(failure.getMessage()));
        }
    }
}
