package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Provides methods for generating moves based on a chess piece's type
 */
public class ChessMoveGenerator {

    public ChessMoveGenerator() {
    }

    /**
     * Takes a row and column and checks if it is a valid move on the board
     * If it is valid, it adds it to the array of moves.
     */
    private static void addMove(Collection<ChessMove> moves, ChessPiece piece, ChessBoard board, ChessPosition endPosition, ChessPosition startPosition) {
        int row = endPosition.getRow();
        int col = endPosition.getColumn();

        // Checks if the move is in bounds
        if (row<1 || row>8 || col<1 || col>8) {
            return;
        }
        ChessPiece squarePiece = board.getPiece(endPosition);
        // check if there is a piece on the square.
        if (squarePiece!=null) {
            // Checks if move is to friendly piece
            if (piece.getTeamColor().equals(squarePiece.getTeamColor())) {
                return;
            }
        }
        moves.add(new ChessMove(startPosition, endPosition, null));
    }

    /**
     * Takes a position and x-direction (dx) & y-direction (dy)
     * Adds moves away from the position until the edge of the board or an enemy piece is reached
     */
    private static void addSlideMove(Collection<ChessMove> moves, ChessPiece piece, ChessBoard board, int dx, int dy, ChessPosition startPosition) {
        // Start at the current square
        int row = startPosition.getRow();
        int col = startPosition.getColumn();

        ChessPosition endPosition;
        boolean hitEnemy = false;

        // Loop through each space along the slide route, adding moves until it can't
        for (int i=0; i<8; i++) {
            row+=dx;
            col+=dy;
            endPosition = new ChessPosition(row,col);
            // Checks if the move is in bounds, or if it just hit an enemy piece
            if (row < 1 || row > 8 || col < 1 || col > 8 || hitEnemy) {
                return;
            }
            ChessPiece squarePiece = board.getPiece(endPosition);
            // check if there is a piece on the square.
            if (squarePiece != null) {
                // Checks if move is to friendly piece
                if (piece.getTeamColor().equals(squarePiece.getTeamColor())) {
                    return;
                } else {
                    hitEnemy = true;
                }
            }
            moves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    /**
     * The following 6 methods return an array of moves based on a different kind of chess Piece
     */
    private static Collection<ChessMove> kingMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();

        // Loop through the 8 positions around the king
        for (int dx=-1; dx<=1; dx++) {
            for (int dy=-1; dy<=1; dy++) {
                if (dx==0 && dy==0) {continue;} // Skip the square the king is on
                addMove(moves,piece,board,new ChessPosition(row+dx,col+dy), position);
            }
        }
        return moves;
    }

    private static Collection<ChessMove> knightMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();

        // Loop through the 8 positions an "L" shape away from the Knight
        for (int dx=-1; dx<=1; dx+=2) {
            for (int dy=-1; dy<=1; dy+=2) {
                addMove(moves,piece,board,new ChessPosition(row+dx,col+dy*2), position);
                addMove(moves,piece,board,new ChessPosition(row+dx*2,col+dy), position);
            }
        }
        return moves;
    }

    private static Collection<ChessMove> rookMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();

        // Add moves for the 4 directions a rook can move
        addSlideMove(moves,piece,board,-1,0,position);
        addSlideMove(moves,piece,board,1,0,position);
        addSlideMove(moves,piece,board,0,-1,position);
        addSlideMove(moves,piece,board,0,1,position);

        return moves;
    }

    /**
     * Returns the moves for a given piece on a given board in a given position
     */
    public static Collection<ChessMove> getMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        return switch(piece.getPieceType()) {
            case ChessPiece.PieceType.KING -> kingMoves(piece,board,position);
            case ChessPiece.PieceType.KNIGHT -> knightMoves(piece,board,position);
            case ChessPiece.PieceType.ROOK -> rookMoves(piece,board,position);
            default -> new ArrayList<>();
        };
    }
}
