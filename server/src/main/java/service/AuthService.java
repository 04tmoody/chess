package service;

import dataaccess.MemoryAuthDao;
import dataaccess.AuthDao;

public class AuthService {
    private final AuthDao authDao = new MemoryAuthDao();

    public void clear() {
        authDao.clear();
    }
}
