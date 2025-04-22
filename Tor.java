public class Tor extends Pieces {

    private ChessModel board;  

    // Written by Zong Xin
    public Tor(int squareSize, ChessModel board, int col, int row, boolean isRed) {
        // Initialize the parent class with appropriate parameters.
        super(squareSize, col, row, isRed, "Tor");
        
        int xPos = col * squareSize;
        int yPos = row * squareSize;
        initializeScreenPosition(xPos, yPos);
        this.board = board;

        // It uses a method from the parent class to load the
        // appropriate image based on its team color.
        initializeImage("pieces/" + (isRed ? "r_tor" : "b_tor"));

        if (getCurrentImage() == null) {
            System.err.println("Failed to load image for " + (isRed ? "red Tor" : "blue Tor"));
        }
    }

    // Determines whether a move is valid for the Tor piece.
    // Written by Yi Thung
    @Override
    public boolean isValid(int col, int row) {
        // The Tor moves either vertically or horizontally.
        return Math.abs(col - getCol()) == 0 || Math.abs(row - getRow()) == 0;
    }

    // Determines whether there are any pieces blocking the Tor's path.
    // The method checks for obstacles between the current position and
    // the target position when moving in a straight line.
    // True if there is obstruction, false otherwise.
    // Written by Yi Thung
    @Override
    public boolean isCollide(int col, int row) {
        // Check for obstructions to the left.
        if (getCol() > col) {
            for (int c = getCol() - 1; c > col; c--) {
                if (board.getPiece(c, getRow()) != null) {
                    return true;
                }
            }
        }

        // Check for obstructions to the right.
        if (getCol() < col) {
            for (int c = getCol() + 1; c < col; c++) {
                if (board.getPiece(c, getRow()) != null) {
                    return true;
                }
            }
        }

        // Check for obstructions above.
        if (getRow() > row) {
            for (int r = getRow() - 1; r > row; r--) {
                if (board.getPiece(getCol(), r) != null) {
                    return true;
                }
            }
        }

        // Check for obstructions below.
        if (getRow() < row) {
            for (int r = getRow() + 1; r < row; r++) {
                if (board.getPiece(getCol(), r) != null) {
                    return true;
                }
            }
        }

        return false;
    }
}
