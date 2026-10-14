package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import model.request.LoginRequest;
import service.*;

import java.util.Map;

public class LoginHandler {
    private final UserService userService;
    private final AuthService authService;

    public LoginHandler(UserService userService, AuthService authService, GameService gameService) {
        this.userService = userService;
        this.authService = authService;
    }

    public void handle(Context ctx) {
        LoginRequest req = new Gson().fromJson(ctx.body(), LoginRequest.class);
        if (req == null || req.username() == null || req.password() == null) {
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", "Error: bad request")));
            return;
        }
        UserData user;
        try {
            user = userService.login(req);
        } catch (UnauthorizedException e) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
            return;
        }
        AuthData auth = authService.login(user.username());
        var response = Map.of(
                "username", user.username(),
                "authToken", auth.authToken()
        );
        ctx.status(200);
        ctx.result(new Gson().toJson(response));
    }
}