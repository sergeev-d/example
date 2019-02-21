package com.example.portal.handler;

import io.vertx.ext.web.api.validation.ValidationHandler;

public interface ValidatorHolder {
    ValidationHandler getValidation();
}
