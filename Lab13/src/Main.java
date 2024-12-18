import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean flag = true;
        do {
            try {
                System.out.print("Input 2 integers:");
                int a, b;
                a = input.nextInt();
                b = input.nextInt();
                flag = false;
                System.out.println("Sum = " + (a+b));
            } catch (InputMismatchException e) {
                input.nextLine();
                e.printStackTrace();
                System.out.println("Try again." +
                        "(Incorrect input: An integer is required)");
            }
        } while (flag);
    }
}