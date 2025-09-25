import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.*;

public class Minesweeper extends JFrame {
    // Constantes para mejorar la mantenibilidad
    private static final int BOARD_SIZE = 10;
    private static final int MINE_COUNT = 10;
    private static final int TOTAL_SAFE_CELLS = BOARD_SIZE * BOARD_SIZE - MINE_COUNT;
    private static final String MINE_SYMBOL = "💣";
    private static final String FLAG_SYMBOL = "🚩";
    
    private JButton[][] buttons;
    private boolean[][] mines;
    private boolean[][] flagged;
    private int[][] surroundingMines;
    private int uncoveredCells;
    private int clickCount;
    private boolean gameOver;
    private JLabel statusLabel;
    private JLabel mineCountLabel;
    private int flagCount;

    public Minesweeper() {
        initializeGame();
        setupUI();
        placeMines();
        countSurroundingMines();
    }

    private void initializeGame() {
        buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
        mines = new boolean[BOARD_SIZE][BOARD_SIZE];
        flagged = new boolean[BOARD_SIZE][BOARD_SIZE];
        surroundingMines = new int[BOARD_SIZE][BOARD_SIZE];
        uncoveredCells = 0;
        clickCount = 0;
        gameOver = false;
        flagCount = 0;
    }

    private void setupUI() {
        setTitle("Minesweeper - ¡Mucha Suerte!");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Panel superior con información del juego
        JPanel topPanel = new JPanel(new FlowLayout());
        statusLabel = new JLabel("Clics: 0");
        mineCountLabel = new JLabel("Minas restantes: " + MINE_COUNT);
        JButton resetButton = new JButton("🔄 Nuevo Juego");
        resetButton.addActionListener(e -> resetGame());
        
        topPanel.add(statusLabel);
        topPanel.add(mineCountLabel);
        topPanel.add(resetButton);
        add(topPanel, BorderLayout.NORTH);
        
        // Panel del tablero
        JPanel gamePanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                buttons[i][j] = createGameButton(i, j);
                gamePanel.add(buttons[i][j]);
            }
        }
        
        add(gamePanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null); // Centrar ventana
        setVisible(true);
    }

    private JButton createGameButton(int i, int j) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(30, 30));
        button.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Usar MouseAdapter para manejar clics izquierdo y derecho
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameOver) return;
                
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (!flagged[i][j]) {
                        uncoverCell(i, j);
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    toggleFlag(i, j);
                }
            }
        });
        
        return button;
    }

    private void toggleFlag(int i, int j) {
        if (buttons[i][j].isEnabled()) {
            flagged[i][j] = !flagged[i][j];
            if (flagged[i][j]) {
                buttons[i][j].setText(FLAG_SYMBOL);
                buttons[i][j].setBackground(Color.YELLOW);
                flagCount++;
            } else {
                buttons[i][j].setText("");
                buttons[i][j].setBackground(null);
                flagCount--;
            }
            updateMineCount();
        }
    }

    private void updateMineCount() {
        mineCountLabel.setText("Minas restantes: " + (MINE_COUNT - flagCount));
    }

    private void updateStatus() {
        statusLabel.setText("Clics: " + clickCount);
    }

    private void placeMines() {
        Random random = new Random();
        int placedMines = 0;

        while (placedMines < MINE_COUNT) {
            int i = random.nextInt(BOARD_SIZE);
            int j = random.nextInt(BOARD_SIZE);

            if (!mines[i][j]) {
                mines[i][j] = true;
                placedMines++;
            }
        }
    }

    private void countSurroundingMines() {
        // Direcciones para los 8 vecinos (optimización con arrays)
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};
        
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (!mines[i][j]) {
                    int count = 0;
                    for (int k = 0; k < 8; k++) {
                        int ni = i + dx[k];
                        int nj = j + dy[k];
                        if (isValidPosition(ni, nj) && mines[ni][nj]) {
                            count++;
                        }
                    }
                    surroundingMines[i][j] = count;
                } else {
                    surroundingMines[i][j] = -1; // Indica que hay una mina
                }
            }
        }
    }

    private boolean isValidPosition(int i, int j) {
        return i >= 0 && i < BOARD_SIZE && j >= 0 && j < BOARD_SIZE;
    }

    private void uncoverCell(int i, int j) {
        if (gameOver || !buttons[i][j].isEnabled() || flagged[i][j]) {
            return;
        }
        
        clickCount++;
        updateStatus();

        if (mines[i][j]) {
            loseGame();
        } else {
            revealCell(i, j);
            if (uncoveredCells == TOTAL_SAFE_CELLS) {
                winGame();
            }
        }
    }

    private void revealCell(int i, int j) {
        if (!isValidPosition(i, j) || !buttons[i][j].isEnabled() || flagged[i][j]) {
            return;
        }

        buttons[i][j].setEnabled(false);
        uncoveredCells++;
        
        int mineCount = surroundingMines[i][j];
        if (mineCount > 0) {
            buttons[i][j].setText(String.valueOf(mineCount));
            buttons[i][j].setForeground(getNumberColor(mineCount));
        } else {
            buttons[i][j].setText("");
            // Auto-revelar celdas vecinas si no hay minas alrededor
            uncoverSurroundingCells(i, j);
        }
        
        buttons[i][j].setBackground(Color.LIGHT_GRAY);
    }

    private Color getNumberColor(int number) {
        Color[] colors = {
            Color.BLUE, Color.GREEN, Color.RED, Color.PURPLE,
            Color.MAGENTA, Color.CYAN, Color.BLACK, Color.DARK_GRAY
        };
        return colors[Math.min(number - 1, colors.length - 1)];
    }

    private void uncoverSurroundingCells(int i, int j) {
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};
        
        for (int k = 0; k < 8; k++) {
            int ni = i + dx[k];
            int nj = j + dy[k];
            if (isValidPosition(ni, nj) && buttons[ni][nj].isEnabled() && !flagged[ni][nj]) {
                revealCell(ni, nj);
            }
        }
    }

    private void winGame() {
        gameOver = true;
        statusLabel.setText("¡GANASTE! 🎉 Clics: " + clickCount);
        
        // Marcar todas las minas con banderas
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (mines[i][j] && !flagged[i][j]) {
                    buttons[i][j].setText(FLAG_SYMBOL);
                    buttons[i][j].setBackground(Color.GREEN);
                }
            }
        }
        
        JOptionPane.showMessageDialog(this, 
            "¡Felicitaciones! Ganaste en " + clickCount + " clics.", 
            "¡Victoria!", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void loseGame() {
        gameOver = true;
        statusLabel.setText("Game Over 💀 Clics: " + clickCount);
        
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                buttons[i][j].setEnabled(false);
                if (mines[i][j]) {
                    buttons[i][j].setText(MINE_SYMBOL);
                    buttons[i][j].setBackground(flagged[i][j] ? Color.GREEN : Color.RED);
                }
            }
        }

        JOptionPane.showMessageDialog(this, 
            "¡Explotaste! Perdiste en " + clickCount + " clics.", 
            "Game Over", 
            JOptionPane.ERROR_MESSAGE);
    }

    private void resetGame() {
        dispose();
        new Minesweeper();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
            } catch (Exception e) {
                // Usar look and feel por defecto
            }
            new Minesweeper();
        });
    }
}