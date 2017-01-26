package net.channel.handler;

import client.MapleBuffStat;
import client.MapleCharacter;
import client.MapleClient;
import net.AbstractMaplePacketHandler;
import server.MaplePortal;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class ChangeMapSpecialHandler extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        slea.readByte();
        String startwp = slea.readMapleAsciiString();
        slea.readByte();
        //byte sourcefm = slea.readByte();
        slea.readByte();
        MapleCharacter player = c.getPlayer();
        if (player.getBuffedValue(MapleBuffStat.MORPH) != null && player.getBuffedValue(MapleBuffStat.COMBO) != null) {
            player.cancelEffectFromBuffStat(MapleBuffStat.MORPH);
            player.cancelEffectFromBuffStat(MapleBuffStat.COMBO);
        }
        if (player.getBuffedValue(MapleBuffStat.PUPPET) != null) {
            player.cancelBuffStats(MapleBuffStat.PUPPET);
        }
        MaplePortal portal = c.getPlayer().getMap().getPortal(startwp);
        if (portal != null) {
            portal.enterPortal(c);
        } else {
            c.getSession().write(MaplePacketCreator.enableActions());
        }
    }
}