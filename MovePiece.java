// MovePiece is co-written by Yi Thung and Zong Xin
// The structure and constructor was written by Yi Thung.
public class MovePiece {
    // Storing the initial and new position of the piece
    private int col, row;
    private int newCol, newRow;

    // References to the piece being moved and the piece being captured(if any)
    private Pieces piece;
    private Pieces capture;     

    public MovePiece(ChessModel board, Pieces p, int newCol, int newRow) {
        this.col = p.getCol();
        this.row = p.getRow();
        this.newCol = newCol;
        this.newRow = newRow;

        this.piece = p;
        this.capture = board.getPiece(newCol, newRow);
    }

    // The code belows are written by Zong Xin
    // It contains getters, setters and two methods that check for winning
    // condition.
    public int getNewCol() {
        return newCol;
    }

    public int getNewRow() {
        return newRow;
    }

    public Pieces getPiece() {
        return piece;
    }

    public Pieces getCapture() {
        return capture;
    }

    // Checks if the captured piece is a "Sau".
    public boolean isSau() {
        if (capture.getName() == "Sau") {
            return true;
        } else {
            return false;
        }
    }

    // Check which color has captured the opposing Sau.
    public boolean getWinnerColor() {
        return (!capture.getIsRed());
    }
}