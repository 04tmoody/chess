package chess;

import java.util.Locale;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessBoard() {
        
    }

    private final ChessPiece[][] board = new ChessPiece[8][8];

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
     * Sets the board according to a given FEN string
     * For example, the starting position would be
     * "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"
     */
    private void setBoardToFen(String fen) {
        String piecesData = fen.split(" ")[0]; //Gets just the piece setup portion

        // Start adding pieces from the top left
        int row = 8;
        int col;

        String[] rowsData = piecesData.split("/");
        for (String rowData : rowsData) {
            col = 0; // Start each row from the left
            row--;
            for (int i=0; i<rowData.length(); i++) {
                String pieceData = rowData.charAt(i)+"";
                if ("12345678".contains(pieceData)) { // Empty Space
                    col += Integer.parseInt(pieceData);
                } else { // Add a piece
                    ChessGame.TeamColor color; // color of the piece to add
                    if (pieceData.toUpperCase().equals(pieceData)) { // piece is uppercase (white)
                        color = ChessGame.TeamColor.WHITE;
                    } else { // piece is lowercase (black)
                        color = ChessGame.TeamColor.BLACK;
                    }
                    ChessPiece.PieceType type = switch(pieceData.toUpperCase()) {
                        case "P" -> ChessPiece.PieceType.PAWN;
                        case "R" -> ChessPiece.PieceType.ROOK;
                        case "N" -> ChessPiece.PieceType.KNIGHT;
                        case "B" -> ChessPiece.PieceType.BISHOP;
                        case "Q" -> ChessPiece.PieceType.QUEEN;
                        case "K" -> ChessPiece.PieceType.KING;
                        default -> null;
                    };
                    if (type==null) { // Add empty space if unknown piece
                        col++;
                        continue;
                    }
                    ChessPiece piece = new ChessPiece(color,type);
                    board[row][col] = piece;
                }
            }
        }
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }
}
