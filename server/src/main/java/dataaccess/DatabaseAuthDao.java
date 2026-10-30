package dataaccess;

import model.AuthData;
import model.UserData;

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
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(username, authToken);
        var statement = "INSERT INTO auth (username, authtoken) VALUES(?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, authToken);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create auth", ex);
        }
        return newAuth;
    }
    public AuthData getAuth(String authToken) throws DataAccessException {
        var statement = "SELECT * FROM auth WHERE authtoken = ?";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authToken);
            var rs = preparedStatement.executeQuery();
            rs.next();
            var username = rs.getString("username");
            return new AuthData(username,authToken);
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get auth", ex);
        }
    }
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authtoken = ?";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authToken);
            preparedStatement.executeQuery();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get auth", ex);
        }
    }
}
