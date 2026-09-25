package chess;

import java.util.Collection;

/**
 * Abstract class for the rules governing piece movement
 */
public abstract class ChessMovementRule {

    private boolean canMove(ChessBoard board, ChessPiece piece, ChessPosition position, boolean canCapture, boolean mustCapture) {
        if (position.getRow()<1 || position.getRow()>8 ||
                position.getColumn()<1 || position.getColumn()>8) {
            return false; // Out of Bounds
        }
        ChessPiece otherPiece = board.getPiece(position);

        if (otherPiece==null) {
            return !mustCapture; // If no piece, it's valid unless you must capture
        }

        return canCapture && otherPiece.getTeamColor() != piece.getTeamColor(); // Capture opponents piece
    }
    public void addMoves(Collection<ChessMove> moves, ChessPiece piece, ChessBoard board, ChessPosition position, int dx, int dy, int maxSteps) {
        addMoves(moves,piece,board,position,dx,dy,maxSteps,true,false,false);
    }

    public void addMoves(Collection<ChessMove> moves, ChessPiece piece, ChessBoard board, ChessPosition position,
                         int dx, int dy, int maxSteps, boolean canCapture, boolean mustCapture, boolean canPromote) {

        ChessPiece.PieceType[] promotionPieces = new ChessPiece.PieceType[]{
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.QUEEN
        };

        int row = position.getRow();
        int col = position.getColumn();

        for (int i=0; i<maxSteps; i++) {
            row+=dx;
            col+=dy;

            ChessPosition newPosition = new ChessPosition(row,col);

            if (canMove(board, piece, newPosition, canCapture, mustCapture)) {
                if (canPromote && (newPosition.getRow()==1 || newPosition.getRow()==8)) { // Promoting
                    for (ChessPiece.PieceType type : promotionPieces) {moves.add(new ChessMove(position,newPosition,type));}
                } else { // Normal Move
                    moves.add(new ChessMove(position,newPosition,null));
                }
                if (board.getPiece(newPosition)!=null) {return;} // Hit a piece, stop sliding
            } else {
                return; // Hit some border, stop sliding
            }
        }
    }

    abstract public Collection<ChessMove> pieceMoves(ChessPiece piece, ChessBoard board, ChessPosition position);
}
