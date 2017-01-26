package net.channel.handler;

import client.MapleClient;
import net.AbstractMaplePacketHandler;
import server.maps.FakeCharacter;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class SkillEffectHandler extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        c.getPlayer().cancelMorphs();
        int skillId = slea.readInt();
        int level = slea.readByte();
        byte flags = slea.readByte();
        int speed = slea.readByte();
        if ((skillId == 3121004 || skillId == 5221004 || skillId == 1121001 || skillId == 1221001 || skillId == 1321001 || skillId == 2121001 || skillId == 2221001 || skillId == 2321001 || skillId == 2111002 || skillId == 4211001 || skillId == 3221001 || skillId == 5101004 || skillId == 5201002) && level >= 1) {
            c.getPlayer().getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.skillEffect(c.getPlayer(), skillId, level, flags, speed), false);
            for (FakeCharacter ch : c.getPlayer().getFakeChars()) {
                ch.getFakeChar().getMap().broadcastMessage(ch.getFakeChar(), MaplePacketCreator.skillEffect(ch.getFakeChar(), skillId, level, flags, speed), false);
            }
        } else {
            c.getSession().close();
            return;
        }
    }
}