package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.sql.SQLException;
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
//        int newID = games.size() + 1;
//        GameData newGame = new GameData(newID, whiteUsername, blackUsername, gameName, game);
//        games.add(newGame);
//        return newGame;
        return null;
    }
    public GameData getGame(int gameID) throws DataAccessException {
        //return games.get(gameID-1);
        return null;
    }
    public List<GameData> listGames() throws DataAccessException {
        //return games;
        return null;
    }
    public void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {
        //GameData newGame = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        //games.set(gameID-1,newGame);
    }
}
