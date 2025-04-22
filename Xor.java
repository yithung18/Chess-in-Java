public class Xor extends Pieces {

    private ChessModel board; 

    // Written by Zong Xin
    public Xor(int squareSize, ChessModel board, int col, int row, boolean isRed) {
        // Initialize the parent class with appropriate parameters.
        super(squareSize, col, row, isRed, "Xor");
        
        int xPos = col * squareSize;
        int yPos = row * squareSize;
        initializeScreenPosition(xPos, yPos);
        this.board = board;

        // It uses a method from the parent class to load the
        // appropriate image based on its team color.
        initializeImage("pieces/" + (isRed ? "r_xor" : "b_xor"));

        if (getCurrentImage() == null) {
            System.err.println("Failed to load image for " + (isRed ? "red Xor" : "blue Xor"));
        }
    }

    // Determines whether a move is valid for the Xor piece.
    // Written by Yi Thung
    public boolean isValid(int col, int row) {
        // The Xor moves diagonally.
        return Math.abs(getCol() - col) == Math.abs(getRow() - row);
    }

    // Determines whether there are any pieces blocking the Xor's path.
    // The method checks for obstacles along the diagonal movement path.
    // True if there is obstruction, false otherwise.
    // Written by Yi Thung
    public boolean isCollide(int col, int row) {
        // Check for obstacles in the diagonal paths.

        // Up-left direction
        if (getCol() > col && getRow() > row) {
            for (int i = 1; i < Math.abs(getCol() - col); i++) {
                if (board.getPiece(getCol() - i, getRow() - i) != null) {
                    return true;
                }
            }
        }

        // Up-right direction
        if (getCol() < col && getRow() > row) {
            for (int i = 1; i < Math.abs(getCol() - col); i++) {
                if (board.getPiece(getCol() + i, getRow() - i) != null) {
                    return true;
                }
            }
        }

        // Down-left direction
        if (getCol() > col && getRow() < row) {
            for (int i = 1; i < Math.abs(getCol() - col); i++) {
                if (board.getPiece(getCol() - i, getRow() + i) != null) {
                    return true;
                }
            }
        }

        // Down-right direction
        if (getCol() < col && getRow() < row) {
            for (int i = 1; i < Math.abs(getCol() - col); i++) {
                if (board.getPiece(getCol() + i, getRow() + i) != null) {
                    return true;
                }
            }
        }

        return false;
    }
}
