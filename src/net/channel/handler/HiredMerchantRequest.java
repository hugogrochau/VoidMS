package net.channel.handler;

import java.util.Arrays;
import client.MapleClient;
import net.AbstractMaplePacketHandler;
import server.maps.MapleMapObjectType;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class HiredMerchantRequest extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        if (!c.isGuest()) {
            if (!c.getPlayer().isGM()) {
                c.getPlayer().dropMessage(1, "Merchants are disabled");
                return;
            }
            if (c.getPlayer().getMap().getMapObjectsInRange(c.getPlayer().getPosition(), 23000, Arrays.asList(MapleMapObjectType.HIRED_MERCHANT, MapleMapObjectType.SHOP)).size() == 0 && (c.getPlayer().getMapId() == 910000001 || c.getPlayer().getMapId() == 910000002 || c.getPlayer().getMapId() == 910000003)) {
                if (c.getPlayer().hasMerchant()) {
                    c.getPlayer().dropMessage(1, "You already have a shop.");
                } else {
                    c.getSession().write(MaplePacketCreator.hiredMerchantBox());
                }
            } else {
                c.getPlayer().dropMessage(1, "You may not establish a store here. Either there is another shop nearby or you are not in Free Market Room 1, 2 or 3.");
            }
        } else {
            c.getPlayer().dropMessage(1, "Guest users are not allowed to open hired merchants.");
        }
    }
}
