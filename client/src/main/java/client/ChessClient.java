package client;

import chess.*;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import client.ServerFacade;
import model.AuthData;
import model.GameData;
import ui.ChessDisplay;

import static ui.EscapeSequences.*;

public class ChessClient {

    private final ServerFacade server;
    private boolean loggedIn = false;
    private String auth = null;
    private String user = null;
    private boolean finished;
    private final ChessDisplay display;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        display = new ChessDisplay();
    }

    public void run() {
        finished = false;
        System.out.println("Welcome to Chess240!");
        try (Scanner scanner = new Scanner(System.in)) {
            while (!finished) {
                System.out.print("\n>>> ");
                String command = scanner.nextLine().toLowerCase(Locale.ROOT);
                if (loggedIn) {
                    postLoginCommand(scanner, command);
                } else {
                    preLoginCommand(scanner, command);
                }
            }
        }
    }

    private void preLoginCommand(Scanner scanner, String command) {
        switch (command) {
            case "quit" -> {
                System.out.println("Thanks for playing chess. Goodbye!");
                finished = true;
            }
            case "help" -> {
                preLoginHelp();
            }
            case "login" -> {
                login(scanner);
            }
            case "register" -> {
                register(scanner);
            }
            case "logout", "create game", "list games", "play game", "observe game" -> {
                System.out.println("You must login first before you can use that command. Type 'login' to do so!");
            }
            default -> {
                System.out.println("Sorry! I didn't understand that. Type 'help' for a list of options.");
            }
        }
    }

    private void postLoginCommand(Scanner scanner, String command) {
        switch (command) {
            case "help" -> {
                postLoginHelp();
            }
            case "logout" -> {
                logout();
            }
            case "create game" -> {
                createGame(scanner);
            }
            case "list games" -> {
                listGames();
            }
            case "play game" -> {
                playGame(scanner);
            }
            case "observe game" -> {
                observeGame(scanner);
            }
            case "quit", "login", "register" -> {
                System.out.println("You must logout first before you can use that command. Type 'logout' to do so!");
            }
            default -> {
                System.out.println("Sorry! I didn't understand that. Type 'help' for a list of options.");
            }
        }
    }

    private void preLoginHelp() {
        System.out.println("To get started, type one of the following commands:\n"+
                "\t'help' - print out these instructions\n"+
                "\t'quit' - end the program ):\n"+
                "\t'login' - login to your Chess240 account\n" +
                "\t'register' - create an account if you don't have one yet");
    }

    private void postLoginHelp() {
        System.out.println("To get started, type one of the following commands:\n"+
                "\t'help' - print out these instructions\n"+
                "\t'logout' - log out of your account\n"+
                "\t'create game' - create a new chess game\n" +
                "\t'list games' - get a list of games you can observe or join\n" +
                "\t'play game' - join a game of chess as a player"+
                "\t'observe game' - observe a game of chess");
    }

    private void login(Scanner scanner) {
        System.out.println("Enter your account information");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        try {
            AuthData response = server.login(username,password);
            auth = response.authToken();
            user = response.username();
            loggedIn = true;
            System.out.println("Welcome "+username+"!");
        } catch (Exception ex) {
            if (ex.getMessage()!=null && ex.getMessage().contains("unauthorized")) {
                System.out.println("That password is incorrect or that username doesn't exist. Type 'login' to try again!");
            } else {
                System.out.println("Hmmm... something went wrong logging in. Try again later!");
            }
        }
    }

    private void register(Scanner scanner) {
        System.out.println("Let's set up your account!");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        try {
            AuthData response = server.register(username,password,email);
            auth = response.authToken();
            user = response.username();
            loggedIn = true;
            System.out.println("Account Registered! Welcome "+username);
        } catch (Exception ex) {
            if (ex.getMessage()!=null && ex.getMessage().contains("already taken")) {
                System.out.println("That username is already taken! Type 'register' to try again.");
            } else {
                System.out.println("Hmmm... something went wrong registering. Try again later!");
            }
        }
    }

    private void logout() {
        try {
            server.logout(auth);
            System.out.println("You have successfully logged out!");
            loggedIn = false;
            user = null;
            auth = null;
        } catch (Exception ex) {
            System.out.println("Something went wrong logging out! Try again later.");
        }
    }

    private void createGame(Scanner scanner) {
        System.out.println("Let's create a new game! What do you want to call it?");
        System.out.print("Game Name: ");
        String name = scanner.nextLine();
        try {
            server.createGame(name,auth);
            System.out.println("New game '" + name + "' created successfully.");
        } catch (Exception ex) {
            System.out.println("Something went wrong creating your game! Try again later.");
        }
    }

    private void listGames() {
        try {
            List<GameData> games = server.listGames(auth);
            if (games.isEmpty()) {
                System.out.println("No games yet... type 'create game' to make one.");
            }
            System.out.println("Here are all current chess games:");
            int i = 0;
            for (GameData game: games) {
                i+=1;
                System.out.printf("\t[%d] %s\n",i,game.gameName());
                String white = game.whiteUsername();
                String black = game.blackUsername();
                System.out.print(SET_TEXT_COLOR_LIGHT_GREY);
                if (white==null && black==null) {
                    System.out.println("\t ┕ (no players joined)");
                } else {
                    if (white==null) white = "(not joined)";
                    if (black==null) black = "(not joined)";
                    System.out.printf("\t ┣ White Player: %s\n",white);
                    System.out.printf("\t ┕ Black Player: %s\n",black);
                }
                System.out.print(RESET_TEXT_COLOR);
            }
        } catch (Exception ex) {
            System.out.println("Something went wrong finding the chess games. Try again later.");
        }
    }

    private void playGame(Scanner scanner) {
        try {
            List<GameData> games = server.listGames(auth);
            System.out.println("Which game would you like to join? (Use 'list games' to see all game options)");
            System.out.print("Game #: ");
            String gameInput = scanner.nextLine();
            try {
                int game = Integer.parseInt(gameInput);
                if (game>games.size() || game<1) throw new Exception();
                int gameID = games.get(game-1).gameID();
                boolean whiteTaken = games.get(game-1).whiteUsername()!=null;
                boolean blackTaken = games.get(game-1).blackUsername()!=null;
                String color = null;
                if (whiteTaken && blackTaken) {
                    System.out.println("Game #"+gameInput+" is full. Try a different game.");
                    return;
                } else if (!whiteTaken && !blackTaken) {
                    System.out.print("Enter your color (White/Black): ");
                    char colorChar = (scanner.nextLine()+" ").toLowerCase(Locale.ROOT).charAt(0);
                    if (colorChar=='w') color = "WHITE";
                    else if (colorChar=='b') color = "BLACK";
                    else {
                        System.out.println("Unknown color. Failed to join game.");
                        return;
                    }
                } else {
                    color = blackTaken ? "WHITE" : "BLACK";
                }
                server.joinGame(gameID,color,auth);
                System.out.println("Joined Game '"+games.get(game-1).gameName()+
                                    "' as "+color.toUpperCase(Locale.ROOT)+"!");
                ChessGame myGame = games.get(game-1).game();
                display.displayBoard(myGame.getBoard(),color.equals("BLACK"));
            } catch (Exception ex) {
                System.out.printf("Game #%s could not be found. Try a number from 1-%d\n",gameInput,games.size());
            }
        } catch (Exception ex) {
            System.out.println("Hmmm... Something went wrong finding games to play. Try again later.");
        }
    }

    private void observeGame(Scanner scanner) {
        try {
            List<GameData> games = server.listGames(auth);
            System.out.println("Which game would you like to join? (Use 'list games' to see all game options)");
            System.out.print("Game #: ");
            String gameInput = scanner.nextLine();
            try {
                int game = Integer.parseInt(gameInput);
                if (game>games.size() || game<1) throw new Exception();
                int gameID = games.get(game-1).gameID();
                System.out.println("Observing Game '"+games.get(game-1).gameName()+ "'!");
                ChessGame myGame = games.get(game-1).game();
                display.displayBoard(myGame.getBoard(),false);
            } catch (Exception ex) {
                System.out.printf("Game #%s could not be found. Try a number from 1-%d\n",gameInput,games.size());
            }
        } catch (Exception ex) {
            System.out.println("Hmmm... Something went wrong finding games to play. Try again later.");
        }
    }
}
