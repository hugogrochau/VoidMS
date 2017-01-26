package net.channel.handler;

import client.MapleClient;
import client.MapleInventoryType;
import net.AbstractMaplePacketHandler;
import server.AutobanManager;
import server.MapleInventoryManipulator;
import tools.data.input.SeekableLittleEndianAccessor;

public class ItemMoveHandler extends AbstractMaplePacketHandler {
    // private static Logger log = LoggerFactory.getLogger(ItemMoveHandler.class);

    /** Creates a new instance of ItemMoveHandler */
    public ItemMoveHandler() {
    }

    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        slea.readInt();
        MapleInventoryType type = MapleInventoryType.getByType(slea.readByte());
        byte src = (byte) slea.readShort();
        byte dst = (byte) slea.readShort();
        long checkq = slea.readShort();
        short quantity = (short) (int) checkq;
        if (src < 0 && dst > 0) {
            MapleInventoryManipulator.unequip(c, src, dst);
        } else if (dst < 0) {
            MapleInventoryManipulator.equip(c, src, dst);
        } else if (dst == 0) {
            if (c.getPlayer().getInventory(type).getItem(src) == null) {
                return;
            }
            if (checkq > 4000 || checkq < 1) {
                AutobanManager.getInstance().autoban(c, "Drop-dupe attemp item (" + c.getPlayer().getInventory(type).getItem(src).getItemId() + ").");
                return;
            }
            MapleInventoryManipulator.drop(c, type, src, quantity);
            //  c.getPlayer().saveToDB(true, false);
        } else {
            MapleInventoryManipulator.move(c, type, src, dst);
        }
    }
}
