import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;

<<<<<<< HEAD
class Minesweeper extends JFrame {
    // Constants for better maintainability
    private static final int BOARD_SIZE = 10;
    private static final int MINE_COUNT = 10;
    private static final int TOTAL_CELLS = BOARD_SIZE * BOARD_SIZE;
    private static final int SAFE_CELLS = TOTAL_CELLS - MINE_COUNT;
    
    // Game state variables
    private JButton[][] buttons;
    private boolean[][] mines;
    private int[][] surroundingMines;
    private int uncoveredCells;
    private int clickCount;
    private boolean gameOver;

    public Minesweeper() {
        // Configuracion de la ventana del juego
        setTitle("Minesweeper - ¡Mucha Suerte!");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        
        // Inicializacion de los arrays y variables
        initializeGame();
        
        // Creacion de los botones y agregacion a la ventana
        createButtons();
        
        // LLamada a los metodos para colocar minas y contar minas alrededor
        placeMines();
        countSurroundingMines();
        
        // Ajuste del tamaño de la ventana y visibilidad
        pack();
        setVisible(true);
    }
    
    private void initializeGame() {
        buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
        mines = new boolean[BOARD_SIZE][BOARD_SIZE];
        surroundingMines = new int[BOARD_SIZE][BOARD_SIZE];
        uncoveredCells = 0;
        clickCount = 0;
        gameOver = false;
    }
    
    private void createButtons() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].addActionListener(new CellClickListener(i, j));
                add(buttons[i][j]);
            }
        }
    }

    // Metodo de donde estan las minas
    // Coloca minas en posiciones aleatorias del tablero usando algoritmo optimizado
    private void placeMines() {
        Random random = new Random();
        int totalCells = BOARD_SIZE * BOARD_SIZE;
        
        // Crear array con todas las posiciones posibles
        int[] positions = new int[totalCells];
        for (int i = 0; i < totalCells; i++) {
            positions[i] = i;
        }
        
        // Mezclar el array usando Fisher-Yates shuffle
        for (int i = totalCells - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = positions[i];
            positions[i] = positions[j];
            positions[j] = temp;
        }
        
        // Colocar minas en las primeras MINE_COUNT posiciones
        for (int i = 0; i < MINE_COUNT; i++) {
            int position = positions[i];
            int row = position / BOARD_SIZE;
            int col = position % BOARD_SIZE;
            mines[row][col] = true;
        }
=======
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

>>>>>>> parent of d9ee308 (Implemente un nuevo metodo para destapar celdas vecinas)
    }
  }
  // Metodo que cuenta las minas alrededor
  // Si una celda no tiene mina, cuenta cuantas minas hay en las celdas vecinas
  // Si una celda tiene mina, se marca con -1 en el array surroundingMines
  private void countSurroundingMines() {

<<<<<<< HEAD
    // Metodo que cuenta las minas alrededor
    // Si una celda no tiene mina, cuenta cuantas minas hay en las celdas vecinas
    // Si una celda tiene mina, se marca con -1 en el array surroundingMines
    private void countSurroundingMines() {
        // Direcciones para las 8 celdas vecinas
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
    
    // Metodo auxiliar para verificar si una posicion es valida
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }

    // Metodo que descubre las celdas
    // Si la celda tiene una mina, se pierde el juego
    // Si no tiene mina, se muestra el numero de minas alrededor
    private void uncoverCell(int i, int j) {
        if (gameOver || !isValidPosition(i, j) || !buttons[i][j].isEnabled()) {
            return;
        }
        
        clickCount++;
        System.out.println("Clic número: " + clickCount);
=======
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
>>>>>>> parent of d9ee308 (Implemente un nuevo metodo para destapar celdas vecinas)

      if (uncoveredCells == 90) {
        winGame();
      }

<<<<<<< HEAD
            if (uncoveredCells == SAFE_CELLS) {
                winGame();
            }

            if (surroundingMines[i][j] == 0) {
                uncoverSurroundingCells(i, j);
            }
        }
=======
      if (surroundingMines[i][j] == 0) {
        uncoverSurroundingCells(i, j);
      }

>>>>>>> parent of d9ee308 (Implemente un nuevo metodo para destapar celdas vecinas)
    }
  }
    // Metodo que descubre las celdas alrededor
    // Si una celda descubierta no tiene minas alrededor, se descubren sus celdas vecinas
<<<<<<< HEAD
    private void uncoverSurroundingCells(int i, int j) {
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};
        
        for (int k = 0; k < 8; k++) {
            int ni = i + dx[k];
            int nj = j + dy[k];
            if (isValidPosition(ni, nj) && buttons[ni][nj].isEnabled()) {
                uncoverCell(ni, nj);
            }
        }
    }
=======
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
>>>>>>> parent of d9ee308 (Implemente un nuevo metodo para destapar celdas vecinas)

  }
    // Metodo que muestra cuando se gana el juego
<<<<<<< HEAD
    private void winGame() {
        gameOver = true;
        JOptionPane.showMessageDialog(this, "¡Felicidades! ¡Has ganado!");
        System.exit(0);
    }

    // Metodo que muestra cuando se pierde el juego
    // Muestra todas las minas y deshabilita todos los botones
    private void loseGame() {
        gameOver = true;
        
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (mines[i][j]) {
                    buttons[i][j].setText("*");
                }
                buttons[i][j].setEnabled(false);
            }
        }

        JOptionPane.showMessageDialog(this, "¡Boom! Has perdido.");
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
=======
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
>>>>>>> parent of d9ee308 (Implemente un nuevo metodo para destapar celdas vecinas)
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
