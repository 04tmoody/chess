package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DatabaseAuthDao implements AuthDao {

    private final Map<String, AuthData> auths = new HashMap<>();

    public void clear() throws DataAccessException {
        var statement = "DELETE FROM auth";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to clear auth", ex);
        }
    }
    public AuthData createAuth(String username) throws DataAccessException {
//        String authToken = UUID.randomUUID().toString();
//        AuthData newAuth = new AuthData(username, authToken);
//        auths.put(authToken, newAuth);
//        return newAuth;
        return null;
    }
    public AuthData getAuth(String authToken) throws DataAccessException {
        //return auths.get(authToken);
        return null;
    }
    public void deleteAuth(String authToken) throws DataAccessException {
        //auths.remove(authToken);
    }
}
