package service;

import dataaccess.MemoryUserDao;
import dataaccess.UserDao;
import model.UserData;

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
}
