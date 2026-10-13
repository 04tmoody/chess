package service;

import dataaccess.MemoryUserDao;
import dataaccess.UserDao;

public class UserService {
    private final UserDao userDao = new MemoryUserDao();

    public void clear() {
        userDao.clear();
    }
}
