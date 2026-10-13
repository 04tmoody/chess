package server;

import com.google.gson.Gson;
import handler.RegisterHandler;
import io.javalin.*;
import io.javalin.http.Context;
import service.AlreadyTakenException;
import service.AuthService;
import service.GameService;
import service.UserService;
import model.UserData;
import model.AuthData;
import model.GameData;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    private final UserService userService = new UserService();
    private final AuthService authService = new AuthService();
    private final GameService gameService = new GameService();
    private final RegisterHandler registerHandler = new RegisterHandler(userService, authService);

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("/db", this::clear);
        javalin.post("/user", registerHandler::handle);
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
