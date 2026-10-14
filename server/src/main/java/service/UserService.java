package service;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDao;
import dataaccess.UserDao;
import model.AuthData;
import model.UserData;
import model.request.*;
import model.result.*;

import java.util.Map;

public class UserService {
    private final UserDao userDao = new MemoryUserDao();
    private final AuthService authService;

    public UserService(AuthService authService) {
         this.authService = authService;
    }

    public void clear() {
        userDao.clear();
    }

    public RegisterResult register(RegisterRequest req) throws Exception {
        if (req == null || req.username() == null || req.password() == null || req.email() == null) {
            throw new Exception("Error: bad request");
        }
        UserData existingUser = userDao.getUser(req.username());
        if (existingUser!=null) {
            throw new AlreadyTakenException("Error: already taken");
        }
        userDao.createUser(new UserData(req.username(),req.password(),req.email()));
        AuthData auth = authService.login(req.username());
        return new RegisterResult(req.username(), auth.authToken());
    }

    public LoginResult login(LoginRequest req) throws Exception {
        if (req == null || req.username() == null || req.password() == null) {
            throw new Exception("Error: bad request");
        }
        UserData user = userDao.getUser(req.username());
        if (user==null || !user.password().equals(req.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        AuthData auth = authService.login(user.username());
        return new LoginResult(user.username(),auth.authToken());
    }

    public void logout(String auth) {

    }
}
