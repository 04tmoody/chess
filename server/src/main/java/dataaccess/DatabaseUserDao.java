package dataaccess;

import dataaccess.DatabaseManager;
import model.UserData;

import java.sql.SQLException;

public class DatabaseUserDao implements UserDao {

    public void clear() throws DataAccessException {
        var statement = "DELETE FROM users";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to clear users", ex);
        }
    };
    public void createUser(UserData user) {
        //users.put(user.username(),user);
    };
    public UserData getUser(String username) {
        //return users.get(username);
        return null;
    };
}
