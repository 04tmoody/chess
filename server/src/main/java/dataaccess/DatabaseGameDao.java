package dataaccess;

import chess.ChessGame;
import model.GameData;
import java.util.List;

public class DatabaseGameDao implements GameDao {

    public void clear() {
        //games.clear();
    }
    public GameData createGame(String whiteUsername, String blackUsername, String gameName, ChessGame game) {
//        int newID = games.size() + 1;
//        GameData newGame = new GameData(newID, whiteUsername, blackUsername, gameName, game);
//        games.add(newGame);
//        return newGame;
        return null;
    }
    public GameData getGame(int gameID) {
        //return games.get(gameID-1);
        return null;
    }
    public List<GameData> listGames() {
        //return games;
        return null;
    }
    public void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        //GameData newGame = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        //games.set(gameID-1,newGame);
    }
}
