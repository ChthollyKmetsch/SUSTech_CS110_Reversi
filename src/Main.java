import java.io.IOException;
import GUI.*;

public class Main {
    public static void main(String[] args) throws IOException {
        int firstOperator = 1;
        int difficulty = 8;
        boolean playWithAI = true;
        /*\

        System.out.println("Console Main menu");
        System.out.println("Blac+k first: 1");
        System.out.println("White first: 2");
        Scanner input = new Scanner(System.in);
        firstOperator = input.nextInt();
        System.out.println("Play with AI? A default AI uses white piece. Input Y or N : ");
        playWithAI = input.next().equals("Y") ? true : false;
        for ( ; ; ) {
            System.out.println("Difficulty(1 to 3) : ");
            difficulty = input.nextInt();
            if (difficulty > 0 && difficulty < 4) break;
            System.out.println("Invalid difficulty. Try again.");
        }
*/
        ReversiBoardFrame frame = new ReversiBoardFrame(firstOperator, playWithAI, difficulty);
        frame.setVisible(true);
    }
}