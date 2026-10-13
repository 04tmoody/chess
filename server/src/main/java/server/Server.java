package server;

import handler.*;
import io.javalin.*;
import io.javalin.http.Context;
import service.*;

public class Server {

    private final Javalin javalin;

    public Server() {

        UserService userService = new UserService();
        AuthService authService = new AuthService();
        GameService gameService = new GameService();
        RegisterHandler registerHandler = new RegisterHandler(userService, authService);
        ClearHandler clearHandler = new ClearHandler(userService, authService, gameService);

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("/db", clearHandler::handle);
        javalin.post("/user", registerHandler::handle);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
