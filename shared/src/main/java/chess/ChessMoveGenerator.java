package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Provides methods for generating moves based on a chess piece's type
 */
public class ChessMoveGenerator {

    public ChessMoveGenerator() {
    }

    /**
     * Forwards to the addMoveFull method with default capturing logic and no promotion
     */
    private static void addMove(Collection<ChessMove> moves, ChessPiece piece, ChessBoard board, ChessPosition endPosition, ChessPosition startPosition) {
        addMoveFull(moves,piece,board,endPosition,startPosition,true,false,false);
    }

    /**
     * Takes a row and column and checks if it is a valid move on the board
     * If it is valid, it adds it to the array of moves.
     */
    private static void addMoveFull(Collection<ChessMove> moves, ChessPiece piece, ChessBoard board, ChessPosition endPosition, ChessPosition startPosition, boolean canCapture, boolean mustCapture, boolean promote) {
        int row = endPosition.getRow();
        int col = endPosition.getColumn();

        // Checks if the move is in bounds
        if (row<1 || row>8 || col<1 || col>8) {
            return;
        }
        ChessPiece squarePiece = board.getPiece(endPosition);
        // check if there is a piece on the square.
        if (squarePiece==null) {
            if (mustCapture) {return;} // Requires capturing for pawns
        } else {
            // Checks if move is to friendly piece (or enemy piece for pawns)
            if (!canCapture || piece.getTeamColor().equals(squarePiece.getTeamColor())) {
                return;
            }
        }
        ChessPiece.PieceType[] promotionPieces = {ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP};
        if (promote) {
            for (ChessPiece.PieceType pieceType : promotionPieces) {
                moves.add(new ChessMove(startPosition, endPosition, pieceType));
            }
        } else {
            moves.add(new ChessMove(startPosition, endPosition, null));
        }

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
    // KING MOVES
    private static Collection<ChessMove> kingMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        addMove(moves,piece,board,new ChessPosition(row-1,col-1), position);
        addMove(moves,piece,board,new ChessPosition(row-1,col), position);
        addMove(moves,piece,board,new ChessPosition(row-1,col+1), position);
        addMove(moves,piece,board,new ChessPosition(row,col-1), position);
        addMove(moves,piece,board,new ChessPosition(row,col+1), position);
        addMove(moves,piece,board,new ChessPosition(row+1,col-1), position);
        addMove(moves,piece,board,new ChessPosition(row+1,col), position);
        addMove(moves,piece,board,new ChessPosition(row+1,col+1), position);
        return moves;
    }

    // KNIGHT MOVES
    private static Collection<ChessMove> knightMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        addMove(moves,piece,board,new ChessPosition(row-2,col-1), position);
        addMove(moves,piece,board,new ChessPosition(row-2,col+1), position);
        addMove(moves,piece,board,new ChessPosition(row-1,col-2), position);
        addMove(moves,piece,board,new ChessPosition(row-1,col+2), position);
        addMove(moves,piece,board,new ChessPosition(row+1,col-2), position);
        addMove(moves,piece,board,new ChessPosition(row+1,col+2), position);
        addMove(moves,piece,board,new ChessPosition(row+2,col-1), position);
        addMove(moves,piece,board,new ChessPosition(row+2,col+1), position);
        return moves;
    }

    // ROOK MOVES
    private static Collection<ChessMove> rookMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();

        // Add moves for the 4 directions a rook can move
        addSlideMove(moves,piece,board,-1,0,position);
        addSlideMove(moves,piece,board,1,0,position);
        addSlideMove(moves,piece,board,0,-1,position);
        addSlideMove(moves,piece,board,0,1,position);

        return moves;
    }

    // BISHOP MOVES
    private static Collection<ChessMove> bishopMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();

        // Add moves for the 4 directions a rook can move
        addSlideMove(moves,piece,board,-1,-1,position);
        addSlideMove(moves,piece,board,1,-1,position);
        addSlideMove(moves,piece,board,-1,1,position);
        addSlideMove(moves,piece,board,1,1,position);

        return moves;
    }

    // QUEEN MOVES
    private static Collection<ChessMove> queenMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();

        // Add moves for the 8 directions the queen can move
        addSlideMove(moves,piece,board,-1,0,position);
        addSlideMove(moves,piece,board,1,0,position);
        addSlideMove(moves,piece,board,0,-1,position);
        addSlideMove(moves,piece,board,0,1,position);
        addSlideMove(moves,piece,board,-1,-1,position);
        addSlideMove(moves,piece,board,1,-1,position);
        addSlideMove(moves,piece,board,-1,1,position);
        addSlideMove(moves,piece,board,1,1,position);

        return moves;
    }

    // PAWN MOVES
    private static Collection<ChessMove> pawnMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();

        // Determine what direction the pawn is moving in
        int dy;
        if (piece.getTeamColor()==ChessGame.TeamColor.BLACK) {
            dy = -1;
        } else {
            dy = 1;
        }

        // Determine if Pawn is promoting or can doublemove
        boolean promotion = (row+dy==1 || row+dy==8);
        boolean doubleMove = ((row==2 || row==7) && board.getPiece(new ChessPosition(row+dy,col))==null);

        // Move forward without capturing
        addMoveFull(moves,piece,board,new ChessPosition(row+dy,col), position,false, false,promotion);
        if (doubleMove) {
            addMoveFull(moves,piece,board,new ChessPosition(row+dy*2,col), position,false, false,promotion);
        }

        // Move diagonally with capturing
        addMoveFull(moves,piece,board,new ChessPosition(row+dy,col-1), position,true, true,promotion);
        addMoveFull(moves,piece,board,new ChessPosition(row+dy,col+1), position,true, true,promotion);
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
            case ChessPiece.PieceType.BISHOP -> bishopMoves(piece,board,position);
            case ChessPiece.PieceType.QUEEN -> queenMoves(piece,board,position);
            case ChessPiece.PieceType.PAWN -> pawnMoves(piece,board,position);
        };
    }
}
