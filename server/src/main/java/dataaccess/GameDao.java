package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.List;

public interface GameDao {
    void clear();
    void createGame(String whiteUsername, String blackUsername, String gameName, ChessGame game);
    GameData getGame(int GameID);
    List<GameData> listGames();
    void updateGame(int GameID, String whiteUsername, String blackUsername, String gameName, ChessGame game);
}
