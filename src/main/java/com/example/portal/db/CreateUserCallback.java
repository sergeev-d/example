package com.example.portal.db;

public interface CreateUserCallback {
    void onSuccessfullyCreated();
    void onUserAlreadyExists();
    void onUnexpectedError(Throwable e);
}
