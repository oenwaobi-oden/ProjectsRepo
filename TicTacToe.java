import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTacToe extends JFrame implements ActionListener {

    // Game state
    private boolean playerXTurn = true;
    private boolean roundOver = false;

    private int player1Score = 0;
    private int player2Score = 0;
    private int drawScore = 0;

    // Top panel
    private JPanel topPanel;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel turnLabel;
    private JButton resetButton;

    // Board
    private JPanel boardPanel;
    private JButton[][] buttons = new JButton[3][3];

    public TicTacToe() {
        setTitle("Tic Tac Toe");
        setSize(600, 600);
        setLayout(new BorderLayout());
        setLocation(100, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        createPanel();
        createBoardPanel();

        setVisible(true);
    }

    public void createPanel() {
        topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(Color.BLACK);

        label1 = new JLabel("Score - Player 1 (X): 0");
        label1.setForeground(Color.WHITE);

        label2 = new JLabel("Player 2 (O): 0");
        label2.setForeground(Color.WHITE);

        label3 = new JLabel("Draws: 0");
        label3.setForeground(Color.WHITE);

        turnLabel = new JLabel("Player 1's turn: X");
        turnLabel.setForeground(Color.YELLOW);
        
        resetButton = new JButton("Reset Round");
        resetButton.setFocusable(false);
        resetButton.addActionListener(e -> resetBoard());

        topPanel.add(label1);
        topPanel.add(label2);
        topPanel.add(label3);
        topPanel.add(turnLabel);
        topPanel.add(resetButton);

        add(topPanel, BorderLayout.NORTH);
    }

    public void createBoardPanel() {
        boardPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        boardPanel.setBackground(Color.BLACK);
        boardPanel.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col] = new JButton();
                buttons[row][col].setFont(
                    new Font("Arial", Font.BOLD, 80)
                );
                buttons[row][col].setFocusPainted(false);
                buttons[row][col].addActionListener(this);

                boardPanel.add(buttons[row][col]);
            }
        }

        add(boardPanel, BorderLayout.CENTER);
    }

    public boolean checkWinner(char playerSymbol) {
        String symbol = String.valueOf(playerSymbol);

        // Rows
        for (int row = 0; row < 3; row++) {
            if (buttons[row][0].getText().equals(symbol)
                    && buttons[row][1].getText().equals(symbol)
                    && buttons[row][2].getText().equals(symbol)) {
                return true;
            }
        }

        // Columns
        for (int col = 0; col < 3; col++) {
            if (buttons[0][col].getText().equals(symbol)
                    && buttons[1][col].getText().equals(symbol)
                    && buttons[2][col].getText().equals(symbol)) {
                return true;
            }
        }

        // Top-left to bottom-right diagonal
        if (buttons[0][0].getText().equals(symbol)
                && buttons[1][1].getText().equals(symbol)
                && buttons[2][2].getText().equals(symbol)) {
            return true;
        }

        // Top-right to bottom-left diagonal
        if (buttons[0][2].getText().equals(symbol)
                && buttons[1][1].getText().equals(symbol)
                && buttons[2][0].getText().equals(symbol)) {
            return true;
        }

        return false;
    }

    public boolean isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText().equals("")) {
                    return false;
                }
            }
        }

        return true;
    }

    public void disableBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setEnabled(false);
            }
        }
    }

    public void updateScoreLabels() {
        label1.setText("Score - Player 1 (X): " + player1Score);
        label2.setText("Player 2 (O): " + player2Score);
        label3.setText("Draws: " + drawScore);
    }
    
    public void resetBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");
                buttons[row][col].setEnabled(true);
                buttons[row][col].setForeground(Color.BLACK);
            }
        }

        // Start every new round with Player 1 / X
        playerXTurn = true;

        // Allow the new round to accept input
        roundOver = false;

        // The score variables are intentionally not changed
        updateScoreLabels();

        turnLabel.setText("Player 1's turn: X");
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (roundOver) {
            return;
        }

        JButton selectedButton = (JButton) event.getSource();

        if (!selectedButton.getText().equals("")) {
            turnLabel.setText("Invalid move. Choose an empty square.");
            return;
        }

        char currentPlayer;

        if (playerXTurn) {
            selectedButton.setText("X");
            selectedButton.setForeground(Color.BLUE);
            currentPlayer = 'X';
        } else {
            selectedButton.setText("O");
            selectedButton.setForeground(Color.RED);
            currentPlayer = 'O';
        }

        // Check the move before allowing a new turn.
        if (checkWinner(currentPlayer)) {
            roundOver = true;
            disableBoard();

            if (currentPlayer == 'X') {
                player1Score++;
                turnLabel.setText("Player 1 wins with X!");
            } else {
                player2Score++;
                turnLabel.setText("Player 2 wins with O!");
            }

            updateScoreLabels();
            return;
        }

        // A full board without a winner is a draw.
        if (isBoardFull()) {
            roundOver = true;
            drawScore++;
            disableBoard();

            turnLabel.setText("Match ends in a draw!");
            updateScoreLabels();
            return;
        }

        playerXTurn = !playerXTurn;

        if (playerXTurn) {
            turnLabel.setText("Player 1's turn: X");
        } else {
            turnLabel.setText("Player 2's turn: O");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicTacToe());
    }
}