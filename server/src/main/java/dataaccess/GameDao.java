package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.List;

public interface GameDao {
    void clear();
    GameData createGame(String whiteUsername, String blackUsername, String gameName, ChessGame game);
    GameData getGame(int gameID);
    List<GameData> listGames();
    void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game);
}
