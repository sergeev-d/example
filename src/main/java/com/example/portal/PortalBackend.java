package com.example.portal;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.impl.Http2ServerResponseImpl;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BasicAuthHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.impl.RouterImpl;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PortalBackend {
    public final static Logger logger = LoggerFactory.getLogger(PortalBackend.class);
    private int port;
    private final Vertx vertx;
    private final HttpServer httpServer;

    public PortalBackend(int port) {
        this.port = port;
        this.vertx = Vertx.vertx();
        this.httpServer = vertx.createHttpServer(new HttpServerOptions());
    }

    public PortalBackend run(){
        initHttpServer();
        return this;
    }

    private void initHttpServer(){
        Router router = new RouterImpl(vertx);
        router.route().method(HttpMethod.GET).path("/").handler(routingCtx -> {
            routingCtx.addCookie(Cookie.cookie("othercookie", "somevalue"));

            HttpServerResponse response = routingCtx.response();
            response.putHeader("contentType", "application/json");
            response.end(Json.encode("{hello world!}"));
        });
        router.route().method(HttpMethod.GET).path("/login").handler(routingCtx -> {
            if (!routingCtx.request().params().contains("user")){
                routingCtx.fail(400);
                routingCtx.response().end("User not found");
            } else {
                HttpServerResponse response = routingCtx.response();
                response.putHeader("contentType", "application/json");
                response.end(Json.encode("{Auth is passed}"));
            }

        });

        httpServer.requestHandler(router).listen(port);
    }


    public void stop(){
        httpServer.close();
    }


    public static void main(String[] args) {
        Options options = new Options()
                .addOption(new Option("p", "port", true, "http port"));


        try {
            final CommandLine parser = new BasicParser().parse(options, args);
            final String parsePort = parser.getOptionValue("p");

            try {
                final int port = parsePort != null ? Integer.parseInt(parsePort) : 8080;
                final PortalBackend portalBackend = new PortalBackend(port).run();
                Runtime.getRuntime().addShutdownHook(new Thread(portalBackend::stop, "shutdown-hook"));
            } catch (NumberFormatException e) {
                System.err.println("Incorrect port value " + parsePort);
            }

        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Info: ", options);
        }



    }
}
