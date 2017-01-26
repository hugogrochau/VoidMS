package net.channel.handler;

import java.rmi.RemoteException;
import client.MapleBuffStat;
import client.MapleCharacter;
import client.MapleClient;
import net.AbstractMaplePacketHandler;
import net.world.remote.WorldChannelInterface;
import server.maps.FakeCharacter;
import server.maps.SavedLocationType;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class EnterCashShopHandler extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        if (player.getCSPoints(1) < 0 || player.getCSPoints(2) < 0 || player.getCSPoints(4) < 0) {
            player.modifyCSPoints(1, -player.getCSPoints(1));
            player.modifyCSPoints(2, -player.getCSPoints(2));
            player.modifyCSPoints(4, -player.getCSPoints(4));
}
        if (player.getClient().getChannelServer().CStoFM()) {
            if (!(c.getPlayer().isAlive())) {
                c.getPlayer().dropMessage("You can't enter the FM when you are dead.");
            } else {
                if (c.getPlayer().getMapId() != 910000000) {
                    c.getPlayer().saveLocation(SavedLocationType.FREE_MARKET);
                    c.getPlayer().changeMap(c.getChannelServer().getMapFactory().getMap(910000000), c.getChannelServer().getMapFactory().getMap(910000000).getPortal("out00"));
                }
            }
            c.getSession().write(MaplePacketCreator.enableActions());
        } else {
            if (player.getNoPets() > 0) {
                player.unequipAllPets();
            }
            for (FakeCharacter fc : player.getFakeChars()) {
                player.getMap().removePlayer(fc.getFakeChar());
            }
            if (player.getBuffedValue(MapleBuffStat.MONSTER_RIDING) != null) {
                player.cancelEffectFromBuffStat(MapleBuffStat.MONSTER_RIDING);
            }
            try {
                WorldChannelInterface wci = c.getChannelServer().getWorldInterface();
                wci.addBuffsToStorage(player.getId(), player.getAllBuffs());
                wci.addCooldownsToStorage(player.getId(), player.getAllCooldowns());
            } catch (RemoteException e) {
                c.getChannelServer().reconnectWorld();
            }
            player.getMap().removePlayer(player);
            c.getSession().write(MaplePacketCreator.warpCS(c, false));
            player.setInCS(true);
            c.getSession().write(MaplePacketCreator.enableCSUse0());
            c.getSession().write(MaplePacketCreator.enableCSUse1());
            c.getSession().write(MaplePacketCreator.enableCSUse2());
            c.getSession().write(MaplePacketCreator.enableCSUse3());
            c.getSession().write(MaplePacketCreator.showNXMapleTokens(player));
            c.getSession().write(MaplePacketCreator.sendWishList(player.getId(), false));
            player.saveToDB(true, true);
        }
    }
}
