package net.channel.handler;

import client.MapleCharacter;
import client.MapleClient;
import client.MaplePet;
import client.anticheat.CheatingOffense;
import net.AbstractMaplePacketHandler;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.maps.MapleMapItem;
import server.maps.MapleMapObject;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;
import client.MapleInventoryType;
import net.channel.ChannelServer;
import net.world.MaplePartyCharacter;

public class PetLootHandler extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (c.getPlayer().getNoPets() == 0) {
            return;
        }
        MaplePet pet = c.getPlayer().getPet(c.getPlayer().getPetIndex(slea.readInt()));
        slea.skip(13);
        int oid = slea.readInt();
        MapleMapObject ob = c.getPlayer().getMap().getMapObject(oid);
        if (ob == null || pet == null) {
            c.getSession().write(MaplePacketCreator.getInventoryFull());
            return;
        }
        if (ob instanceof MapleMapItem) {
            MapleMapItem mapitem = (MapleMapItem) ob;
            synchronized (mapitem) {
                if (mapitem.isPickedUp()) {
                    c.getSession().write(MaplePacketCreator.getInventoryFull());
                    return;
                }
                double distance = pet.getPos().distanceSq(mapitem.getPosition());
                c.getPlayer().getCheatTracker().checkPickupAgain();
                if (distance > 90000.0) { // 300^2, 550 is approximatly the range of ultis
                    c.getPlayer().getCheatTracker().registerOffense(CheatingOffense.ITEMVAC);
                } else if (distance > 22500.0) {
                    c.getPlayer().getCheatTracker().registerOffense(CheatingOffense.SHORT_ITEMVAC);
                }
                if (mapitem.getMeso() > 0) {
                    if (c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).findById(1812000) != null) { //Evil hax until I find the right packet - Ramon
                        if (c.getPlayer().getParty() != null) {
                            ChannelServer cserv = c.getChannelServer();
                            int mesosamm = mapitem.getMeso();
                            int partynum = 0;
                            for (MaplePartyCharacter partymem : c.getPlayer().getParty().getMembers()) {
                                if (partymem.isOnline() && partymem.getMapid() == c.getPlayer().getMap().getId() && partymem.getChannel() == c.getChannel()) {
                                    partynum++;
                                }
                            }
                            int mesosgain = mesosamm / partynum;
                            for (MaplePartyCharacter partymem : c.getPlayer().getParty().getMembers()) {
                                if (partymem.isOnline() && partymem.getMapid() == c.getPlayer().getMap().getId()) {
                                    MapleCharacter somecharacter = cserv.getPlayerStorage().getCharacterById(partymem.getId());
                                    if (somecharacter != null) {
                                        somecharacter.gainMeso(mesosgain, true, true);
                                    }
                                }
                            }
                        } else {
                            c.getPlayer().gainMeso(mapitem.getMeso(), true, true);
                        }
                        c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.removeItemFromMap(mapitem.getObjectId(), 5, c.getPlayer().getId(), true, c.getPlayer().getPetIndex(pet)), mapitem.getPosition());
                        c.getPlayer().getCheatTracker().pickupComplete();
                        c.getPlayer().getMap().removeMapObject(ob);
                        if ((c.getPlayer().haveItem(4002002, 1, false, true) || c.getPlayer().haveItem(4031561, 1, false, true)) && c.getPlayer().stampOn()) {
                            if (c.getPlayer().getMeso() > 1999999999) {
                                c.getPlayer().gainMeso(-2000000000, true);
                                MapleInventoryManipulator.addById(c, 4002000, (short) 2);
                                c.getPlayer().dropMessage("You have exchanged two billion mesos for two snail stamps");
                            }
                        }
                    } else {
                        c.getPlayer().getCheatTracker().pickupComplete();
                        mapitem.setPickedUp(false);
                        c.getSession().write(MaplePacketCreator.enableActions());
                        return;
                    }
                } else {
                    if (c.getPlayer().getGMLevel() < 3 && ((mapitem.getItem().getItemId() >= 3990000 && mapitem.getItem().getItemId() <= 3990023) || (mapitem.getItem().getItemId() >= 3991000 && mapitem.getItem().getItemId() <= 3991051))) {
                        return;
                    }
                    if (ii.isPet(mapitem.getItem().getItemId())) {
                        if (MapleInventoryManipulator.addById(c, mapitem.getItem().getItemId(), mapitem.getItem().getQuantity(), null)) {
                            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.removeItemFromMap(mapitem.getObjectId(), 5, c.getPlayer().getId(), true, c.getPlayer().getPetIndex(pet)), mapitem.getPosition());
                            c.getPlayer().getCheatTracker().pickupComplete();
                            c.getPlayer().getMap().removeMapObject(ob);
                        } else {
                            c.getPlayer().getCheatTracker().pickupComplete();
                            return;
                        }
                    } else {
                        if (MapleInventoryManipulator.addFromDrop(c, mapitem.getItem(), true)) {
                            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.removeItemFromMap(mapitem.getObjectId(), 5, c.getPlayer().getId(), true, c.getPlayer().getPetIndex(pet)), mapitem.getPosition());
                            c.getPlayer().getCheatTracker().pickupComplete();
                            c.getPlayer().getMap().removeMapObject(ob);
                        } else {
                            c.getPlayer().getCheatTracker().pickupComplete();
                            return;
                        }
                    }
                }
                mapitem.setPickedUp(true);
            }
        }
        c.getSession().write(MaplePacketCreator.enableActions());
    }
}
