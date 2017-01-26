package net.channel.handler;

import client.ExpTable;
import client.MapleClient;
import client.MapleInventoryType;
import net.AbstractMaplePacketHandler;
import server.MapleInventoryManipulator;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class UseMountFoodHandler extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        slea.readInt();
        slea.readShort();
        int itemid = slea.readInt();
        if (c.getPlayer().getInventory(MapleInventoryType.USE).findById(itemid) != null) {
            if (c.getPlayer().getMount() != null) {
                c.getPlayer().getMount().setTiredness(c.getPlayer().getMount().getTiredness() - 30);
                c.getPlayer().getMount().setExp((int) ((Math.random() * 26) + 12) + c.getPlayer().getMount().getExp());
                int level = c.getPlayer().getMount().getLevel();
                boolean levelup = c.getPlayer().getMount().getExp() >= ExpTable.getMountExpNeededForLevel(level) && level < 31 && c.getPlayer().getMount().getTiredness() != 0;
                if (levelup)
                    c.getPlayer().getMount().setLevel(level + 1);
                c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.updateMount(c.getPlayer().getId(), c.getPlayer().getMount(), levelup));
                MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, itemid, 1, true, false);
            } else
                c.getPlayer().dropMessage("Please get on your mount first before using the mount food.");
        }
    }
}