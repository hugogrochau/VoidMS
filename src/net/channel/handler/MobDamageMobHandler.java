package net.channel.handler;

import client.MapleClient;
import net.AbstractMaplePacketHandler;
import server.life.MapleMonster;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class MobDamageMobHandler extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        int attackerOid = slea.readInt();
        int cid = slea.readInt();
        int damagedOid = slea.readInt();
        MapleMonster damaged = c.getPlayer().getMap().getMonsterByOid(damagedOid);
        MapleMonster attacker = c.getPlayer().getMap().getMonsterByOid(attackerOid);
        if (damaged == null || attacker == null) {
            return;
        }
        int damage = (int) (Math.random() * (damaged.getMaxHp() / 13 + attacker.getPADamage() * 10)) * 2 + 500;
        c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.MobDamageMob(damaged, damage), damaged.getPosition());
    }
}
