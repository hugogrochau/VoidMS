package net.channel.handler;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import client.MapleCharacter;
import net.channel.ChannelServer;
import net.world.MaplePartyCharacter;
import client.MapleClient;
import client.MaplePet;
import client.anticheat.CheatingOffense;
import net.AbstractMaplePacketHandler;
import net.MaplePacket;
import net.world.WorldServer;
import server.MapleInventoryManipulator;
import server.maps.MapleMap;
import server.maps.MapleMapItem;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class ItemPickupHandler extends AbstractMaplePacketHandler {

    /** Creates a new instance of ItemPickupHandler */
    public ItemPickupHandler() {
    }

    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        @SuppressWarnings("unused")
        byte mode = slea.readByte();
        slea.readInt();
        slea.readInt();
        int oid = slea.readInt();
        WorldServer wserv = WorldServer.getInstance();
        MapleMapObject ob = c.getPlayer().getMap().getMapObject(oid);
        if (ob == null) {
            c.getSession().write(MaplePacketCreator.getInventoryFull());
            c.getSession().write(MaplePacketCreator.getShowInventoryFull());
            return;
        }
        if (ob instanceof MapleMapItem) {
            MapleMapItem mapitem = (MapleMapItem) ob;
            synchronized (mapitem) {
                if (mapitem.isPickedUp()) {
                    c.getSession().write(MaplePacketCreator.getInventoryFull());
                    c.getSession().write(MaplePacketCreator.getShowInventoryFull());
                    return;
                }
                double distance = c.getPlayer().getPosition().distanceSq(mapitem.getPosition());
                // c.getPlayer().getCheatTracker().checkPickupAgain();
                if (distance > 90000.0) { // 300^2, 550 is approximatly the range of ultis
                    c.getPlayer().getCheatTracker().registerOffense(CheatingOffense.ITEMVAC);
                } else if (distance > 22500.0) {
                    c.getPlayer().getCheatTracker().registerOffense(CheatingOffense.SHORT_ITEMVAC);
                }
                if (mapitem.getMeso() > 0) {
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
                    if ((c.getPlayer().haveItem(4002002, 1, false, true) || c.getPlayer().haveItem(4031561, 1, false, true)) && c.getPlayer().stampOn()) {
                        if (c.getPlayer().getMeso() > 1999999999) {
                            c.getPlayer().gainMeso(-2000000000, true);
                            MapleInventoryManipulator.addById(c, 4002000, (short) 2);
                            c.getPlayer().dropMessage("You have exchanged two billion mesos for two snail stamps");
                        }
                    }
                    c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.removeItemFromMap(mapitem.getObjectId(), 2, c.getPlayer().getId()), mapitem.getPosition());
                    c.getPlayer().getCheatTracker().pickupComplete();
                    c.getPlayer().getMap().removeMapObject(ob);
                } else {
                    if (mapitem.getItem().getItemId() >= 5000000 && mapitem.getItem().getItemId() <= 5000100) {
                        int petId = MaplePet.createPet(mapitem.getItem().getItemId());
                        if (petId == -1) {
                            return;
                        }
                        MapleInventoryManipulator.addById(c, mapitem.getItem().getItemId(), mapitem.getItem().getQuantity(), null, petId);
                        c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.removeItemFromMap(mapitem.getObjectId(), 2, c.getPlayer().getId()), mapitem.getPosition());
                        // c.getPlayer().getCheatTracker().pickupComplete();
                        c.getPlayer().getMap().removeMapObject(ob);
                        if (mapitem.getItem().getItemId() == 4001025) {
                            c.getPlayer().setHasFlag(true);
                            MapleMap map = c.getPlayer().getMap();
                            List<MapleMapObject> items = map.getMapObjectsInRange(c.getPlayer().getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.ITEM));
                            for (MapleMapObject i : items) {
                                map.removeMapObject(i);
                                map.broadcastMessage(MaplePacketCreator.removeItemFromMap(i.getObjectId(), 0, c.getPlayer().getId()));
                            }
                        }
                    } else {
                        if (c.getPlayer().getGMLevel() < 3 && ((mapitem.getItem().getItemId() >= 3990000 && mapitem.getItem().getItemId() <= 3990023) || (mapitem.getItem().getItemId() >= 3991000 && mapitem.getItem().getItemId() <= 3991051))) {
                            return;
                        }
                        if (MapleInventoryManipulator.addFromDrop(c, mapitem.getItem(), true)) {
                            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.removeItemFromMap(mapitem.getObjectId(), 2, c.getPlayer().getId()), mapitem.getPosition());
                            // c.getPlayer().getCheatTracker().pickupComplete();
                            if (wserv.hasDropEvent()) {
                                if (mapitem.getItem() == wserv.getDropEventItem() && c.getPlayer().getMapId() == wserv.getDropEventMap()) {
                                    if (c.getPlayer().getName().equalsIgnoreCase(wserv.getDropEventHost())) {
                                        return;
                                    }
                                    MaplePacket packet = MaplePacketCreator.serverNotice(6, "[Drop Event] " + c.getPlayer().getName() + " has just won " + wserv.getDropEventHost() + "'s drop event! Players are free to create a new one now.");
                                    try {
                                        ChannelServer.getInstance(c.getChannel()).getWorldInterface().broadcastMessage(c.getPlayer().getName(), packet.getBytes());
                                    } catch (RemoteException e) {
                                        c.getChannelServer().reconnectWorld();
                                    }
                                    wserv.endDropEvent();
                                }
                            }
                            if (mapitem.getItem().getItemId() == 4001025) {
                                c.getPlayer().setHasFlag(true);
                                c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.serverNotice(5, c.getPlayer().getName() + " looted the flag!"));
                                MapleMap map = c.getPlayer().getMap();
                                List<MapleMapObject> items = map.getMapObjectsInRange(c.getPlayer().getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.ITEM));
                                for (MapleMapObject i : items) {
                                    map.removeMapObject(i);
                                    map.broadcastMessage(MaplePacketCreator.removeItemFromMap(i.getObjectId(), 0, c.getPlayer().getId()));
                                }

                            }
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
