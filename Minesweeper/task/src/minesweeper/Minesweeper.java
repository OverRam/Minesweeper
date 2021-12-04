package minesweeper;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Minesweeper {
    private final boolean[][] bollField;
    private final int size;
    private final Scanner sc = new Scanner(System.in);
    private final String[][] playerField;
    private final String[][] hiddenField;
    private int availableCells;
    private int mine;
    private boolean continueGame = true;

    Minesweeper(int size) {
        this.size = size;
        bollField = new boolean[size][size];
        playerField = new String[size][size];
        hiddenField = new String[size][size];
        availableCells = size * size;
        Stream.of(playerField).forEach(x -> Arrays.fill(x, "."));
    }

    private void initMineOnField(int numOfMines) {
        Random rnd = new Random();
        if (numOfMines < size * size) {
            while (numOfMines > 0) {
                System.out.println(numOfMines);
                int row = rnd.nextInt(9);
                int col = rnd.nextInt(9);

                if (!bollField[row][col]) {
                    bollField[row][col] = rnd.nextBoolean();
                    numOfMines -= bollField[row][col] ? 1 : 0;
                }
            }
        } else {
            for (boolean[] arr : bollField) {
                Arrays.fill(arr, true);
            }
        }
    }

    private void prepareHiddenField() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                hiddenField[i][j] = defCell(i, j);
            }
        }
    }

    private void printField(String[][] field) {
        System.out.println(" |123456789|\n" +
                "-|---------|");
        for (int i = 0; i < size; i++) {
            System.out.print((i + 1) + "|");
            for (int j = 0; j < size; j++) {
                System.out.print(field[i][j]);
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println("-|---------|");
    }

    private String defCell(int pozX, int pozY) {
        int counter = 0;
        if (bollField[pozX][pozY]) {
            return "X";
        }

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                counter += checkCoordinates(pozX + i, pozY + j) && bollField[pozX + i][pozY + j] ? 1 : 0;
            }
        }
        return counter > 0 ? String.valueOf(counter) : "/";
    }

    private void playerMark() {
        System.out.print("Set/unset mine marks or claim a cell as free:");
        String[] inp = sc.nextLine().trim().split(" ");
        List<Integer> cord = Stream.of(inp)
                .limit(2)
                .map(Integer::parseInt)
                .map(e -> e -= 1).collect(Collectors.toList());

        String mineOrFree = inp.length == 3 && ("free".equals(inp[2]) || "mine".equals(inp[2])) ? inp[2] : "Error";

        if (cord.size() == 2 && !mineOrFree.equals("Error") && (playerField[cord.get(1)][cord.get(0)].equals(".") ||
                playerField[cord.get(1)][cord.get(0)].equals("*"))) {

            if ("mine".equals(mineOrFree)) {
                if (bollField[cord.get(1)][cord.get(0)]) {
                    //if good mine cell mark
                    availableCells--;
                    mine += !playerField[cord.get(1)][cord.get(0)].equals("*") ? -1 : 1;
                } else {
                    availableCells++;
                    mine += playerField[cord.get(1)][cord.get(0)].equals("*") ? -1 : 1;
                }

                playerField[cord.get(1)][cord.get(0)] = playerField[cord.get(1)][cord.get(0)].equals(".") ? "*" : ".";

            } else if (playerField[cord.get(1)][cord.get(0)].equals(".")) {    //free mode
                continueGame = !bollField[cord.get(1)][cord.get(0)];
                markCells(cord.get(1), cord.get(0));
            }

            System.out.println();
        } else {
            System.out.println("Wrong coordinates!\n");
        }
    }

    private void markCells(int x, int y) {
        if (!hiddenField[x][y].equals("/")) {
            playerField[x][y] = hiddenField[x][y];
            availableCells--;
        } else if (hiddenField[x][y].equals("/")) {
            getCellsMarker(x, y);
        }
    }

    void getCellsMarker(int x, int y) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (checkCoordinates(x + i, y + j) && (playerField[x + i][y + j].equals(".") ||
                        playerField[x + i][y + j].equals("*")) && !hiddenField[x + i][y + j].equals("X")) {
                    playerField[x + i][y + j] = hiddenField[x + i][y + j];
                    availableCells--;
                    if (playerField[x + i][y + j].equals("/")) {
                        getCellsMarker(x + i, y + j);
                    } else if (playerField[x + i][y + j].equals("*")) {
                        playerField[x + i][y + j] = hiddenField[x + i][y + j];
                    }
                }
            }
        }

    }

    boolean checkCoordinates(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    void run() {
        System.out.print("How many mines do you want on the field? ");
        mine = Integer.parseInt(sc.nextLine());

        initMineOnField(mine);
        availableCells -= mine;
        prepareHiddenField();

        while (mine > 0 && continueGame && availableCells > 0) {
            printField(playerField);
            playerMark();
        }

        printField(playerField);
        System.out.println(continueGame ? "Congratulations! You found all the mines!" :
                "You stepped on a mine and failed!");
    }
}
