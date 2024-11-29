import GUI.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        Algo app = new Algo();
        int op = 1;
        for (int i = 0; i < 100; ++i) {
            if (op == 1) {
                for (int j = 0; j < 100; ++j) { // player's move
                    app.findValidPlace(op);
                    app.feedback(op); //
                    int x, y;
                    x = input.nextInt();
                    y = input.nextInt();
                    if (app.validMoves.isEmpty()) {
                        System.out.println("White wins");
                        break;
                    }
                    if (app.placeChess(x, y, op)) {
                        break;
                    } else {
                        System.out.println("Invalid attempt. Try again:");
                    }
                    app.clearPlayerOptions();
                }
            }
            else { // AI's move
                AI ai = new AI(app, 4);
                Pair pos = ai.search(op,1);
//                System.out.println(pos.getFt() + " " + pos.getSc() + " " + op);
                ai.findValidPlace(op);
                app.validMoves = new ArrayList<>(ai.validMoves);
                if (app.validMoves.isEmpty()) {
                    System.out.println("Black wins");
                    break;
                }
                app.placeChess(pos.getFt(),pos.getSc(),op);
            }
            app.clearPlayerOptions();
            op = op == 1 ? 2 : 1;
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
}