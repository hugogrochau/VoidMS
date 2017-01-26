package net.channel.handler;

import client.IItem;
import client.MapleClient;
import client.MapleInventoryType;
import net.AbstractMaplePacketHandler;
import server.AutobanManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;


public class UseSummonBag extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        if (!c.getPlayer().isAlive()) {
            c.getSession().write(MaplePacketCreator.enableActions());
            return;
        }
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        slea.readInt();
        byte slot = (byte) slea.readShort();
        int itemId = slea.readInt();
        IItem toUse = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);
        if (toUse != null && toUse.getQuantity() > 0 && toUse.getItemId() == itemId && c.getPlayer().isAlive()) {
            MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
            int[][] toSpawn = ii.getSummonMobs(itemId);
            for (int z = 0; z < toSpawn.length; z++) {
                int[] toSpawnChild = toSpawn[z];
                if ((int) Math.ceil(Math.random() * 100) <= toSpawnChild[1]) {
                    MapleMonster ht = MapleLifeFactory.getMonster(toSpawnChild[0]);
                    c.getPlayer().getMap().spawnMonsterOnGroundBelow(ht, c.getPlayer().getPosition());
                }
            }
        } else if (itemId != 2100067) {
            AutobanManager.getInstance().autoban(c, "Trying to use a summonbag not in item inventory.");
            return;
        }
        c.getSession().write(MaplePacketCreator.enableActions());
    }
}
