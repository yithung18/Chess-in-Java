
/**
 * ChessModel serves as the core of the chess game logic.
 * It manages the game state, including piece positions, turn tracking, 
 * move validation, game saving/loading, and notifying observers about updates.
 * The file was jointly coded by everyone in the group
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ChessModel implements Subject {
    private boolean isRedTurn;
    private ArrayList<Pieces> piecesArr;        
    private ArrayList<Observer> observers = new ArrayList<>();      
    public Pieces selectedPiece;       
    private int turnCount;
    private boolean gameOver;
    private int col = 5;
    private int row = 8;
    private String winner = "";
    private int squareSize;

    // Written by Zong Xin
    public ChessModel() {
        this.isRedTurn = true; // White starts first
        this.piecesArr = new ArrayList<>();
        this.selectedPiece = null;
        this.turnCount = 0;
        this.gameOver = false;
        setSquareSize(65); // Due to the preferred dimension, the initial square size is 65

        initializeBoard();
    }

    // ================== Board Setup and Updates ==================

    // Get the current square size
    // Written by Zong Xin
    public int getSquareSize() {
        return squareSize;
    }

    // Set the new square size and notify observer
    // Written by Zong Xin
    public void setSquareSize(int newSquareSize) {
        this.squareSize = newSquareSize;
        for (Pieces piece : piecesArr) {
            piece.updateImageSize(squareSize); // Update the image size of each piece
            piece.updatePosition(squareSize); // Update the position of each piece
        }
        notifyObservers();
    }

    // Initializes the board with all pieces at their starting positions.
    // Written by Yi Thung
    public void initializeBoard() {
        // Add Rams for each column
        for (int i = 0; i < col; i++) {
            piecesArr.add(new Ram(squareSize, i, 6, true)); // Red team Rams
            piecesArr.add(new Ram(squareSize, i, 1, false)); // Blue team Rams
        }

        // Add other pieces    
        piecesArr.add(new Biz(squareSize, 1, 7, true)); // Red Biz
        piecesArr.add(new Biz(squareSize, 3, 7, true)); // Red Biz
        piecesArr.add(new Biz(squareSize, 1, 0, false)); // Blue Biz
        piecesArr.add(new Biz(squareSize, 3, 0, false)); // Blue Biz

        piecesArr.add(new Tor(squareSize, this, 4, 7, true)); // Red Tor
        piecesArr.add(new Tor(squareSize, this, 0, 0, false)); // Blue Tor

        piecesArr.add(new Xor(squareSize, this, 4, 0, false)); // Blue Xor
        piecesArr.add(new Xor(squareSize, this, 0, 7, true)); // Red Xor

        piecesArr.add(new Sau(squareSize, 2, 7, true)); // Red Sau
        piecesArr.add(new Sau(squareSize, 2, 0, false)); // Blue Sau
    }

    // ================== Observer Pattern ==================

    // Setting up for model to be the subject
    // Written Zong Xin
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    @Override
    public void notifyWinner(String winner) {
        for (Observer observer : observers) {
            observer.updateWinner(winner);
        }
    }

    // ================== Game Logic =================

    // Retrieves all pieces currently in play.
    // Written by Zong Xin
    public ArrayList<Pieces> getPiecesArr() {
        return piecesArr;
    }

    // Retrieves the selected piece for movement.
    // Written by Zong Xin
    public Pieces getSelectedPiece() {
        return selectedPiece;
    }

    // Retrieves a piece at a specific board position.
    // Written by Yi Thung
    public Pieces getPiece(int col, int row) {
        for (Pieces p : piecesArr) {
            if (p.getCol() == col && p.getRow() == row) {
                return p;
            }
        }
        return null;
    }

    // Executes a move by updating the piece's position and checking for captures.
    // Written by Yi Thung
    public void makeMove(MovePiece move) {
        move.getPiece().setCol(move.getNewCol());
        move.getPiece().setXPos(move.getNewCol() * getSquareSize());
        move.getPiece().setRow(move.getNewRow());
        move.getPiece().setYPos(move.getNewRow() * getSquareSize());

        // Check if a piece was captured
        if (move.getCapture() != null) {
            gameOver = move.isSau();
            if (gameOver) {
                boolean winnerColor;
                winnerColor = move.getWinnerColor();
                if (winnerColor) {
                    winner = "Red";
                } else {
                    winner = "Blue";
                }
                notifyWinner(winner);
                clearPieces();
                notifyObservers();
            }
            kill(move);
        }
        endTurn();
    }

    // Removes a captured piece from the game.
    // Written by Yi Thung
    public void kill(MovePiece move) {
        piecesArr.remove(move.getCapture());
    }

    // Validates if a move is legal according to game rules.
    // Written by Yi Thung
    public boolean isValidMove(MovePiece move) {
        if (move.getPiece().getIsRed() != isRedTurn) {
            System.out.println("Invalid move: Not your turn!");
            return false;
        }

        if (move.getNewCol() < 0 || move.getNewCol() >= col || move.getNewRow() < 0 || move.getNewRow() >= row) {
            return false; // Move is out of bounds
        }

        if (sameTeam(move.getPiece(), move.getCapture())) {
            return false; // Cannot capture your own piece
        }

        if (!move.getPiece().isValid(move.getNewCol(), move.getNewRow())) {
            return false; // Piece-specific movement rules
        }

        if (move.getPiece().isCollide(move.getNewCol(), move.getNewRow())) {
            return false; // Movement blocked by other pieces
        }

        return true;
    }

    // Generates all legal moves for the current piece
    // Written by Zong Xin
    public ArrayList<MovePiece> getValidMoves() {
        ArrayList<MovePiece> validMoves = new ArrayList<>();
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                MovePiece move = new MovePiece(this, this.getSelectedPiece(), c, r);
                if (isValidMove(move)) {
                    validMoves.add(move);
                }
            }
        }
        return validMoves;
    }

    // Checks if two pieces belong to the same team.
    // Written by Yi Thung
    public boolean sameTeam(Pieces p1, Pieces p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        return p1.getIsRed() == p2.getIsRed();
    }

    // The removes every pieces from the game.
    // Written by Sarven
    public void clearPieces() {
        piecesArr.clear();
    }

    // Save the match into a textfile
    // Written by Sarven
    public void saveGame(String fileName) {
        try (FileWriter writer = new FileWriter(fileName + ".txt")) {
            writer.write("Is it Red's Turn," + isRedTurn + "\n"); // Save the turn
            for (Pieces piece : piecesArr) {
                writer.write(
                        piece.getName() + "," + piece.getCol() + "," + piece.getRow() + "," + piece.getIsRed() + "\n");
            }
            System.out.println("Game saved to " + fileName + ".txt");
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    // Load the game from the previous saved file
    // Written by Sarven
    public void loadGame(String fileName) {
        clearPieces();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            if (line.startsWith("Is")) {
                isRedTurn = Boolean.parseBoolean(line.split(",")[1]); // Restore turn
            }
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String pieceName = parts[0];
                int col = Integer.parseInt(parts[1]);
                int row = Integer.parseInt(parts[2]);
                boolean isRed = Boolean.parseBoolean(parts[3]);
                Pieces piece = createPiece(pieceName, col, row, isRed);
                if (piece != null) {
                    piece.setFlipped(!isRedTurn); // Restore flipped state
                    piecesArr.add(piece);
                }
            }
            // Adjust board flip state after loading
            if (!isRedTurn) {
                flipImage();
                flipImage();
            }
            notifyObservers();
            System.out.println("Game loaded from " + fileName);
        } catch (IOException e) {
            System.err.println("Error loading game: " + e.getMessage());
        }
    }

    // This method is used for creating the pieces from reading the save file
    // Written by Sarven
    public Pieces createPiece(String pieceName, int col, int row, boolean isRed) {
        switch (pieceName) {
            case "Ram":
                return new Ram(squareSize, col, row, isRed);
            case "Biz":
                return new Biz(squareSize, col, row, isRed);
            case "Tor":
                return new Tor(squareSize, this, col, row, isRed);
            case "Xor":
                return new Xor(squareSize, this, col, row, isRed);
            case "Sau":
                return new Sau(squareSize, col, row, isRed);
            default:
                return null;
        }
    }

    // This method wipes the board and reinitialize the board.
    // Written by Sarven
    public void resetBoard() {
        clearPieces();
        initializeBoard();
        this.turnCount = 0; // Reset turn count
        this.isRedTurn = true; // Ensure Red always starts after restart
        this.gameOver = false;
        notifyObservers();
    }

    // It will update every existing pieces to the opposite side of the board
    // Written by Aveeniskh
    public void flipBoard() {
        for (Pieces piece : piecesArr) {
            piece.setCol((col - 1) - piece.getCol());
            piece.setRow((row - 1) - piece.getRow());
            piece.setXPos(piece.getCol() * squareSize);
            piece.setYPos(piece.getRow() * squareSize);
            piece.flipOrientation();
        }
        notifyObservers();
    }

    // It will update the image's orientation when the pieces are loaded into the
    // save file during blue's turn
    // Written by Zong Xin
    public void flipImage() {
        for (Pieces piece : piecesArr) {
            piece.flipOrientation();
        }
    }

    // It manages what happens when the turn end.
    // Written by Sarven
    public void endTurn() {
        // It toggles isRedTurn to keep track of which player's turn
        isRedTurn = !isRedTurn;

        // It makes Xor and Tor pieces switch place after every 2 turns (Blue move twice
        // and Red move twice) and increment the
        // turn count after each turn.
        if (++turnCount % 4 == 0) {
            for (int i = 0; i < piecesArr.size(); i++) {
                Pieces piece = piecesArr.get(i);
                if (piece instanceof Tor) {
                    piecesArr.set(i, new Xor(squareSize, this, piece.getCol(), piece.getRow(), piece.getIsRed()));
                } else if (piece instanceof Xor) {
                    piecesArr.set(i, new Tor(squareSize, this, piece.getCol(), piece.getRow(), piece.getIsRed()));
                }
            }
        }

        // Once the necessary data has been updated, it will flip the board so that the
        // current player's turn will
        // be displayed at the bottom of the screen.
        flipBoard();
        System.out.println("Turn ended. Current turn: " + (isRedTurn ? "Red" : "Blue"));
    }

    // Setter to get update who's turn
    // Written by Sarven
    public void setRedTurn(boolean isRedTurn) {
        this.isRedTurn = isRedTurn;
    }

}
