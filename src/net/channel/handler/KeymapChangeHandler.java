package net.channel.handler;

import client.MapleClient;
import client.MapleCharacter;
import client.MapleKeyBinding;
import net.AbstractMaplePacketHandler;
import tools.data.input.SeekableLittleEndianAccessor;

public class KeymapChangeHandler extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        MapleCharacter player = c.getPlayer();
        if (slea.available() != 8) {
            slea.readInt();
            int numChanges = slea.readInt();
            for (int i = 0; i < numChanges; i++) {
                int key = slea.readInt();
                int type = slea.readByte();
                int action = slea.readInt();
                MapleKeyBinding newbinding = new MapleKeyBinding(type, action);
                player.changeKeybinding(key, newbinding);
            }
        }
    }
}