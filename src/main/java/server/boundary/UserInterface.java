package server.boundary;

import java.util.Scanner;

/**
 * UserInterface class for the server/loggers UI handling the IO.
 *
 * @author Linnéa Mörk
 */
public class UserInterface {
    public static String askForString(String output) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(output);
        return scanner.nextLine();
    }
    public static void println(String out) {
        System.out.println(out);
    }

    public static void printDivider() {
        System.out.println("-----------------------------------------");
    }

    public static void printDates(String startDate, String endDate) {
        printDivider();
        System.out.printf("Showing log between: %s and %s\n", startDate, endDate);
        printDivider();
    }
}