package com.waffelmonster.server;

import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class ServerLauncher {
    private static final int PORT = 42069;

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
        server.bind(PORT);
    }
}
