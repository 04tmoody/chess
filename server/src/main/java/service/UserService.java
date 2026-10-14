package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDao;
import dataaccess.UserDao;
import model.UserData;
import model.request.*;

public class UserService {
    private final UserDao userDao = new MemoryUserDao();

    public void clear() {
        userDao.clear();
    }

    public UserData register(UserData user) throws AlreadyTakenException {
        UserData existingUser = userDao.getUser(user.username());
        if (existingUser!=null) {
            throw new AlreadyTakenException("Error: already taken");
        }
        userDao.createUser(user);
        return user;
    }

    public UserData login(LoginRequest req) throws UnauthorizedException {
        UserData user = userDao.getUser(req.username());
        if (user==null || !user.password().equals(req.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return user;
    }
}
