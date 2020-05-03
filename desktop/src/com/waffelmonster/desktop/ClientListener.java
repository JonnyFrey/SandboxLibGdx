package com.waffelmonster.desktop;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.waffelmonster.message.ConnectResponse;

public class ClientListener extends Listener {

    @Override
    public void connected(Connection connection) {
        System.out.println("Connected to " + connection.getRemoteAddressTCP() + " with " + connection);
    }

    @Override
    public void disconnected(Connection connection) {
        System.out.println("Disconnected from " + connection.getRemoteAddressTCP() + " with " + connection);
    }

    @Override
    public void received(Connection connection, Object object) {
        System.out.println("Receive event from " + connection.getRemoteAddressTCP() + " with " + connection);
        if (object instanceof ConnectResponse) {
            final ConnectResponse response = (ConnectResponse) object;
            System.out.println("ConnectResponse " + response.success);
        }

    }

    @Override
    public void idle(Connection connection) {
        System.out.println("Idling on " + connection.getRemoteAddressTCP() + " with " + connection);
    }
}
