package net.channel.handler;

import client.MapleClient;
import net.AbstractMaplePacketHandler;
import server.MapleItemInformationProvider;
import server.MapleStatEffect;
import server.maps.FakeCharacter;
import tools.data.input.SeekableLittleEndianAccessor;

public class CancelItemEffectHandler extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        int sourceid = slea.readInt();
        MapleStatEffect effect = MapleItemInformationProvider.getInstance().getItemEffect(-sourceid);
        c.getPlayer().cancelEffect(effect, false, -1);
        if (c.getPlayer().hasFakeChar()) {
            for (FakeCharacter ch : c.getPlayer().getFakeChars()) {
                ch.getFakeChar().cancelEffect(effect, false, -1);
            }
        }
    }
}