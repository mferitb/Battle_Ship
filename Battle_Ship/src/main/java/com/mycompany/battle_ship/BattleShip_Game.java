/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battle_ship;

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Mehmet Ferit Bilen
 */
public class BattleShip_Game {
     private int boardSize;
    private char[][] playerBoard;
    private char[][] computerBoard;

    private static final char EMPTY = '~';
    private static final char SHIP = 'G';
    private static final char HIT = 'X';
    private static final char MISS = 'O';

    private Scanner scanner;
    private Random random;

    public BattleShip_Game() {
        scanner = new Scanner(System.in);
        random = new Random();
    }

    public void startGame() {
        selectDifficulty();
        prepareBoards();
        printBoards();

        while (!gameOver()) {
            playerTurn();
            computerTurn();
            printBoards();
        }

        System.out.println("Game over!");
    }

    private void selectDifficulty() {
        System.out.println("Select difficulty level:");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                boardSize = 5;
                break;
            case 2:
                boardSize = 7;
                break;
            case 3:
                boardSize = 9;
                break;
            default:
                System.out.println("Invalid choice. Medium difficulty selected...");
                boardSize = 7;
                break;
        }
    }

    private void prepareBoards() {
        playerBoard = new char[boardSize][boardSize];
        computerBoard = new char[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                playerBoard[i][j] = EMPTY;
                computerBoard[i][j] = EMPTY;
            }
        }

        int shipCount;
        switch (boardSize) {
            case 5:
                shipCount = 3;
                placeShips(playerBoard, shipCount, 1);
                placeShips(computerBoard, shipCount, 1);
                break;
            case 7:
                shipCount = 5;
                placeShips(playerBoard, shipCount, 2);
                placeShips(computerBoard, shipCount, 2);
                break;
            case 9:
                shipCount = 7;
                placeShips(playerBoard, shipCount, 3);
                placeShips(computerBoard, shipCount, 3);
                break;
            default:
                shipCount = 5;
                placeShips(playerBoard, shipCount, 2);
                placeShips(computerBoard, shipCount, 2);
                break;
        }
    }

    private void placeShips(char[][] board, int shipCount, int shipLength) {
        for (int i = 0; i < shipCount; i++) {
            int row, column;
            boolean valid = false;
            do {
                row = random.nextInt(boardSize);
                column = random.nextInt(boardSize);
                boolean horizontal = random.nextBoolean();

                valid = true;
                if (horizontal) {
                    if (column + shipLength > boardSize) {
                        valid = false;
                        continue;
                    }
                    for (int j = 0; j < shipLength; j++) {
                        if (board[row][column + j] != EMPTY) {
                            valid = false;
                            break;
                        }
                    }

                    if (valid) {
                        for (int j = 0; j < shipLength; j++) {
                            board[row][column + j] = SHIP;
                        }
                    }
                } else {
                    if (row + shipLength > boardSize) {
                        valid = false;
                        continue;
                    }
                    for (int j = 0; j < shipLength; j++) {
                        if (board[row + j][column] != EMPTY) {
                            valid = false;
                            break;
                        }
                    }

                    if (valid) {
                        for (int j = 0; j < shipLength; j++) {
                            board[row + j][column] = SHIP;
                        }
                    }
                }
            } while (!valid);
        }
    }

    private void playerTurn() {
        int row, column;
        do {
            System.out.println("Your turn! Enter coordinates to fire (1-" + boardSize + " range): ");
            int[] coordinates = getCoordinates();
            row = coordinates[0] - 1;
            column = coordinates[1] - 1;

            if (row < 0 || row >= boardSize || column < 0 || column >= boardSize) {
                System.out.println("Invalid coordinates! Please try again.");
            } else if (computerBoard[row][column] == HIT || computerBoard[row][column] == MISS) {
                System.out.println("You've already fired at this coordinate! Please try again.");
            }
        } while (row < 0 || row >= boardSize || column < 0 || column >= boardSize || computerBoard[row][column] == HIT || computerBoard[row][column] == MISS);

        if (computerBoard[row][column] == SHIP) {
            System.out.println("You hit a ship!");
            computerBoard[row][column] = HIT;
        } else {
            System.out.println("You missed!");
            computerBoard[row][column] = MISS;
        }
    }

    private void computerTurn() {
        int row, column;
        do {
            row = random.nextInt(boardSize);
            column = random.nextInt(boardSize);
        } while (playerBoard[row][column] == HIT || playerBoard[row][column] == MISS);

        if (playerBoard[row][column] == SHIP) {
            System.out.println("Computer hit your ship!");
            playerBoard[row][column] = HIT;
        } else {
            System.out.println("Computer missed!");
            playerBoard[row][column] = MISS;
        }
    }

    private int[] getCoordinates() {
        int[] coordinates = new int[2];
        coordinates[0] = scanner.nextInt();
        coordinates[1] = scanner.nextInt();
        return coordinates;
    }

    private void printBoards() {
        System.out.println("  Your Board              Computer's Board");
        System.out.print("  ");
        for (int i = 1; i <= boardSize; i++) {
            System.out.print(i + " ");
        }
        System.out.print("       ");
        for (int i = 1; i <= boardSize; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < boardSize; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < boardSize; j++) {
                System.out.print(playerBoard[i][j] + " ");
            }
            System.out.print("     ");
            System.out.print((i + 1) + " ");
            for (int j = 0; j < boardSize; j++) {
                if (computerBoard[i][j] == SHIP || computerBoard[i][j] == EMPTY) {
                    System.out.print(EMPTY + " ");
                } else {
                    System.out.print(computerBoard[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    private boolean gameOver() {
        return boardEmpty(playerBoard) || boardEmpty(computerBoard);
    }

    private boolean boardEmpty(char[][] board) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == SHIP) {
                    return false;
                }
            }
        }
        return true;
    }
}


