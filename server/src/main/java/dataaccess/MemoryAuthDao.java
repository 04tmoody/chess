package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDao implements AuthDao {

    private final Map<String, AuthData> auths = new HashMap<>();

    public void clear() {
        auths.clear();
    };
    public void createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(username, authToken);
        auths.put(authToken, newAuth);
    };
    public AuthData getAuth(String authToken) {
        return auths.get(authToken);
    };
    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    };
}
