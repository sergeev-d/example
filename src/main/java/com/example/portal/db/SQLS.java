package com.example.portal.db;

public class SQLS {
    private SQLS() {}

    public static String createSchema(){
        return "CREATE SCHEMA PORTAL;";
    }

    public static String dropObjects(){
        return "DROP ALL OBJECTS;";
    }

    public static String createTableUser(){
        return "CREATE TEMP TABLE PORTAL.users (" +
                "id IDENTITY PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "login VARCHAR(255) NOT NULL, " +
                "password VARCHAR(255) NOT NULL)" +
                ";";
    }
}
