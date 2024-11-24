import GUI.Frame;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        Algo app = new Algo();
        app.initiate();
        int op = 1;
        for (int i = 0; i < 10; ++i) {
            app.findValidPlace(op);
            app.feedback();
            app.printValidPlace();
            System.out.printf("You are now playing as %s\n", op == 1 ? "BLACK(1)" : "WHITE(2)");
            int x, y;
            for (int j = 0; j < 100; ++j) {
                x = input.nextInt();
                y = input.nextInt();
                if (app.placeChess(x,y,op)) {
                    break;
                } else {
                    System.out.println("Invalid attempt. Try again:");
                }
            }
            app.clearPlayerOptions();
            op = op == 1 ? 2 : 1;
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
}