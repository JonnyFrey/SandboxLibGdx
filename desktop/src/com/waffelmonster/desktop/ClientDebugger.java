package com.waffelmonster.desktop;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.waffelmonster.message.ConnectRequest;
import com.waffelmonster.message.ConnectResponse;

import java.io.IOException;

public class ClientDebugger {

    public static void main(String[] args) throws IOException, InterruptedException {
        final Client client = new Client();

        final Kryo kryo = client.getKryo();
        kryo.register(ConnectRequest.class);
        kryo.register(ConnectResponse.class);

        client.addListener(new ClientListener());

        client.start();
        client.connect(10000, args[0], 42069);

        final ConnectRequest request = new ConnectRequest();
        request.playerName = args[1];

        client.sendTCP(request);

        Thread.sleep(5000);

        client.stop();
    }

}
