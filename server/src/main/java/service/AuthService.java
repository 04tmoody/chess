package service;

import dataaccess.MemoryAuthDao;
import dataaccess.AuthDao;
import model.AuthData;

public class AuthService {
    private final AuthDao authDao = new MemoryAuthDao();

    public void clear() {
        authDao.clear();
    }

    public AuthData login(String username) {
        return authDao.createAuth(username);
    }
}
