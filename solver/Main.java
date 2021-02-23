package solver;

//
// Zach Halderwood's Program 1 for CS461 - Spring 2021
// zjuvz6@mail.umkc.edu
// 12368443
//
// References:
// https://docs.oracle.com/javase/9/docs/api/overview-summary.html
// https://faramira.com/solving-8-puzzle-problem-using-a-star-search/
//

import java.io.File;
import java.util.Scanner;
import java.util.HashMap;


public class Main {

    private static Integer numPuzzles;
    private static String defaultPuzzleFile = "prog1_input.txt";
    public static final String CURRENT = "current", START = "start", BLANK = "E", GOAL = "goal";
    public static HashMap<Integer, Puzzle> puzzleMap = new HashMap<Integer, Puzzle>();
    public static Scanner userScanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("8 Puzzle Problem Solver");
        readFile(getFileChoice(userScanner));
        for (int n = 0; n < numPuzzles; n++) {
            Puzzle activePuzzle = puzzleMap.get(n);
            System.out.println("Puzzle #" + (n + 1) + " Initial State");
            activePuzzle.printNode(activePuzzle.getCurrentNode());;
            if (!activePuzzle.isSolvable()) {
                System.out.println("This puzzle has no solution.");
            }
            else {
                while (!activePuzzle.isSolved()) {
                    System.out.println("Manhattan distance: " + activePuzzle.getManhattan());
                    System.out.println("Search depth      : " + activePuzzle.);
                }
            }

            if (n < numPuzzles - 1) {
                System.out.println("Press Enter to continue to puzzle " + (n + 2));
                userScanner.nextLine();
            }
        }
        userScanner.close();
    }

    // reads initial states of puzzles from a properly formatted file
    public static void readFile(String fileName) {
        int puzzleIndex = 0;
        try {
            File boardFile = new File("src\\" + fileName);
            if (!boardFile.exists()) { throw new Exception("File not found"); }
            Scanner fileScanner = new Scanner(boardFile);
            numPuzzles = Integer.parseInt(fileScanner.nextLine());
            System.out.println(numPuzzles + " puzzles detected");
            for (int n = 0; n < numPuzzles; n++) {
                String tempArray[][] = new String[3][3];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        tempArray[i][j] = fileScanner.next();
                    }
                    fileScanner.nextLine();
                }
                puzzleMap.put(n, new Puzzle(tempArray));
            }
            fileScanner.close();
        } catch (Exception e) {
            System.err.println(e);
            readFile(fileName);
        }
    }

    // gets input from user and returns file name
    public static String getFileChoice(Scanner userScanner) {
        String puzzleFile = null;
        System.out.println("Use default puzzle file \"prog1_input.txt\"? (Y/N)");
        try {
            String inputChoice = userScanner.nextLine();
            if (inputChoice.equalsIgnoreCase("y")) {
                puzzleFile = defaultPuzzleFile;
            } else if (inputChoice.equalsIgnoreCase("n")) {
                System.out.println("Enter puzzle file name: ");
                puzzleFile = userScanner.nextLine();
            } else throw new Exception("Invalid choice");
        } catch (Exception e) {
            System.err.println(e);
            getFileChoice(userScanner);
        } finally {
            return puzzleFile;
        }
    }

}
