package client.messages;

import client.MapleClient;

public interface Command {
    CommandDefinition[] getDefinition();
    void execute(MapleClient c, MessageCallback mc, String[] splittedLine) throws Exception;
}