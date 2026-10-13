package server;

import io.javalin.*;
import io.javalin.http.Context;
import service.AuthService;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;
    private final UserService userService = new UserService();
    private final AuthService authService = new AuthService();
    private final GameService gameService = new GameService();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("/db", this::clear);
    }

    private void clear(Context ctx) {
        userService.clear();
        authService.clear();
        gameService.clear();
        ctx.status(200);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
