package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDao;
import model.UserData;

import java.sql.SQLException;

public class DatabaseUserDao implements UserDao {

    public void clear() throws DataAccessException {
        var statement = "DELETE FROM users";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {throw new DataAccessException("Error: failed to clear users", ex);}
    };
    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES(?, ?, ?)";
        var username = user.username();
        var password = user.password();
        var email = user.email();
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {throw new DataAccessException("Error: failed to create user", ex);}
    };
    public UserData getUser(String username) throws DataAccessException {
        var statement = "SELECT * FROM users WHERE username = ?";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, username);
            var rs = preparedStatement.executeQuery();
            if (rs.next()) {
                var password = rs.getString("password");
                var email = rs.getString("email");
                var user = new UserData(username,password,email);
                return user;
            } else {
                return null;
            }
        } catch (SQLException ex) { throw new DataAccessException("Error: failed to get user", ex);}
    };
}
