// This file was primarily written by Leong Zong Xin,  
// except for the paintComponent method, which was jointly coded by Wong Yi Thung and Zong Xin.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ChessView extends JPanel implements Observer {    
    private ChessModel board;
    private ChessController controller;     

    // Constructor for ChessView
    public ChessView(ChessModel model) {
        this.board = model;

        // Register the view as an observer of the model
        this.board.addObserver(this);

        // Add a listener to handle resizing the panel
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Notify model to update square size
                board.setSquareSize(getSquareSize());
            }
        });
    }

    // Sets up the controller to handle mouse interactions,
    // forwarding user input for processing.
    public void setController(ChessController controller) {
        this.controller = controller;
        this.addMouseListener(controller);
        this.addMouseMotionListener(controller);
    }

    // Setting up the observer
    @Override
    public void update() {
        // Will be called whenever the model notifies of a change
        repaint();
    }

    @Override
    public void updateWinner(String winner) {
        // Display the winner when the game ends
        JOptionPane.showMessageDialog(this, "The winner is: " + winner);
    }

    // Renders the chessboard, pieces, and highlights valid moves for the selected
    // piece.
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int maxRow = 8;
        int maxCol = 5;
        int squareSize = getSquareSize();

        // Draw the chessboard
        for (int r = 0; r < maxRow; r++) {
            for (int c = 0; c < maxCol; c++) {
                g2.setColor((c + r) % 2 == 0 ? Color.WHITE : Color.BLACK);
                g2.fillRect(c * squareSize, r * squareSize, squareSize, squareSize);
            }
        }

        // Draw the pieces
        ArrayList<Pieces> pieceForRender = controller.getPiecesForRender();
        for (Pieces p : pieceForRender) {
            if (p.getCurrentImage() != null) {
                g2.drawImage(p.getCurrentImage(), p.getXPos(), p.getYPos(), squareSize, squareSize, null);
            }
        }

        // Highlight valid moves for the selected piece
        ArrayList<MovePiece> validMoves = controller.getValidMovesForSelectedPiece();

        for (MovePiece piece : validMoves) {
            int x = piece.getNewCol() * squareSize;
            int y = piece.getNewRow() * squareSize;
            g2.setColor(new Color(251, 255, 101, 90));
            g2.fillRect(x, y, squareSize, squareSize);
        }
    }

    // Calculates the size of each square based on the window’s dimensions,
    // ensuring that the board and images scale proportionally.
    public int getSquareSize() {
        int boardSize = Math.min(getWidth(), getHeight());
        return boardSize / 8;
    }
}