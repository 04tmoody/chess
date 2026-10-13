package dataaccess;

import model.AuthData;

public interface AuthDao {
    void clear();
    void createAuth(String username);
    AuthData getAuth(String authToken);
    void deleteAuth(String authToken);
}
