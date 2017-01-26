package net.channel.handler;

import client.MapleClient;
import net.AbstractMaplePacketHandler;
import server.life.MapleMonster;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class MonsterBombHandler extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        int oid = slea.readInt();
        MapleMonster monster = c.getPlayer().getMap().getMonsterByOid(oid);
        if (!c.getPlayer().isAlive() || monster == null) {
            return;
        }
        if (monster.getId() == 8500003 || monster.getId() == 8500004) {
            monster.getMap().broadcastMessage(MaplePacketCreator.killMonster(monster.getObjectId(), 4));
            c.getPlayer().getMap().removeMapObject(oid);
        }
    }
}