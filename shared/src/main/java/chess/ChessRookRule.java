package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Abstract class for the rules governing piece movement
 */
public class ChessRookRule extends ChessMovementRule {

    public Collection<ChessMove> pieceMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        addMoves(moves,piece,board,position,-1,0,8);
        addMoves(moves,piece,board,position,0,-1,8);
        addMoves(moves,piece,board,position,0,1,8);
        addMoves(moves,piece,board,position,1,0,8);

        return moves;
    }
}
