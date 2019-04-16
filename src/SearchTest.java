import java.io.*;
import java.util.Scanner;

public class SearchTest {
    public static void main(String[] args) throws IOException {
        if(args.length != 2) {
            System.out.println("Command line arguments are too few or too many.");
            System.out.println("[Usage] :\" $ Java SearchTest [field file name] [search mode g/a]\"");
            System.exit(1);
        }

        Field field = Field.loadFromFile(args[0]);

        if(field == null) {
            System.out.println("Field not found or wrong.");
            System.out.println("[Error] : exit with error.");
            System.exit(1);
        }

        if(!args[1].equals("g") && !args[1].equals("a")) {
            System.out.println("Search argument is wrong. Please type \"g\" (Greedy search) \"a\" (A* search).");
            System.out.println("[Error] : exit with error.");
            System.exit(1);
        }

        System.out.println();
        System.out.println("--- Initial map of this field ---");
        System.out.println();
        field.print();
        System.out.println();

        long start = 0, end = 0;
        boolean result = false;
        if(args[1].equals("g")) {
            start = System.currentTimeMillis();
            result = field.greedySearch();
            end = System.currentTimeMillis();
        }
        else if(args[1].equals("a")) {
            start = System.currentTimeMillis();
            result = field.aStarSearch();
            end = System.currentTimeMillis();
        }

        System.out.println("--- The result of this field ---");
        System.out.println();
        if(result) {
            field.print();
        }
        else {
            System.out.println("Cannot solve...");
        }
        System.out.println();

        System.out.println("The cpu time : " + (end - start) + "ms");
    }
}