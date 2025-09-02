import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;

public class Minesweeper extends JFrame {
  private JButton[][] buttons;
  private boolean[][] mines;
  private int[][] surroundingMines;
  private int uncoveredCells;

  public Minesweeper() {
    setTitle("Minesweeper");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new GridLayout(10, 10));

    buttons = new JButton[10][10];
    mines = new boolean[10][10];
    surroundingMines = new int[10][10];
    uncoveredCells = 0;

    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        buttons[i][j] = new JButton();
        buttons[i][j].addActionListener(new CellClickListener(i, j));
        add(buttons[i][j]);
      }
    }

    placeMines();
    countSurroundingMines();

    pack();
    setVisible(true);
  }

  private void placeMines() {
    Random random = new Random();
    int placedMines = 0;
    while (placedMines < 10) {
      int i = random.nextInt(10);
      int j = random.nextInt(10);
      if (!mines[i][j]) {
        mines[i][j] = true;
        placedMines++;
      }
    }
  }