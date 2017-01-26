package net.login.handler;

import client.MapleCharacterUtil;
import client.MapleClient;
import net.AbstractMaplePacketHandler;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class CheckCharNameHandler extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        String name = slea.readMapleAsciiString();
        c.getSession().write(MaplePacketCreator.charNameResponse(name, !MapleCharacterUtil.canCreateChar(name, c.getWorld())));
    }
}