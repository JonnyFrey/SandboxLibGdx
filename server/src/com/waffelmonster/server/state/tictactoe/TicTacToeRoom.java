package com.waffelmonster.server.state.tictactoe;

import com.waffelmonster.server.state.Room;

import java.util.Arrays;
import java.util.Objects;

public class TicTacToeRoom extends Room {
    private String[][] board;
    private String currentPlayer;

    public TicTacToeRoom() {
        super("tictactoe");
        this.board = new String[3][3];
    }

    public String[][] getBoard() {
        return this.board;
    }

    public boolean move(String playerName, int x, int y) {
        // Can't make a move if it isn't your turn
        if (currentPlayer != null && currentPlayer.equals(playerName)) {
            return false;
        }
        // Can't make a move in a square that is already taken
        if (board[x][y] != null) {
            return false;
        }
        board[x][y] = playerName;
        currentPlayer = playerName;
        return true;
    }

    public boolean reset() {
        boolean gameOver = isGameOver();
        if (gameOver) {
            this.board = new String[3][3];
            this.currentPlayer = null;
        }
        return gameOver;
    }

    private boolean isGameOver() {
        // Check horizontally
        for (int i = 0; i < 3; ++i) {
            if (board[i][0] != null && board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2])) {
                return true;
            }
        }
        // Check vertically
        for (int i = 0; i < 3; ++i) {
            if (board[0][i] != null && board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i])) {
                return true;
            }
        }
        // Check diagonally \
        if (board[0][0] != null && board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2])) {
            return true;
        }
        // Check diagonally /
        if (board[2][0] != null && board[2][0].equals(board[1][1]) && board[2][0].equals(board[0][2])) {
            return true;
        }
        // Check if every square is filled
        return Arrays.stream(board).flatMap(Arrays::stream).allMatch(Objects::nonNull);
    }
}
