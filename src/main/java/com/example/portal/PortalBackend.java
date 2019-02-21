package com.example.portal;

import com.example.portal.db.DBH2ServiceImpl;
import com.example.portal.db.DBService;
import com.example.portal.db.OnComplete;
import com.example.portal.handler.CreateUserHandler;
import com.example.portal.handler.GetUserHandler;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.impl.RouterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PortalBackend {
    public final static Logger logger = LoggerFactory.getLogger(PortalBackend.class);
    private final Vertx vertx;
    private final HttpServer httpServer;
    private DBService dbService;
    private JsonObject config;

    public PortalBackend() {
        this.vertx = Vertx.vertx();
        this.httpServer = vertx.createHttpServer(new HttpServerOptions());

    }

    public PortalBackend run(){
        initConfig(() -> {
            JDBCClient jdbcClient = JDBCClient.createShared(vertx, config, "test");
            dbService = new DBH2ServiceImpl(jdbcClient);

            initDatabase();
            initHttpServer();
        });

        return this;
    }

    private void initHttpServer(){
        Router router = new RouterImpl(vertx);
        router.post().path("/user").handler(BodyHandler.create()).handler(new CreateUserHandler(dbService));
        router.get().path("/user/:id").handler(new GetUserHandler(dbService));

        httpServer.requestHandler(router)
                        .listen(config.getInteger("http.port", 8080));


        logger.info("Service is successfully started");
    }

    private void initDatabase(){
        dbService.init();
    }


    public void stop(){
        httpServer.close();
        dbService.stop();
    }

    public void initConfig(OnComplete complete){
        final ConfigStoreOptions env = new ConfigStoreOptions()
                .setType("file")
                .setFormat("json")
                .setConfig(new JsonObject()
                        .put("path", "config.json")
                );

        final ConfigStoreOptions db = new ConfigStoreOptions()
                .setType("file")
                .setFormat("json")
                .setConfig(new JsonObject()
                        .put("path", "dbconfig.json")
                );


        final ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(env).addStore(db);
        final ConfigRetriever configRetriever = ConfigRetriever.create(vertx, options);

        configRetriever.getConfig(c -> {
            if (c.succeeded()){
                this.config = c.result();
                complete.done();
            } else {
                throw new InternalError(c.cause());
            }
        });

    }

    public static void main(String[] args) {
        final PortalBackend portalBackend = new PortalBackend().run();
        Runtime.getRuntime().addShutdownHook(new Thread(portalBackend::stop, "shutdown-hook"));
    }
}
