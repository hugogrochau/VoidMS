package client.messages.commands;

import java.awt.Point;
import java.io.File;
import java.net.InetAddress;
import java.rmi.RemoteException;
import client.IItem;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleInventoryType;
import client.MaplePet;
import client.MapleStat;
import client.Equip;
import client.messages.Command;
import client.messages.MessageCallback;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import tools.MaplePacketCreator;
import net.channel.ChannelServer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import client.Item;
import client.MapleCharacterUtil;
import client.MapleDisease;
import client.MapleJob;
import client.SkillFactory;
import client.messages.CommandDefinition;
import client.messages.CommandProcessor;
import database.DatabaseConnection;
import net.world.remote.CheaterData;
import scripting.npc.NPCScriptManager;
import server.MaplePortal;
import server.MapleShopFactory;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleMonsterStats;
import server.life.MapleNPC;
import server.life.MobSkillFactory;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import net.channel.handler.ChangeChannelHandler;
import net.world.remote.WorldChannelInterface;
import net.world.remote.WorldLocation;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.MapleTrade;
import server.maps.FakeCharacter;
import static client.messages.CommandProcessor.getNamedDoubleArg;
import static client.messages.CommandProcessor.getNamedIntArg;
import static client.messages.CommandProcessor.getOptionalIntArg;
import static client.messages.CommandProcessor.joinAfterString;
import server.TimerManager;
import server.maps.MapleMapItem;
import tools.Pair;
import tools.StringUtil;

public class GM implements Command {

    public static short getNamedShortArg(String splitted[], int startpos, String name) {
        String arg = null;
        for (int i = startpos; i < splitted.length; i++) {
            if (splitted[i].equalsIgnoreCase(name) && i + 1 < splitted.length) {
                arg = splitted[i + 1];
            }
        }
        if (arg != null) {
            try {
                return Short.parseShort(arg);
            } catch (NumberFormatException nfe) {
                return Short.MIN_VALUE;
            }
        }
        return Short.MIN_VALUE;
    }

    public static int getOptionalIntArg(String splitted[], int position, int def) {
        if (splitted.length > position) {
            try {
                return Integer.parseInt(splitted[position]);
            } catch (NumberFormatException nfe) {
                return def;
            }
        }
        return def;
    }

    public static byte getNamedByteArg(String splitted[], int startpos, String name) {
        String arg = null;
        for (int i = startpos; i < splitted.length; i++) {
            if (splitted[i].equalsIgnoreCase(name) && i + 1 < splitted.length) {
                arg = splitted[i + 1];
            }
        }
        if (arg != null) {
            try {
                return Byte.parseByte(arg);
            } catch (NumberFormatException nfe) {
                return Byte.MIN_VALUE;
            }
        }
        return Byte.MIN_VALUE;
    }

