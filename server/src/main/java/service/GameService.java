package service;

import dataaccess.GameDao;
import dataaccess.MemoryGameDao;

public class GameService {
    private final GameDao gameDao = new MemoryGameDao();

    public void clear() {
        gameDao.clear();
    }
}
