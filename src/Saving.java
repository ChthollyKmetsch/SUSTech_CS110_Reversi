import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
    A class that stores a status of a game of chess
    map[][] is what the chess board looks like
    currentOperator is the next player who will place a piece of chess
    idx is the number of moves that the game has been through
 */
public class Saving {
    int[][] map;
    int currentOperator;

    public Saving(int[][] map, int currentOperator) {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (this.map != null) {
                    this.map[i][j] = map[i][j];
                }
            }
        }
        this.currentOperator = currentOperator;
    }
    public Saving(File file) throws FileNotFoundException {
        Scanner input = new Scanner(file);
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                map[i][j] = input.nextInt();
            }
        }
        currentOperator = input.nextInt();
    }
}
