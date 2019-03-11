package com.example.portal;

import com.example.portal.db.DBH2ServiceImpl;
import com.example.portal.db.DBService;
import com.example.portal.handler.*;
import com.example.portal.utils.ResourceParams;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.api.validation.ValidationHandler;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.impl.RouterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class PortalBackend {
    public final static Logger logger = LoggerFactory.getLogger(PortalBackend.class);
    private final Vertx vertx;
    private final HttpServer httpServer;
    private DBService dbService;
    private ConfigRetrieverOptions options;
    private JsonObject config;

    public PortalBackend() {
        this.vertx = Vertx.vertx();
        this.httpServer = vertx.createHttpServer(new HttpServerOptions());
    }

    public PortalBackend run(){
        initConfigOption();
        ConfigRetriever.create(vertx, options).getConfig(res -> {
            this.config = res.result();

            initDatabaseService();
            initDatabase();
            initHttpServer();
        });

        return this;
    }

    private void initHttpServer(){
        final Router router = new RouterImpl(vertx);

        final Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add("x-requested-with");
        allowedHeaders.add("Access-Control-Allow-Origin");
        allowedHeaders.add("origin");
        allowedHeaders.add("Content-Type");
        allowedHeaders.add("accept");
        allowedHeaders.add("X-PINGARUNER");

        final Set<HttpMethod> allowedMethods = new HashSet<>();
        allowedMethods.add(HttpMethod.GET);
        allowedMethods.add(HttpMethod.POST);
        allowedMethods.add(HttpMethod.OPTIONS);
        allowedMethods.add(HttpMethod.DELETE);
        allowedMethods.add(HttpMethod.PATCH);
        allowedMethods.add(HttpMethod.PUT);

        router.route()
                .handler(BodyHandler.create())
                .handler(CorsHandler.create("*")
                        .allowedHeaders(allowedHeaders)
                        .allowedMethods(allowedMethods));

        initRoute(router.get().path("/assessments"), new AssessmentsHandler());

//        initRoute(router.get().path("/reg"), new RegistrationHandler());
//        initRoute(router.post().path("/reg"), new FinalRegistrationHandler());
        initRoute(router.post().path("/user"), new CreateUserHandler(dbService));
        initRoute(router.get().path(String.format("/user/:%s", ResourceParams.USER_ID)), new GetUserHandler(dbService));
        initRoute(router.post().path("/login"), new LoginHandler());

        httpServer.requestHandler(router)
                        .listen(config.getInteger("http.port", 8080));

        logger.info("Service is successfully started");

    }

    private void initRoute(Route route, Handler<RoutingContext> resourceHandler) {
        if (resourceHandler instanceof ValidatorHolder) {
            final ValidationHandler validator = ((ValidatorHolder) resourceHandler).getValidation();
            if (validator != null) {
                route.handler(validator);
            }
        }
        route.handler(resourceHandler);
        route.failureHandler(FailureHandler.INSTANCE);
    }

    private void initDatabaseService(){
        JDBCClient jdbcClient = JDBCClient.createShared(vertx, config);
        dbService = new DBH2ServiceImpl(jdbcClient, config.getInteger("port"));
    }

    private void initDatabase(){
        dbService.init();
    }

    public void stop(){
        httpServer.close();
        dbService.stop();
    }

    private void initConfigOption(){
        final ConfigStoreOptions server = new ConfigStoreOptions()
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

        this.options = new ConfigRetrieverOptions()
                .addStore(server)
                .addStore(db);
    }

    public static void main(String[] args) {
        final PortalBackend portalBackend = new PortalBackend().run();
        Runtime.getRuntime().addShutdownHook(new Thread(portalBackend::stop, "shutdown-hook"));
    }
}
