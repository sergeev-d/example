package com.example.portal.db;

import com.example.portal.entity.User;

public interface GetUserCallback {
    void onSuccess(User user);
    void onError(Throwable t);
    void userNotFound();
}
