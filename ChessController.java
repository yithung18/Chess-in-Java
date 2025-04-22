// This file was co-written by Leong Zong Xin and Wong Yi Thung.
// Yi Thung implemented the overridden methods from MouseEvent, 
// while Zong Xin worked on the remaining functionalities.
// The ChessController class handles user interactions, including mouse events for piece selection and movement,  
// as well as button actions such as restarting, saving, and loading the game.

import java.awt.event.*;
import java.util.*;

public class ChessController extends MouseAdapter implements ActionListener {
    private ChessModel board;       
    private ChessView view;
    private boolean wasDragged = false; // Keep track of whether the piece has been dragged before.

    // Constructor for ChessController.
    // Initializes the controller with references to the model and view.
    public ChessController(ChessModel model, ChessView view) {
        this.board = model;
        this.view = view;
    }

    /**
     * Handles mouse press events.
     * Determines if a piece is selected when the player clicks on a square.
     *
     * @param e The MouseEvent triggered by the player's click.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        wasDragged = false;
        int squareSize = view.getSquareSize();
        int col = e.getX() / squareSize;
        int row = e.getY() / squareSize;

        Pieces piecePos = board.getPiece(col, row);
        if (piecePos != null) {
            board.selectedPiece = piecePos;
        }
    }

    /**
     * Handles mouse release events.
     * Moves a selected piece to a new square if the move is valid, otherwise
     * restored to original position.
     *
     * @param e The MouseEvent triggered by releasing the mouse button.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (board.selectedPiece != null && wasDragged) {
            int squareSize = view.getSquareSize();
            int col = e.getX() / squareSize;
            int row = e.getY() / squareSize;

            MovePiece move = new MovePiece(board, board.selectedPiece, col, row);

            if (board.isValidMove(move)) {
                board.makeMove(move);
            } else {
                board.selectedPiece.setXPos(board.selectedPiece.getCol() * view.getSquareSize());
                board.selectedPiece.setYPos(board.selectedPiece.getRow() * view.getSquareSize());
            }
        }
        board.selectedPiece = null; // deselect the piece
        view.repaint(); // update the visual state
    }

    /**
     * Handles mouse drag events.
     * Allows a selected piece to be dragged while ensuring it remains within board
     * boundaries.
     *
     * @param e The MouseEvent triggered by dragging the mouse.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (board.selectedPiece != null) {
            wasDragged = true;
            // Calculate new positions
            int squareSize = view.getSquareSize();
            int newX = e.getX() - squareSize / 2;
            int newY = e.getY() - squareSize / 2;

            // Ensures the piece stays within board boundaries.
            newX = Math.max(0, Math.min(newX, view.getWidth() - squareSize));
            newY = Math.max(0, Math.min(newY, view.getHeight() - squareSize));

            // Making sure the pieces aligns with the grid
            newX = (newX / squareSize) * squareSize;
            newY = (newY / squareSize) * squareSize;

            // Update the position
            board.selectedPiece.setXPos(newX);
            board.selectedPiece.setYPos(newY);

            // Repaint to reflect changes
            view.repaint();
        }
    }

    /**
     * Handles button click events.
     * Executes actions based on the button pressed (Restart, Save, Load).
     *
     * @param e The ActionEvent triggered by button clicks.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Restart":
                startNewGame();
                break;
            case "Save":
                savingBoardState();
                break;
            case "Load File":
                loadSaveFile();
                break;
        }
    }

    /**
     * Retrieves the valid moves for the currently selected piece.
     *
     * @return A list of valid move options for the selected piece.
     */
    public ArrayList<MovePiece> getValidMovesForSelectedPiece() {
        Pieces piece = board.getSelectedPiece();
        return (piece != null) ? board.getValidMoves() : new ArrayList<>();
    }

    /**
     * Retrieves all chess pieces currently on the board.
     *
     * @return A list of all active chess pieces.
     */
    public ArrayList<Pieces> getPiecesForRender() {
        ArrayList<Pieces> piecesList = new ArrayList<>();
        for (Pieces p : board.getPiecesArr()) {
            piecesList.add(p);
        }
        return piecesList;
    }

    // Starts a new game by clearing and reinitializing the board.
    public void startNewGame() {
        board.resetBoard();
        board.setRedTurn(true);
        view.repaint();
    }

    // Saves the current game state to a file named "Save".
    private void savingBoardState() {
        board.saveGame("Save");
    }

    // Loads the previously saved game state from the file "Save".
    private void loadSaveFile() {
        board.loadGame("Save.txt");
    }

}
