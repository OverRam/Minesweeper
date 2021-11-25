package minesweeper;

import java.util.Random;
import java.util.Scanner;

public class Minesweeper {
    private final boolean[][] field;
    private final int size;
    private final Scanner sc = new Scanner(System.in);

    public Minesweeper(int size) {
        this.size = size;
        field = new boolean[size][size];
    }

    private void initField(int numOfMines) {
        Random rnd = new Random();
        while (numOfMines > 0) {
            for (int i = 0; i < size && numOfMines > 0; i++) {
                for (int j = 0; j < size && numOfMines > 0; j++) {
                    if (!field[i][j]) {
                        field[i][j] = rnd.nextBoolean();
                        numOfMines -= field[i][j] ? 1 : 0;
                    }
                }
            }
        }
    }

    private void printFieldWithNumbers() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(markCell(i, j));
            }
            System.out.println();
        }
    }

    private void printField() {
        for (boolean[] arr : field) {
            for (boolean cell : arr) {
                System.out.print(cell ? 'X' : '.');
            }
            System.out.println();
        }
    }

    String markCell(int pozX, int pozY) {
        int counter = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (pozX + i >= 0 && pozX + i < size && pozY + j >= 0 && pozY + j < size) {
                    counter += field[pozX + i][pozY + j] ? 1 : 0;
                }
            }
        }
        return field[pozX][pozY] ? "X" : counter > 0 ? String.valueOf(counter) : ".";
    }

    void run() {
        System.out.println("How many mines do you want on the field?");
        initField(sc.nextInt());
        printFieldWithNumbers();
    }
}
