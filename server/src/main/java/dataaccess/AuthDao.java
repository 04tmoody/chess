package dataaccess;

import model.AuthData;

public interface AuthDao {
    void clear();
    void createAuth();
    AuthData getAuth(String authToken);
    void deleteAuth(String authToken);
}
