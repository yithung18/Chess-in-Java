public class Biz extends Pieces {

    // Constructor written by Zong Xin
    public Biz(int squareSize, int col, int row, boolean isRed) {
        // Initialize the parent class with appropriate parameters.
        super(squareSize, col, row, isRed, "Biz");
        
        int xPos = col * squareSize;
        int yPos = row * squareSize;
        initializeScreenPosition(xPos, yPos);

        // It uses a method from the parent class to load the
        // appropriate image based on its team color.
        initializeImage("pieces/" + (isRed ? "r_biz" : "b_biz"));

        if (getCurrentImage() == null) {
            System.err.println("Failed to load image for " + (isRed ? "red Biz" : "blue Biz"));
        }
    }

    // Determines whether a move is valid for the Biz piece.
    // Written by Yi Thung
    @Override
    public boolean isValid(int col, int row) {
        // The Biz moves in an "L" shape: 2 squares in one direction, 1 in the other.
        return Math.abs(col - getCol()) * Math.abs(row - getRow()) == 2;
        // get +ve value
    }
}
