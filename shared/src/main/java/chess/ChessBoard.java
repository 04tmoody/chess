package chess;

import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private final ChessPiece[][] board;
    private Map<ChessGame.TeamColor, Boolean> canCastleQ;
    private Map<ChessGame.TeamColor, Boolean> canCastleK;

    public ChessBoard() {
        board = new ChessPiece[8][8];

        // Setup Castling Rights
        canCastleQ = new HashMap<>();
        canCastleQ.put(ChessGame.TeamColor.WHITE,true);
        canCastleQ.put(ChessGame.TeamColor.BLACK,true);
        canCastleK = new HashMap<>();
        canCastleK.put(ChessGame.TeamColor.WHITE,true);
        canCastleK.put(ChessGame.TeamColor.BLACK,true);
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

    public void removePiece(ChessPosition position) {
        addPiece(position,null);
    }

    public void makeMove(ChessMove move) {
        ChessPiece piece = getPiece(move.getStartPosition());
        addPiece(move.getEndPosition(),piece);
        removePiece(move.getStartPosition());

        // Update Castling Rights
        if (move.getStartPosition().equals(new ChessPosition(1,1)) ||
                move.getStartPosition().equals(new ChessPosition(8,1))) {
            canCastleQ.put(piece.getTeamColor(),false);
        }
        if (move.getStartPosition().equals(new ChessPosition(1,8)) ||
                move.getStartPosition().equals(new ChessPosition(8,8))) {
            canCastleK.put(piece.getTeamColor(),false);
        }
    }

    public boolean canCastleQ(ChessGame.TeamColor teamColor) {
        return canCastleQ.get(teamColor);
    }
    public boolean canCastleK(ChessGame.TeamColor teamColor) {
        return canCastleK.get(teamColor);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        setBoardToFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
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

    public ChessBoard copy() {
        ChessBoard copyBoard = new ChessBoard();
        int row = 1;
        for (ChessPiece[] pieceRow : board) {
            int col = 1;
            for (ChessPiece piece : pieceRow) {
                copyBoard.addPiece(new ChessPosition(row,col),piece);
                col++;
            }
            row++;
        }
        return copyBoard;
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
                    boardString.append("|");
                }
            }
            boardString.append("\n");
        }
        return boardString.toString();
    }
}
