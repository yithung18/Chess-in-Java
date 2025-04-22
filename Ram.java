public class Ram extends Pieces {

    // Boolean flag to track whether
    // the Ram piece has reached the edge of the board.
    private Boolean reachEnd = false;

    // Written by Zong Xin
    public Ram(int squareSize, int col, int row, boolean isRed) {
        // Initialize the parent class with appropriate parameters.
        super(squareSize, col, row, isRed, "Ram");
        
        int xPos = col * squareSize;
        int yPos = row * squareSize;
        initializeScreenPosition(xPos, yPos);

        // It uses a method from the parent class to load the
        // appropriate image based on its team color.
        initializeImage("pieces/" + (isRed ? "r_ram" : "b_ram"));
        if (getCurrentImage() == null) {
            System.err.println("Failed to load image for " + (isRed ? "r_ram" : "b_ram"));
        }
    }

    // Check if the Ram is at the edge of the board.
    // If it does, flip its movement direction.
    // Written by Aveeniskh
    private void isAtEdge() {
        // For red piece
        if ((getIsRed() == true && getRow() == 7) || (getIsRed() == true && getRow() == 0)) {
            this.reachEnd = !reachEnd; // Toggle the reachEnd state
            setFlipped(reachEnd); // Update flipped state
        }

        // For blue piece
        if ((getIsRed() == false && getRow() == 7) || (getIsRed() == false && getRow() == 0)) {
            this.reachEnd = !reachEnd;
            setFlipped(!reachEnd); // The flipped state is opposite of reachEnd for blue.
        }
    }

    // Determines if the move to the specified position is valid for the Ram piece.
    // The Ram piece moves in a straight vertical direction. When it reaches the
    // edge of the board, it "flips" and moves in the opposite direction.
    // Written by Zong Xin
    @Override
    public boolean isValid(int col, int row) {
        // First check whether ram has reached the edge of the board.
        isAtEdge();
        // Determine the next valid row based on the current movement direction.
        int ramMove = getRow() + (reachEnd ? 1 : -1);

        return col == getCol() && row == ramMove;
    }
    

}