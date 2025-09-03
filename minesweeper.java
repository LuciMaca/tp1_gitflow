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
    // Configuracion de la ventana del juego
    // Crea una ventana de 10x10 con botones para cada celda
    setTitle("Minesweeper");
    setTitle("Mucha Suerte");
    // Configuracion de la ventana del juego
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new GridLayout(10, 10));
    // Inicializacion de los arrays y variables
    buttons = new JButton[10][10];
    mines = new boolean[10][10];
    surroundingMines = new int[10][10];
    uncoveredCells = 0;
    // Creacion de los botones y agregacion a la ventana
    for (int i = 0; i < 10; i++) {

      for (int j = 0; j < 10; j++) {
        buttons[i][j] = new JButton();
        buttons[i][j].addActionListener(new CellClickListener(i, j));
        add(buttons[i][j]);
      }

    }
    // LLamada a los metodos para colocar minas y contar minas alrededor
    placeMines();
    countSurroundingMines();
    // Ajuste del tamaño de la ventana y visibilidad
    pack();
    setVisible(true);
  }
 // Metodo de donde estan las minas
 // Coloca 10 minas en posiciones aleatorias del tablero
 // Asegura que no haya dos minas en la misma celda
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
  // Metodo que cuenta las minas alrededor
  // Si una celda no tiene mina, cuenta cuantas minas hay en las celdas vecinas
  // Si una celda tiene mina, se marca con -1 en el array surroundingMines
  private void countSurroundingMines() {

    for (int i = 0; i < 10; i++) {

      for (int j = 0; j < 10; j++) {

        if (!mines[i][j]) {
          int count = 0;
          if (i > 0 && mines[i - 1][j]) count++;
          if (i < 9 && mines[i + 1][j]) count++;
          if (j > 0 && mines[i][j - 1]) count++;
          if (j < 9 && mines[i][j + 1]) count++;
          if (i > 0 && j > 0 && mines[i - 1][j - 1]) count++;
          if (i < 9 && j < 9 && mines[i + 1][j + 1]) count++;
          if (i > 0 && j < 9 && mines[i - 1][j + 1]) count++;
          if (i < 9 && j > 0 && mines[i + 1][j - 1]) count++;
          surroundingMines[i][j] = count;
        }   else {
          surroundingMines[i][j] = -1; // Indica que hay una mina
        }   

      }

    }
  }
   // Metodo que descubre las celdas
   // Si la celda tiene una mina, se pierde el juego
   // Si no tiene mina, se muestra el numero de minas alrededor
  private void uncoverCell(int i, int j) {

    if (mines[i][j]) {
      loseGame();
    } else {
      buttons[i][j].setText(Integer.toString(surroundingMines[i][j]));
      buttons[i][j].setEnabled(false);
      uncoveredCells++;

      if (uncoveredCells == 90) {
        winGame();
      }

      if (surroundingMines[i][j] == 0) {
        uncoverSurroundingCells(i, j);
      }

    }
  }
    // Metodo que descubre las celdas alrededor
    // Si una celda descubierta no tiene minas alrededor, se descubren sus celdas vecinas
    // Recursivamente se llama a uncoverCell para cada celda vecina
  private void uncoverSurroundingCells(int i, int j) {
    if (i > 0 && buttons[i - 1][j].isEnabled()) uncoverCell(i - 1, j);
    if (i < 9 && buttons[i + 1][j].isEnabled()) uncoverCell(i + 1, j);
    if (j > 0 && buttons[i][j - 1].isEnabled()) uncoverCell(i, j - 1);
    if (j < 9 && buttons[i][j + 1].isEnabled()) uncoverCell(i, j + 1);
    if (i > 0 && j > 0 && buttons[i - 1][j - 1].isEnabled()) uncoverCell(
      i - 1,
      j - 1
    );

    if (i < 9 && j < 9 && buttons[i + 1][j + 1].isEnabled()) uncoverCell(
      i + 1,
      j + 1
    );

    if (i > 0 && j < 9 && buttons[i - 1][j + 1].isEnabled()) uncoverCell(
      i - 1,
      j + 1
    );

    if (i < 9 && j > 0 && buttons[i + 1][j - 1].isEnabled()) uncoverCell(
      i + 1,
      j - 1
    );

  }
    // Metodo que muestra cuando se gana el juego
  private void winGame() {
    JOptionPane.showMessageDialog(this, "You won!");
    System.exit(0);
  }
    // Metodo que muestra cuando se pierde el juego
    // Muestra todas las minas y deshabilita todos los botones
    // Luego muestra un mensaje de que se ha perdido el juego
  private void loseGame() {

    for (int i = 0; i < 10; i++) {

      for (int j = 0; j < 10; j++) {

        if (mines[i][j]) {
          buttons[i][j].setText("*");
        }

        buttons[i][j].setEnabled(false);

      }

    }

    JOptionPane.showMessageDialog(this, "You lost.");
    System.exit(0);

  }

   // Clase interna para manejar los clics en las celdas
   // Cada vez que se hace clic en una celda, se llama al método uncoverCell
   // con las coordenadas de la celda clicada
  private class CellClickListener implements ActionListener {

    private int i;
    private int j;

    public CellClickListener(int i, int j) {
      this.i = i;
      this.j = j;
    }

    public void actionPerformed(ActionEvent e) {
      uncoverCell(i, j);
    }

  }

private int clickCount = 0; // Nuevo atributo

// Modifica el método uncoverCell para incrementar el contador
// y mostrar el número de clics en la consola

private void uncoverCell(int i, int j) {

  clickCount++; // Incrementa en cada clic
  
  System.out.println("Clic número: " + clickCount); // Muestra en consola

  if (mines[i][j]) {
    loseGame();
  } else {
    buttons[i][j].setText(Integer.toString(surroundingMines[i][j]));
    buttons[i][j].setEnabled(false);
    uncoveredCells++;

    if (uncoveredCells == 90) {
      winGame();
    }

    if (surroundingMines[i][j] == 0) {
      uncoverSurroundingCells(i, j);
    }
  }
}


}
