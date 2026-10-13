package dataaccess;

import chess.ChessGame;
import model.GameData;
import java.util.ArrayList;
import java.util.List;

public class MemoryGameDao implements GameDao {

    private final List<GameData> games = new ArrayList<>();

    public void clear() {
        games.clear();
    }
    public void createGame(String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        int newID = games.size();
        GameData newGame = new GameData(newID, whiteUsername, blackUsername, gameName, game);
        games.add(newGame);
    }
    public GameData getGame(int gameID) {
        return games.get(gameID);
    }
    public List<GameData> listGames() {
        return games;
    }
    public void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        GameData newGame = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        games.set(gameID,newGame);
    }
}
