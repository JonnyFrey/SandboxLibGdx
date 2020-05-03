package com.waffelmonster.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.waffelmonster.message.*;
import com.waffelmonster.message.tictactoe.BoardRequest;
import com.waffelmonster.message.tictactoe.BoardResponse;
import com.waffelmonster.message.tictactoe.MoveRequest;
import com.waffelmonster.message.tictactoe.MoveResponse;
import com.waffelmonster.message.tictactoe.ResetRequest;
import com.waffelmonster.message.tictactoe.ResetResponse;
import com.waffelmonster.server.state.ChatMessage;
import com.waffelmonster.server.state.Player;
import com.waffelmonster.server.state.tictactoe.TicTacToeRoom;

import java.io.IOException;
import java.util.Arrays;

public class ServerLauncher {
    private static final int PORT = 42069;
    private static TicTacToeRoom ticTacToeRoom = new TicTacToeRoom();

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
        server.bind(PORT);
        KryoUtils.registerMessages(server.getKryo());
        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                System.out.println(connection.getRemoteAddressTCP());
                if (object instanceof ConnectRequest) {
                    ConnectRequest connectRequest = (ConnectRequest) object;
                    System.out.println(connectRequest.playerName);
                    Player player = new Player(connectRequest.playerName);
                    ConnectResponse connectResponse = new ConnectResponse();
                    connectResponse.success = ticTacToeRoom.join(connection, player);
                    connection.sendTCP(connectResponse);
                } else if (object instanceof DisconnectRequest) {
                    DisconnectResponse disconnectResponse = new DisconnectResponse();
                    disconnectResponse.success = ticTacToeRoom.quit(connection);
                    connection.sendTCP(disconnectResponse);
                } else if (object instanceof MoveRequest) {
                    MoveRequest moveRequest = (MoveRequest) object;
                    MoveResponse moveResponse = new MoveResponse();
                    moveResponse.success = ticTacToeRoom.move(moveRequest.playerName, moveRequest.x, moveRequest.y);
                    connection.sendTCP(moveResponse);
                } else if (object instanceof ResetRequest) {
                    ResetResponse resetResponse = new ResetResponse();
                    resetResponse.success = ticTacToeRoom.reset();
                    connection.sendTCP(resetResponse);
                } else if (object instanceof BoardRequest) {
                    BoardResponse boardResponse = new BoardResponse();
                    boardResponse.board = ticTacToeRoom.getBoard();
                    connection.sendTCP(boardResponse);
                } else if (object instanceof RoomChatRequest) {
                    RoomChatRequest roomChatRequest = (RoomChatRequest) object;
                    ChatMessage chatMessage = new ChatMessage(ticTacToeRoom.getPlayer(connection), roomChatRequest.message);
                    ticTacToeRoom.addMessage(chatMessage);
                    RoomChatResponse roomChatResponse = new RoomChatResponse();
                    roomChatResponse.roomName = ticTacToeRoom.getName();
                    roomChatResponse.playerName = chatMessage.getPlayer().getName();
                    roomChatResponse.message = chatMessage.getMessage();
                    for (Connection c : server.getConnections()) {
                        if (ticTacToeRoom.getPlayers().containsKey(c)) {
                            c.sendTCP(roomChatResponse);
                        }
                    }
                }
            }

            @Override
            public void disconnected(Connection connection) {
                ticTacToeRoom.quit(connection);
            }
        });
    }
}
