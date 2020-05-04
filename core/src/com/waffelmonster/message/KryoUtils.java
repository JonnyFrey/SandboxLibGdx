package com.waffelmonster.message;

import com.esotericsoftware.kryo.Kryo;
import com.waffelmonster.message.tictactoe.*;

import java.util.Arrays;

public class KryoUtils {

    private KryoUtils() {
        // Static utility class, don't create
    }

    public static void registerMessages(final Kryo kryo) {
        Arrays.asList(
                ConnectRequest.class, ConnectResponse.class,
                DisconnectRequest.class, DisconnectResponse.class,
                MoveRequest.class, MoveResponse.class,
                ResetRequest.class, ResetResponse.class,
                BoardRequest.class, BoardResponse.class,
                RoomChatRequest.class, RoomChatResponse.class,
                String[].class, String[][].class
        ).forEach(kryo::register);
    }

}
