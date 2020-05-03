package com.waffelmonster.server.state.tictactoe;

import com.waffelmonster.server.state.Room;

public class RoomTicTacToe extends Room {
    private int[][] board;

    public RoomTicTacToe(int[][] board) {
        super("tictactoe");
        this.board = board;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }
}
