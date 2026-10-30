package service;

import dataaccess.DataAccessException;
import dataaccess.DatabaseAuthDao;
import dataaccess.AuthDao;
import model.AuthData;

public class AuthService {
    private final AuthDao authDao = new DatabaseAuthDao();

    public void clear() throws DataAccessException {
        authDao.clear();
    }

    public AuthData login(String username) throws DataAccessException {
        return authDao.createAuth(username);
    }

    public void logout(String auth) throws UnauthorizedException, DataAccessException {
        AuthData authData = authDao.getAuth(auth);
        if (authData==null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        authDao.deleteAuth(auth);
    }

    public boolean authorized(String auth) throws DataAccessException {
        return authDao.getAuth(auth)!=null;
    }

    public String username(String auth) throws DataAccessException {
        return authDao.getAuth(auth).username();
    }
}
