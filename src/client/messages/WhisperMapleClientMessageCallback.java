package client.messages;

import client.MapleClient;
import tools.MaplePacketCreator;

public class WhisperMapleClientMessageCallback implements MessageCallback {

    private MapleClient client;
    private String whisperfrom;

    public WhisperMapleClientMessageCallback(String whisperfrom, MapleClient client) {
        this.whisperfrom = whisperfrom;
        this.client = client;
    }

    @Override
    public void dropMessage(String message) {
        client.getSession().write(MaplePacketCreator.getWhisper(whisperfrom, client.getChannel(), message));
    }
}