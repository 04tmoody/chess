package chess;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private final ChessPiece[][] board;

    public ChessBoard() {
        board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to a specified FEN string
     */
    public void setBoardToFen(String fen) {
        for (int i = 1; i < 8; i++) {
            String spaces = " ".repeat(i);
            fen = fen.replace(String.valueOf(i), spaces);
        }
        fen = fen.split(" ")[0];
        String[] fenRows = fen.split("/");
        int row = 8;
        for (String fenRow : fenRows) {
            int col = 1;
            for (String symbol : fenRow.split("")) {
                ChessPiece piece;
                if (symbol.equals(" ")) {
                    piece = null;
                } else {
                    piece = ChessPiece.getPieceFromSymbol(symbol);
                }
                addPiece(new ChessPosition(row,col),piece);
                col++;
            }
            row--;
        }
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        setBoardToFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        /*
        this.addPiece(new ChessPosition(1,1),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(1,2),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(1,3),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(1,4),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.QUEEN));
        this.addPiece(new ChessPosition(1,5),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.KING));
        this.addPiece(new ChessPosition(1,6),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(1,7),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(1,8),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.ROOK));

        this.addPiece(new ChessPosition(2,1),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(2,2),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(2,3),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(2,4),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(2,5),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(2,6),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(2,7),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(2,8),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN));

        this.addPiece(new ChessPosition(8,1),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(8,2),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(8,3),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(8,4),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.QUEEN));
        this.addPiece(new ChessPosition(8,5),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.KING));
        this.addPiece(new ChessPosition(8,6),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(8,7),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(8,8),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.ROOK));

        this.addPiece(new ChessPosition(7,1),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(7,2),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(7,3),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(7,4),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(7,5),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(7,6),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(7,7),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(7,8),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN));
         */
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                String letter = " ";
                if (board[i][j]!=null) {
                    letter = board[i][j].toString();
                }
                boardString.append(letter);
                if (j!=board[i].length-1) {
                    boardString.append(" ");
                }
            }
            boardString.append("\n");
        }
        return boardString.toString();
    }
}
