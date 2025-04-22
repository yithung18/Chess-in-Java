
/**
 * This file was co-written by Leong Zong Xin, Wong Yi Thung and Aveeniskh A/L Thiagarajan. Specifics will be specified below.
 * The Pieces class represents a chess piece in the game.
 * It handles piece properties such as position, color, name, and graphical representation.
 * The class also supports flipping images, resizing, and updating positions.
 */

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class Pieces {
    private BufferedImage currentImage; // Current image of the piece
    private BufferedImage defaultImage; // Default orientation
    private BufferedImage flippedImage; // Flipped orientation      
    private int col, row; // Logical position
    private int xPos, yPos; // Screen position
    private boolean isRed; // Team color
    private String name; // Piece name
    private int squareSize; // Current square size
    private boolean flipped = false; // To decide the pieces' orientation

    // Constructor by Zong Xin. Initializes a piece with the specified attributes.
    public Pieces(int squareSize, int col, int row, boolean isRed, String name) {
        this.squareSize = squareSize;
        this.col = col;
        this.row = row;
        this.isRed = isRed;
        this.name = name;
    }

    public void initializeScreenPosition(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    // Loads an image for the chess piece from the given file path.
    // Written by Yi Thung.
    public BufferedImage getImage(String imagePath) {
        BufferedImage image = null;

        try {
            // Load the image
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            if (image == null) {
                System.err.println("Image not found: " + imagePath + ".png");
            }
        } catch (IOException e) {
            e.printStackTrace();
            // throwable along with other details like the line number and class name where the exception occurred.
        }

        return image;
    }

    // Resizes the piece's image based on the square size.
    // Applies high-quality interpolation for better scaling.
    // Written by Zong Xin
    public void updateImageSize(int squareSize) {
        int newSize = squareSize - 10;

        BufferedImage bufferedScaledImage = new BufferedImage(newSize, newSize, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bufferedScaledImage.createGraphics();

        // Set the rendering hints for high-quality scaling
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC); // High-quality
                                                                                                            // interpolation
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Smooth edges
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY); // Quality rendering

        g2d.drawImage(currentImage, 0, 0, newSize, newSize, null);
        g2d.dispose();
        this.currentImage = bufferedScaledImage;
    }

    // Updates the screen position of the piece based on the logical board position.
    // Ensures the piece aligns correctly with the board grid.
    // Written by Zong Xin.
    public void updatePosition(int squareSize) {
        // Calculate new positions
        int newX = xPos / squareSize;
        int newY = yPos / squareSize;
        this.squareSize = squareSize;

        // Making sure the pieces aligns with the grid
        newX = newX + (newX * squareSize);
        newY = newY + (newY * squareSize);

        // Update the position
        this.xPos = newX;
        this.yPos = newY;
    }

    // Creates a flipped version of the given image.
    // This is used to visually differentiate opposing sides.
    // Co-written by Zong Xin and Aveeniskh
    private BufferedImage createFlippedImage(BufferedImage img) {
        if (img == null)
            return null;

        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage flipped = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);  //aggregation
        Graphics2D g2d = flipped.createGraphics();

        // Translate the graphics context to the bottom of the image
        g2d.translate(0, height);

        // Flip the image vertically by scaling it negatively along the Y-axis
        g2d.scale(1, -1);

        // Draw the original image with the inverted graphics
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return flipped;
    }

    // Loads the default and flipped images for the piece.
    // Initializes the piece with the default orientation.
    // Unfortunately the default pic is inverted therefore initial orientation is
    // the flipped image.
    // Written by Aveeniskh.
    public void initializeImage(String imagePath) {
        this.defaultImage = getImage(imagePath); // Load the default image
        this.flippedImage = createFlippedImage(this.defaultImage); // Create the flipped version
        this.currentImage = this.flippedImage; // Set the initial orientation
    }

    // Toggles the piece's orientation between default and flipped.
    // This visually rotates the piece when needed.
    // Written by Zong Xin.
    public void flipOrientation() {
        if (!flipped) {
            this.currentImage = defaultImage; // Use the flipped orientation
            this.flipped = true;
        } else {
            this.currentImage = flippedImage; // Use the default orientation
            this.flipped = false;
        }
    }

    // Determines if the piece can legally move to the given position.
    // This method should be overridden by specific piece types.
    // Written by Yi Thung
    public boolean isValid(int col, int row) {
        return true;
    }

    // Checks if this piece would collide with another piece at the given position.
    // This method will be implemented with tor and xor's game logic.
    // Written by Yi Thung
    public boolean isCollide(int col, int row) {
        return false;
    }

    // Below are getters and setters needed to perform other functions.
    // Written by Zong Xin
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    public BufferedImage getCurrentImage() {
        return currentImage;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public Boolean getIsRed() {
        return isRed;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    public void setYPos(int yPos) {
        this.yPos = yPos;
    }

    public String getName() {
        return name;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
