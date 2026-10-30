package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDao;
import dataaccess.DatabaseGameDao;
import model.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.result.*;

public class GameService {
    private final GameDao gameDao = new DatabaseGameDao();
    private final AuthService authService;

    public GameService(AuthService authService) {
        this.authService = authService;
    }

    public ListGamesResult listGames(String auth) throws UnauthorizedException, DataAccessException {
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
        GameData newGame = gameDao.createGame(null,null,req.gameName(),new ChessGame());
        return new CreateGameResult(newGame.gameID());
    }

    public void joinGame(JoinGameRequest req, String auth) throws Exception {
        if (req == null || req.playerColor() == null || req.gameID() == null || gameDao.getGame(req.gameID()) == null) {
            throw new Exception("Error: bad request");
        }
        if (!authService.authorized(auth)) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        String username = authService.username(auth);
        GameData game = gameDao.getGame(req.gameID());
        String currentPlayerUsername = switch (req.playerColor()) {
            case "WHITE" -> game.whiteUsername();
            case "BLACK" -> game.blackUsername();
            default -> throw new Exception("Error: bad request");
        };
        if (currentPlayerUsername!=null) {
            throw new AlreadyTakenException("Error: already taken");
        }
        String whiteUsername = game.whiteUsername();
        String blackUsername = game.blackUsername();
        if (req.playerColor().equals("WHITE")) {
            whiteUsername = username;
        } else {
            blackUsername = username;
        }
        gameDao.updateGame(game.gameID(),whiteUsername,blackUsername,game.gameName(),game.game());
    }

    public void clear() throws DataAccessException {
        gameDao.clear();
    }
}
