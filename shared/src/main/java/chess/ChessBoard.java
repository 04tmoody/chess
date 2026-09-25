package chess;

import java.util.*;

import static java.lang.Math.abs;

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
    private ChessPosition enPassant;

    public ChessBoard() {
        board = new ChessPiece[8][8];

        // Setup Castling Rights
        canCastleQ = new HashMap<>();
        setCanCastleQ(ChessGame.TeamColor.WHITE,true);
        setCanCastleQ(ChessGame.TeamColor.BLACK,true);
        canCastleK = new HashMap<>();
        setCanCastleK(ChessGame.TeamColor.WHITE,true);
        setCanCastleK(ChessGame.TeamColor.BLACK,true);

        enPassant = null;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;
        // Remove Castling Rights If King in Wrong Spot
        if (piece==null) {return;}
        if (piece.getPieceType()==ChessPiece.PieceType.KING) {
            if (position.getColumn()!=5 || (position.getRow()!=1 && position.getRow()!=8)) {
                setCanCastleQ(piece.getTeamColor(),false);
                setCanCastleK(piece.getTeamColor(),false);
            }
        }
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

        // Move Rook if Castling
        int dx = move.getEndPosition().getColumn()-move.getStartPosition().getColumn();
        if (piece.getPieceType()==ChessPiece.PieceType.KING && abs(dx)>1) {
            int oldRookCol = dx==2 ? 8 : 1; // Get col rook is on
            int newRookCol = dx==2 ? 6 : 4; // Get col rook moves to
            int row = move.getStartPosition().getRow();
            removePiece(new ChessPosition(row,oldRookCol));
            addPiece(new ChessPosition(row,newRookCol), new ChessPiece(piece.getTeamColor(),ChessPiece.PieceType.ROOK));
        }

        // Update Castling Rights
        if (move.getStartPosition().equals(new ChessPosition(1,1)) ||
                move.getStartPosition().equals(new ChessPosition(8,1))) {
            canCastleQ.put(piece.getTeamColor(),false);
        }
        if (move.getStartPosition().equals(new ChessPosition(1,8)) ||
                move.getStartPosition().equals(new ChessPosition(8,8))) {
            canCastleK.put(piece.getTeamColor(),false);
        }
        if (piece.getPieceType()==ChessPiece.PieceType.KING) {
            canCastleQ.put(piece.getTeamColor(),false);
            canCastleK.put(piece.getTeamColor(),false);
        }

        // Update En Passant Square
        enPassant = null;
        int dy = move.getEndPosition().getRow()-move.getStartPosition().getRow();
        int enPassantCol = move.getEndPosition().getColumn();
        int enPassantRow = move.getStartPosition().getRow() + dy/2;
        if (piece.getPieceType()==ChessPiece.PieceType.PAWN && abs(dy)>1) {
            enPassant = new ChessPosition(enPassantRow,enPassantCol);
        }

        // Check capturing En Passant Square
        if (piece.getPieceType()==ChessPiece.PieceType.PAWN &&
                move.getEndPosition().equals(enPassant)) {
            removePiece(new ChessPosition(move.getStartPosition().getRow()-dy,enPassantCol));
        }
    }

    public boolean canCastleQ(ChessGame.TeamColor teamColor) {
        return canCastleQ.get(teamColor);
    }
    public boolean canCastleK(ChessGame.TeamColor teamColor) {
        return canCastleK.get(teamColor);
    }

    public void setCanCastleQ(ChessGame.TeamColor teamColor, boolean canCastle) {
        canCastleQ.put(teamColor,canCastle);
    }
    public void setCanCastleK(ChessGame.TeamColor teamColor, boolean canCastle) {
        canCastleK.put(teamColor,canCastle);
    }

    public ChessPosition getEnPassant() {
        return enPassant;
    }
    public void setEnPassant(ChessPosition square) {
        enPassant = square;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        setBoardToFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        setCanCastleQ(ChessGame.TeamColor.WHITE,true);
        setCanCastleQ(ChessGame.TeamColor.BLACK,true);
        setCanCastleK(ChessGame.TeamColor.WHITE,true);
        setCanCastleK(ChessGame.TeamColor.BLACK,true);
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
        copyBoard.setCanCastleQ(ChessGame.TeamColor.WHITE,canCastleQ.get(ChessGame.TeamColor.WHITE));
        copyBoard.setCanCastleQ(ChessGame.TeamColor.BLACK,canCastleQ.get(ChessGame.TeamColor.BLACK));
        copyBoard.setCanCastleK(ChessGame.TeamColor.WHITE,canCastleK.get(ChessGame.TeamColor.WHITE));
        copyBoard.setCanCastleK(ChessGame.TeamColor.BLACK,canCastleK.get(ChessGame.TeamColor.BLACK));
        copyBoard.setEnPassant(getEnPassant());
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
