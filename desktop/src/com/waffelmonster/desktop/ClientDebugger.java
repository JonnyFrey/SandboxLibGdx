package com.waffelmonster.desktop;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.google.gson.Gson;
import com.waffelmonster.message.*;
import com.waffelmonster.message.tictactoe.*;

import java.io.IOException;
import java.util.Arrays;

import static com.waffelmonster.SandboxGame.DEFAULT_ADDRESS;
import static com.waffelmonster.SandboxGame.DEFAULT_PORT;

public class ClientDebugger extends Listener {

    private final Gson gson = new Gson();
    private final Client client;
    private final String name;

    public ClientDebugger(final String name) {
        this.client = new Client();
        this.name = name;

        final Kryo kryo = client.getKryo();
        Arrays.asList(
                ConnectRequest.class, ConnectResponse.class,
                DisconnectRequest.class, DisconnectResponse.class,
                MoveRequest.class, MoveResponse.class,
                ResetRequest.class, ResetResponse.class,
                BoardRequest.class, BoardResponse.class,
                RoomChatRequest.class, RoomChatResponse.class,
                String[].class, String[][].class
        ).forEach(kryo::register);

        client.addListener(this);

        this.client.start();
    }

    public void execute() throws IOException, InterruptedException {
        client.connect(10000, DEFAULT_ADDRESS, DEFAULT_PORT);

        final ConnectRequest request = new ConnectRequest();
        request.playerName = name;

        client.sendTCP(request);

        //Break Point (suspend thread only) on the Thread Line to _interactively_ play
        Thread.sleep(5000);

        client.stop();
    }

    public void getBoard() {
        this.client.sendTCP(new BoardRequest());
    }

    public void makeMove(int x, int y) {
        final MoveRequest request = new MoveRequest();
        request.playerName = this.name;
        request.x = x;
        request.y = y;
        this.client.sendTCP(request);
    }

    public void reset() {
        this.client.sendTCP(new ResetRequest());
    }

    @Override
    public void received(Connection connection, Object object) {
        if (!(object instanceof FrameworkMessage.KeepAlive)) {
            System.out.println(gson.toJson(object));
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // Set your player name here
        ClientDebugger debugger = new ClientDebugger("waffelmonster");
        debugger.execute();
    }

}
