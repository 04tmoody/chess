package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.*;

import java.util.Map;

public class ClearHandler {
    private final UserService userService;
    private final AuthService authService;
    private final GameService gameService;

    public ClearHandler(UserService userService, AuthService authService, GameService gameService) {
        this.userService = userService;
        this.authService = authService;
        this.gameService = gameService;
    }

    public void handle(Context ctx) {
        userService.clear();
        authService.clear();
        gameService.clear();
        ctx.status(200);
        ctx.result(new Gson().toJson(new Object()));
    }
}