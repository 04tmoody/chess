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

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("/db",
                new ClearHandler(userService, authService, gameService)::handle);
        javalin.post("/user",
                new RegisterHandler(userService, authService)::handle);
        javalin.post("/session",
                new LoginHandler(userService, authService, gameService)::handle);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
