package net.channel.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import client.MapleClient;
import client.MaplePet;
import database.DatabaseConnection;
import net.AbstractMaplePacketHandler;
import server.AutobanManager;
import server.CashItemFactory;
import server.CashItemInfo;
import server.MapleInventoryManipulator;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class BuyCSItemHandler extends AbstractMaplePacketHandler {

    private void updateInformation(MapleClient c, int item) {
        CashItemInfo Item = CashItemFactory.getItem(item);
        c.getSession().write(MaplePacketCreator.showBoughtCSItem(Item.getId()));
        updateInformation(c);
    }

    private void updateInformation(MapleClient c) {
        c.getSession().write(MaplePacketCreator.showNXMapleTokens(c.getPlayer()));
        c.getSession().write(MaplePacketCreator.enableCSUse0());
        c.getSession().write(MaplePacketCreator.enableCSUse1());
        c.getSession().write(MaplePacketCreator.enableCSUse2());
        c.getSession().write(MaplePacketCreator.enableCSUse3());
        c.getSession().write(MaplePacketCreator.enableActions());
    }

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        int action = slea.readByte();
        if (action == 3) {
            slea.skip(1);
            int useNX = slea.readInt();
            int snCS = slea.readInt();
            CashItemInfo item = CashItemFactory.getItem(snCS);
            int itemID = item.getId();
            if ((itemID >= 5390000 && itemID <= 5390002) || itemID == 1812006 || itemID == 5040001 || itemID == 5072000 || itemID == 5040000 || itemID == 5041000 || itemID == 2320000 || itemID == 1012074 || itemID == 1002774 || itemID == 5060000 || itemID >= 5211000 && itemID <= 5211050 || itemID >= 5360000 && itemID <= 5360043 || itemID == 1112116 || itemID == 1112120 || itemID == 1112226 || itemID == 1112206 || itemID == 1112214 || itemID == 1112211 || itemID == 1112117 || itemID == 1112232 || itemID == 1112122 || itemID == 1112207 || itemID == 1112215 || itemID == 1112106 || itemID == 1112103 || itemID == 1112119 || itemID == 1112230 || itemID == 5100000 || itemID == 5110000 || itemID >= 5120000 && itemID <= 5120015 || itemID >= 5121000 && itemID <= 5121017 || itemID == 5122000 || itemID >= 5140000 && itemID <= 5140006) {
                c.getPlayer().getClient().getSession().write(MaplePacketCreator.serverNotice(1, "You may not purchase this item. Try looking at @shop for it."));
                c.getSession().write(MaplePacketCreator.showNXMapleTokens(c.getPlayer()));
                c.getSession().write(MaplePacketCreator.enableCSUse1());
                c.getSession().write(MaplePacketCreator.enableCSUse2());
                c.getSession().write(MaplePacketCreator.enableCSUse3());
                return;
            }
            /*         if (c.getPlayer().isRare(itemID)) { //TODO : FIX THE RARE LIST.
            AutobanManager.getInstance().autoban(c, "[Autoban] Packet Editing to buy non-available item from Cash Shop");
            }*/
            if (c.getPlayer().getCSPoints(useNX) >= item.getPrice()) {
                c.getPlayer().modifyCSPoints(useNX, -item.getPrice());
            } else {
                c.getSession().write(MaplePacketCreator.enableActions());
                AutobanManager.getInstance().autoban(c, "Trying to purchase from the CS when they have no NX.");
                return;
            }
            if (itemID >= 5000000 && itemID <= 5000100) {
                int petId = MaplePet.createPet(itemID);
                if (petId == -1) {
                    c.getSession().write(MaplePacketCreator.enableActions());
                    return;
                }
                MapleInventoryManipulator.addById(c, itemID, (short) 1, null, petId);
            } else {
                MapleInventoryManipulator.addById(c, itemID, (short) item.getCount());
            }
            updateInformation(c, snCS);
        } else if (action == 5) {
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("DELETE FROM wishlist WHERE charid = ?");
                ps.setInt(1, c.getPlayer().getId());
                ps.executeUpdate();
                ps.close();

                int i = 10;
                while (i > 0) {
                    int sn = slea.readInt();
                    if (sn != 0) {
                        ps = con.prepareStatement("INSERT INTO wishlist(charid, sn) VALUES(?, ?) ");
                        ps.setInt(1, c.getPlayer().getId());
                        ps.setInt(2, sn);
                        ps.executeUpdate();
                        ps.close();
                    }
                    i--;
                }
            } catch (SQLException se) {
            }
            c.getSession().write(MaplePacketCreator.sendWishList(c.getPlayer().getId(), true));
        } else if (action == 7) {
            slea.skip(1);
            byte toCharge = slea.readByte();
            int toIncrease = slea.readInt();
            if (c.getPlayer().getCSPoints(toCharge) >= 4000 && c.getPlayer().getStorage().getSlots() < 48) {
                c.getPlayer().modifyCSPoints(toCharge, -4000);
                if (toIncrease == 0) {
                    c.getPlayer().getStorage().gainSlots(4);
                }
                updateInformation(c);
            }
        } else if (action == 28) { // Package
            slea.skip(1);
            int useNX = slea.readInt();
            int snCS = slea.readInt();
            CashItemInfo item = CashItemFactory.getItem(snCS);
            if (c.getPlayer().getCSPoints(useNX) >= item.getPrice()) {
                c.getPlayer().modifyCSPoints(useNX, -item.getPrice());
            } else {
                c.getSession().write(MaplePacketCreator.enableActions());
                AutobanManager.getInstance().autoban(c, "Trying to purchase from the CS when they have no NX.");
                return;
            }
            for (int i : CashItemFactory.getPackageItems(item.getId())) {
                if (i >= 5000000 && i <= 5000100) {
                    int petId = MaplePet.createPet(i);
                    if (petId == -1) {
                        c.getSession().write(MaplePacketCreator.enableActions());
                        return;
                    }
                    MapleInventoryManipulator.addById(c, i, (short) 1, null, petId);
                } else {
                    MapleInventoryManipulator.addById(c, i, (short) item.getCount());
                }
            }
            updateInformation(c, snCS);
        } else if (action == 30) {
            int snCS = slea.readInt();
            CashItemInfo item = CashItemFactory.getItem(snCS);
            if (c.getPlayer().getMeso() >= item.getPrice()) {
                c.getPlayer().gainMeso(-item.getPrice(), false);
                MapleInventoryManipulator.addById(c, item.getId(), (short) item.getCount());
            } else {
                c.getSession().write(MaplePacketCreator.enableActions());
                AutobanManager.getInstance().autoban(c, "Trying to purchase from the CS with an insufficient amount.");
                return;
            }
        }
    }
}

