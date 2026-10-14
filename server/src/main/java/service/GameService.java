package service;

import dataaccess.GameDao;
import dataaccess.MemoryGameDao;
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

    public void clear() {
        gameDao.clear();
    }
}
