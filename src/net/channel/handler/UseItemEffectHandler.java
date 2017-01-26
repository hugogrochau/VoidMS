package net.channel.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import client.IItem;
import client.MapleClient;
import client.MapleInventoryType;
import client.anticheat.CheatingOffense;
import net.AbstractMaplePacketHandler;
import server.maps.FakeCharacter;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class UseItemEffectHandler extends AbstractMaplePacketHandler {

    private static Logger log = LoggerFactory.getLogger(UseItemHandler.class);

    public UseItemEffectHandler() {
    }

    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        int itemId = slea.readInt();

        if (itemId >= 5000000 && itemId <= 5000053) {
            log.warn(slea.toString());
        }

        if (itemId != 0) {
            IItem toUse = c.getPlayer().getInventory(MapleInventoryType.CASH).findById(itemId);

            if (toUse == null) {
                c.getPlayer().getCheatTracker().registerOffense(CheatingOffense.USING_UNAVAILABLE_ITEM, Integer.toString(itemId));
                return;
            }
        }
        c.getPlayer().setItemEffect(itemId);
        c.getPlayer().getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.itemEffect(c.getPlayer().getId(), itemId), false);
        if (c.getPlayer().hasFakeChar()) {
            for (FakeCharacter ch : c.getPlayer().getFakeChars()) {
                ch.getFakeChar().setItemEffect(itemId);
                c.getPlayer().getMap().broadcastMessage(ch.getFakeChar(), MaplePacketCreator.itemEffect(c.getPlayer().getId(), itemId), false);
            }
        }
    }
}