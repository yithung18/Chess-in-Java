public class Sau extends Pieces {
    // Written by Zong Xin
    public Sau(int squareSize, int col, int row, boolean isRed) {
        // Initialize the parent class with appropriate parameters.
        super(squareSize, col, row, isRed, "Sau");
        
        int xPos = col * squareSize;
        int yPos = row * squareSize;
        initializeScreenPosition(xPos, yPos);

        // It uses a method from the parent class to load the
        // appropriate image based on its team color.
        initializeImage("pieces/" + (isRed ? "r_sau" : "b_sau"));
        if (getCurrentImage() == null) {
            System.err.println("Failed to load image for " + (isRed ? "red Sau" : "blue Sau"));
        }
    }

    // Determines whether the move to the specified position is valid.
    // The Sau piece moves one square in any direction
    // Written by Yi Thung
    @Override
    public boolean isValid(int col, int row) {
        // Movement directions relative to the BLUE team's perspective:
        // The Sau piece can move one square in any direction.

        // Up
        if (getRow() - row == 1 && getCol() - col == 0) {
            return true;
        }
        // Up-right
        if (getRow() - row == 1 && getCol() - col == 1)
            return true;
        // Up-left
        if (getRow() - row == 1 && getCol() - col == -1)
            return true;
        // Left
        if (getCol() - col == 1 && getRow() - row == 0)
            return true;
        // Right
        if (getCol() - col == -1 && getRow() - row == 0)
            return true;
        // Down
        if (getCol() - col == 0 && getRow() - row == -1)
            return true;
        // Down-left
        if (getCol() - col == 1 && getRow() - row == -1)
            return true;
        // Down-right
        if (getCol() - col == -1 && getRow() - row == -1)
            return true;

        // If none of the valid moves match, return false.
        return false;
    }
}
