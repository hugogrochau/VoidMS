package net.channel.handler;

import client.MapleClient;
import net.AbstractMaplePacketHandler;
import tools.data.input.SeekableLittleEndianAccessor;

public class CancelDebuffHandler extends AbstractMaplePacketHandler {
    // private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CancelDebuffHandler.class);

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        // MapleCharacter handles the timing.
    }
}