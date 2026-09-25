package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turn;
    private ChessBoard board;

    public ChessGame() {
        turn = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(startPosition);
        if (piece==null) {return moves;}
        moves = (ArrayList<ChessMove>) piece.pieceMoves(board,startPosition);
        for (int i=moves.size()-1; i>=0; i--) {
            ChessBoard originalBoard = board.copy();
            board.makeMove(moves.get(i));
            if (isInCheck(piece.getTeamColor())) {
                moves.remove(i);
            }
            board = originalBoard.copy();
        }
        return moves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece==null) {
            throw new InvalidMoveException("No piece on square.");
        }
        if (piece.getTeamColor()!=turn) {
            throw new InvalidMoveException("Piece cannot move not on turn.");
        }
        if (validMoves(move.getStartPosition()).contains(move)) {
            board.makeMove(move);
        } else {
            throw new InvalidMoveException("Piece cannot move to square.");
        }
        if (move.getPromotionPiece()!=null) {
            board.addPiece(move.getEndPosition(),new ChessPiece(turn,move.getPromotionPiece()));
        }
        if (turn==TeamColor.WHITE) {
            turn=TeamColor.BLACK;
        } else {
            turn = TeamColor.WHITE;
        }
    }

    private ChessPosition getKingPosition(TeamColor teamColor) {
        for (int row=1; row<=8; row++) {
            for (int col=1; col<=8; col++) {
                ChessPosition position = new ChessPosition(row,col);
                ChessPiece piece = board.getPiece(position);
                if (piece==null) {continue;}
                if (piece.getTeamColor().equals(teamColor) &&
                        piece.getPieceType().equals(ChessPiece.PieceType.KING)) {
                    return position;
                }
            }
        }
        return null;
    }

    private ArrayList<ChessMove> getPieceMoves(TeamColor teamColor) {
        return getPieceMoves(teamColor,false);
    }

    private ArrayList<ChessMove> getPieceMoves(TeamColor teamColor, boolean valid) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        for (int row=1; row<=8; row++) {
            for (int col=1; col<=8; col++) {
                ChessPosition position = new ChessPosition(row,col);
                ChessPiece piece = board.getPiece(position);
                if (piece==null) {continue;}
                if (piece.getTeamColor()!=teamColor) {continue;}
                ArrayList<ChessMove> pieceMoves;
                if (valid) {
                    pieceMoves = (ArrayList<ChessMove>) validMoves(position);
                } else {
                    pieceMoves = (ArrayList<ChessMove>) piece.pieceMoves(board,position);
                }
                moves.addAll(pieceMoves);
            }
        }
        return moves;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor enemyColor = teamColor==TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
        ArrayList<ChessMove> moves = getPieceMoves(enemyColor);
        ChessPosition kingPosition = getKingPosition(teamColor);
        for (ChessMove move : moves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {return false;}
        ArrayList<ChessMove> moves = getPieceMoves(teamColor,true);
        return moves.isEmpty();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {return false;}
        ArrayList<ChessMove> moves = getPieceMoves(teamColor,true);
        return moves.isEmpty();
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
