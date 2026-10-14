package handler;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import service.AlreadyTakenException;
import service.AuthService;
import service.UserService;
import io.javalin.http.Context;

import java.util.Map;

public class RegisterHandler {
    private final UserService userService;
    private final AuthService authService;

    public RegisterHandler(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    public void handle(Context ctx) {
        try {
            UserData user = new Gson().fromJson(ctx.body(), UserData.class);

            if (user == null || user.username() == null ||
                    user.password() == null || user.email() == null) {
                ctx.status(400);
                ctx.result(new Gson().toJson(Map.of("message", "Error: bad request")));
                return;
            }

            user = userService.register(user);
            AuthData auth = authService.login(user.username());
            var response = Map.of(
                    "username", user.username(),
                    "authToken", auth.authToken()
            );
            ctx.status(200);
            ctx.result(new Gson().toJson(response));
        } catch (AlreadyTakenException e) {
            ctx.status(403);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }
}