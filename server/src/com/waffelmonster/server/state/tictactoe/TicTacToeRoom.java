package com.waffelmonster.server.state.tictactoe;

import com.waffelmonster.message.tictactoe.GameUpdate;
import com.waffelmonster.message.tictactoe.MoveResponse;
import com.waffelmonster.message.tictactoe.ResetResponse;
import com.waffelmonster.server.state.Player;
import com.waffelmonster.server.state.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TicTacToeRoom extends Room {
    private String[][] board;
    private Player currentPlayer;
    private List<Player> playing;

    public TicTacToeRoom() {
        super("tictactoe");
        this.board = new String[3][3];
        this.playing = new ArrayList<>(2);
    }

    public String[][] getBoard() {
        return this.board;
    }

    public void move(Player player, int x, int y) {
        // Can't make a move if there are already two players playing
        if (this.playing.size() >= 2 && !this.playing.contains(player)) {
            sendMoveResponse(player, false);
            sendGameUpdate();
        }
        // Can't make a move if it isn't your turn
        if (currentPlayer != null && currentPlayer.getName().equals(player.getName())) {
            sendMoveResponse(player, false);
            sendGameUpdate();
        }
        // Can't make a move in a square that is already taken
        if (board[x][y] != null) {
            sendMoveResponse(player, false);
            sendGameUpdate();
        }
        // Can't make a move if the game is over
        if (isGameOver()) {
            sendMoveResponse(player, false);
            sendGameUpdate();
        }
        if (this.playing.size() < 2) {
            this.playing.add(player);
        }
        board[x][y] = player.getName();
        currentPlayer = player;
        sendMoveResponse(player, true);
        sendGameUpdate();
    }

    public void reset(Player player) {
        boolean gameOver = isGameOver();
        if (gameOver) {
            this.board = new String[3][3];
            this.currentPlayer = null;
            this.playing = new ArrayList<>(2);
            sendResetResponse(player, true);
            sendGameUpdate();
        }
        sendResetResponse(player, false);
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

    private void sendMoveResponse(Player player, boolean success) {
        MoveResponse moveResponse = new MoveResponse();
        moveResponse.success = success;
        player.getConnection().sendTCP(moveResponse);
    }

    private void sendResetResponse(Player player, boolean success) {
        ResetResponse resetResponse = new ResetResponse();
        resetResponse.success = success;
        player.getConnection().sendTCP(resetResponse);
    }

    private void sendGameUpdate() {
        GameUpdate gameUpdate = new GameUpdate();
        gameUpdate.gameOver = isGameOver();
        gameUpdate.currentPlayer = this.currentPlayer.getName();
        gameUpdate.playing = (String[]) this.playing.toArray();
        gameUpdate.board = this.getBoard();
        for (Player player : this.getPlayers().values()) {
            player.getConnection().sendTCP(gameUpdate);
        }
    }
}
