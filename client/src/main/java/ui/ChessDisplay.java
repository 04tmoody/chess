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

        whitePieceColor = setColor(true,255,230,205);//SET_TEXT_COLOR_WHITE;
        blackPieceColor = setColor(true,50,25,0);//SET_TEXT_COLOR_BLACK;

        labelColor = SET_TEXT_COLOR_LIGHT_GREY;
        lightColor = setColor(false,207,173,91);//SET_BG_COLOR_LIGHT_GREY;
        darkColor = setColor(false,164,119,54);//SET_BG_COLOR_DARK_GREY;
        resetColor = RESET_BG_COLOR + RESET_TEXT_COLOR;

    }

    public void displayBoard(ChessBoard board, boolean flipped) {

        List<String> columns = List.of("a","b","c","d","e","f","g","h");
        if (flipped) columns = List.of("h","g","f","e","d","c","b","a");
        System.out.print(resetColor+"  "+labelColor);
        columns.forEach(letter -> System.out.print(" "+letter+"\u2003"));
        System.out.println("  "+resetColor);
        for (int row_i=8; row_i>=1; row_i--) {
            int row = flipped ? 9-row_i : row_i;
            System.out.print(SET_BG_COLOR_DARK_GREY+labelColor+row+" "+resetColor);
            for (int col_i=1; col_i<=8; col_i++) {
                int col = flipped ? 9-col_i : col_i;
                ChessPiece piece = board.getPiece(new ChessPosition(row,col));
                String icon;
                if (piece==null) {
                    icon = "\u2003";
                } else {
                    Map<PieceType,String> icons = piece.getTeamColor() == WHITE ? whiteIcons : blackIcons;
                    icon = icons.get(piece.getPieceType());
                    icon = (piece.getTeamColor() == WHITE ? whitePieceColor : blackPieceColor) + icon;
                }
                icon = icon + RESET_TEXT_COLOR;
                if ((row+col)%2==1) System.out.print(lightColor);
                else System.out.print(darkColor);
                System.out.printf(" %s ",icon);
                System.out.print(resetColor);
            }
            System.out.print(SET_BG_COLOR_DARK_GREY+labelColor+" "+row+resetColor);
            System.out.println();
        }
        System.out.print(resetColor+"  "+labelColor);
        columns.forEach(letter -> System.out.print(" "+letter+"\u2003"));
        System.out.println("  "+resetColor);
    }

    private static String setColor(boolean text, int red, int green, int blue) {
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
            throw new IllegalArgumentException("Colors must be between 0 - 255");
        }
        return String.format("\u001B[%s8;2;%d;%d;%dm", (text) ? "3" : "4", red, green, blue);
    }

    public void setColorScheme(int scheme) throws Exception {
        switch (scheme) {
            case 1 -> { // Sandcastle
                whitePieceColor = setColor(true,255,230,205);
                blackPieceColor = setColor(true,50,25,0);
            }
            case 2 -> { // Coral
                whitePieceColor = setColor(true,177,228,185);
                blackPieceColor = setColor(true,112,162,163);
            }
            case 3 -> { // Dusk
                whitePieceColor = setColor(true,204,183,174);
                blackPieceColor = setColor(true,112,102,119);
            }
            case 4 -> { // Marine
                whitePieceColor = setColor(true,157,172,255);
                blackPieceColor = setColor(true,111,115,210);
            }
            case 5 -> { // Wheat
                whitePieceColor = setColor(true,234,240,206);
                blackPieceColor = setColor(true,187,190,100);
            }
            default -> {
                throw new Exception("Invalid Color Scheme (1-5)");
            }
        }
    }
}