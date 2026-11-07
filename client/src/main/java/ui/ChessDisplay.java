package ui;

import chess.ChessBoard;
import chess.ChessGame;
import static chess.ChessGame.TeamColor.*;
import chess.ChessPiece;
import chess.ChessPiece.*;
import static chess.ChessPiece.PieceType.*;
import static ui.EscapeSequences.*;

import chess.ChessPosition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChessDisplay {

    Map<PieceType,String> whiteIcons;
    Map<PieceType,String> blackIcons;
    String lightColor;
    String darkColor;
    String resetColor;
    String labelColor;
    String whitePieceColor;
    String blackPieceColor;
    String lightTextColor;
    String darkTextColor;

    public ChessDisplay() {
        whiteIcons = new HashMap<>();
        blackIcons = new HashMap<>();

        whiteIcons.put(ROOK,"♜");
        whiteIcons.put(KNIGHT,"♞");
        whiteIcons.put(BISHOP,"♝");
        whiteIcons.put(QUEEN,"♛");
        whiteIcons.put(KING,"♚");
        whiteIcons.put(PAWN,"♟");
        blackIcons.put(ROOK,"♜");
        blackIcons.put(KNIGHT,"♞");
        blackIcons.put(BISHOP,"♝");
        blackIcons.put(QUEEN,"♛");
        blackIcons.put(KING,"♚");
        blackIcons.put(PAWN,"♟");

        whitePieceColor = SET_TEXT_COLOR_WHITE;
        blackPieceColor = SET_TEXT_COLOR_BLACK;

        labelColor = SET_TEXT_COLOR_LIGHT_GREY;
        lightColor = setColor(false,227,193,111);//SET_BG_COLOR_LIGHT_GREY;
        darkColor = setColor(false,184,139,74);//SET_BG_COLOR_DARK_GREY;
        resetColor = RESET_BG_COLOR + RESET_TEXT_COLOR;

        lightTextColor = lightColor.replace("[4","[3");
        darkTextColor = darkColor.replace("[4","[3");
    }

    public void displayBoard(ChessBoard board, boolean flipped) {

        List<String> columns = List.of("a","b","c","d","e","f","g","h");
        final String gap = " ";
        System.out.print("  "+labelColor);
        columns.forEach(letter -> System.out.print(gap+letter+gap));
        System.out.println(resetColor);
        for (int row=8; row>=1; row--) {
            System.out.print(labelColor+row+" "+resetColor);
            for (int col=1; col<=8; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row,col));
                String icon;
                if (piece==null) {
                    icon = whiteIcons.get(PAWN);
                } else {
                    Map<PieceType,String> icons = piece.getTeamColor() == WHITE ? whiteIcons : blackIcons;
                    icon = icons.get(piece.getPieceType());
                    icon = (piece.getTeamColor() == WHITE ? whitePieceColor : blackPieceColor) + icon;
                }
                icon = icon + RESET_TEXT_COLOR;
                if ((row+col)%2==1) System.out.print(lightColor+lightTextColor);
                else System.out.print(darkColor+darkTextColor);
                System.out.printf(" %s ",icon);
                System.out.print(resetColor);
            }
            System.out.print(labelColor+" "+row+resetColor);
            System.out.println();
        }
        System.out.print("  "+labelColor);
        columns.forEach(letter -> System.out.print(gap+letter+gap));
        System.out.println(resetColor);
    }

    private static String setColor(boolean text, int red, int green, int blue) {
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
            throw new IllegalArgumentException("Colors must be between 0 - 255");
        }
        return String.format("\u001B[%s8;2;%d;%d;%dm", (text) ? "3" : "4", red, green, blue);
    }

    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        ChessDisplay display = new ChessDisplay();
        display.displayBoard(game.getBoard(),false);
    }
}