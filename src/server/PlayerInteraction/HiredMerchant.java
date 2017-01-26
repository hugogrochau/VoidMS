package server.PlayerInteraction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ScheduledFuture;
import client.IItem;
import client.MapleCharacter;
import client.MapleClient;
import database.DatabaseConnection;
import server.AutobanManager;
import server.MapleInventoryManipulator;
import server.TimerManager;
import server.maps.MapleMap;
import server.maps.MapleMapObjectType;
import tools.MaplePacketCreator;

public class HiredMerchant extends PlayerInteractionManager {

    private boolean open;
    public ScheduledFuture<?> schedule = null;
    private MapleMap map;
    private int itemId;

    public HiredMerchant(MapleCharacter owner, int itemId, String desc) {
        super(owner, itemId % 10, desc, 3);
        this.itemId = itemId;
        this.map = owner.getMap();
        this.schedule = TimerManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                HiredMerchant.this.closeShop(true);
            }
        }, 1000 * 60 * 60 * 24);
    }

    public byte getShopType() {
        return IPlayerInteractionManager.HIRED_MERCHANT;
    }

    @Override
    public void buy(MapleClient c, int item, short quantity) {
        MaplePlayerShopItem pItem = items.get(item);
        boolean stamps = false;
        if (pItem.getPrice() < 1000) {
            stamps = true;
        }
        if (pItem.getBundles() > 0) {
            synchronized (items) {
                IItem newItem = pItem.getItem().copy();
                newItem.setQuantity(pItem.getBundles());
                if (!stamps) {
                    if (c.getPlayer().getMeso() >= pItem.getPrice() * quantity) {
                        if (quantity > 0 && pItem.getBundles() >= quantity && pItem.getBundles() > 0) {
                            if (c.getPlayer().getHiredMerchantMesos() + pItem.getPrice() < Integer.MAX_VALUE) {
                                if (MapleInventoryManipulator.addFromDrop(c, newItem, true)) {
                                    Connection con = DatabaseConnection.getConnection();
                                    try {
                                        PreparedStatement ps = con.prepareStatement("UPDATE characters SET MerchantMesos = MerchantMesos + " + pItem.getPrice() * quantity + " WHERE id = ?");
                                        ps.setInt(1, getOwnerId());
                                        ps.executeUpdate();
                                        ps.close();
                                    } catch (SQLException se) {
                                        se.printStackTrace();
                                    }
                                    c.getPlayer().gainMeso(-pItem.getPrice() * quantity, false);
                                    pItem.setBundles((short) (pItem.getBundles() - quantity));
                                    tempItemsUpdate();
                                } else {
                                    c.getPlayer().dropMessage(1, "Your inventory is full.");
                                }
                            } else {
                                c.getPlayer().dropMessage(1, "Buying this would make the store owner go over 2.1b Mesos. You cannot buy this item.");
                            }
                        } else {
                            AutobanManager.getInstance().autoban(c.getPlayer().getClient(), "XSource| Attempted to Merchant dupe.");
                        }
                    } else {
                        c.getPlayer().dropMessage(1, "You do not have enough mesos.");
                    }
                } else if (stamps) {
                    if (c.getPlayer().getItemQuantity(4002000, false) >= pItem.getPrice() * quantity) {
                        if (quantity > 0 && pItem.getBundles() >= quantity && pItem.getBundles() > 0) {
                            if (MapleInventoryManipulator.addFromDrop(c, newItem, true)) {
                                Connection con = DatabaseConnection.getConnection();
                                try {
                                    PreparedStatement ps = con.prepareStatement("UPDATE characters SET MerchantStamps = MerchantStamps + " + pItem.getPrice() * quantity + " WHERE id = ?");
                                    ps.setInt(1, getOwnerId());
                                    ps.executeUpdate();
                                    ps.close();
                                } catch (SQLException se) {
                                    se.printStackTrace();
                                }
                                c.getPlayer().removeItem(4002000, pItem.getPrice() * quantity);
                                pItem.setBundles((short) (pItem.getBundles() - quantity));
                                tempItemsUpdate();
                            } else {
                                c.getPlayer().dropMessage(1, "Your inventory is full.");
                            }
                        } else {
                            AutobanManager.getInstance().autoban(c.getPlayer().getClient(), "XSource| Attempted to Merchant dupe.");
                        }
                    } else {
                        c.getPlayer().dropMessage(1, "If a price is shown under 1000 mesos, it is calculated in Snail Stamps. You do not have enough Snail Stamps to purchase this item.");
                    }
                }
            }
        }
    }

    @Override
    public void closeShop(boolean saveItems) {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characters SET HasMerchant = 0 WHERE id = ?");
            ps.setInt(1, getOwnerId());
            ps.executeUpdate();
            ps.close();
            tempItems(false, false);
            if (saveItems) {
                saveItems();
            }
        } catch (SQLException se) {
            return;
        }
        map.removeMapObject(this);
        map.broadcastMessage(MaplePacketCreator.destroyHiredMerchant(getOwnerId()));
        schedule.cancel(false);
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean set) {
        this.open = set;
    }

    public MapleMap getMap() {
        return map;
    }

    public int getItemId() {
        return itemId;
    }

    @Override
    public void sendDestroyData(MapleClient client) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MapleMapObjectType getType() {
        return MapleMapObjectType.HIRED_MERCHANT;
    }

    @Override
    public void sendSpawnData(MapleClient client) {
        client.getSession().write(MaplePacketCreator.spawnHiredMerchant(this));
    }
}