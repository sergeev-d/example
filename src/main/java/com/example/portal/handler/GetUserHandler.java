package com.example.portal.handler;


import com.example.portal.db.DBService;
import com.example.portal.db.GetUserCallback;
import com.example.portal.entity.User;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.api.validation.HTTPRequestValidationHandler;
import io.vertx.ext.web.api.validation.ParameterType;
import io.vertx.ext.web.api.validation.ValidationHandler;
import org.apache.http.HttpStatus;

public class GetUserHandler implements ValidatorHolder, Handler<RoutingContext> {
    private DBService dbService;

    public GetUserHandler(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public ValidationHandler getValidation() {
        return HTTPRequestValidationHandler.create().addQueryParam("id", ParameterType.INT, true);
    }

    @Override
    public void handle(RoutingContext ctx) {
        int id = Integer.parseInt(ctx.request().getParam("id"));
        dbService.getUser(id, new GetUserCallback() {
            @Override
            public void onSuccess(User user) {
                ctx.response()
                        .setStatusCode(HttpStatus.SC_OK)
                        .end(Json.encode(user));
            }

            @Override
            public void onError(Throwable t) {
                ctx.response()
                        .setStatusCode(HttpStatus.SC_SERVICE_UNAVAILABLE)
                        .end();
            }

            @Override
            public void userNotFound() {
                ctx.response()
                        .setStatusCode(HttpStatus.SC_NOT_FOUND)
                        .end();
            }
        });
    }
}
