package net.channel.handler;

import client.IItem;
import client.Item;
import client.MapleClient;
import client.MapleInventoryType;
import net.AbstractMaplePacketHandler;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import tools.MaplePacketCreator;
import scripting.npc.NPCScriptManager;
import tools.data.input.SeekableLittleEndianAccessor;

public class UseItemHandler extends AbstractMaplePacketHandler {

    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        int[] items = {1702167, 1702170, 1702187, 1012108, 1012109, 1012110, 1012111, 1060127, 1102176, 1061149, 1302088, 1102177, 1060128, 1061150, 1102178,
            1302089, 1060129, 1061151, 1102179, 1102180, 1102181, 1102182, 1302090,
            1302091, 1060130, 1060131, 1061152, 1061153, 1302092, 1061154, 1060132, 1372035, 1372036, 1372037, 1372038, 1372039, 1372040, 1372041, 1372042,
            1382045, 1382046, 1382047, 1382048, 1382049, 1382050, 1382051, 1382052, 1302081, 1312037, 1322060, 1402046, 1412033, 1422037, 1482023,
            1302094, 1102183, 1060133, 1061155, 1442066, 1442065, 1072333, 1072334, 1072246, 1001046, 1000031, 1702120, 1052092, 1072282, 1000032, 1001047, 1052093, 1702118, 1072283, 1000030, 2049100, 1001045, 1072281, 1052091, 1702119, 1442023, 1702150, 1702149, 1702184, 1702183, 1050018, 1040022, 1041032, 1040007, 1040074, 1002119, 1452001, 1082069, 1462000, 1041065, 1002037, 1002034, 1060014, 1061036, 1051026, 1372004, 1061034, 1372017, 1002016, 1372006, 1051005, 1382004, 1002153, 1372001, 1040044, 1002172, 1002175, 1002173, 1041057, 1041060, 1002184, 1002150, 1040061, 1061041, 1332012, 1040060, 1472008, 1092014, 1002055, 1442009, 1302004, 1061017, 1442006, 1322000, 1002099, 1002058, 1060011, 1050006, 1002009, 1302009, 1060074, 1082149, 1302090, 1082147, 1082150, 1102041, 1002586, 1002587, 1082145, 1082146, 2340000, 1102041, 1002586, 1002587, 1082149, 1002587, 1102041, 1002085, 1002089, 1002219, 1002244, 1102000, 1102023, 1102067, 1102156, 1040121, 1041017, 1041058, 1082015, 1082084, 1082147, 1082167, 1050021, 4031348, 1050048, 1050074, 1050090, 1050133, 1051009, 1051033, 1051066, 1051095, 1051142, 1052014, 1052076, 1052147, 1060039, 1060060, 1060090, 1061002, 1061022, 1061064, 1061089, 1061124, 1062001, 1062047, 1092019, 1092008, 1092001, 1092021, 1092031, 1092050, 1071001, 1071014, 1072030, 1072053, 1072081, 1072107, 1072129, 1072151, 1072161, 1072184, 1072204, 1072244, 1072268, 1072326, 1302026, 1302073, 1302074, 1312009, 1322001, 1332018, 1332056, 1372002, 1382008, 1382032, 1402006, 1402029, 1422008, 1432007, 1442012, 1442051, 1452017, 1452054, 1462014, 1462039, 1472035, 1472062, 1012074, 1002774, 2000016, 2000017, 2000018, 2000019, 2001000, 2001001, 2001002, 2002000, 2002001, 2002002, 2002003, 2002004, 2002005, 2002006, 2002007, 2002008, 2002009, 2002010, 2002011, 2002015, 2002016, 2002017, 2002018, 2002019, 2002020, 2002021, 2002022, 2002023, 2002024, 2002025, 2002026, 2020000, 2020001, 2020002, 2020003, 2020004, 2020005, 2020006, 2020007, 2020008, 2020009, 2020010, 2020011, 2020012, 2020013, 2020014, 2020015, 2020016, 2020017, 2020018, 2020019, 2020020, 2020025, 2020026, 2020027, 2020028, 2020029, 2020030, 1002001, 1002002, 1002003, 1002004, 1002005, 1002006, 1002007, 1002008, 1002009, 1002010, 1002011, 1002012, 1002013, 1002014, 1002015, 1002016, 1002017, 1002018, 1002019, 1002166, 1002167, 1002168, 1002169, 1002170, 1002171, 1002172, 1002173, 1002174, 1002175, 1002176, 1002177, 1002178, 1002179, 1002180, 1002181, 1002182, 1002183, 1002184, 1002185, 1002186, 1002189, 1051159, 1050139, 1051158, 1050138, 1042118, 1002735, 1002736, 1002774, 1072350, 1072331, 1072341, 1072349, 3010048};
        c.getPlayer().resetAfkTime();
        if (!c.getPlayer().isAlive() || c.getPlayer().getMapId() == 240050310) {
            c.getSession().write(MaplePacketCreator.enableActions());
            return;
        }
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        slea.readInt();
        byte slot = (byte) slea.readShort();
        int itemId = slea.readInt();
        IItem toUse = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);
        if (itemId == 2022118) {
            c.getPlayer().dropMessage(1, "Please keep this item for scrolling purposes. Gives you 100% scroll rate.");
            c.getSession().write(MaplePacketCreator.enableActions());
            return;
        } else if (itemId == 2022065) {
            NPCScriptManager.getInstance().start(c, 9010000, "JobChanger", null);
        } else if (itemId == 2022280) {
            int random = (int) Math.floor(Math.random() * items.length);
            int random1 = (int) Math.floor(Math.random() * items.length);
            int random2 = (int) Math.floor(Math.random() * items.length);
            IItem toDrop;
            IItem toDrop1;
            IItem toDrop2;
            if (ii.getInventoryType(items[random]) == MapleInventoryType.EQUIP) {
                toDrop = ii.getEquipById(items[random]);
            } else {
                toDrop = new Item(items[random], (byte) 0, (short) 1);
            } if (ii.getInventoryType(items[random1]) == MapleInventoryType.EQUIP) {
                toDrop1 = ii.getEquipById(items[random1]);
            } else {
                toDrop1 = new Item(items[random1], (byte) 0, (short) 1);
            }
            if (ii.getInventoryType(items[random2]) == MapleInventoryType.EQUIP) {
                toDrop2 = ii.getEquipById(items[random2]);
            } else {
                toDrop2 = new Item(items[random2], (byte) 0, (short) 1);
            }
            MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, itemId, 1, true, true);
            c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), toDrop, c.getPlayer().getPosition(), true, true);
            c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), toDrop1, c.getPlayer().getPosition(), true, true);
            c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), toDrop2, c.getPlayer().getPosition(), true, true);
            c.getPlayer().dropMessage(5, "3 Items have dropped. Be sure to grab em all then make sure you Change channels first!");
        } else if (toUse != null && toUse.getQuantity() > 0) {
            if (toUse.getItemId() != itemId) {
                return;
            }
            if (ii.isTownScroll(itemId)) {
                if (ii.getItemEffect(toUse.getItemId()).applyTo(c.getPlayer())) {
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                }
                c.getSession().write(MaplePacketCreator.enableActions());
                return;
            }

            MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
            ii.getItemEffect(toUse.getItemId()).applyTo(c.getPlayer());
        }
    }
}