package com.example.portal.db;

import com.example.portal.entity.User;

public interface DBService {
    void createUser(User user, CreateUserCallback callback);
    void getUser(int id, GetUserCallback callback);
    void init();
    void stop();
}
