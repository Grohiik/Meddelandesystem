package server.boundary;

import java.util.Scanner;

/**
 * UserInterface class for the server/loggers UI handling the IO.
 *
 * @author Linnéa Mörk
 * @version 1.0
 */
public class UserInterface {
    /**
     * Asks for a String and returns the answer.
     *
     * @param output The String to print as the question.
     * @return The input from the user.
     */
    public static String askForString(String output) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(output);
        return scanner.nextLine();
    }

    /**
     * Prints a line.
     *
     * @param out The String to print.
     */
    public static void println(String out) {
        System.out.println(out);
    }

    public static void printDivider() {
        System.out.println("-----------------------------------------");
    }

    /**
     * Takes two dates and prints them with some nice formatting.
     *
     * @param startDate The starting date of the log to print from.
     * @param endDate The end date of the log to be printed.
     */
    public static void printDates(String startDate, String endDate) {
        printDivider();
        System.out.printf("Showing log between: %s and %s\n", startDate, endDate);
        printDivider();
    }
}