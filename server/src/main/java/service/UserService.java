package service;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseUserDao;
import dataaccess.UserDao;
import model.AuthData;
import model.UserData;
import model.request.*;
import model.result.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Map;

public class UserService {
    private final UserDao userDao = new DatabaseUserDao();
    private final AuthService authService;

    public UserService(AuthService authService) {
         this.authService = authService;
    }

    public void clear() throws DataAccessException {
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
        String hashedPassword = BCrypt.hashpw(req.password(), BCrypt.gensalt());
        userDao.createUser(new UserData(req.username(),hashedPassword,req.email()));
        AuthData auth = authService.login(req.username());
        return new RegisterResult(req.username(), auth.authToken());
    }

    public LoginResult login(LoginRequest req) throws Exception {
        if (req == null || req.username() == null || req.password() == null) {
            throw new Exception("Error: bad request");
        }
        UserData user = userDao.getUser(req.username());
        if (user==null || !BCrypt.checkpw(req.password(), user.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        AuthData auth = authService.login(user.username());
        return new LoginResult(user.username(),auth.authToken());
    }
}
