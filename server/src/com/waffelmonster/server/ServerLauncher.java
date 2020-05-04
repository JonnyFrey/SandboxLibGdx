package com.waffelmonster.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.waffelmonster.message.ConnectRequest;
import com.waffelmonster.message.ConnectResponse;
import com.waffelmonster.message.DisconnectRequest;
import com.waffelmonster.message.DisconnectResponse;
import com.waffelmonster.message.RoomChatRequest;
import com.waffelmonster.message.RoomChatResponse;
import com.waffelmonster.message.tictactoe.BoardRequest;
import com.waffelmonster.message.tictactoe.BoardResponse;
import com.waffelmonster.message.tictactoe.GameUpdate;
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
        Kryo kryo = server.getKryo();
        Arrays.asList(
                ConnectRequest.class, ConnectResponse.class,
                DisconnectRequest.class, DisconnectResponse.class,
                MoveRequest.class, MoveResponse.class,
                ResetRequest.class, ResetResponse.class,
                BoardRequest.class, BoardResponse.class,
                GameUpdate.class,
                RoomChatRequest.class, RoomChatResponse.class,
                String[].class, String[][].class
        ).forEach(kryo::register);
        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof ConnectRequest) {
                    ConnectRequest connectRequest = (ConnectRequest) object;
                    Player player = new Player(connection, connectRequest.playerName);
                    ConnectResponse connectResponse = new ConnectResponse();
                    connectResponse.success = ticTacToeRoom.join(player);
                    connection.sendTCP(connectResponse);
                }
                // The client must send a successful connect request before the server will respond to anything else.
                Player player = ticTacToeRoom.getPlayer(connection);
                if (player == null) {
                    return;
                }
                if (object instanceof DisconnectRequest) {
                    DisconnectResponse disconnectResponse = new DisconnectResponse();
                    disconnectResponse.success = ticTacToeRoom.quit(connection);
                    connection.sendTCP(disconnectResponse);
                } else if (object instanceof MoveRequest) {
                    MoveRequest moveRequest = (MoveRequest) object;
                    ticTacToeRoom.move(player, moveRequest.x, moveRequest.y);
                } else if (object instanceof ResetRequest) {
                    ticTacToeRoom.reset(player);
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
