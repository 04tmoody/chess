package service;

import chess.ChessGame;
import dataaccess.GameDao;
import dataaccess.MemoryGameDao;
import model.GameData;
import model.request.CreateGameRequest;
import model.result.*;

public class GameService {
    private final GameDao gameDao = new MemoryGameDao();
    private final AuthService authService;

    public GameService(AuthService authService) {
        this.authService = authService;
    }

    public ListGamesResult listGames(String auth) throws UnauthorizedException {
        if (!authService.authorized(auth)) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return new ListGamesResult(gameDao.listGames());
    }

    public CreateGameResult createGame(CreateGameRequest req, String auth) throws Exception {
        if (req == null || req.gameName() == null) {
            throw new Exception("Error: bad request");
        }
        if (!authService.authorized(auth)) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        GameData newGame = gameDao.createGame(req.gameName(),null,null,new ChessGame());
        return new CreateGameResult(newGame.gameID());
    }

    public void clear() {
        gameDao.clear();
    }
}
