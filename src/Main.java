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
            app.feedback();
            app.findValidPlace(op);
            app.printValidPlace();
            int x, y;
            do {
                x = input.nextInt();
                y = input.nextInt();
            } while (app.placeChess(x,y,op));
            app.clearPlayerOptions();
            op = op == 1 ? 2 : 1;
        }
    }
}