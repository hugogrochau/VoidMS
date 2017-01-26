package net.channel.handler;

import client.MapleClient;
import client.MapleInventoryType;
import net.AbstractMaplePacketHandler;
import server.MapleItemInformationProvider;
import tools.data.input.SeekableLittleEndianAccessor;

public class NPCShopHandler extends AbstractMaplePacketHandler {
	
    /** Creates a new instance of NPCBuyHandler */
    public NPCShopHandler() {
    }

    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        byte bmode = slea.readByte();
        if (bmode == 0) { // Buy
            slea.readShort();
            int itemId = slea.readInt();
            short quantity = slea.readShort();
            c.getPlayer().getShop().buy(c, itemId, quantity);
        } else if (bmode == 1) { // Sell
            byte slot = (byte) slea.readShort();
            int itemId = slea.readInt();
            MapleInventoryType type = MapleItemInformationProvider.getInstance().getInventoryType(itemId);
            short quantity = slea.readShort();
            c.getPlayer().getShop().sell(c, type, slot, quantity);
        } else if (bmode == 2) { // Recharge
            byte slot = (byte) slea.readShort();
            c.getPlayer().getShop().recharge(c, slot);
        }
    }
}