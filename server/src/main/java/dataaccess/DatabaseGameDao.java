package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseGameDao implements GameDao {

    public void clear() throws DataAccessException{
        var statement = "DELETE FROM games";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to clear games", ex);
        }
    }
    public GameData createGame(String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {
        var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES(?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, whiteUsername);
            preparedStatement.setString(2, blackUsername);
            preparedStatement.setString(3, gameName);
            String gameString = new Gson().toJson(game);
            preparedStatement.setString(4, gameString);
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            int generatedId = generatedKeys.getInt(1);
            return getGame(generatedId);
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create game", ex);
        }
    }
    public GameData getGame(int gameID) throws DataAccessException {
        var statement = "SELECT * FROM games WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, gameID);
            var rs = preparedStatement.executeQuery();
            rs.next();
            var whiteUsername = rs.getString("whiteUsername");
            var blackUsername = rs.getString("blackUsername");
            var gameName = rs.getString("gameName");
            var gameString = rs.getString("game");
            var game = new Gson().fromJson(gameString, ChessGame.class);
            return new GameData(gameID,whiteUsername,blackUsername,gameName,game);
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get game", ex);
        }
    }
    public List<GameData> listGames() throws DataAccessException {
        var statement = "SELECT * FROM games";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            var rs = preparedStatement.executeQuery();

            ArrayList<GameData> games = new ArrayList<GameData>();
            while (rs.next()) {
                var gameID = rs.getInt("gameID");
                var whiteUsername = rs.getString("whiteUsername");
                var blackUsername = rs.getString("blackUsername");
                var gameName = rs.getString("gameName");
                var gameString = rs.getString("game");
                var game = new Gson().fromJson(gameString, ChessGame.class);
                games.add(new GameData(gameID,whiteUsername,blackUsername,gameName,game));
            }
            return games;
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get list of games", ex);
        }
    }
    public void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {
        var statement = "UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ?, WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, whiteUsername);
            preparedStatement.setString(2, blackUsername);
            preparedStatement.setString(3, gameName);
            String gameString = new Gson().toJson(game);
            preparedStatement.setString(4, gameString);
            preparedStatement.setInt(5,gameID);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to update game", ex);
        }
    }
}
