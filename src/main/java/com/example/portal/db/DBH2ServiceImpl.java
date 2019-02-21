package com.example.portal.db;

import com.example.portal.entity.User;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import org.h2.tools.Server;

import java.sql.SQLException;
import java.util.List;

public class DBH2ServiceImpl implements DBService {
    private JDBCClient client;
    private Server server;
    //private Server webServer;

    public DBH2ServiceImpl(JDBCClient client, int port) {
        this.client = client;
        try {
            this.server = Server.createTcpServer("-tcp","-tcpAllowOthers","-tcpPort", Integer.toString(port));
            //this.webServer = Server.createWebServer("-web","-webAllowOthers","-webPort","8082");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUser(User user, CreateUserCallback callback) {
        String sql = "insert into portal.users(name, login, password) values(?,?,?)";
        JsonArray objects = new JsonArray()
                .add(user.getName())
                .add(user.getLogin())
                .add(user.getPassword());

        client.getConnection(conn -> {
            if (conn.succeeded()){
                SQLConnection sqlConnection =  conn.result();
                sqlConnection
                        .setAutoCommit(false, res -> {})
                        .queryWithParams(sql, objects, res -> {
                            if (res.succeeded()){
                                sqlConnection.commit(ok ->
                                        callback.onSuccessfullyCreated());
                            } else {
                                callback.onUnexpectedError(res.cause());
                            }
                        });
                ;
            }
        });
    }

    @Override
    public void getUser(int id, GetUserCallback callback) {
        String sql = "select name, login, password from portal.users where id = ?";
        JsonArray objects = new JsonArray().add(id);

        client.getConnection(c -> {
            if (c.succeeded()){
                SQLConnection conn = c.result();
                    conn.setAutoCommit(false, res -> {})
                        .queryWithParams(sql, objects, res ->{
                            if (res.succeeded()){
                                final List<JsonObject> rows = res.result().getRows();
                                if (!rows.isEmpty()){
                                    final JsonObject user = rows.get(0);
                                    callback.onSuccess(new User(
                                            user.getString("NAME"),
                                            user.getString("LOGIN"),
                                            user.getString("PASSWORD"))
                                    );
                                    conn.commit(commit -> {
                                        // successfully committed
                                    });
                                } else {
                                    callback.userNotFound();
                                    closeConnection(conn);
                                }

                            } else {
                                callback.onError(res.cause());
                                closeConnection(conn);
                            }
                        })
                ;
            }
        });
    }

    @Override
    public void init() {
        try {
            server.start();

            client.getConnection(c -> {
                if (c.succeeded()){
                    SQLConnection conn = c.result();

                    conn.execute(SQLS.dropObjects(), res ->
                            conn.execute(SQLS.createSchema(), res2 ->
                                    conn.execute(SQLS.createTableUser(), res3 -> {
                            })));
                }
            });

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        //webServer.stop();
        server.stop();
    }

    public void closeConnection(SQLConnection connection){
        if (connection != null){
            connection.close();
        }
    }
}
