package com.example.portal.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.api.validation.ValidationHandler;

public class FinalRegistrationHandler implements ValidatorHolder, Handler<RoutingContext> {
    @Override
    public ValidationHandler getValidation() {
        return null;
    }

    @Override
    public void handle(RoutingContext event) {

    }
}