    private static String getBannedReason(String name) {
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps;
            ResultSet rs;
            ps = con.prepareStatement("SELECT name, banned, banreason, macs FROM accounts WHERE name = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("banned") > 0) {
                    String user, reason, mac;
                    user = rs.getString("name");
                    reason = rs.getString("banreason");
                    mac = rs.getString("macs");
                    rs.close();
                    ps.close();
                    return "Username: " + user + " | BanReason: " + reason + " | Macs: " + mac;
                } else {
                    rs.close();
                    ps.close();
                    return "Player is not banned.";
                }
            }
            rs.close();
            ps.close();
            int accid;
            ps = con.prepareStatement("SELECT accountid FROM characters WHERE name = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                return "This character / account does not exist.";
            } else {
                accid = rs.getInt("accountid");
            }
            ps = con.prepareStatement("SELECT name, banned, banreason, macs FROM accounts WHERE id = ?");
            ps.setInt(1, accid);
            rs = ps.executeQuery();
            if (rs.getInt("banned") > 0) {
                String user, reason, mac;
                user = rs.getString("name");
                reason = rs.getString("banreason");
                mac = rs.getString("macs");
                rs.close();
                ps.close();
                return "Username: " + user + " | BanReason: " + reason + " | Macs: " + mac;
            } else {
                rs.close();
                ps.close();
                return "Player is not banned.";
            }
        } catch (SQLException exe) {
        }
        return "Player is not banned.";
    }

    @Override
    public void execute(final MapleClient c, MessageCallback mc, String[] splitted) throws Exception {
        splitted[0] = splitted[0].toLowerCase();
        ChannelServer cserv = c.getChannelServer();
        Collection<ChannelServer> cservs = ChannelServer.getAllInstances();
        MapleCharacter player = c.getPlayer();
        if (splitted[0].equals("!lowhp")) {
            player.setHp(1);
            player.updateSingleStat(MapleStat.HP, 1);
        } else if (splitted[0].equalsIgnoreCase("!checkid")) {
            NPCScriptManager npc = NPCScriptManager.getInstance();
            npc.start(player.getClient(), 9201062);
        } else if (splitted[0].equals("!giveitem")) { //works offline
            if (splitted.length == 5) {

                int type = 0;
                if (splitted[4].equals("equip")) {
                    type = 1;
                } else if (splitted[4].equals("use")) {
                    type = 2;
                } else if (splitted[4].equals("setup")) {
                    type = 3;
                } else if (splitted[4].equals("etc")) {
                    type = 4;
                } else if (splitted[4].equals("cash")) {
                    type = 5;
                } else {
                    mc.dropMessage("Invalid inventory type");
                    return;
                }

                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("select `position` from inventoryitems where inventorytype = " + type + " && characterid = (select id from characters where name = \"" + splitted[1] + "\") order by `position` desc limit 1");
                ResultSet rs = ps.executeQuery();
                rs.first();

                int next_free_slot = rs.getInt("position");
                next_free_slot++;

                ps = DatabaseConnection.getConnection().prepareStatement("SELECT id FROM characters where name = \"" + splitted[1].toString() + "\"");
                rs = ps.executeQuery();
                rs.first();

                ps = DatabaseConnection.getConnection().prepareStatement("insert into inventoryitems (`characterid`,`itemid`,`inventorytype`,`position`,`quantity`,`owner`,`petid`) VALUES (?,?,?,?,?,?,?)");

                ps.setInt(1, rs.getInt("id"));
                ps.setInt(2, Integer.parseInt(splitted[2].toString()));
                ps.setInt(3, type);
                ps.setInt(4, next_free_slot);
                ps.setInt(5, Integer.parseInt(splitted[3].toString()));
                ps.setString(6, "");
                ps.setInt(7, -1);
                ps.execute();
            } else {
                mc.dropMessage("Syntax: !giveitem <charactername> <itemid> <quantity> <equip/use/setup/etc/cash>");
            }

        } else if (splitted[0].equals("!addhideout")) {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("insert into hideout_maps VALUES(" + splitted[1] + ");");
            ps.execute();

        } else if (splitted[0].equals("!sp")) {
            if (splitted.length != 2) {
                return;
            }
            int sp;
            try {
                sp = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException asd) {
                return;
            }
            player.setRemainingSp(sp + player.getRemainingSp());
            player.updateSingleStat(MapleStat.AVAILABLESP, player.getRemainingSp());
        } else if (splitted[0].equals("!ap")) {
            if (splitted.length != 2) {
                return;
            }
            int ap;
            try {
                ap = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException asd) {
                return;
            }
            player.setRemainingAp(ap + player.getRemainingAp());
            player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
        } else if (splitted[0].equals("!setjob")) {
            if (splitted.length == 2) {
                int job;
                try {
                    job = Integer.parseInt(splitted[1]);
                } catch (NumberFormatException nfe) {
                    mc.dropMessage("Invalid Job ID");
                    return;
                }
                if (MapleJob.getById(job) != null) {
                    player.setJob(job);
                    mc.dropMessage("Changed job to " + MapleJob.getJobName(job));
                } else {
                    mc.dropMessage("Invalid Job ID");
                }
            } else {
                mc.dropMessage("Syntax: !setjob <jobid>");
            }
            /*} else if (splitted[0].equals("!skillrape")) {
            if (splitted.length == 1) {
            for (MapleCharacter chars : player.getMap().getCharacters()) {
            if (!chars.isGM()) {
            chars.disableSkills();
            chars.dropMessage(player.getName() + " has just disabled your skills. Use @maxskills to get them back once you leave the map.");
            }
            }
            } else if (splitted.length == 2) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
            victim.disableSkills();
            mc.dropMessage("You just removed all of " + victim.getName() + "'s skills.");
            victim.dropMessage(player.getName() + " has just disabled your skills. Use @maxskills to get them back once you leave the map.");
            } else {
            mc.dropMessage("Player not online or not in the same channel as you.");
            }
            } else {
            mc.dropMessage("Syntax : !skillrape <ign> / !skillrape (map)");
            }*/
        } else if (splitted[0].equalsIgnoreCase("!maptimer")) {
            if (splitted.length == 2) {
                if (splitted[1].equalsIgnoreCase("clear")) {
                    player.getMap().clearMapTimer();
                } else {
                    if (!player.getMap().isMapTimerOn()) {
                        int time = Integer.parseInt(splitted[1]);
                        player.getMap().addMapTimer(time);
                        player.getMap().broadcastMessage(MaplePacketCreator.getClock(time));
                        mc.dropMessage("Map timer created with " + time + " seconds");
                    } else {
                        player.dropMessage(1, "There already is a map timer in place here. Use !maptimer <clear> to clear the current timer.");
                    }
                }
            } else if (splitted.length == 3) {
                if (!player.getMap().isMapTimerOn()) {
                    int time = Integer.parseInt(splitted[1]);
                    int map = Integer.parseInt(splitted[2]);
                    player.getMap().addMapTimer(time, map);
                    player.getMap().broadcastMessage(MaplePacketCreator.getClock(time));
                    mc.dropMessage("Map timer created with " + time + " seconds and " + map + " as the warping map");
                } else {
                    player.dropMessage(1, "There already is a map timer in place here. Use !maptimer <clear> to clear the current timer");
                }
            } else {
                mc.dropMessage("Syntax : !maptimer <time> <map-to-warp-to> / !maptimer <clear>");
            }
        } else if (splitted[0].equals("!hidechat")) {
            mc.dropMessage(player.hideChat() ? "Hide chat is toggled on" : "Hide chat is toggled off");
        } else if (splitted[0].equals("!whereami")) {
            mc.dropMessage("You are on map " + player.getMap().getId());
        } else if (splitted[0].equals("!openshop")) {
            if (splitted.length != 2) {
                return;
            }
            int shopid;
            try {
                shopid = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException asd) {
                return;
            }
            MapleShopFactory.getInstance().getShop(shopid).sendShop(c);
        } else if (splitted[0].equalsIgnoreCase("!move")) {
            if (splitted.length != 3) {
                mc.dropMessage("Syntax: !move <target1> (to) <target2>");
            } else {
                MapleCharacter victim2 = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                MapleCharacter victim1 = cserv.getPlayerStorage().getCharacterByName(splitted[2]);
                if (victim1 == null || victim2 == null) {
                    if (victim1 == null && victim2 == null) {
                        mc.dropMessage("Neither " + splitted[1] + " or " + splitted[2] + " are online");
                    } else {
                        mc.dropMessage((victim1 == null ? splitted[2] : splitted[1]) + " is offline");
                    }
                } else {
                    MapleMap target = victim1.getMap();
                    victim2.changeMap(target, target.findClosestSpawnpoint(victim1.getPosition()));
                }
            }
        } else if (splitted[0].equals("!opennpc")) {
            if (splitted.length != 2) {
                return;
            }
            int npcid;
            try {
                npcid = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException asd) {
                return;
            }
            MapleNPC npc = MapleLifeFactory.getNPC(npcid);
            if (npc != null && !npc.getName().equalsIgnoreCase("MISSINGNO")) {
                NPCScriptManager.getInstance().start(c, npcid);
            } else {
                mc.dropMessage("UNKNOWN NPC");
            }
        } else if (splitted[0].equals("!levelup")) {
            player.levelUp();
            player.setExp(0);
            player.updateSingleStat(MapleStat.EXP, 0);
        } else if (splitted[0].equals("!setmaxmp")) {
            if (splitted.length != 2) {
                return;
            }
            int amt;
            try {
                amt = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException asd) {
                return;
            }
            player.setMaxMp(amt);
            player.updateSingleStat(MapleStat.MAXMP, player.getMaxMp());
        } else if (splitted[0].equals("!setmaxhp")) {
            if (splitted.length != 2) {
                return;
            }
            int amt;
            try {
                amt = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException asd) {
                return;
            }
            player.setMaxHp(amt);
            player.updateSingleStat(MapleStat.MAXHP, player.getMaxHp());
        } else if (splitted[0].equalsIgnoreCase("!organize")) {
            for (MapleCharacter mch : player.getMap().getCharacters()) {
                MapleMap map = player.getMap();
                if (mch != null && mch != player) {
                    mch.changeMap(map, map.getPortal(0));
                    mch.setHp(0);
                    mch.updateSingleStat(MapleStat.HP, 0);
                }
            }
        } else if (splitted[0].equals("!itemvac")) {
            List<MapleMapObject> items = player.getMap().getMapObjectsInRange(player.getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.ITEM));
            for (MapleMapObject item : items) {
                MapleMapItem mapItem = (MapleMapItem) item;
                if (mapItem.getMeso() > 0) {
                    player.gainMeso(mapItem.getMeso(), true);
                } else if (mapItem.getItem().getItemId() >= 5000000 && mapItem.getItem().getItemId() <= 5000100) {
                    int petId = MaplePet.createPet(mapItem.getItem().getItemId());
                    if (petId == -1) {
                        return;
                    }
                    MapleInventoryManipulator.addById(c, mapItem.getItem().getItemId(), mapItem.getItem().getQuantity(), null, petId);
                } else {
                    MapleInventoryManipulator.addFromDrop(c, mapItem.getItem(), true);
                }
                mapItem.setPickedUp(true);
                player.getMap().removeMapObject(item); // just incase ?
                player.getMap().broadcastMessage(MaplePacketCreator.removeItemFromMap(mapItem.getObjectId(), 2, player.getId()), mapItem.getPosition());
            }
        } else if (splitted[0].equals("!rare") || splitted[0].equals("!addrare")) {
            if (splitted.length > 1) {
                MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                int itemid = -1;
                try {
                    itemid = Integer.parseInt(splitted[1]);
                } catch (NumberFormatException nfo) {
                    mc.dropMessage("Invalid itemid");
                    return;
                }
                int gacha = 0;
                if (splitted.length == 3) {
                    try {
                        gacha = Integer.parseInt(splitted[2]);
                    } catch (NumberFormatException nfo) {
                        mc.dropMessage("Invalid amount for gacha. <0 = false, 1 = true>");
                        return;
                    }
                }
                String name = ii.getName(itemid);
                if (name == null) {
                    mc.dropMessage("Non existent item");
                    return;
                }
                if (gacha < 0 || gacha > 1) {
                    mc.dropMessage("Invalid amount for gacha <0 = false, 1 = true>");
                    return;
                }
                if (MapleItemInformationProvider.isRare(itemid)) {
                    mc.dropMessage("'" + name + "' is already in the rare list");
                    return;
                }
                try {
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps_temp = con.prepareStatement("SELECT * FROM rares order by id desc limit 1");
                    ResultSet rs = ps_temp.executeQuery();
                    rs.first();
                    int id = rs.getInt("id");
                    id++;

                    PreparedStatement ps = con.prepareStatement("INSERT INTO rares (`id`, `itemid` , `gacha`) VALUES (?,?,?)");

                    ps.setInt(1, id);
                    ps.setInt(2, itemid);
                    ps.setInt(3, gacha);
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    mc.dropMessage("SQL exception");
                    e.printStackTrace();
                    return;
                }
                mc.dropMessage("'" + name + "' was added to the" + (gacha == 1 ? " gachapon" : "") + " rare list");
            } else {
                mc.dropMessage("Syntax : !rare <itemd> // !rare <itemid> <1> (Gacha)");
            }
        } else if (splitted[0].equals("!drare") || splitted[0].equals("!deleterare")) {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            if (splitted.length == 2) {
                int itemid = -1;
                try {
                    itemid = Integer.parseInt(splitted[1]);
                } catch (NumberFormatException nfe) {
                    mc.dropMessage("Invalid itemid");
                    return;
                }
                String name = ii.getName(itemid);
                if (name == null) {
                    mc.dropMessage("Non existent item");
                    return;
                }
                if (!MapleItemInformationProvider.isRare(itemid)) {
                    mc.dropMessage(name + " is not in the rare list");
                }
                try {
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("DELETE FROM `rares` WHERE `itemid` = ?");
                    ps.setInt(1, itemid);
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    mc.dropMessage("SQL Exception");
                    e.printStackTrace();
                    return;
                }
                mc.dropMessage("'" + name + "' was removed from the rare list");
            } else {
                mc.dropMessage("Syntax : !drare <itemid>");
            }
        } else if (splitted[0].equals("!crare") || splitted[0].equals("!checkrare")) {

            if (splitted.length == 2) {
                int itemid = -1;
                try {
                    itemid = Integer.parseInt(splitted[1]);
                } catch (NumberFormatException nfe) {
                    mc.dropMessage("Invalid itemid");
                }
                MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                String name = ii.getName(itemid);
                if (name == null) {
                    mc.dropMessage("Non existent item");
                    return;
                } else {
                    mc.dropMessage("'" + name + "' is " + (MapleItemInformationProvider.isRare(itemid) ? "" : "not ") + "rare");
                }
            } else {
                mc.dropMessage("Syntax: !crare <itemid>");
            }
        } else if (splitted[0].equals("!grare") || splitted[0].equals("!gacharare")) {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            if (splitted.length > 1) {
                int itemid = -1;
                int gacha = 1;
                try {
                    itemid = Integer.parseInt(splitted[1]);
                } catch (NumberFormatException nfo) {
                    mc.dropMessage("Invalid itemid");
                    return;
                }
                if (splitted.length == 3) {
                    try {
                        gacha = Integer.parseInt(splitted[2]);
                    } catch (NumberFormatException nfo) {
                        mc.dropMessage("Invalid amount for gacha. <0 = false, 1 = true>");
                        return;
                    }
                }
                if (gacha < 0 || gacha > 1) {
                    mc.dropMessage("Invalid amount for gacha. <0 = false, 1 = true>");
                    return;
                }
                String name = ii.getName(itemid);
                if (name == null) {
                    mc.dropMessage("Non existent item");
                    return;
                }
                if (!MapleItemInformationProvider.isRare(itemid)) {
                    mc.dropMessage("'" + name + "' is not in the rare list");
                    return;
                }
                try {
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("UPDATE `rares` SET `gacha` = ? WHERE `itemid` = ?");
                    ps.setInt(1, gacha);
                    ps.setInt(2, itemid);
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException sqle) {
                    mc.dropMessage("SQL Exception");
                    sqle.printStackTrace();
                    return;
                }
                mc.dropMessage("'" + name + "' was " + (gacha == 1 ? "added to" : "removed from") + " the gachapon list");
            } else {
                mc.dropMessage("!grare <itemid> / !grare <itemid> <0>");
            }

        } else if (splitted[0].equals("!rrare") || splitted[0].equals("!randomrare")) {
            if (splitted.length > 0 && splitted.length < 3) {
                int option = 2;
                if (splitted.length == 2) {
                    try {
                        option = Integer.parseInt(splitted[1]);
                    } catch (NumberFormatException nfe) {
                        mc.dropMessage("Syntax: !rrare <0 (not gacha), 1 (gacha), 2 (both)>");
                        return;
                    }
                }
                int itemid = MapleItemInformationProvider.randomRare(option);
                if (itemid != -1) {
                    if (player.gainItem(itemid)) {
                        mc.dropMessage("Random rare item given");
                    } else {
                        mc.dropMessage("You do not have enough space in your inventory");
                    }
                } else {
                    mc.dropMessage("Invalid option");
                }
            } else {
                mc.dropMessage("Syntax: !rrare <0 (not gacha), 1 (gacha), 2 (both)>");
            }
        } else if (splitted[0].equals("!shopitem")) {
            if (splitted.length > 2 && splitted.length < 5) {
                MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                int shopid = Integer.parseInt(splitted[1]);
                int itemid = Integer.parseInt(splitted[2]);
                int price = 1;
                if (splitted.length == 4) {
                    price = Integer.parseInt(splitted[3]);
                }
                if (price < 1 || price > Integer.MAX_VALUE) {
                    mc.dropMessage("Invalid price.");
                    return;
                }
                String name = ii.getName(itemid);
                if (name == null) {
                    mc.dropMessage("Non existent item");
                    return;
                }
                if (!MapleShopFactory.getInstance().hasShop(shopid)) {
                    mc.dropMessage("Non existent shop.");
                    return;
                }
                if (MapleShopFactory.getInstance().getShop(shopid).hasItem(itemid)) {
                    mc.dropMessage("Item already added.");
                    return;
                }
                try {
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("INSERT INTO shopitems (shopid, itemid, price) VALUES (?,?,?)");
                    ps.setInt(1, shopid);
                    ps.setInt(2, itemid);
                    ps.setInt(3, price);
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    mc.dropMessage("SQL Exception");
                    return;
                }
                MapleShopFactory.getInstance().reloadShop(shopid);
                mc.dropMessage("Shop item added!");
            } else {
                mc.dropMessage("Syntax : !shopitem <shopid> <itemid> (OPT) <price>");
            }
        } else if (splitted[0].equals("!reloadshop")) {
            if (splitted.length == 2) {
                int shopid = -1;
                try {
                    shopid = Integer.parseInt(splitted[1]);
                } catch (NumberFormatException nfe) {
                    mc.dropMessage("Invalid ShopID");
                    return;
                }
                if (!MapleShopFactory.getInstance().hasShop(shopid)) {
                    mc.dropMessage("Non existent shop");
                } else {
                    MapleShopFactory.getInstance().reloadShop(shopid);
                    mc.dropMessage("Shop " + shopid + " reloaded");
                }
            } else {
                mc.dropMessage("Syntax: !reloadshop <shopid>");
            }
        } else if (splitted[0].equals("!dshopitem")) {
            if (splitted.length == 3) {
                MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                int shopid = Integer.parseInt(splitted[1]);
                int itemid = Integer.parseInt(splitted[2]);

                String name = ii.getName(itemid);
                if (name == null) {
                    mc.dropMessage("Non existent item");
                    return;
                }
                if (!MapleShopFactory.getInstance().hasShop(shopid)) {
                    mc.dropMessage("Non existent shop");
                    return;
                }
                if (!MapleShopFactory.getInstance().getShop(shopid).hasItem(itemid)) {
                    mc.dropMessage("Item not in shop");
                    return;
                }
                try {
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("DELETE FROM shopitems WHERE shopid = ? AND itemid = ?");
                    ps.setInt(1, shopid);
                    ps.setInt(2, itemid);
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    mc.dropMessage("SQL Exception");
                    return;
                }
                mc.dropMessage("Shop item deleted!");
                MapleShopFactory.getInstance().reloadShop(shopid);
            } else {
                mc.dropMessage("Syntax : !shopitem <shopid> <itemid>");
            }
        } else if (splitted[0].equals("!item")) {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            if (splitted.length < 2) {
                return;
            }
            int item;
            short quantity = (short) getOptionalIntArg(splitted, 2, 1);
            try {
                item = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException e) {
                mc.dropMessage("Error while making item.");
                return;
            }
            if (item >= 5000000 && item <= 5000100) {
                if (quantity > 1) {
                    quantity = 1;
                }
                int petId = MaplePet.createPet(item);
                MapleInventoryManipulator.addById(c, item, quantity, player.getName(), petId);
            } else if (ii.getInventoryType(item).equals(MapleInventoryType.EQUIP) && !ii.isThrowingStar(ii.getEquipById(item).getItemId()) && !ii.isBullet(ii.getEquipById(item).getItemId())) {
                MapleInventoryManipulator.addFromDrop(c, ii.randomizeStats(c, (Equip) ii.getEquipById(item)), true, player.getName(), false);
            } else {
                MapleInventoryManipulator.addById(c, item, quantity, player.getName());
            }
        } else if (splitted[0].equals("!noname")) {
            if (splitted.length < 2) {
                return;
            }
            int quantity = getOptionalIntArg(splitted, 2, 1);
            int item;
            try {
                item = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException asd) {
                return;
            }
            MapleInventoryManipulator.addById(c, item, (short) quantity);
        } else if (splitted[0].equals("!dropmesos")) {
            if (splitted.length < 2) {
                return;
            }
            int amt;
            try {
                amt = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException asd) {
                return;
            }
            player.getMap().spawnMesoDrop(amt, amt, player.getPosition(), player, player, false);
        } else if (splitted[0].equals("!level")) {
            if (splitted.length != 2) {
                return;
            }
            int level;
            try {
                level = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException asd) {
                return;
            }
            player.setLevel(level - 1);
            player.levelUp();
            player.setExp(0);
            player.updateSingleStat(MapleStat.EXP, 0);
        } else if (splitted[0].equals("!banreason")) {
            if (splitted.length != 2) {
                return;
            }
            mc.dropMessage(getBannedReason(splitted[1]));
        } else if (splitted[0].equals("!joinguild")) {
            if (splitted.length != 2) {
                return;
            }
            Connection con = DatabaseConnection.getConnection();
            try {
                PreparedStatement ps = con.prepareStatement("SELECT guildid FROM guilds WHERE name = ?");
                ps.setString(1, splitted[1]);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    if (player.getGuildId() > 0) {
                        try {
                            cserv.getWorldInterface().leaveGuild(player.getMGC());
                        } catch (java.rmi.RemoteException re) {
                            c.getSession().write(MaplePacketCreator.serverNotice(5, "Unable to connect to the World Server. Please try again later."));
                            return;
                        }
                        c.getSession().write(MaplePacketCreator.showGuildInfo(null));

                        player.setGuildId(0);
                        player.saveGuildStatus();
                    }
                    player.setGuildId(rs.getInt("guildid"));
                    player.setGuildRank(2); // Jr.master :D
                    try {
                        cserv.getWorldInterface().addGuildMember(player.getMGC());
                    } catch (RemoteException e) {
                        cserv.reconnectWorld();
                    }
                    c.getSession().write(MaplePacketCreator.showGuildInfo(player));
                    player.getMap().broadcastMessage(player, MaplePacketCreator.removePlayerFromMap(player.getId()), false);
                    player.getMap().broadcastMessage(player, MaplePacketCreator.spawnPlayerMapobject(player), false);
                    if (player.getNoPets() > 0) {
                        for (MaplePet pet : player.getPets()) {
                            player.getMap().broadcastMessage(player, MaplePacketCreator.showPet(player, pet, false, false), false);
                        }
                    }
                    player.saveGuildStatus();
                } else {
                    mc.dropMessage("Guild name does not exist.");
                }
                rs.close();
                ps.close();
            } catch (SQLException e) {
                return;
            }
        } else if (splitted[0].equals("!unbuffmap")) {
            for (MapleCharacter mapchar : player.getMap().getCharacters()) {
                if (mapchar != null && mapchar != player && !mapchar.isGM()) {
                    mapchar.cancelAllBuffs();
                }
            }
        } else if (splitted[0].equals("!mesos")) {
            if (splitted.length != 2) {
                return;
            }
            int meso;
            try {
                meso = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException ex) {
                return;
            }
            player.setMeso(meso);
        } else if (splitted[0].equalsIgnoreCase("!gm")) {
            c.getPlayer().changeJob(MapleJob.getById(900));
        } else if (splitted[0].equalsIgnoreCase("!sgm")) {
            c.getPlayer().changeJob(MapleJob.getById(910));
        } else if (splitted[0].equals("!ariantpq")) {
            if (splitted.length < 2) {
                player.getMap().AriantPQStart();
            } else {
                c.getSession().write(MaplePacketCreator.updateAriantPQRanking(splitted[1], 5, false));
            }
        } else if (splitted[0].equals("!scoreboard")) {
            player.getMap().broadcastMessage(MaplePacketCreator.showAriantScoreBoard());
        } else if (splitted[0].equals("!array")) {
            if (splitted.length >= 2) {
                if (splitted[1].equalsIgnoreCase("*CLEAR")) {
                    cserv.setArrayString("");
                    mc.dropMessage("Array flushed.");
                } else {
                    cserv.setArrayString(cserv.getArrayString() + StringUtil.joinStringFrom(splitted, 1));
                    mc.dropMessage("Added " + StringUtil.joinStringFrom(splitted, 1) + " to the array. Use !array to check.");
                }
            } else {
                mc.dropMessage("Array: " + cserv.getArrayString());
            }
        } else if (splitted[0].equals("!rreactor")) {
            player.getMap().resetReactors();
        } else if (splitted[0].equals("!nxslimes")) {
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9400202), player.getPosition());
            }
        } else if (splitted[0].equals("!horntail")) {
            MapleMonster ht = MapleLifeFactory.getMonster(8810026);
            player.getMap().spawnMonsterOnGroudBelow(ht, player.getPosition());
            player.getMap().killMonster(ht, player, false);
            player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(0, "As the cave shakes and rattles, here comes Horntail."));
        } else if (splitted[0].equals("!killall")) {
            String mapMessage = "";
            MapleMap map = player.getMap();
            double range = Double.POSITIVE_INFINITY;
            if (splitted.length > 1) {
                int irange = Integer.parseInt(splitted[1]);
                if (splitted.length <= 2) {
                    range = irange * irange;
                } else {
                    map = cserv.getMapFactory().getMap(Integer.parseInt(splitted[2]));
                    mapMessage = " in " + map.getStreetName() + " : " + map.getMapName();
                }
            }
            List<MapleMapObject> monsters = map.getMapObjectsInRange(player.getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER));
            for (MapleMapObject monstermo : monsters) {
                MapleMonster monster = (MapleMonster) monstermo;
                map.killMonster(monster, player, false);
            }
            mc.dropMessage("Killed " + monsters.size() + " monsters" + mapMessage + ".");
        } else if (splitted[0].equals("!gmhelp") || splitted[0].equals("!sgmhelp") || splitted[0].equals("!internhelp") || splitted[0].equals("!donatorhelp") || splitted[0].equals("!adminhelp")) {
            int page = CommandProcessor.getOptionalIntArg(splitted, 1, 1);
            CommandProcessor.getInstance().dropHelp(c.getPlayer(), mc, page);

        } else if (splitted[0].equals("!gender")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                victim.setGender(victim.getGender() == 1 ? 0 : 1);
                victim.getClient().getSession().write(MaplePacketCreator.getCharInfo(victim));
                victim.getMap().removePlayer(victim);
                victim.getMap().addPlayer(victim);
            } else {
                mc.dropMessage("Player is not on.");
            }

        } else if (splitted[0].equals("!levelperson")) {
            if (splitted.length == 3) {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);

                victim.setLevel(Integer.parseInt(splitted[2]) - 1);

                victim.levelUp();
                victim.setExp(0);
                victim.updateSingleStat(MapleStat.EXP, 0);
            } else {
                mc.dropMessage("Syntax : !levelperson <ign> <level>");
            }
        } else if (splitted[0].equals("!skill")) {
            int skill;
            try {
                skill = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException asd) {
                return;
            }
            int maxlevel = SkillFactory.getSkill(skill).getMaxLevel();
            int level = getOptionalIntArg(splitted, 2, maxlevel);
            int masterlevel = getOptionalIntArg(splitted, 3, maxlevel);
            if (splitted.length == 4) {
                player.changeSkillLevel(SkillFactory.getSkill(skill), level, masterlevel);
            } else if (splitted.length == 5) {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[4]);
                if (victim != null) {
                    victim.changeSkillLevel(SkillFactory.getSkill(skill), level, masterlevel);
                } else {
                    mc.dropMessage("Victim was not found.");
                }
            }
        } else if (splitted[0].equals("!setall")) {
            int max;
            try {
                max = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException asda) {
                return;
            }
            player.setStr(max);
            player.setDex(max);
            player.setInt(max);
            player.setLuk(max);
            player.updateSingleStat(MapleStat.STR, max);
            player.updateSingleStat(MapleStat.DEX, max);
            player.updateSingleStat(MapleStat.INT, max);
            player.updateSingleStat(MapleStat.LUK, max);
        } else if (splitted[0].equals("!giftnx")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                int amount;
                try {
                    amount = Integer.parseInt(splitted[2]);
                } catch (NumberFormatException ex) {
                    return;
                }
                int type = getOptionalIntArg(splitted, 3, 1);
                victim.modifyCSPoints(type, amount);
                victim.dropMessage(5, player.getName() + " has gifted you " + amount + " NX points.");
                mc.dropMessage("NX recieved.");
            } else {
                mc.dropMessage("Player not found.");
            }
        } else if (splitted[0].equals("!givegp")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                int amount;
                try {
                    amount = Integer.parseInt(splitted[2]);
                } catch (NumberFormatException ex) {
                    return;
                }
                int gp = c.getPlayer().getGuild().getGP();
                victim.gainGP(gp + amount);
                victim.dropMessage(5, player.getName() + " has given your guild " + amount + " GP. You are in charge to let them know.");
                mc.dropMessage("GP Given.");
            } else {
                mc.dropMessage("Player not found. Try someone else from that guild.");
            }
        } else if (splitted[0].equals("!maxskills")) {
            player.maxAllSkills();
        } else if (splitted[0].equals("!fame")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                victim.setFame(getOptionalIntArg(splitted, 2, 1));
                victim.updateSingleStat(MapleStat.FAME, victim.getFame());
            } else {
                mc.dropMessage("Player not found");
            }
        } else if (splitted[0].equals("!unhide")) {
            if (splitted.length == 1) {
                player.dispelSkill(9101004);
                player.dispelDebuffs();
                mc.dropMessage("Unhid yourself");
            } else if (splitted.length == 2) {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                if (victim != null && victim.getGMLevel() < player.getGMLevel()) {
                    victim.dispelSkill(9101004);
                    player.dispelDebuffs();
                    mc.dropMessage("Unhid " + victim.getName());
                } else {
                    mc.dropMessage("Player not found");
                }
            } else {
                mc.dropMessage("Syntax : !unhide <ign> OR !unhide");
            }
        } else if (splitted[0].equals("!sendcustomhint")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                int l = Integer.parseInt(splitted[2]);
                int w = Integer.parseInt(splitted[3]);
                String message = StringUtil.joinStringFrom(splitted, 4);
                if (message.length() > 0) {
                    victim.getClient().getSession().write(MaplePacketCreator.sendHint(message, l, w));
                } else {
                    mc.dropMessage("Please type something");
                }
            } else {
                mc.dropMessage("Player not found");
            }
        } else if (splitted[0].equals("!sendhint")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                String message = StringUtil.joinStringFrom(splitted, 2);
                if (message.length() > 0) {
                    victim.getClient().getSession().write(MaplePacketCreator.sendHint(message, 0, 0));
                } else {
                    mc.dropMessage("Please type something");
                }
            } else {
                mc.dropMessage("Player not found");
            }
        } else if (splitted[0].equals("!smega")) {
            if (splitted.length > 3) {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                if (victim != null) {
                    String type = splitted[2];
                    String text = StringUtil.joinStringFrom(splitted, 3);
                    int itemID = 5390002; // default.
                    if (type.equalsIgnoreCase("love")) {
                        itemID = 5390002;
                    } else if (type.equalsIgnoreCase("cloud")) {
                        itemID = 5390001;
                    } else if (type.equalsIgnoreCase("diablo")) {
                        itemID = 5390000;
                    }
                    String[] lines = {"", "", "", ""};

                    if (text.length() > 30) {
                        lines[0] = text.substring(0, 10);
                        lines[1] = text.substring(10, 20);
                        lines[2] = text.substring(20, 30);
                        lines[3] = text.substring(30);
                    } else if (text.length() > 20) {
                        lines[0] = text.substring(0, 10);
                        lines[1] = text.substring(10, 20);
                        lines[2] = text.substring(20);
                    } else if (text.length() > 10) {
                        lines[0] = text.substring(0, 10);
                        lines[1] = text.substring(10);
                    } else if (text.length() <= 10) {
                        lines[0] = text;
                    }
                    LinkedList<String> list = new LinkedList<String>();
                    list.add(lines[0]);
                    list.add(lines[1]);
                    list.add(lines[2]);
                    list.add(lines[3]);

                    try {
                        victim.getClient().getChannelServer().getWorldInterface().broadcastSMega(null, MaplePacketCreator.getAvatarMega(victim, c.getChannel(), itemID, list, false).getBytes());
                    } catch (RemoteException e) {
                        cserv.reconnectWorld();
                    }
                } else {
                    mc.dropMessage("Player not found.");
                }
            } else {
                mc.dropMessage("Syntax: !smega <player> <love/diablo/cloud> text");
            }
        } else if (splitted[0].equals("!mutesmega")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                victim.setCanSmega(!victim.getCanSmega());
                victim.dropMessage(5, "Your smega ability is now " + (victim.getCanSmega() ? "on" : "off"));
                player.dropMessage(6, "Player's smega ability is now set to " + victim.getCanSmega());
            } else {
                mc.dropMessage("Player not found");
            }
        } else if (splitted[0].equals("!pin")) {
            if (splitted[1].equalsIgnoreCase("3452")) {
                player.allow();
            }
        } else if (splitted[0].equals("!givedisease")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            int type;
            if (splitted[2].equalsIgnoreCase("SEAL")) {
                type = 120;
            } else if (splitted[2].equalsIgnoreCase("DARKNESS")) {
                type = 121;
            } else if (splitted[2].equalsIgnoreCase("WEAKEN")) {
                type = 122;
            } else if (splitted[2].equalsIgnoreCase("STUN")) {
                type = 123;
            } else if (splitted[2].equalsIgnoreCase("POISON")) {
                type = 125;
            } else if (splitted[2].equalsIgnoreCase("SEDUCE")) {
                type = 128;
            } else {
                mc.dropMessage("ERROR.");
                return;
            }
            victim.giveDebuff(MapleDisease.getType(type), MobSkillFactory.getMobSkill(type, 1));
        } else if (splitted[0].equals("!dc")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                if (victim.getGMLevel() < player.getGMLevel()) {
                    victim.getClient().disconnect();
                    victim.getClient().getSession().close();
                }
            } else {
                mc.dropMessage(splitted[1] + " is not online");
            }
        } else if (splitted[0].equals("!charinfo")) {
            StringBuilder builder = new StringBuilder();
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim == null) {
                return;
            }
            builder.append(MapleClient.getLogMessage(victim, "")); // Could use null i think ?
            mc.dropMessage(builder.toString());
            builder = new StringBuilder();
            builder.append("Positions: X: ");
            builder.append(victim.getPosition().x);
            builder.append(" Y: ");
            builder.append(victim.getPosition().y);
            builder.append(" | RX0: ");
            builder.append(victim.getPosition().x + 50);
            builder.append(" | RX1: ");
            builder.append(victim.getPosition().x - 50);
            builder.append(" | FH: ");
            builder.append(victim.getMap().getFootholds().findBelow(player.getPosition()).getId());
            mc.dropMessage(builder.toString());
            builder = new StringBuilder();
            builder.append("HP: ");
            builder.append(victim.getHp());
            builder.append("/");
            builder.append(victim.getCurrentMaxHp());
            builder.append(" | MP: ");
            builder.append(victim.getMp());
            builder.append("/");
            builder.append(victim.getCurrentMaxMp());
            builder.append(" | EXP: ");
            builder.append(victim.getExp());
            builder.append(" | In a Party: ");
            builder.append(victim.getParty() != null);
            builder.append(" | In a Trade: ");
            builder.append(victim.getTrade() != null);
            mc.dropMessage(builder.toString());
            builder = new StringBuilder();
            builder.append("Remote Address: ");
            builder.append(victim.getClient().getSession().getRemoteAddress());
            builder.append(" | Macs: ");
            for (String mac : victim.getClient().getMacs()) {
                builder.append(mac);
            }
            mc.dropMessage(builder.toString());
            victim.getClient().dropDebugMessage(mc);
        } else if (splitted[0].equals("!connected")) {
            try {
                Map<Integer, Integer> connected = cserv.getWorldInterface().getConnected();
                StringBuilder conStr = new StringBuilder();
                mc.dropMessage("Connected Clients: ");
                for (int i : connected.keySet()) {
                    if (i == 0) {
                        conStr.append("Total: ");
                        conStr.append(connected.get(i));
                    } else {
                        conStr.append(" Channel ");
                        conStr.append(i);
                        conStr.append(": ");
                        conStr.append(connected.get(i));
                    }
                }
                mc.dropMessage(conStr.toString());
            } catch (RemoteException e) {
                cserv.reconnectWorld();
            }

        } else if (splitted[0].equals("!warp")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                if (splitted.length == 2) {
                    MapleMap target = victim.getMap();
                    player.changeMap(target, target.findClosestSpawnpoint(victim.getPosition()));
                } else {
                    MapleMap target = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(Integer.parseInt(splitted[2]));
                    victim.changeMap(target, target.getPortal(0));
                }
            } else {
                try {
                    victim = player;
                    WorldLocation loc = cserv.getWorldInterface().getLocation(splitted[1]);
                    if (loc != null) {
                        mc.dropMessage("You will be cross-channel warped. This may take a few seconds.");
                        MapleMap target = cserv.getMapFactory().getMap(loc.map);
                        victim.cancelAllBuffs();
                        String ip = cserv.getIP(loc.channel);
                        victim.getMap().removePlayer(victim);
                        victim.setMap(target);
                        String[] socket = ip.split(":");
                        if (victim.getTrade() != null) {
                            MapleTrade.cancelTrade(player);
                        }
                        victim.saveToDB(true, true);
                        if (victim.getCheatTracker() != null) {
                            victim.getCheatTracker().dispose();
                        }
                        ChannelServer.getInstance(c.getChannel()).removePlayer(player);
                        c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION);
                        try {
                            c.getSession().write(MaplePacketCreator.getChannelChange(InetAddress.getByName(socket[0]), Integer.parseInt(socket[1])));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        int map = -1;
                        try {
                            MapleMap target = cserv.getMapFactory().getMap(Integer.parseInt(splitted[1]));
                            if (target != null) {
                                player.changeMap(target, target.getPortal(0));
                            } else {
                                mc.dropMessage("Invalid map id");
                            }
                        } catch (NumberFormatException nfe) {
                            mc.dropMessage("Player not online");
                        }

                    }
                } catch (Exception e) {
                }
            }
        } else if (splitted[0].equals("!addstorageslot")) {
            player.getStorage().gainSlots(1);
        } else if (splitted[0].equals("!warphere")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            MapleMap pmap = player.getMap();
            if (victim != null) {
                if (victim.getGMLevel() < player.getGMLevel()) {
                    if (pmap.isEvent()) {
                        victim.changeMapSpecial(pmap, pmap.findClosestSpawnpoint(player.getPosition()));
                    } else {
                        victim.changeMap(pmap, pmap.findClosestSpawnpoint(player.getPosition()));
                    }
                } else {
                    mc.dropMessage("You can't warp GMs");
                }
            } else {
                try {
                    String name = splitted[1];
                    WorldChannelInterface wci = cserv.getWorldInterface();
                    int channel = wci.find(name);
                    if (channel > -1) {
                        ChannelServer pserv = ChannelServer.getInstance(channel);
                        final MapleCharacter world_victim = pserv.getPlayerStorage().getCharacterByName(name);
                        if (world_victim != null) {
                            if (world_victim.getGMLevel() < player.getGMLevel()) {
                                if (pmap.isEvent()) {
                                    world_victim.changeMapSpecial(pmap, pmap.findClosestSpawnpoint(player.getPosition()));
                                } else {
                                    world_victim.changeMap(pmap, pmap.findClosestSpawnpoint(player.getPosition()));
                                }
                                world_victim.dropMessage(player.getName() + " is warping you across channels. This might take a few seconds");
                                TimerManager.getInstance().schedule(new Runnable() {

                                    public void run() {
                                        ChangeChannelHandler.changeChannel(c.getChannel(), world_victim.getClient());
                                    }
                                }, 1 * 1000);
                            } else {
                                mc.dropMessage("You can't warp GMs");
                            }
                        }
                    } else {
                        mc.dropMessage("Player not online.");
                    }
                } catch (RemoteException e) {
                    cserv.reconnectWorld();
                }
            }
        } else if (splitted[0].equals("!map")) {
            int mapid;
            try {
                mapid = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException nfa) {
                return;
            }
            if (mapid != 980000404 && mapid != 980000603 && mapid != 180000000 && mapid != 0) {
                player.changeMap(mapid, getOptionalIntArg(splitted, 2, 0));
            } else {
                player.dropMessage("You cannot warp to this map.");
            }
        } else if (splitted[0].equals("!warpallhere")) {
            for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                if (mch.getMapId() != player.getMapId()) {
                    mch.changeMap(player.getMap(), player.getPosition());
                }
            }
        } else if (splitted[0].equals("!warpwholeworld")) {
            for (ChannelServer channels : cservs) {
                for (MapleCharacter mch : channels.getPlayerStorage().getAllCharacters()) {
                    if (mch.getClient().getChannel() != c.getChannel()) {
                        ChangeChannelHandler.changeChannel(c.getChannel(), mch.getClient());
                    }
                    if (mch.getMapId() != player.getMapId()) {
                        mch.changeMap(player.getMap(), player.getPosition());
                    }
                }
            }
        } else if (splitted[0].equals("!mesosrate")) { // All these could be so much shorter but cbf.
            int set;
            try {
                set = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException asd) {
                return;
            }
            if (splitted.length > 2) {
                for (ChannelServer channel : cservs) {
                    channel.setMesoRate(set);
                    channel.broadcastPacket(MaplePacketCreator.serverNotice(0, "Meso Rate has been changed to " + set + "x"));
                }
            } else if (splitted.length == 2) {
                cserv.setMesoRate(set);
                cserv.broadcastPacket(MaplePacketCreator.serverNotice(0, "Meso Rate has been changed to " + set + "x"));
            } else {
                mc.dropMessage("Syntax: !mesorate <number>");
            }
        } else if (splitted[0].equals("!droprate")) {
            int set;
            try {
                set = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException asd) {
                return;
            }
            if (splitted.length > 2) {
                for (ChannelServer channel : cservs) {
                    channel.setDropRate(set);
                    channel.broadcastPacket(MaplePacketCreator.serverNotice(0, "Drop Rate has been changed to " + set + "x"));
                }
            } else if (splitted.length == 2) {
                cserv.setDropRate(set);
                cserv.broadcastPacket(MaplePacketCreator.serverNotice(0, "Drop Rate has been changed to " + set + "x"));
            } else {
                mc.dropMessage("Syntax: !droprate <number>");
            }
        } else if (splitted[0].equals("!bossdroprate")) {
            int set;
            try {
                set = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException asd) {
                return;
            }
            if (splitted.length > 2) {
                for (ChannelServer channel : cservs) {
                    channel.setBossDropRate(set);
                    channel.broadcastPacket(MaplePacketCreator.serverNotice(0, "Boss Drop Rate has been changed to " + set + "x"));
                }
            } else if (splitted.length == 2) {
                cserv.setBossDropRate(set);
                cserv.broadcastPacket(MaplePacketCreator.serverNotice(0, "Boss Drop Rate has been changed to " + set + "x"));
            } else {
                mc.dropMessage("Syntax: !bossdroprate <number>");
            }
        } else if (splitted[0].equals("!exprate")) {
            int set;
            try {
                set = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException asd) {
                return;
            }
            if (splitted.length > 2) {
                for (ChannelServer channel : cservs) {
                    channel.setExpRate(set);
                    channel.broadcastPacket(MaplePacketCreator.serverNotice(0, "Exp Rate has been changed to " + set + "x"));
                }
            } else if (splitted.length == 2) {
                cserv.setExpRate(set);
                cserv.broadcastPacket(MaplePacketCreator.serverNotice(0, "Exp Rate has been changed to " + set + "x"));
            } else {
                mc.dropMessage("Syntax: !exprate <number>");
            }
        } else if (splitted[0].equals("!godlyitemrate")) {
            int set;
            try {
                set = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException asd) {
                return;
            }
            if (splitted.length > 2) {
                for (ChannelServer channel : cservs) {
                    channel.setGodlyItemRate((short) set);
                    channel.broadcastPacket(MaplePacketCreator.serverNotice(0, "Godly items will now drop at a " + set + "% rate."));
                }
            } else if (splitted.length == 2) {
                cserv.setGodlyItemRate((short) set);
                cserv.broadcastPacket(MaplePacketCreator.serverNotice(0, "Godly items will now drop at a  " + set + "% rate."));
            } else {
                mc.dropMessage("Syntax: !godlyitemrate <number>");
            }
        } else if (splitted[0].equals("!itemstat")) {
            int set;
            try {
                set = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException asd) {
                return;
            }
            if (splitted.length > 2) {
                for (ChannelServer channel : cservs) {
                    channel.setItemMultiplier((short) set);
                    channel.broadcastPacket(MaplePacketCreator.serverNotice(0, "Item stat multiplier has been changed to " + set + "x"));
                }
            } else if (splitted.length == 2) {
                cserv.setItemMultiplier((short) set);
                cserv.broadcastPacket(MaplePacketCreator.serverNotice(0, "Item stat multiplier has been changed to " + set + "x"));
            } else {
                mc.dropMessage("Syntax: !setItemMultiplier <number>");
            }
        } else if (splitted[0].equals("!togglegodlyitems")) {
            if (splitted.length > 1) {
                for (ChannelServer channel : cservs) {
                    channel.setGodlyItems(!cserv.isGodlyItems());
                    if (channel.isGodlyItems()) {
                        channel.broadcastPacket(MaplePacketCreator.serverNotice(0, "Godly items will now drop at a " + channel.getGodlyItemRate() + "% rate. Items like these will be multiplied by " + channel.getItemMultiplier() + "x each rate."));
                    } else {
                        channel.broadcastPacket(MaplePacketCreator.serverNotice(0, "Godly item drops have been turned off."));
                    }
                }
            } else {
                cserv.setGodlyItems(!cserv.isGodlyItems());
                if (cserv.isGodlyItems()) {
                    cserv.broadcastPacket(MaplePacketCreator.serverNotice(0, "Godly items will now drop at a " + cserv.getGodlyItemRate() + "% rate. Items like these will be multiplied by " + cserv.getItemMultiplier() + "x each rate."));
                } else {
                    cserv.broadcastPacket(MaplePacketCreator.serverNotice(0, "Godly item drops have been turned off."));
                }
            }
        } else if (splitted[0].equals("!servermessage")) {
            String outputMessage = StringUtil.joinStringFrom(splitted, 1);
            if (outputMessage.equalsIgnoreCase("!array")) {
                outputMessage = cserv.getArrayString();
            }
            cserv.setServerMessage(outputMessage);
        } else if (splitted[0].equals("!whosthere")) {
            StringBuilder builder = new StringBuilder();
            mc.dropMessage("Players on Map: ");
            for (MapleCharacter chr : player.getMap().getCharacters()) {
                if (builder.length() > 150) { // wild guess :o
                    mc.dropMessage(builder.toString());
                    builder = new StringBuilder();
                }
                builder.append(MapleCharacterUtil.makeMapleReadable(chr.getName()));
                builder.append(", ");
            }
            player.dropMessage(6, builder.toString());
            player.dropMessage("Total : " + player.getMap().getCharacters().size());
        } else if (splitted[0].equals("!cheaters")) {
            try {
                List<CheaterData> cheaters = cserv.getWorldInterface().getCheaters();
                if (cheaters.size() < 1) {
                    mc.dropMessage("No cheater data to display.");
                } else {
                    for (CheaterData cheater : cheaters) {
                        mc.dropMessage(cheater.getInfo());
                    }
                }
            } catch (RemoteException e) {
                cserv.reconnectWorld();
            }
            /*      } else if (splitted[0].equals("!getrings")) {
            mc.dropMessage("1112800 - clover");
            mc.dropMessage("1112001 - crush");
            mc.dropMessage("1112801 - flower");
            mc.dropMessage("1112802 - Star");
            mc.dropMessage("1112803 - moonstone");
            mc.dropMessage("1112806 - Stargem");
            mc.dropMessage("1112807 - golden");
            mc.dropMessage("1112809 - silverswan");
            } else if (splitted[0].equals("!ring")) {
            Map<String, Integer> rings = new HashMap<String, Integer>();
            rings.put("clover", 1112800);
            rings.put("crush", 1112001);
            rings.put("flower", 1112801);
            rings.put("star", 1112802);
            rings.put("stargem", 1112806);
            rings.put("silverswan", 1112809);
            rings.put("golden", 1112807);
            if (rings.containsKey(splitted[3])) {
            MapleCharacter partner1 = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            MapleCharacter partner2 = cserv.getPlayerStorage().getCharacterByName(splitted[2]);
            int ret = MapleRing.createRing(rings.get(splitted[3]), partner1, partner2);
            switch (ret) {
            case -2:
            mc.dropMessage("Partner number 1 was not found.");
            break;

            case -1:
            mc.dropMessage("Partner number 2 was not found.");
            break;

            case 0:
            mc.dropMessage("Error. One of the players already posesses a ring");
            break;

            default:
            mc.dropMessage("Sucess !");
            }
            } else {
            mc.dropMessage("Ring name was not found.");
            }
            rings.clear();
            } else if (splitted[0].equals("!removering")) {
            MapleCharacter victim = player;
            if (splitted.length == 2) {
            victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            }
            if (victim != null) {
            if (MapleRing.checkRingDB(victim)) {
            MapleRing.removeRingFromDb(victim);
            } else {
            victim.dropMessage("You have no ring..");
            }
            }*/
        } else if (splitted[0].equals("!nearestPortal")) {
            final MaplePortal portal = player.getMap().findClosestSpawnpoint(player.getPosition());
            mc.dropMessage(portal.getName() + " id: " + portal.getId() + " script: " + portal.getScriptName());

        } else if (splitted[0].equals("!spawn")) {
            int mid;
            int num = getOptionalIntArg(splitted, 2, 1);
            try {
                mid = Integer.parseInt(splitted[1]);
                if (MapleLifeFactory.getMonster(mid) == null) {
                    return;
                }
            } catch (NumberFormatException asd) {
                return;
            }
            if (num > 50) {
                mc.dropMessage("Remember that we know what you're doing ;] please dont over summon");
            }
            Integer hp = getNamedIntArg(splitted, 1, "hp");
            Integer exp = getNamedIntArg(splitted, 1, "exp");
            Double php = getNamedDoubleArg(splitted, 1, "php");
            Double pexp = getNamedDoubleArg(splitted, 1, "pexp");
            MapleMonster onemob = MapleLifeFactory.getMonster(mid);
            int newhp = 0;
            int newexp = 0;
            if (hp != null) {
                newhp = hp.intValue();
            } else if (php != null) {
                newhp = (int) (onemob.getMaxHp() * (php.doubleValue() / 100));
            } else {
                newhp = onemob.getMaxHp();
            }
            if (exp != null) {
                newexp = exp.intValue();
            } else if (pexp != null) {
                newexp = (int) (onemob.getExp() * (pexp.doubleValue() / 100));
            } else {
                newexp = onemob.getExp();
            }
            if (newhp < 1) {
                newhp = 1;
            }
            MapleMonsterStats overrideStats = new MapleMonsterStats();
            overrideStats.setHp(newhp);
            overrideStats.setExp(newexp);
            overrideStats.setMp(onemob.getMaxMp());
            if (num > 50) {
                num = 50;
            }
            for (int i = 0; i < num; i++) {
                MapleMonster mob = MapleLifeFactory.getMonster(mid);
                mob.setHp(newhp);
                mob.setOverrideStats(overrideStats);
                player.getMap().spawnMonsterOnGroudBelow(mob, player.getPosition());
            }
        } else if (splitted[0].equals("!unban")) {
            if (MapleCharacter.unban(splitted[1])) {
                mc.dropMessage("Success!");
            } else {
                mc.dropMessage("Error while unbanning.");
            }
        } else if (splitted[0].equalsIgnoreCase("!statedit") || splitted[0].equalsIgnoreCase("!changestats")) {
            if (splitted.length > 2 && splitted.length < 29) {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                if (victim != null) {
                    int str = (getNamedIntArg(splitted, 2, "str") == null ? victim.getStr() : getNamedIntArg(splitted, 2, "str"));
                    int dex = (getNamedIntArg(splitted, 2, "dex") == null ? victim.getDex() : getNamedIntArg(splitted, 2, "dex"));
                    int Int = (getNamedIntArg(splitted, 2, "int") == null ? victim.getInt() : getNamedIntArg(splitted, 2, "int"));
                    int luk = (getNamedIntArg(splitted, 2, "luk") == null ? victim.getLuk() : getNamedIntArg(splitted, 2, "luk"));
                    int hp = (getNamedIntArg(splitted, 2, "hp") == null ? victim.getHp() : getNamedIntArg(splitted, 2, "hp"));
                    int mp = (getNamedIntArg(splitted, 2, "mp") == null ? victim.getMp() : getNamedIntArg(splitted, 2, "mp"));
                    int maxhp = (getNamedIntArg(splitted, 2, "maxhp") == null ? victim.getMaxHp() : getNamedIntArg(splitted, 2, "maxhp"));
                    int maxmp = (getNamedIntArg(splitted, 2, "maxmp") == null ? victim.getMaxMp() : getNamedIntArg(splitted, 2, "maxmp"));
                    int fame = (getNamedIntArg(splitted, 2, "fame") == null ? victim.getFame() : getNamedIntArg(splitted, 2, "fame"));
                    int job = (getNamedIntArg(splitted, 2, "job") == null ? victim.getJob().getId() : getNamedIntArg(splitted, 2, "job"));
                    int meso = (getNamedIntArg(splitted, 2, "meso") == null ? victim.getMeso() : getNamedIntArg(splitted, 2, "meso"));
                    int lvl = (getNamedIntArg(splitted, 2, "lvl") == null ? victim.getLevel() : getNamedIntArg(splitted, 2, "lvl"));
                    int exp = (getNamedIntArg(splitted, 2, "exp") == null ? victim.getExp() : getNamedIntArg(splitted, 2, "exp"));
                    victim.setStr(str);
                    victim.setDex(dex);
                    victim.setInt(Int);
                    victim.setLuk(luk);
                    victim.setHp(hp);
                    victim.setMp(mp);
                    victim.setMaxHp(maxhp);
                    victim.setMaxMp(maxmp);
                    victim.setFame(fame);
                    victim.setJob(job);
                    victim.setMeso(meso);
                    victim.setLevel(lvl);
                    victim.setExp(exp);
                    victim.updateSingleStat(MapleStat.STR, str);
                    victim.updateSingleStat(MapleStat.DEX, dex);
                    victim.updateSingleStat(MapleStat.INT, Int);
                    victim.updateSingleStat(MapleStat.LUK, luk);
                    victim.updateSingleStat(MapleStat.HP, hp);
                    victim.updateSingleStat(MapleStat.MP, mp);
                    victim.updateSingleStat(MapleStat.MAXHP, maxhp);
                    victim.updateSingleStat(MapleStat.MAXMP, maxmp);
                    victim.updateSingleStat(MapleStat.FAME, fame);
                    victim.updateSingleStat(MapleStat.MESO, meso);
                    victim.updateSingleStat(MapleStat.JOB, job);
                    victim.updateSingleStat(MapleStat.LEVEL, lvl);
                    victim.updateSingleStat(MapleStat.EXP, exp);
                    victim.dropMessage("Your stats have been changed by " + player.getName());
                    player.dropMessage("You have changed " + victim.getName() + "'s stats");
                    String update = "New stats: str: " + str + " dex: " + dex + " int: " + Int + " luk: " + luk + " hp: " + hp + " mp: " + mp + " maxhp: " + maxhp + " maxmp: " + maxmp + " fame: " + fame + " meso: " + meso + " job: " + job + " lvl: " + lvl + " exp: " + exp;
                    player.dropMessage(update);
                    victim.dropMessage(update);
                } else {
                    player.dropMessage(victim + " could not be found");
                }
            } else {
                mc.dropMessage("Syntax : !statedit <ign> <str/dex/int/luk/mp/hp/maxhp/maxmp/fame/meso/job/lvl/exp>");
                mc.dropMessage("Example : !statedit noob str 10 dex 10 int 100");
            }
        } else if (splitted[0].equals("!pursue")) {
            if (splitted.length == 2) {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                if (victim != null) {
                    victim.setFollower(player.getId());
                    mc.dropMessage("You started following " + victim.getName());

                }
            } else {
                mc.dropMessage("Syntax: !pursue <ign>");
            }
        } else if (splitted[0].equals("!unpursue")) {
            if (splitted.length == 2) {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                if (victim != null) {
                    victim.setFollower(-1);
                    mc.dropMessage("You stopped following " + victim.getName());
                }
            } else {
                mc.dropMessage("Syntax: !unpursue <ign>");
            }
        } else if (splitted[0].equals("!ban")) {
            String name = splitted[1];
            String oreason = StringUtil.joinStringFrom(splitted, 2);
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(name);
            String readableTargetName = MapleCharacterUtil.makeMapleReadable(name);
            String reason = readableTargetName + " has been banned by " + player.getName() + " for " + oreason;
            if (victim != null) {
                if (!victim.isGM()) {
                    victim.ban(reason, true);
                    try {
                        cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(6, reason).getBytes());
                    } catch (RemoteException e) {
                        cserv.reconnectWorld();
                    }
                } else {
                    mc.dropMessage("You can't ban a GM. Sorry");
                }
            } else {
                if (MapleCharacter.ban(name, reason, false)) {
                    try {
                        cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(6, reason).getBytes());
                    } catch (RemoteException e) {
                        cserv.reconnectWorld();
                    }
                } else {
                    mc.dropMessage("Error while banning.");
                }
            }
        } else if (splitted[0].equals("!accban")) {
            String originalReason = StringUtil.joinStringFrom(splitted, 2);
            String reason = player.getName() + " banned " + splitted[1] + ": " + originalReason;
            MapleCharacter target = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (target != null) {
                if (!target.isGM() || player.getGMLevel() > 3) {
                    String readableTargetName = MapleCharacterUtil.makeMapleReadable(target.getName());
                    String ip = target.getClient().getSession().getRemoteAddress().toString().split(":")[0];
                    reason += "  IP: " + ip;
                    target.ban(reason, false);
                    try {
                        cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(6, readableTargetName + " has been banned by " + player.getName() + " for " + originalReason).getBytes());
                    } catch (RemoteException e) {
                        cserv.reconnectWorld();
                    }
                } else {
                    mc.dropMessage("Please don't ban " + cserv.getServerName() + " GMs");
                }
            } else {
                if (MapleCharacter.ban(splitted[1], reason, false)) {
                    String readableTargetName = MapleCharacterUtil.makeMapleReadable(target.getName());
                    String ip = target.getClient().getSession().getRemoteAddress().toString().split(":")[0];
                    reason += " (IP: " + ip + ")";
                    try {
                        cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(6, readableTargetName + " has been banned by " + player.getName() + " for " + originalReason).getBytes());
                    } catch (RemoteException e) {
                        cserv.reconnectWorld();
                    }
                } else {
                    mc.dropMessage("Failed to ban " + splitted[1]);
                }
            }
        } else if (splitted[0].equals("!tempban")) {
            Calendar tempB = Calendar.getInstance();
            String originalReason = joinAfterString(splitted, ":");
            if (splitted.length < 4 || originalReason == null) {
                mc.dropMessage("Syntax helper: !tempban <name> <i / m / w / d / h> <amount> r <reason id> : <Text Reason>");
                return;
            }
            int yChange = getNamedIntArg(splitted, 1, "y", 0);
            int mChange = getNamedIntArg(splitted, 1, "m", 0);
            int wChange = getNamedIntArg(splitted, 1, "w", 0);
            int dChange = getNamedIntArg(splitted, 1, "d", 0);
            int hChange = getNamedIntArg(splitted, 1, "h", 0);
            int iChange = getNamedIntArg(splitted, 1, "i", 0);
            int gReason = getNamedIntArg(splitted, 1, "r", 7);
            String reason = player.getName() + " tempbanned " + splitted[1] + ": " + originalReason;
            if (gReason > 14) {
                mc.dropMessage("You have entered an incorrect ban reason ID, please try again.");
                return;
            }
            DateFormat df = DateFormat.getInstance();
            tempB.set(tempB.get(Calendar.YEAR) + yChange, tempB.get(Calendar.MONTH) + mChange, tempB.get(Calendar.DATE)
                    + (wChange * 7) + dChange, tempB.get(Calendar.HOUR_OF_DAY) + hChange, tempB.get(Calendar.MINUTE)
                    + iChange);
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim == null) {
                int accId = MapleClient.findAccIdForCharacterName(splitted[1]);
                if (accId >= 0 && MapleCharacter.tempban(reason, tempB, gReason, accId)) {
                    String readableTargetName = MapleCharacterUtil.makeMapleReadable(victim.getName());
                    cserv.broadcastPacket(MaplePacketCreator.serverNotice(6, readableTargetName + " has been banned for " + originalReason));
                } else {
                    mc.dropMessage("There was a problem offline banning character " + splitted[1] + ".");
                }
            } else {
                victim.tempban(reason, tempB, gReason);
                mc.dropMessage("The character " + splitted[1] + " has been successfully tempbanned till " + df.format(tempB.getTime()));
            }
        } else if (splitted[0].equals("!npc")) {
            int npcId;
            try {
                npcId = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException nfe) {
                return;
            }
            MapleNPC npc = MapleLifeFactory.getNPC(npcId);
            if (npc != null && !npc.getName().equalsIgnoreCase("MISSINGNO")) {
                npc.setPosition(player.getPosition());
                npc.setCy(player.getPosition().y);
                npc.setRx0(player.getPosition().x + 50);
                npc.setRx1(player.getPosition().x - 50);
                npc.setFh(player.getMap().getFootholds().findBelow(player.getPosition()).getId());
                npc.setCustom(true);
                player.getMap().addMapObject(npc);
                player.getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc));
            } else {
                mc.dropMessage("You have entered an invalid Npc-Id");
            }

        } else if (splitted[0].equals("!removenpcs")) {
            List<MapleMapObject> npcs = player.getMap().getMapObjectsInRange(player.getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.NPC));
            for (MapleMapObject npcmo : npcs) {
                MapleNPC npc = (MapleNPC) npcmo;
                if (npc.isCustom()) {
                    player.getMap().removeMapObject(npc.getObjectId());
                }
            }
        } else if (splitted[0].equals("!mynpcpos")) {
            Point pos = player.getPosition();
            mc.dropMessage("X: " + pos.x + " | Y: " + pos.y + "  | RX0: " + (pos.x + 50) + " | RX1: " + (pos.x - 50) + " | FH: " + player.getMap().getFootholds().findBelow(pos).getId());
        } else if (splitted[0].equals("!cleardrops")) {
            MapleMap map = player.getMap();
            double range = Double.POSITIVE_INFINITY;
            java.util.List<MapleMapObject> items = map.getMapObjectsInRange(player.getPosition(), range, Arrays.asList(MapleMapObjectType.ITEM));
            for (MapleMapObject itemmo : items) {
                map.removeMapObject(itemmo);
                map.broadcastMessage(MaplePacketCreator.removeItemFromMap(itemmo.getObjectId(), 0, player.getId()));
            }
            mc.dropMessage("You have destroyed " + items.size() + " items on the ground.");
        } else if (splitted[0].equals("!clearshops")) {
            MapleShopFactory.getInstance().clear();
        } else if (splitted[0].equals("!clearevents")) {
            for (ChannelServer instance : ChannelServer.getAllInstances()) {
                instance.reloadEvents();
            }
        } else if (splitted[0].equalsIgnoreCase("!bob")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9400551), player.getPosition());
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.serverNotice(0, "RUN!!! BOB is Back!"));

        } else if (splitted[0].equals("!emote")) {
            String name = splitted[1];
            int emote;
            try {
                emote = Integer.parseInt(splitted[2]);
            } catch (NumberFormatException nfe) {
                return;
            }
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(name);
            if (victim != null) {
                victim.getMap().broadcastMessage(victim, MaplePacketCreator.facialExpression(victim, emote), victim.getPosition());
            } else {
                mc.dropMessage("Player was not found");
            }
        } else if (splitted[0].equals("!proitem")) {
            if (splitted.length == 3) {
                int itemid;
                short multiply;
                try {
                    itemid = Integer.parseInt(splitted[1]);
                    multiply = Short.parseShort(splitted[2]);
                } catch (NumberFormatException asd) {
                    return;
                }
                MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                IItem item = ii.getEquipById(itemid);
                MapleInventoryType type = ii.getInventoryType(itemid);
                if (type.equals(MapleInventoryType.EQUIP)) {
                    MapleInventoryManipulator.addFromDrop(c, ii.hardcoreItem((Equip) item, multiply), true);
                } else {
                    mc.dropMessage("Make sure it's an equippable item.");
                }
            } else {
                mc.dropMessage("Invalid syntax.");
            }
        } else if (splitted[0].equals("!addclones")) {
            if (splitted.length < 2) {
                mc.dropMessage("Syntax: !addclones <1-5>");
            } else {
                int clones;
                try {
                    clones = getOptionalIntArg(splitted, 1, 1);
                } catch (NumberFormatException nfe) {
                    mc.dropMessage("Invalid number of clones");
                    return;
                }
                if (player.getFakeChars().size() >= 5) {
                    mc.dropMessage("You are not allowed to clone yourself over 5 times.");
                } else {
                    for (int i = 0; i < clones && i + player.getFakeChars().size() <= 6; i++) {
                        FakeCharacter fc = new FakeCharacter(player, player.getId() + player.getFakeChars().size() + clones + i);
                        player.getFakeChars().add(fc);
                        c.getChannelServer().addClone(fc);
                    }
                    mc.dropMessage("You have cloned yourself " + player.getFakeChars().size() + " times so far.");
                }
            }
        } else if (splitted[0].equals("!removeclones")) {
            for (FakeCharacter fc : player.getFakeChars()) {
                if (fc.getFakeChar().getMap() == player.getMap()) {
                    c.getChannelServer().getAllClones().remove(fc);
                    player.getMap().removePlayer(fc.getFakeChar());
                }
            }
            player.getFakeChars().clear();
            mc.dropMessage("All your clones in the map removed.");
        } else if (splitted[0].equals("!removeallclones")) {
            for (FakeCharacter fc : c.getChannelServer().getAllClones()) {
                if (fc.getOwner() != null) {
                    fc.getOwner().getFakeChars().remove(fc);
                }
                fc.getFakeChar().getMap().removePlayer(fc.getFakeChar());
            }
            c.getChannelServer().getAllClones().clear();
            mc.dropMessage("ALL clones have been removed.");
        } else if (splitted[0].equals("!follow")) {
            int slot = Integer.parseInt(splitted[1]);
            FakeCharacter fc = player.getFakeChars().get(slot);
            if (fc == null) {
                mc.dropMessage("Clone does not exist.");
            } else {
                fc.setFollow(true);
            }
        } else if (splitted[0].equals("!pause")) {
            int slot = Integer.parseInt(splitted[1]);
            FakeCharacter fc = player.getFakeChars().get(slot);
            if (fc == null) {
                mc.dropMessage("Clone does not exist.");
            } else {
                fc.setFollow(false);
            }
        } else if (splitted[0].equalsIgnoreCase("!fakerelog")) {
            c.getSession().write(MaplePacketCreator.getCharInfo(player));
            player.getMap().removePlayer(player);
            player.getMap().addPlayer(player);
        } else if (splitted[0].equals("!stance")) {
            if (splitted.length == 3) {
                int slot = Integer.parseInt(splitted[1]);
                int stance = Integer.parseInt(splitted[2]);
                player.getFakeChars().get(slot).getFakeChar().setStance(stance);
            }
        } else if (splitted[0].equals("!killmonster")) {
            if (splitted.length == 2) {
                MapleMap map = c.getPlayer().getMap();
                int targetId = Integer.parseInt(splitted[1]);
                List<MapleMapObject> monsters = map.getMapObjectsInRange(c.getPlayer().getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.MONSTER));
                for (MapleMapObject monsterm : monsters) {
                    MapleMonster monster = (MapleMonster) monsterm;
                    if (monster.getId() == targetId) {
                        map.killMonster(monster, player, false);
                        break;
                    }
                }
            }
        } else if (splitted[0].equals("!removeoid")) {
            if (splitted.length == 2) {
                MapleMap map = c.getPlayer().getMap();
                int oid = Integer.parseInt(splitted[1]);
                MapleMapObject obj = map.getMapObject(oid);
                if (obj == null) {
                    mc.dropMessage("This oid does not exist.");
                } else {
                    map.removeMapObject(obj);
                }
            }
        } else if (splitted[0].equals("!textcolor") || splitted[0].equals("!gmtext")) {
            if (splitted.length == 2) {
                Map<String, Integer> text = new HashMap<String, Integer>();
                text.put("normal", 0);
                text.put("orange", 1);
                text.put("pink", 2);
                text.put("purple", 3);
                text.put("green", 4);
                text.put("red", 5);
                text.put("blue", 6);
                text.put("yellow", 9);
                if (player.isGM()) {
                    text.put("lightgreen", 8);
                    text.put("white", 7);
                }
                if (text.containsKey(splitted[1].toLowerCase())) {
                    player.setGMText(text.get(splitted[1].toLowerCase()));
                    mc.dropMessage("Changed your text color to " + splitted[1].toLowerCase());
                } else {
                    mc.dropMessage("Wrong syntax: use !textcolor normal/orange/pink/purple/green/blue/red/yellow" + (c.getPlayer().getGMLevel() > 2 ? "/lightgreen/white" : ""));
                }
            } else {
                mc.dropMessage("Wrong syntax: use !textcolor normal/orange/pink/purple/green/blue/red/yellow" + (c.getPlayer().getGMLevel() > 2 ? "/lightgreen/white" : ""));
            }
        } else if (splitted[0].equals("!currentdate")) {
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH) + 1; // it's an array of months.
            int year = cal.get(Calendar.YEAR);
            mc.dropMessage(day + "/" + month + "/" + year);
        } else if (splitted[0].equals("!maxmesos")) {
            player.gainMeso(Integer.MAX_VALUE - player.getMeso());
        } else if (splitted[0].equals("!fullcharge")) {
            player.setEnergyBar(10000);
            c.getSession().write(MaplePacketCreator.giveEnergyCharge(10000));

        } else if (splitted[0].equals("!speak")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                String text = StringUtil.joinStringFrom(splitted, 2);
                victim.getMap().broadcastMessage(MaplePacketCreator.getChatText(victim.getId(), text, false, 0));
            } else {
                mc.dropMessage("Player not found");
            }
        } else if (splitted[0].equals("!changejob")) {
            //change this command to public if u want.
            int id = c.getPlayer().getId();
            int job = Integer.parseInt(splitted[1]);
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps;
            try {
                ps = con.prepareStatement("select * from jobs where characterid = ? and jobid = ?");
                ps.setInt(1, id);
                ps.setInt(2, job);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    mc.dropMessage("You don't have the following job before you rebirthed.");
                    ps.close();
                }
                job = rs.getInt("jobid");
                c.getPlayer().changeJob(MapleJob.getById(job));
                ps.close();
            } catch (SQLException e) {
                System.out.println("SQL Exception: " + e);
            }
        } else if (splitted[0].equals("!gmmap")) {
            c.getPlayer().changeMap(180000000);
        } else if (splitted[0].equals("!gmshop")) {
            NPCScriptManager.getInstance().start(c, 1002006);
        } else if (splitted[0].equals("!exempt")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                if (!victim.isExempted()) {
                    victim.exempt();
                    mc.dropMessage("You excepted " + victim.getName() + " from the map mute");
                    victim.dropMessage(player.getName() + " has just excepted you from the map mute");
                } else {
                    victim.unExempt();
                    mc.dropMessage("You added " + victim.getName() + " back to the map mute");
                    victim.dropMessage(player.getName() + " has just added you back to the map mute");
                }
            } else {
                mc.dropMessage("Player is not online or not in the same channel as you");
            }
        } else if (splitted[0].equals("!mutemap")) {
            if (!player.getMap().isMuted()) {
                player.exempt();
                player.getMap().mute();
                for (MapleCharacter chars : player.getMap().getCharacters()) {
                    chars.dropMessage(player.getName() + " has just muted the map. You cannot talk here");
                }
            } else {
                player.unExempt();
                player.getMap().unmute();
                for (MapleCharacter chars : player.getMap().getCharacters()) {
                    chars.dropMessage(player.getName() + " has just unmuted the map. You can now talk here");
                }
            }
        } else if (splitted[0].equalsIgnoreCase("!hidemap")) {
            if (player.getGMLevel() > 2) {
                for (MapleCharacter mch : player.getMap().getCharacters()) {
                    SkillFactory.getSkill(9101004).getEffect(1).applyTo(mch);
                }
                mc.dropMessage("You just hid your map!");
            } else if (player.getMap().getId() == 109020001) {
                for (MapleCharacter mch : player.getMap().getCharacters()) {
                    SkillFactory.getSkill(9101004).getEffect(1).applyTo(mch);
                    mc.dropMessage("You just hid the event map!");
                }
            } else {
                mc.dropMessage("You cannot do this here.");
            }
        } else if (splitted[0].equalsIgnoreCase("!removechairs") || splitted[0].equalsIgnoreCase("!removemapchairs")) {
            for (MapleCharacter mch : player.getMap().getCharacters()) {
                mch.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                mch.getMap().broadcastMessage(mch, MaplePacketCreator.showChair(mch.getId(), 0), false);
            }
            mc.dropMessage("Everyone stood up suddenly!");
        } else if (splitted[0].equalsIgnoreCase("!removechair")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                if (player.getGMLevel() >= victim.getGMLevel()) {
                    victim.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                    victim.getMap().broadcastMessage(victim, MaplePacketCreator.showChair(victim.getId(), 0), false);
                    mc.dropMessage("Made " + victim.getName() + " stand up");
                } else {
                    mc.dropMessage("You can't seduce a staff member with a higher staff position than you");
                }
            } else {
                mc.dropMessage("The player you're trying to seduce isn't online or not in the same channel as you");
            }
        } else if (splitted[0].equalsIgnoreCase("!stunmap")) {
            for (MapleCharacter mch : player.getMap().getCharacters()) {
                if (mch.getGMLevel() < 3) {
                    mch.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                    mch.getMap().broadcastMessage(mch, MaplePacketCreator.showChair(mch.getId(), 0), false);
                    mch.giveDebuff(MapleDisease.getType(123), MobSkillFactory.getMobSkill(123, 1));
                }
            }
        } else if (splitted[0].equalsIgnoreCase("!seducemap")) {
            int level = Integer.parseInt(splitted[1]);
            for (MapleCharacter mch : player.getMap().getCharacters()) {
                if (mch.getGMLevel() < 3) {
                    mch.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                    mch.getMap().broadcastMessage(mch, MaplePacketCreator.showChair(mch.getId(), 0), false);
                    mch.giveDebuff(MapleDisease.getType(128), MobSkillFactory.getMobSkill(128, level));
                }
            }
        } else if (splitted[0].equalsIgnoreCase("!seduceperson")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            int level = Integer.parseInt(splitted[2]);
            if (victim != null) {
                if (player.getGMLevel() >= victim.getGMLevel()) {
                    victim.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                    victim.getMap().broadcastMessage(victim, MaplePacketCreator.showChair(victim.getId(), 0), false);
                    victim.giveDebuff(MapleDisease.getType(128), MobSkillFactory.getMobSkill(128, level));
                    mc.dropMessage("Seduced " + victim.getName());
                } else {
                    mc.dropMessage("You can't seduce a staff member with a higher staff position than you");
                }
            } else {
                mc.dropMessage("The player you're trying to seduce isn't online or not in the same channel as you");
            }

        } else if (splitted[0].equalsIgnoreCase("!specitem")) {
            if (splitted.length < 4) {
                mc.dropMessage("Syntax: !specitem <equipid> <str/dex/luk/int/hp/mp/wa/ma/jmp/avd/acc/wd/md/slots> <num>");
            }
            int itemid = 0;
            try {
                itemid = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException nfe) {
                mc.dropMessage("Syntax: !specitem <equipid> <str/dex/luk/int/hp/mp/wa/ma/jmp/spd/avd/acc/wd/md/acc/slots> <num>");
            }
            try {
                MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                if (ii.getInventoryType(itemid).equals(MapleInventoryType.EQUIP)) {
                    Equip eqp = (Equip) ii.getEquipById(itemid);
                    String[] chng = {"str", "dex", "int", "luk", "hp", "mp", "wa", "ma", "jmp", "spd", "avd", "wd", "md", "acc", "tag", "slots"};
                    int counter = 0;
                    for (String stat : chng) {
                        if (getNamedShortArg(splitted, 1, stat) != Short.MIN_VALUE) {
                            switch (counter) {
                                case 0:
                                    eqp.setStr(getNamedShortArg(splitted, 1, stat));
                                    break;
                                case 1:
                                    eqp.setDex(getNamedShortArg(splitted, 1, stat));
                                    break;
                                case 2:
                                    eqp.setInt(getNamedShortArg(splitted, 1, stat));
                                    break;
                                case 3:
                                    eqp.setLuk(getNamedShortArg(splitted, 1, stat));
                                    break;
                                case 4:
                                    eqp.setHp(getNamedShortArg(splitted, 1, stat));
                                    break;
                                case 5:
                                    eqp.setMp(getNamedShortArg(splitted, 1, stat));
                                    break;
                                case 6:
                                    eqp.setWatk(getNamedShortArg(splitted, 1, stat));
                                    break;
                                case 7:
                                    eqp.setMatk(getNamedShortArg(splitted, 1, stat));
                                    break;
                                case 8:
                                    eqp.setJump(getNamedShortArg(splitted, 1, stat));
                                    break;
                                case 9:
                                    eqp.setSpeed(getNamedShortArg(splitted, 1, stat));
                                    break;
                                case 10:
                                    eqp.setAvoid(getNamedShortArg(splitted, 1, stat));
                                    break;
                                case 11:
                                    eqp.setWdef(getNamedShortArg(splitted, 1, stat));
                                    break;
                                case 12:
                                    eqp.setMdef(getNamedShortArg(splitted, 1, stat));
                                    break;
                                case 13:
                                    eqp.setAcc(getNamedShortArg(splitted, 1, stat));
                                    break;
                                case 14:
                                    eqp.setOwner(splitted[2]);
                                    break;
                                case 15:
                                    if (getNamedByteArg(splitted, 1, stat) != Byte.MIN_VALUE) {
                                        eqp.setUpgradeSlots(getNamedByteArg(splitted, 1, stat));
                                    }
                                    break;
                            }
                        }
                        counter++;
                    }
                    MapleInventoryManipulator.addFromDrop(c, (IItem) eqp, true);
                } else {
                    mc.dropMessage("Syntax: !specitem <equipid> <str/dex/luk/int/hp/mp/wa/ma/jmp/spd/avd/acc/wd/md/acc/slots> <num>");

                }
            } catch (Exception e) {
                mc.dropMessage("Syntax: !specitem <equipid> <str/dex/luk/int/hp/mp/wa/ma/jmp/spd/avd/acc/wd/md/acc/slots> <num>");

            }
        } else if (splitted[0].equals("!jobperson")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            int job;
            try {
                job = Integer.parseInt(splitted[2]);
            } catch (NumberFormatException blackness) {
                return;
            }
            if (victim != null) {
                victim.setJob(job);
            } else {
                mc.dropMessage("Player not found");
            }
        } else if (splitted[0].equalsIgnoreCase("!untagworld")) {
            for (ChannelServer cservers : ChannelServer.getAllInstances()) {
                for (MapleCharacter victim : cservers.getPlayerStorage().getAllCharacters()) {
                    if (victim.isTagged()) {
                        victim.setTagged(false);
                    }
                }
            }
        } else if (splitted[0].equalsIgnoreCase("!tag")) {
            if (splitted.length == 2) {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                if (!victim.isTagged()) {
                    victim.setTagged(true);
                    mc.dropMessage(victim.getName() + " can Tag!");
                    victim.dropMessage("You can tag!");
                } else {
                    victim.setTagged(false);
                    mc.dropMessage(victim.getName() + " can't Tag!");
                    victim.dropMessage("You cant tag anymore");
                }
            } else {
                if (!player.isTagged()) {
                    player.setTagged(true);
                    mc.dropMessage("You can Tag!");
                } else {
                    player.setTagged(false);
                    mc.dropMessage("You can't Tag!");
                }
            }
        } else if (splitted[0].equals("!tagitem")) {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            int itemId = Integer.parseInt(splitted[1]);
            short quantity = (short) (short) getOptionalIntArg(splitted, 2, 1);
            IItem toDrop;
            if (ii.getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                toDrop = ii.getEquipById(itemId);
            } else {
                toDrop = new Item(itemId, (byte) 0, (short) quantity);
            }
            toDrop.setOwner(splitted[2]);
            player.getMap().spawnItemDrop(player, player, (Equip) toDrop, player.getPosition(), true, true);
        } else if (splitted[0].equalsIgnoreCase("!drop") || splitted[0].equalsIgnoreCase("!droprandomstatitem")) {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            int itemId = Integer.parseInt(splitted[1]);
            short quantity = (short) getOptionalIntArg(splitted, 2, 1);
            IItem toDrop;
            if (splitted[0].equalsIgnoreCase("!drop")) {
                if (ii.getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                    toDrop = ii.getEquipById(itemId);
                } else {
                    toDrop = new Item(itemId, (byte) 0, quantity);
                }
                toDrop.setOwner(player.getName());
                player.getMap().spawnItemDrop(player, player, toDrop, player.getPosition(), true, true);
            }
        } else if (splitted[0].equalsIgnoreCase("!write")) {
            if (splitted.length > 1) { // Will drop syntax if you use the command by itself
                boolean left = player.isFacingLeft(); // Getting the direction that the player is facing
                String txt = StringUtil.joinStringFrom(splitted, 1); // Getting desired text, from splitted[1] -> splitted[infinite]
                final Map<String, Integer> letters = new HashMap<String, Integer>() {

                    {
                        put("a", 0);// start of defining letter item ids
                        put("b", 1);
                        put("c", 2);
                        put("d", 3);
                        put("e", 4);
                        put("f", 5);
                        put("g", 6);
                        put("h", 7);
                        put("i", 8);
                        put("j", 9);
                        put("k", 10);
                        put("l", 11);
                        put("m", 12);
                        put("n", 13);
                        put("o", 14);
                        put("p", 15);
                        put("q", 16);
                        put("r", 17);
                        put("s", 18);
                        put("t", 19);
                        put("u", 20);
                        put("v", 21);
                        put("w", 22);
                        put("x", 23);
                        put("y", 24);
                        put("z", 25);
                        put("1", -990);
                        put("2", -989);
                        put("3", -988);
                        put("4", -987);
                        put("5", -986);
                        put("6", -985);
                        put("7", -984);
                        put("8", -983);
                        put("9", -982);
                        put("0", -981);
                        put("+", -978);
                        put("-", -977);
                    }
                };
                if (left) { // if the player is facing left
                    int i, len = txt.length(); // start of reversing string method
                    StringBuffer reverse = new StringBuffer(len);
                    for (i = (len - 1); i >= 0; i--) {
                        reverse.append(txt.charAt(i));
                    }
                    txt = reverse.toString(); // end of reversing string method
                }
                Point pos = player.getPosition();

                for (char ch : txt.toCharArray()) { // for each character in the string
                    if (left) { // if player is facing left then the directions of the letters is negative x (<)
                        pos.x -= 30;
                    } else { // if player is facing right then the directions of the letters is positive x (>)
                        pos.x += 30;
                    }
                    if (letters.containsKey(Character.toString(ch).toLowerCase())) { // matching the current iteration of the char list with a letter item id
                        Item toDrop = new Item((letters.get(Character.toString(ch).toLowerCase()) + 3991000), (byte) 0, (short) 1); //letter item id + 3991000 = real number id (faster to type the seven digit number only once)
                        player.getMap().spawnItemDrop(player, player, toDrop, pos, true, true); // drop the item in the current position
                    }
                }
                letters.clear();
            } else {
                mc.dropMessage("Syntax: !write <message>"); // will only run if splitted < 2 (i.e if you use the command by itself)
            }
        } else if (splitted[0].equalsIgnoreCase("!unseducemap")) {
            for (MapleCharacter mch : player.getMap().getCharacters()) {
                mch.dispelDebuff(MapleDisease.SEDUCE);
                mch.dispelDebuffs();
                mch.dispel();
            }
        } else if (splitted[0].equals("!coke")) {
            int[] coke = {9500144, 9500151, 9500152, 9500153, 9500154, 9500143, 9500145, 9500149, 9500147};
            for (int i = 0; i < coke.length; i++) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(coke[i]), player.getPosition());
            }

        } else if (splitted[0].equals("!papu")) {
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8500001), player.getPosition());
            }

        } else if (splitted[0].equals("!zakum")) {
            for (int m = 8800003; m <= 8800010; m++) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(m), player.getPosition());
            }
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8800000), player.getPosition());
            player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(0, "The almighty Zakum has awakened!"));
        } else if (splitted[0].equals("!ergoth")) {
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300028), player.getPosition());
            }
        } else if (splitted[0].equals("!ludimini")) {
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8160000), player.getPosition());
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8170000), player.getPosition());
            }
        } else if (splitted[0].equals("!clone")) {
            int[] clone = {9001002, 9001003, 9001000, 9001001};
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                for (int i = 0; i < clone.length; i++) {
                    player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(clone[i]), player.getPosition());
                }
            }

        } else if (splitted[0].equals("!cornian")) {
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8150201), player.getPosition());
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8150200), player.getPosition());
            }
        } else if (splitted[0].equals("!balrog")) {
            int[] balrog = {8130100, 8150000, 9400536};
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                for (int i = 0; i < balrog.length; i++) {
                    player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(balrog[i]), player.getPosition());
                }
            }
        } else if (splitted[0].equals("!mushmom")) {
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                int[] mushmom = {6130101, 6300005, 9400205};
                for (int i = 0; i < mushmom.length; i++) {
                    player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(mushmom[i]), player.getPosition());
                }
            }
        } else if (splitted[0].equals("!necki")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9100001), player.getPosition());
        } else if (splitted[0].equals("!stirge")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9100002), player.getPosition());
        } else if (splitted[0].equals("!trickster")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9100006), player.getPosition());
        } else if (splitted[0].equals("!anego")) {
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9400121), player.getPosition());
            }
        } else if (splitted[0].equals("!theboss")) {
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9400300), player.getPosition());
            }
        } else if (splitted[0].equals("!snackbar")) {
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9500179), player.getPosition());
            }
        } else if (splitted[0].equals("!papapixie")) {
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300039), player.getPosition());
            }
        } else if (splitted[0].equals("!horseman")) {
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9400549), player.getPosition());
            }
        } else if (splitted[0].equals("!blackcrow")) {
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9400014), player.getPosition());
            }
        } else if (splitted[0].equals("!leafreboss")) {
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9400014), player.getPosition());
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8180001), player.getPosition());
            }
        } else if (splitted[0].equals("!shark")) {
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8150101), player.getPosition());
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8150100), player.getPosition());
            }
        } else if (splitted[0].equals("!franken")) {
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300139), player.getPosition());
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300140), player.getPosition());
            }
        } else if (splitted[0].equals("!bird")) {
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300090), player.getPosition());
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300089), player.getPosition());
            }
        } else if (splitted[0].equals("!pianus")) {
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8510000), player.getPosition());
            }
        } else if (splitted[0].equals("!centipede")) {
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9500177), player.getPosition());
            }
        } else if (splitted[0].equals("!wyvern")) {
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                for (int i = 8150300; i <= 8150302; i++) {
                    player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(i), player.getPosition());
                }
            }
        } else if (splitted[0].equals("!piratemob")) {
            int[] pirate = {9300119, 9300107, 9300105, 9300106};
            for (int amnt = getOptionalIntArg(splitted, 1, 1); amnt > 0; amnt--) {
                for (int i = 0; i < pirate.length; i++) {
                    player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(pirate[i]), player.getPosition());
                }
            }
            /*   } else if (splitted[0].equals("!makenpc")) {
            if (splitted.length == 2) {
            String dir = "C:\\wamp\\www\\npc\\";
            dir += splitted[1];
            dir += ".js";
            // File (or directory) to be moved
            File file = new File(dir);
            if (!file.exists()) {
            mc.dropMessage("File doesn't exist!");
            return;
            }
            // Destination directory
            File to = new File("C:\\Users\\Administrator\\Desktop\\XiuzSource\\scripts\\npc\\");
            // Move file to new directory
            boolean success = file.renameTo(new File(to, file.getName()));
            if (!success) {
            mc.dropMessage("Error moving the file"); // File was not successfully moved
            } else {
            mc.dropMessage("You just moved " + splitted[1] + ".js into the npc folder");
            }
            } else {
            mc.dropMessage("Syntax : !makenpc <id>");
            }*/
        } else if (splitted[0].equals("!checkhide")) {
            if (splitted.length == 1) {
                String players = "The following players are hidden here: ";
                int playerCount = 0;
                for (MapleCharacter mch : player.getMap().getCharacters()) {
                    if (mch.isHidden()) {
                        players += mch.getName() + ", ";
                        playerCount++;
                    }
                }
                if (playerCount < 1) {
                    mc.dropMessage("There is no one hidden in your map.");
                } else {
                    mc.dropMessage(players);
                }
            } else if (splitted.length == 2) {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                if (victim != null) {
                    String check;
                    if (victim.isHidden()) {
                        check = " is";
                    } else {
                        check = " isn't";
                    }
                    mc.dropMessage(victim.getName() + check + " hidden");
                } else {
                    mc.dropMessage("Syntax : !checkhide OR !checkhide <ign>");
                }
            } else {
                mc.dropMessage("This character isn't online");
            }
        } else if (splitted[0].equals("!bombardment")) {
            MapleMap map = player.getMap();
            for (int i = map.getFootholds().getMinDropX(); i < map.getFootholds().getMaxDropX(); i += 30) {
                map.spawnBombOnGroudBelow(MapleLifeFactory.getMonster(9300166), new Point(i, player.getPosition().y));
            }
        } else if (splitted[0].equals("!bomb")) {
            player.getMap().spawnBombOnGroudBelow(MapleLifeFactory.getMonster(9300166), player.getPosition());
        } else if (splitted[0].equalsIgnoreCase("!hide")) {
            if (splitted.length == 1) {
                SkillFactory.getSkill(9101004).getEffect(1).applyTo(player);
                mc.dropMessage("You hid yourself. Don't do naughty things in hide now.");
            } else if (splitted.length == 2) {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                SkillFactory.getSkill(9101004).getEffect(1).applyTo(victim);
                mc.dropMessage("You hid " + victim.getName() + " make sure to watch what he/she does");
            } else {
                mc.dropMessage("Syntax : !hide <ign> OR !hide");
            }
        } else if (splitted[0].equalsIgnoreCase("!bombevent")) {
            Random rand = new Random();
            final MapleMap target = cserv.getMapFactory().getMap(260000201);
            for (MapleCharacter chr : player.getMap().getCharacters()) {
                MapleInventoryManipulator.removeAllById(chr.getClient(), 2100067, false);
                MaplePortal portal = target.getPortal(rand.nextInt(target.getPortals().size()));
                chr.changeMapSpecial(target, portal);
                chr.getClient().getSession().write(MaplePacketCreator.sendYellowTip("[BOMBERMAN] Welcome to the BomberMan event, use those bombs in your inventory to pwn some noobs! If you manage to survive, you'll win a prize!"));
                chr.getClient().getSession().write(MaplePacketCreator.sendYellowTip("Remember that you will self-destruct if you use any skills in here!"));
                chr.getClient().getSession().write(MaplePacketCreator.sendYellowTip("Use `@refill` to refill your bomb stock for a penalty"));
                chr.gainItem(2100067, 5);
            }
            rand = null;
        } else if (splitted[0].equalsIgnoreCase("!search")) {
            if (splitted.length > 2) {
                String type = splitted[1];
                String search = StringUtil.joinStringFrom(splitted, 2);
                MapleData data = null;
                MapleDataProvider dataProvider = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/" + "String.wz"));
                mc.dropMessage("<<Type: " + type + " | Search: " + search + ">>");
                if (type.equalsIgnoreCase("NPC") || type.equalsIgnoreCase("NPCS")) {
                    List<String> retNpcs = new ArrayList<String>();
                    data = dataProvider.getData("Npc.img");
                    List<Pair<Integer, String>> npcPairList = new LinkedList<Pair<Integer, String>>();
                    for (MapleData npcIdData : data.getChildren()) {
                        int npcIdFromData = Integer.parseInt(npcIdData.getName());
                        String npcNameFromData = MapleDataTool.getString(npcIdData.getChildByPath("name"), "NO-NAME");
                        npcPairList.add(new Pair<Integer, String>(npcIdFromData, npcNameFromData));
                    }
                    for (Pair<Integer, String> npcPair : npcPairList) {
                        if (npcPair.getRight().toLowerCase().contains(search.toLowerCase())) {
                            retNpcs.add(npcPair.getLeft() + " - " + npcPair.getRight());
                        }
                    }
                    if (retNpcs != null && retNpcs.size() > 0) {
                        for (String singleRetNpc : retNpcs) {
                            mc.dropMessage(singleRetNpc);
                        }
                    } else {
                        mc.dropMessage("No NPC's Found");
                    }
                } else if (type.equalsIgnoreCase("MAP") || type.equalsIgnoreCase("MAPS")) {
                    List<String> retMaps = new ArrayList<String>();
                    data = dataProvider.getData("Map.img");
                    List<Pair<Integer, String>> mapPairList = new LinkedList<Pair<Integer, String>>();
                    for (MapleData mapAreaData : data.getChildren()) {
                        for (MapleData mapIdData : mapAreaData.getChildren()) {
                            int mapIdFromData = Integer.parseInt(mapIdData.getName());
                            String mapNameFromData = MapleDataTool.getString(mapIdData.getChildByPath("streetName"), "NO-NAME") + " - " + MapleDataTool.getString(mapIdData.getChildByPath("mapName"), "NO-NAME");
                            mapPairList.add(new Pair<Integer, String>(mapIdFromData, mapNameFromData));
                        }
                    }
                    for (Pair<Integer, String> mapPair : mapPairList) {
                        if (mapPair.getRight().toLowerCase().contains(search.toLowerCase())) {
                            retMaps.add(mapPair.getLeft() + " - " + mapPair.getRight());
                        }
                    }
                    if (retMaps != null && retMaps.size() > 0) {
                        for (String singleRetMap : retMaps) {
                            mc.dropMessage(singleRetMap);
                        }
                    } else {
                        mc.dropMessage("No Maps Found");
                    }
                } else if (type.equalsIgnoreCase("MOB") || type.equalsIgnoreCase("MOBS") || type.equalsIgnoreCase("MONSTER") || type.equalsIgnoreCase("MONSTERS")) {
                    List<String> retMobs = new ArrayList<String>();
                    data = dataProvider.getData("Mob.img");
                    List<Pair<Integer, String>> mobPairList = new LinkedList<Pair<Integer, String>>();
                    for (MapleData mobIdData : data.getChildren()) {
                        int mobIdFromData = Integer.parseInt(mobIdData.getName());
                        String mobNameFromData = MapleDataTool.getString(mobIdData.getChildByPath("name"), "NO-NAME");
                        mobPairList.add(new Pair<Integer, String>(mobIdFromData, mobNameFromData));
                    }
                    for (Pair<Integer, String> mobPair : mobPairList) {
                        if (mobPair.getRight().toLowerCase().contains(search.toLowerCase())) {
                            retMobs.add(mobPair.getLeft() + " - " + mobPair.getRight());
                        }
                    }
                    if (retMobs != null && retMobs.size() > 0) {
                        for (String singleRetMob : retMobs) {
                            mc.dropMessage(singleRetMob);
                        }
                    } else {
                        mc.dropMessage("No Mob's Found");
                    }
                } else if (type.equalsIgnoreCase("REACTOR") || type.equalsIgnoreCase("REACTORS")) {
                    mc.dropMessage("NOT ADDED YET");

                } else if (type.equalsIgnoreCase("ITEM") || type.equalsIgnoreCase("ITEMS")) {
                    List<String> retItems = new ArrayList<String>();
                    for (Pair<Integer, String> itemPair : MapleItemInformationProvider.getInstance().getAllItems()) {
                        if (itemPair.getRight().toLowerCase().contains(search.toLowerCase())) {
                            retItems.add(itemPair.getLeft() + " - " + itemPair.getRight());
                        }
                    }
                    if (retItems != null && retItems.size() > 0) {
                        for (String singleRetItem : retItems) {
                            mc.dropMessage(singleRetItem);
                        }
                    } else {
                        mc.dropMessage("No Item's Found");
                    }
                } else if (type.equalsIgnoreCase("SKILL") || type.equalsIgnoreCase("SKILLS")) {
                    List<String> retSkills = new ArrayList<String>();
                    data = dataProvider.getData("Skill.img");
                    List<Pair<Integer, String>> skillPairList = new LinkedList<Pair<Integer, String>>();
                    for (MapleData skillIdData : data.getChildren()) {
                        int skillIdFromData = Integer.parseInt(skillIdData.getName());
                        String skillNameFromData = MapleDataTool.getString(skillIdData.getChildByPath("name"), "NO-NAME");
                        skillPairList.add(new Pair<Integer, String>(skillIdFromData, skillNameFromData));
                    }
                    for (Pair<Integer, String> skillPair : skillPairList) {
                        if (skillPair.getRight().toLowerCase().contains(search.toLowerCase())) {
                            retSkills.add(skillPair.getLeft() + " - " + skillPair.getRight());
                        }
                    }
                    if (retSkills != null && retSkills.size() > 0) {
                        for (String singleRetSkill : retSkills) {
                            mc.dropMessage(singleRetSkill);
                        }
                    } else {
                        mc.dropMessage("No Skills Found");
                    }
                } else {
                    mc.dropMessage("Sorry, that search call is unavailable");
                }
            } else {
                mc.dropMessage("Invalid search. Syntax: '!search <type> <query>', where <type> is MAP, USE, ETC, CASH, EQUIP, MOB or SKILL");
            }
        } else if (splitted[0].equalsIgnoreCase("!staffchat") || splitted[0].equalsIgnoreCase("!msg")) {
            player.setStaffChat(!player.inStaffChat());
            mc.dropMessage("You are now " + (player.inStaffChat() ? "" : "not ") + "talking in the staff chat");
        } else if (splitted[0].equalsIgnoreCase("!dispelmap")) {
            for (MapleCharacter players : player.getMap().getCharacters()) {
                if (players.getGMLevel() < 3) {
                    players.dispelDebuffs();
                }
            }
        } else if (splitted[0].equalsIgnoreCase("!maxall")) {
            player.setStr(32767);
            player.setDex(32767);
            player.setInt(32767);
            player.setLuk(32767);
            player.setLevel(255);
            player.setFame(13337);
            player.setMaxHp(30000);
            player.setMaxMp(30000);
            player.updateSingleStat(MapleStat.STR, 32767);
            player.updateSingleStat(MapleStat.DEX, 32767);
            player.updateSingleStat(MapleStat.INT, 32767);
            player.updateSingleStat(MapleStat.LUK, 32767);
            player.updateSingleStat(MapleStat.LEVEL, 255);
            player.updateSingleStat(MapleStat.FAME, 13337);
            player.updateSingleStat(MapleStat.MAXHP, 30000);
            player.updateSingleStat(MapleStat.MAXMP, 30000);
        } else if (splitted[0].equalsIgnoreCase("!spymode")) {
            player.toggleSpyMode();
            mc.dropMessage("Spy mode toggled " + (player.inSpyMode() ? "on" : "off"));
        } else if (splitted[0].equalsIgnoreCase("!giftkarma")) {
            cserv.getPlayerStorage().getCharacterByName(splitted[1]).gainKarma(Integer.parseInt(splitted[2]));
            mc.dropMessage("You have given " + splitted[1] + " " + splitted[2] + " Karma Levels.");
        } else if (splitted[0].equalsIgnoreCase("!checkitems")) {
            NPCScriptManager npc = NPCScriptManager.getInstance();
            npc.start(c, 1022101);
        } else if (splitted[0].equals("!givedp")) {
            if (splitted.length != 3) {
                mc.dropMessage("!givedp <IGN> <amount>");
            } else {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                int amount = 0;
                try {
                    amount = Integer.parseInt(splitted[2]);
                } catch (NumberFormatException nbe) {
                    mc.dropMessage("Invalid amount");
                    return;
                }
                if (victim != null) {
                    victim.giveDP(amount);
                    victim.dropMessage(player.getName() + " has just given you " + amount + " donor points!");
                    mc.dropMessage("You have just given " + amount + " donor points to " + victim.getName());
                } else {
                    try {
                        String name = splitted[1];
                        WorldChannelInterface wci = cserv.getWorldInterface();
                        int channel = wci.find(name);
                        if (channel > -1) {
                            ChannelServer pserv = ChannelServer.getInstance(channel);
                            final MapleCharacter wVictim = pserv.getPlayerStorage().getCharacterByName(name);
                            if (wVictim != null) {
                                wVictim.giveDP(amount);
                                wVictim.dropMessage(player.getName() + " has just given you " + amount + " donor points!");
                                player.dropMessage("You have just given " + amount + " donor points to " + wVictim.getName() + " who is in channel " + channel);
                            } else {
                                mc.dropMessage("Something went horribly wrong");
                            }
                        } else {
                            try {
                                Connection con = DatabaseConnection.getConnection();
                                PreparedStatement ps = con.prepareStatement("UPDATE `accounts` SET `donorPoints` = `donorPoints` + ? WHERE id = (SELECT `accountid` FROM `characters` WHERE `name` = ?)");
                                ps.setInt(1, amount);
                                ps.setString(2, splitted[1]);
                                int affected = ps.executeUpdate();
                                ps.close();
                                if (affected == 1) {
                                    mc.dropMessage("You have given " + amount + " donor points to " + splitted[1]);
                                } else if (affected == 0) {
                                    mc.dropMessage(splitted[1] + " is not a registered character");
                                } else {
                                    mc.dropMessage("Something went horribly wrong, post on the forums");
                                }
                            } catch (SQLException e) {
                                mc.dropMessage("SQL Exception");
                            }
                        }
                    } catch (RemoteException e) {
                        cserv.reconnectWorld();
                        mc.dropMessage("Remote Exception");
                    }
                }
            }
        } else if (splitted[0].equals("!checkaccounts") || splitted[0].equals("!checkcharacters")) {
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT name FROM accounts WHERE lastknownip = (SELECT lastknownip FROM accounts WHERE id = (SELECT accountid FROM characters WHERE name = ?))");
                ps.setString(1, splitted[1]);
                ResultSet rs = ps.executeQuery();
                if (!rs.first()) {
                    rs.close();
                    ps.close();
                    mc.dropMessage("Could not find any information for the character " + splitted[1]);
                    return;
                }
                mc.dropMessage(splitted[1] + "'s Accounts:");
                StringBuilder out = new StringBuilder();
                do {
                    out.append(rs.getString("name") + (rs.isLast() ? "" : ", "));
                } while (rs.next());
                mc.dropMessage(out.toString());
                rs.close();
                ps.close();
                ps = DatabaseConnection.getConnection().prepareStatement("SELECT name FROM characters WHERE accountid IN (SELECT id FROM accounts WHERE lastknownip = (SELECT lastknownip FROM accounts where id = (SELECT accountid FROM characters WHERE name = ?)))");
                ps.setString(1, splitted[1]);
                rs = ps.executeQuery();
                if (!rs.first()) {
                    rs.close();
                    ps.close();
                    mc.dropMessage("Could not find any information for the character " + splitted[1]);
                    return;
                }
                mc.dropMessage(splitted[1] + "'s Characters:");
                out.setLength(0);
                do {
                    out.append(rs.getString("name") + (rs.isLast() ? "" : ", "));
                } while (rs.next());
                mc.dropMessage(out.toString());
            } catch (SQLException sqle) {
                mc.dropMessage("Invalid name" + splitted[1]);
            }
        } else if (splitted[0].equals("!unstuck")) {
            if (splitted.length != 2) {
                mc.dropMessage("!unstuck <IGN>");
            } else {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                if (victim != null) {
                    mc.dropMessage(victim.getName() + " was disconnected");
                    victim.getClient().disconnect();
                    victim.getClient().getSession().close();
                } else {
                    try {
                        String name = splitted[1];
                        WorldChannelInterface wci = cserv.getWorldInterface();
                        int channel = wci.find(name);
                        if (channel > -1) {
                            ChannelServer pserv = ChannelServer.getInstance(channel);
                            final MapleCharacter wVictim = pserv.getPlayerStorage().getCharacterByName(name);
                            if (wVictim != null) {
                                mc.dropMessage(wVictim.getName() + " was disconnected");
                                wVictim.getClient().disconnect();
                                wVictim.getClient().getSession().close();
                            } else {
                                mc.dropMessage("Something went horribly wrong");
                            }
                        } else {
                            try {
                                Connection con = DatabaseConnection.getConnection();
                                PreparedStatement ps = con.prepareStatement("UPDATE `accounts` SET `loggedin` = 0 WHERE id = (SELECT `accountid` FROM `characters` WHERE `name` = ?)");
                                ps.setString(1, splitted[1]);
                                ps.executeUpdate();
                                ps.close();
                                mc.dropMessage(splitted[1] + " has been unstuck");
                            } catch (SQLException e) {
                                mc.dropMessage("SQL Exception");
                            }
                        }
                    } catch (RemoteException e) {
                        cserv.reconnectWorld();
                        mc.dropMessage("Remote Exception");
                    }
                }
            }
        } else if (splitted[0].equalsIgnoreCase("!karmaperson")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[2]);
            if (splitted[1].equalsIgnoreCase("up")) {
                victim.upKarma();
                mc.dropMessage("You have raised " + victim.getName() + "'s madness. His madness level is currently at " + victim.getKarma() + ".");
                victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "Your Madness level has been raised to " + victim.getKarma() + " by " + player.getName() + "."));
            } else if (splitted[1].equalsIgnoreCase("down")) {
                victim.downKarma();
                mc.dropMessage("You have dropped " + victim.getName() + "'s Madness level. His madness level is currently at " + victim.getKarma() + ".");
                victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "Your Madness level has been dropped to " + victim.getKarma() + " by " + player.getName() + "."));
            }
        }

    }

    @Override
    public CommandDefinition[] getDefinition() {
        return new CommandDefinition[]{
                    new CommandDefinition("trickster", 2),
                    new CommandDefinition("necki", 2),
                    new CommandDefinition("stirge", 2),
                    new CommandDefinition("move", 2),
                    new CommandDefinition("horseman", 3),
                    new CommandDefinition("blackcrow", 3),
                    new CommandDefinition("leafreboss", 3),
                    new CommandDefinition("shark", 3),
                    new CommandDefinition("franken", 3),
                    new CommandDefinition("bird", 3),
                    new CommandDefinition("pianus", 3),
                    new CommandDefinition("coke", 3),
                    new CommandDefinition("papu", 3),
                    new CommandDefinition("zakum", 3),
                    new CommandDefinition("ergoth", 3),
                    new CommandDefinition("ludimini", 3),
                    new CommandDefinition("cornian", 3),
                    new CommandDefinition("balrog", 3),
                    new CommandDefinition("mushmom", 3),
                    new CommandDefinition("wyvern", 3),
                    new CommandDefinition("piratemob", 3),
                    new CommandDefinition("clone", 3),
                    new CommandDefinition("anego", 3),
                    new CommandDefinition("theboss", 3),
                    new CommandDefinition("snackbar", 3),
                    new CommandDefinition("papapixie", 3),
                    new CommandDefinition("centipede", 3),
                    new CommandDefinition("lowhp", 3),
                    new CommandDefinition("sp", 3),
                    new CommandDefinition("ap", 3),
                    new CommandDefinition("setjob", 2),
                    new CommandDefinition("whereami", 2),
                    new CommandDefinition("openshop", 3),
                    new CommandDefinition("opennpc", 3),
                    new CommandDefinition("levelup", 3),
                    new CommandDefinition("setmaxmp", 3),
                    new CommandDefinition("setmaxhp", 3),
                    new CommandDefinition("item", 3),
                    new CommandDefinition("noname", 3),
                    new CommandDefinition("dropmesos", 3),
                    new CommandDefinition("level", 3),
                    new CommandDefinition("banreason", 3),
                    new CommandDefinition("whitechat", 3),
                    new CommandDefinition("joinguild", 4),
                    new CommandDefinition("unbuffmap", 3),
                    new CommandDefinition("mesos", 3),
                    new CommandDefinition("ariantpq", 3),
                    new CommandDefinition("scoreboard", 3),
                    new CommandDefinition("array", 3),
                    new CommandDefinition("rreactor", 3),
                    new CommandDefinition("nxslimes", 3),
                    new CommandDefinition("horntail", 3),
                    new CommandDefinition("droprandomstatitem", 3),
                    new CommandDefinition("hide", 2),
                    new CommandDefinition("killall", 2),
                    new CommandDefinition("adminhelp", 5),
                    new CommandDefinition("checkitems", 3),
                    new CommandDefinition("gender", 3),
                    new CommandDefinition("levelperson", 4),
                    new CommandDefinition("skill", 4),
                    new CommandDefinition("setall", 3),
                    new CommandDefinition("giftnx", 4),
                    new CommandDefinition("givegp", 4),
                    new CommandDefinition("maxskills", 3),
                    new CommandDefinition("fame", 3),
                    new CommandDefinition("unhide", 2),
                    new CommandDefinition("unbuff", 3),
                    new CommandDefinition("sendhint", 4),
                    new CommandDefinition("smega", 3),
                    new CommandDefinition("mutesmega", 2),
                    new CommandDefinition("givedisease", 3),
                    new CommandDefinition("dc", 3),
                    new CommandDefinition("charinfo", 2),
                    new CommandDefinition("connected", 2),
                    new CommandDefinition("warp", 2),
                    new CommandDefinition("warphere", 2),
                    new CommandDefinition("map", 2),
                    new CommandDefinition("warpallhere", 4),
                    new CommandDefinition("warpwholeworld", 5),
                    new CommandDefinition("mesosrate", 5),
                    new CommandDefinition("droprate", 5),
                    new CommandDefinition("bossdroprate", 5),
                    new CommandDefinition("exprate", 5),
                    new CommandDefinition("godlyitemrate", 5),
                    new CommandDefinition("itemstat", 5),
                    new CommandDefinition("togglegodlyitems", 5),
                    new CommandDefinition("servermessage", 5),
                    new CommandDefinition("whosthere", 3),
                    new CommandDefinition("cheaters", 2),
                    new CommandDefinition("fakerelog", 2),
                    /*
                    new CommandDefinition("getrings", 3),
                    new CommandDefinition("ring", 3),
                    new CommandDefinition("removering", 3),*/
                    new CommandDefinition("nearestportal", 3),
                    new CommandDefinition("unban", 3),
                    new CommandDefinition("spawn", 3),
                    new CommandDefinition("ban", 2),
                    new CommandDefinition("tempban", 2),
                    new CommandDefinition("npc", 3),
                    new CommandDefinition("removenpcs", 3),
                    new CommandDefinition("mynpcpos", 3),
                    new CommandDefinition("cleardrops", 2),
                    new CommandDefinition("clearshops", 3),
                    new CommandDefinition("clearevents", 3),
                    new CommandDefinition("emote", 3),
                    new CommandDefinition("proitem", 4),
                    new CommandDefinition("addclones", 1),
                    new CommandDefinition("removeclones", 1),
                    new CommandDefinition("removeallclones", 3),
                    new CommandDefinition("unpursue", 3),
                    new CommandDefinition("pursue", 3),
                    new CommandDefinition("pause", 3),
                    new CommandDefinition("stance", 3),
                    new CommandDefinition("killmonster", 3),
                    new CommandDefinition("removeoid", 3),
                    new CommandDefinition("gmtext", 2),
                    new CommandDefinition("textcolor", 2),
                    new CommandDefinition("currentdate", 3),
                    new CommandDefinition("maxmesos", 3),
                    new CommandDefinition("fullcharge", 3),
                    new CommandDefinition("changejob", 3),
                    new CommandDefinition("gmmap", 2),
                    new CommandDefinition("gmshop", 3),
                    new CommandDefinition("mutemap", 2),
                    new CommandDefinition("unseducemap", 3),
                    new CommandDefinition("seducemap", 3),
                    new CommandDefinition("seduceperson", 2),
                    new CommandDefinition("stunmap", 2),
                    new CommandDefinition("removechairs", 2),
                    new CommandDefinition("removemapchairs", 2),
                    new CommandDefinition("removechair", 2),
                    new CommandDefinition("specitem", 3),
                    new CommandDefinition("maxall", 3),
                    new CommandDefinition("drop", 3),
                    new CommandDefinition("write", 3),
                    new CommandDefinition("tagitem", 3),
                    new CommandDefinition("tag", 2),
                    new CommandDefinition("bomb", 3),
                    new CommandDefinition("bombevent", 3),
                    new CommandDefinition("organize", 3),
                    new CommandDefinition("checkhide", 3),
                    new CommandDefinition("maptimer", 3),
                    new CommandDefinition("jobperson", 3),
                    new CommandDefinition("speak", 3),
                    new CommandDefinition("giftkarma", 4),
                    new CommandDefinition("gm", 2),
                    new CommandDefinition("sgm", 2),
                    new CommandDefinition("search", 2),
                    new CommandDefinition("sendcustomhint", 3),
                    new CommandDefinition("dispelmap", 2),
                    new CommandDefinition("reloadshop", 3),
                    new CommandDefinition("addstorageslot", 3),
                    new CommandDefinition("msg", 2),
                    new CommandDefinition("staffchat", 2),
                    new CommandDefinition("rare", 3),
                    new CommandDefinition("addrare", 3),
                    new CommandDefinition("drare", 3),
                    new CommandDefinition("deleterare", 3),
                    new CommandDefinition("crare", 3),
                    new CommandDefinition("checkrare", 3),
                    new CommandDefinition("grare", 3),
                    new CommandDefinition("gacharare", 3),
                    new CommandDefinition("rrare", 3),
                    new CommandDefinition("randomrare", 3),
                    new CommandDefinition("shopitem", 3),
                    new CommandDefinition("dshopitem", 3),
                    new CommandDefinition("accban", 2),
                    new CommandDefinition("bob", 2),
                    new CommandDefinition("itemvac", 3),
                    new CommandDefinition("exempt", 2),
                    new CommandDefinition("statedit", 3),
                    new CommandDefinition("untagworld", 2),
                    new CommandDefinition("changestats", 3),
                    new CommandDefinition("hidechat", 3),
                    new CommandDefinition("pin", 3),
                    new CommandDefinition("unstuck", 2),
                    new CommandDefinition("givedp", 3),
                    new CommandDefinition("makenpc", 3),
                    new CommandDefinition("spymode", 2),
                    new CommandDefinition("checkaccounts", 2),
                    new CommandDefinition("checkcharacters", 2),
                    new CommandDefinition("follow", 2),
                    new CommandDefinition("bombardment", 4),
                    new CommandDefinition("karmaperson", 3),
                    new CommandDefinition("addhideout", 3),
                    new CommandDefinition("giveitem", 3),
                    new CommandDefinition("checkid", 2)
                };

    }
}



