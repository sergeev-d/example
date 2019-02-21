package com.example.portal.handler;


import com.example.portal.db.DBService;
import com.example.portal.db.GetUserCallback;
import com.example.portal.entity.User;
import com.example.portal.utils.ResourceParams;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.api.RequestParameters;
import io.vertx.ext.web.api.validation.HTTPRequestValidationHandler;
import io.vertx.ext.web.api.validation.ParameterTypeValidator;
import io.vertx.ext.web.api.validation.ValidationHandler;
import org.apache.http.HttpStatus;

public class GetUserHandler implements ValidatorHolder, Handler<RoutingContext> {
    private DBService dbService;

    public GetUserHandler(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public ValidationHandler getValidation() {
        return HTTPRequestValidationHandler.
                create()
                .addPathParamWithCustomTypeValidator(ResourceParams.USER_ID,
                ParameterTypeValidator.createIntegerTypeValidator(null), false);
    }

    @Override
    public void handle(RoutingContext ctx) {
        int user_id = ((RequestParameters) ctx.get("parsedParameters")).pathParameter(ResourceParams.USER_ID).getInteger();

        dbService.getUser(user_id, new GetUserCallback() {
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
