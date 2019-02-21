package com.example.portal.handler;

import com.example.portal.db.CreateUserCallback;
import com.example.portal.db.DBService;
import com.example.portal.entity.User;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.api.validation.HTTPRequestValidationHandler;
import io.vertx.ext.web.api.validation.ValidationHandler;
import org.apache.http.HttpStatus;


public class CreateUserHandler implements ValidatorHolder, Handler<RoutingContext> {
    private DBService dbService;

    public CreateUserHandler(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public ValidationHandler getValidation() {
        return HTTPRequestValidationHandler.create().addJsonBodySchema("application/json");
    }

    @Override
    public void handle(RoutingContext ctx) {
        User user = Json.decodeValue(ctx.getBody(), User.class);

        dbService.createUser(user, new CreateUserCallback() {
            @Override
            public void onSuccessfullyCreated() {
                ctx.response()
                        .setStatusCode(HttpStatus.SC_CREATED)
                        .end();

            }

            @Override
            public void onUserAlreadyExists() {
                ctx.fail(HttpStatus.SC_CONFLICT);
            }

            @Override
            public void onUnexpectedError(Throwable e) {
                ctx.fail(HttpStatus.SC_BAD_GATEWAY, e);
            }
        });
    }
}
