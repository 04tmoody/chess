package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Abstract class for the rules governing piece movement
 */
public class ChessPawnRule extends ChessMovementRule {

    public Collection<ChessMove> pieceMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        int dx = 1;
        int startingRow = 2;
        if (piece.getTeamColor()==ChessGame.TeamColor.BLACK) {
            dx = -1;
            startingRow = 7;
        }

        int forwardSteps = 1;
        if (position.getRow()==startingRow) {
            forwardSteps = 2;
        }

        addMoves(moves,piece,board,position,dx,0,forwardSteps,false,false,true);
        addMoves(moves,piece,board,position,dx,-1,1,true,true,true);
        addMoves(moves,piece,board,position,dx,1,1,true,true,true);

        return moves;
    }
}
