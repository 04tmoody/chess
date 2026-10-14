package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import model.UserData;
import model.request.*;
import model.result.*;
import service.*;

import java.util.Map;

public class Server {

    private final AuthService authService = new AuthService();
    private final UserService userService = new UserService(authService);
    private final GameService gameService = new GameService(authService);
    private final Javalin javalin;

    public Server() {

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("/db",this::clear);
        javalin.post("/user", this::register);
        javalin.post("/session", this::login);
        javalin.delete("/session",this::logout);
        javalin.get("/game",this::listGames);
        javalin.post("/game",this::createGame);
    }

    private void clear(Context ctx) {
        userService.clear();
        authService.clear();
        gameService.clear();
        ctx.status(200);
        ctx.result(new Gson().toJson(new Object()));
    }

    private void register(Context ctx) throws Exception {
        RegisterRequest req = new Gson().fromJson(ctx.body(), RegisterRequest.class);
        try {
            RegisterResult result = userService.register(req);
            ctx.status(200);
            ctx.result(new Gson().toJson(result));
        } catch (AlreadyTakenException e) {
            ctx.status(403);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        } catch (Exception e) {
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }

    private void login(Context ctx) {
        LoginRequest req = new Gson().fromJson(ctx.body(), LoginRequest.class);
        try {
            LoginResult result = userService.login(req);
            ctx.status(200);
            ctx.result(new Gson().toJson(result));
        } catch (AlreadyTakenException e) {
            ctx.status(403);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        } catch (UnauthorizedException e) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        } catch (Exception e) {
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }

    private void logout(Context ctx) {
        String auth = ctx.header("Authorization");
        try {
            authService.logout(auth);
            ctx.status(200);
            ctx.result(new Gson().toJson(new Object()));
        } catch (UnauthorizedException e) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }

    private void listGames(Context ctx) {
        String auth = ctx.header("Authorization");
        try {
            ListGamesResult result = gameService.listGames(auth);
            ctx.status(200);
            ctx.result(new Gson().toJson(result));
        } catch (UnauthorizedException e) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }

    private void createGame(Context ctx) {
        CreateGameRequest req = new Gson().fromJson(ctx.body(), CreateGameRequest.class);
        String auth = ctx.header("Authorization");
        try {
            CreateGameResult result = gameService.createGame(req,auth);
            ctx.status(200);
            ctx.result(new Gson().toJson(result));
        } catch (UnauthorizedException e) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        } catch (Exception e) {
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