//        if (slea.available() > 22) {
//            slea.skip(1);
//            int dob = slea.readInt();
//            int payment = slea.readByte();
//            slea.skip(3);
//            int snCS = slea.readInt();
//            CashItemInfo ring = CashItemFactory.getItem(snCS);
//            int userLength = slea.readByte();
//            slea.skip(1);
//            String partner = slea.readAsciiString(userLength);
//            slea.skip(2);
//            int left = (int) slea.available();
//            String text = slea.readAsciiString(left);
//            MapleCharacter partnerChar = c.getChannelServer().getPlayerStorage().getCharacterByName(partner);
//            if (partnerChar == null) {
//                c.getPlayer().getClient().getSession().write(MaplePacketCreator.serverNotice(1, "The partner you specified cannot be found.\r\nPlease make sure your partner is online and in the same channel."));
//            } else {
//                c.getSession().write(MaplePacketCreator.showBoughtCSItem(ring.getId()));
//                c.getPlayer().modifyCSPoints(payment, -ring.getPrice());
//                MapleRing.createRing(c, ring.getId(), c.getPlayer().getId(), c.getPlayer().getName(), partnerChar.getId(), partnerChar.getName(), text);
//                c.getPlayer().getClient().getSession().write(MaplePacketCreator.serverNotice(1, "Successfully created a ring for both you and your partner!\r\nIf you cannot see the effect, please try relogging."));
//            }
//            c.getSession().write(MaplePacketCreator.showNXMapleTokens(c.getPlayer()));
//            c.getSession().write(MaplePacketCreator.enableCSUse0());
//            c.getSession().write(MaplePacketCreator.enableCSUse1());
//            c.getSession().write(MaplePacketCreator.enableCSUse2());
//            c.getSession().write(MaplePacketCreator.enableCSUse3());
//        } else {