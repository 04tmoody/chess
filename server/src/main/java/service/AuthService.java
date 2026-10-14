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

    public void logout(String auth) throws UnauthorizedException {
        AuthData authData = authDao.getAuth(auth);
        if (authData==null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        authDao.deleteAuth(auth);
    }

    public boolean authorized(String auth) {
        return authDao.getAuth(auth)!=null;
    }

    public String username(String auth) {
        return authDao.getAuth(auth).username();
    }
}
