// This file was written by Leong Zong Xin.  
// It initializes the chess game's Model-View-Controller (MVC) structure and sets up the graphical user interface (GUI).  
// - The `ChessModel` represents the game logic and state.  
// - The `ChessView` is responsible for displaying the game board and pieces.  
// - The `ChessController` handles user input and updates the model accordingly.  
// The main window (`JFrame`) contains the game board and interactive buttons for player actions.  

import java.awt.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        ChessModel model = new ChessModel();
        ChessView view = new ChessView(model);  
        ChessController controller = new ChessController(model, view);  
        view.setController(controller);

        JFrame frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(540, 560);
        frame.setLayout(new BorderLayout());
        frame.add(view, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton restartBtn = new JButton("Restart");
        JButton saveBtn = new JButton("Save");
        JButton loadBtn = new JButton("Load File");
        buttonPanel.add(restartBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(loadBtn);
        frame.add(buttonPanel, BorderLayout.NORTH);
        restartBtn.addActionListener(controller);
        saveBtn.addActionListener(controller);
        loadBtn.addActionListener(controller);

        frame.setVisible(true);
        frame.setResizable(true);
    }
}