package client.messages.commands;

import java.rmi.RemoteException;
import java.util.HashMap;
import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import net.world.remote.WorldChannelInterface;
import client.MapleStat;
import client.SkillFactory;
import client.messages.Command;
import client.messages.CommandDefinition;
import client.messages.MessageCallback;
import net.channel.ChannelServer;
import scripting.npc.NPCScriptManager;
import server.MapleInventoryManipulator;
import server.maps.SavedLocationType;
import tools.MaplePacketCreator;
import tools.StringUtil;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import client.BuddyList;
import client.BuddyList.BuddyAddResult;
import client.BuddyList.BuddyOperation;
import client.BuddylistEntry;
import client.Equip;
import client.IItem;
import client.MapleDisease;
import client.MapleInventoryType;
import client.MapleJob;
import client.achievement.Achievement;
import database.DatabaseConnection;
import net.channel.handler.BuddylistModifyHandler;
import net.channel.handler.ChangeChannelHandler;
import net.channel.remote.ChannelWorldInterface;
import net.world.WorldServer;
import server.MapleItemInformationProvider;
import server.TimerManager;
import server.life.MobSkillFactory;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;

public class PlayerCommands implements Command {

    public void clearSlot(MapleClient c, int type) {
        MapleInventoryType invent;
        if (type == 1) {
            invent = MapleInventoryType.EQUIP;
        } else if (type == 2) {
            invent = MapleInventoryType.USE;
        } else if (type == 3) {
            invent = MapleInventoryType.ETC;
        } else if (type == 4) {
            invent = MapleInventoryType.SETUP;
        } else {
            invent = MapleInventoryType.CASH;
        }
        List<Integer> itemMap = new LinkedList<Integer>();
        for (IItem item : c.getPlayer().getInventory(invent).list()) {
            if (invent.equals(MapleInventoryType.EQUIP)) {
                if (((Equip) item).getRingId() < 1) {
                    itemMap.add(item.getItemId());
                }
            } else {
                itemMap.add(item.getItemId());
            }
        }
        for (int itemid : itemMap) {
            MapleInventoryManipulator.removeAllById(c, itemid, false);
        }
    }

    public static ResultSet karma() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name,karma FROM characters WHERE gm < 2 ORDER BY karma desc LIMIT 20");
            return ps.executeQuery();
        } catch (Exception ex) {
        }
        return null;
    }

    public static ResultSet achievements() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name,achievements FROM characters WHERE gm < 2 ORDER BY achievements desc LIMIT 20");
            return ps.executeQuery();
        } catch (Exception ex) {
        }
        return null;
    }

    public static ResultSet matchcardwins() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name,matchcardwins FROM characters WHERE gm <= 5 ORDER BY matchcardwins desc LIMIT 20");
            return ps.executeQuery();
        } catch (Exception ex) {
        }
        return null;
    }

    public static ResultSet voting() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT account,times FROM votingrecords ORDER BY times desc LIMIT 20");
            return ps.executeQuery();
        } catch (Exception ex) {
        }

        return null;
    }

    public static ResultSet matchcardlosses() { // I use public static ResultSets for rankings and etc. That get's used pretty constantly. Could it really be this?
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name,matchcardlosses FROM characters WHERE gm <= 5 ORDER BY matchcardlosses desc LIMIT 20");
            return ps.executeQuery();
        } catch (Exception ex) {
        }

        return null;
    }

    public static ResultSet omoklosses() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name,omoklosses FROM characters WHERE gm <= 5 ORDER BY omoklosses desc LIMIT 20");
            return ps.executeQuery();
        } catch (Exception ex) {
        }

        return null;
    }

    public static ResultSet omokwins() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name,omokwins FROM characters WHERE gm <= 5 ORDER BY omokwins desc LIMIT 20");
            return ps.executeQuery();
        } catch (Exception ex) {
        }
        return null;
    }

    public static ResultSet reborns() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name,reborns,level FROM characters WHERE gm < 2 ORDER BY reborns desc,level desc LIMIT 10");
            return ps.executeQuery();
        } catch (Exception ex) {
        }
        return null;
    }

    public static ResultSet pvpkills() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name,pvpkills FROM characters WHERE gm < 2 ORDER BY pvpkills desc LIMIT 10");
            return ps.executeQuery();
        } catch (Exception ex) {
        }
        return null;
    }

    @Override
    public void execute(MapleClient c, MessageCallback mc, String[] splitted) throws Exception {
        splitted[0] = splitted[0].toLowerCase();
        final MapleCharacter player = c.getPlayer();
        ChannelServer cserv = c.getChannelServer();
        final WorldServer wserv = WorldServer.getInstance();
        if (player.getMapId() == 0) {
            mc.dropMessage("Dont go anywhere yet, Click the NPC's and get your starter pack.");
        } else {
            if (!player.isInJail()) {
                if (splitted[0].equals("@command") || splitted[0].equals("@commands") || splitted[0].equals("@help")) {
                    List<String> out = new LinkedList<String>();
                    if (splitted.length != 2) {
                        out.add("Command Categories");
                        out.add("===========================================");
                        out.add("Please choose a category");
                        out.add("@help stats - Statistical Commands");
                        out.add("@help social - Community Related Commands");
                        out.add("@help training - Training, Leveling and Rebirthing Related Comamnds");
                        out.add("@help looks - Apperance Related Commands");
                        out.add("@help fun - Fun Commands");
                        out.add("@help misc - Miscellaneous Commands");
                    } else {
                        String str = splitted[1].toLowerCase();
                        if (str.equals("stats")) {
                            out.add("Statistical Commands");
                            out.add("===========================================");
                            out.add("@checkstats - This command will show you: Your STR, DEX, LUK, INT, Available AP, HP, MP, Rebirths, Vote Points, JQ Points, Achievement Points, Donor Points, EXP, Weapon Attack, and Weapon Defense.");
                            out.add("@str <amount> - This command will let you add or subtract the amount of STR you want");
                            out.add("@dex <amount> - This command will let you add or subtract the amount of DEX you want");
                            out.add("@luk <amount> - This command will let you add or subtract the amount of LUK you want");
                            out.add("@int <amount> - This command will let you add or subtract the amount of INT you want");
                            out.add("You can also do:");
                            out.add("@str all - This command will add all your current AP into STR");
                            out.add("@dex all - This command will add all your current AP into DEX");
                            out.add("@luk all - This command will add all your current AP into LUK");
                            out.add("@int all - This command will add all your current AP into INT");
                            out.add("@reset - This command will open up the NPC 'Robin' which can reset your stats");
                        } else if (str.equals("social")) {
                            out.add("Social Commands");
                            out.add("===========================================");
                            out.add("@online - This command will show you all players online at the moment, and the Channel they are in. It will also show you the total amount of players online");
                            out.add("@stafflist - This command will show you all the Staff");
                            out.add("@staffonline - This command will show you all the Staff that are online at the moment, and what Channel they are in");
                            out.add("@gmmsg <msg> - This command will send a message to all the Staff online");
                            out.add("@donatorlist - This command will show you a list of all the Donors");
                            out.add("@donatorsonline - This command will show you all the Donors that are online at the moment, and what Channel they are in");
                            out.add("@buddy <add/delete> <name> - This command will allow you to add Players to your Buddy List");
                            out.add("@togglesmega - This command will disable, and enable you from viewing Smegas");
                            out.add("@ranking - This command will show you the Top 10 players with the most Reborns");
                            out.add("@achievementrank - This command will show you the top 20 Players with the most Achievement Points.");
                            out.add("@jqrank - This command will show you the top 20 players with the most JQ Points");
                            out.add("@pvprank - This command will show you the top 10 players with the most PVP kills");
                            out.add("@ignore <name> - This command will ignore a certain person's chat.");
                            out.add("@afk <name> - This command will tell you if a player has been AFK, and for how long.");
                            out.add("@afkworld - This command will show you how many people are AFK on Void");
                            out.add("@onlinetime <name> - This command will show you the amount of time a Player has been online");
                        } else if (str.equals("training")) {
                            out.add("Training, Leveling and Rebirthing Commands");
                            out.add("===========================================");
                            out.add("@buff set - This command will open up an NPC that lets you choose what buffs you would like");
                            out.add("@buffme - This command will buff you will your chosen buffs");
                            out.add("@debuff - This command will take away all of your buffs");
                            out.add("@rebirth - This command will bring you from Lvl 200 back to Lvl 0");
                            out.add("@rebirthw - This command will rebirth you into a Warrior");
                            out.add("@rebirthm - This command will rebirth you into a Magician");
                            out.add("@rebirthb - This command will rebirth you into a Bowman");
                            out.add("@rebirtht - This command will rebirth you into a Thirf");
                            out.add("@rebirthp - This command will rebirth you into a Pirate");
                            out.add("@<jobname> - This command will change your job without rebirthing (Required 200 Rebirths)");
                            out.add("@showjobs - This command will show you all the available jobs that you can Rebirth into");
                            out.add("@job - This command will change your job according to your starting job and level");
                            out.add("@recharge - This command will recharge all of your stars (If you are a Night Lord)");
                            out.add("@spawner <1/2/3/4/5/6> - This command will warp you to the Boss Spawners");
                            out.add("@cdrops - This command will clear all the drops for you for the cost of 1M (only in FM rooms 1-22, and all spawners)");
                        } else if (str.equals("looks")) {
                            out.add("Appearance Commands");
                            out.add("===========================================");
                            out.add("@kin - This command open the male look changer, Kin");
                            out.add("@nimakin - This command open the female look changer, Nimakin");
                            out.add("@changegender - This command will change your gender for the cost of 1,000,000 mesos");
                            out.add("@shop - This command will open the all in one shop where you can buy various items");
                        } else if (str.equals("fun")) {
                            out.add("Fun Commands");
                            out.add("===========================================");
                            out.add("@jq - This command will open an NPC that can warp you to a Jump Quest of your choice");
                            out.add("@giveup - This command will warp you to Henesys from a Jump Quest... if you give up");
                            out.add("@emo - This command will kill you");
                            out.add("@join - This command will warp you into the event, if there is one going on");
                            out.add("@eventstatus - This command will give you details on the event going on. It shows you the channel, the hoster, the event name, and the map that the event is in");
                            out.add("@zombie - This command will warp you into the Zombies vs. Humans Event");
                            out.add("@leave - This command will warp you out of the Zombies vs. Humans Event");
                            out.add("@pvp - This command will warp you to the PVP Map");
                            out.add("@p2p - This command will give you a list of all available Player2Player commands");
                        } else if (str.equals("misc")) {
                            out.add("Miscellaneous Commands");
                            out.add("===========================================");
                            out.add("@help - This command will give you a list of all Player Commands");
                            out.add("@fixkeys - If your keyboard gets wiped, use this command");
                            out.add("@dispose - This command will unstuck your character");
                            out.add("@expfix - This command will fix your glitched/negative EXP");
                            out.add("@joko - This command will open up an Informational NPC");
                            out.add("@save - This command will save your progress");
                            out.add("@shop - This command will open the all in one Shop");
                            out.add("@all - This command will open an NPC that can lead you to");
                            out.add("@time - This command will show the current Server Time");
                            out.add("@revive - This command will revive a player for 500M");
                            out.add("@time - This command will show the current server time");
                            out.add("@go <mapname> - This command will warp you directly to the map of your choice");
                            out.add("@go map list - This command will show you all the maps that you can go to through @go");
                            out.add("@exchange <from> <to> - This command will exchange currencies for you");
                            out.add("@exchange help me - This command will show you available currency exchanges");
                        } else {
                            out.add("Invalid help page");
                            out.add("Available pages: stats, social, training, looks, fun, misc");
                        }
                    }
                    for (String msg : out) {
                        mc.dropMessage(msg);
                    }
                } else if (splitted[0].equals("@checkstats")) {
                    player.updateVotePoints();
                    mc.dropMessage("Your stats are:");
                    mc.dropMessage("Str: " + player.getStr() + "  |  Dex: " + player.getDex() + "  |  Int: " + player.getInt() + "  |  Luk: " + player.getLuk());
                    mc.dropMessage("Rebirths: " + player.getReborns() + "  |  Vote Points: " + player.votePoints());
                    mc.dropMessage("JQ Points: " + player.getKarma() + "  |  Achievement Points: " + player.getAchievementPoints() + "  |  Donor Points: " + player.getDP());
                    mc.dropMessage("Available AP: " + player.getRemainingAp() + "  |  HP: " + player.getHp() + "  |  MP: " + player.getMp() + "  |  EXP: " + player.getExp());
                    mc.dropMessage("Weapon Attack: " + player.getTotalWatk() + "  |  Weapon Defence: " + player.getTotalWdef());
                } else if (splitted[0].equals("@save")) {
                    player.saveToDB(true, true);
                    mc.dropMessage("Saved all progress");
                } else if (splitted[0].equals("@stamp") || splitted[0].equals("@stump")) {
                    if (player.haveItem(4002002, 1, false, true) || player.haveItem(4031560, 1, false, true) || player.haveItem(4031561, 1, false, true)) {
                        if (splitted.length == 1) {
                            if (player.stampOn()) {
                                player.setStampOff();
                                mc.dropMessage("You've toggled your stamp(s) off");
                            } else {
                                player.setStampOn();
                                mc.dropMessage("You've toggled your stamp(s) on");
                            }
                        } else if (splitted.length == 2) {
                            if (player.haveItem(4002002, 1, false, true) || player.haveItem(4031560, 1, false, true)) {
                                int job = 0;
                                if (splitted[1].equalsIgnoreCase("w")) {
                                    job = 1;
                                } else if (splitted[1].equalsIgnoreCase("t")) {
                                    job = 4;
                                } else if (splitted[1].equalsIgnoreCase("b")) {
                                    job = 3;
                                } else if (splitted[1].equalsIgnoreCase("m")) {
                                    job = 2;
                                } else if (splitted[1].equalsIgnoreCase("p")) {
                                    job = 5;
                                } else if (splitted[1].equalsIgnoreCase("bg")) {
                                    job = 0;
                                } else {
                                    mc.dropMessage("Syntax: @stamp <w/t/b/m/p/bg>");
                                    return;
                                }
                                job = job * 100;
                                player.setStampJob(job);
                                mc.dropMessage("You've changed your stamp job to " + MapleJob.getJobName(job));
                            } else {
                                mc.dropMessage("You do not have a stump stamp or a dark lord stamp");
                            }
                        } else {
                            mc.dropMessage("Syntax : @stamp / @stamp job <w/t/b/m/p/bg>");
                        }
                    } else {
                        mc.dropMessage("You do not have a special stamp");
                    }
                } else if (splitted[0].equalsIgnoreCase("@giveup")) {
                    player.giveUp();
                } else if (splitted[0].equalsIgnoreCase("@buddy")) {
                    if (splitted[1].equalsIgnoreCase("add")) {
                        c.getPlayer().resetAfkTime();
                        WorldChannelInterface worldInterface = c.getChannelServer().getWorldInterface();
                        BuddyList buddylist = player.getBuddylist();
                        String addName = splitted[2];
                        if (addName.toLowerCase().equalsIgnoreCase(player.getName().toLowerCase())) {
                            player.dropMessage(1, "You can't buddy yourself!");
                            return;
                        }
                        BuddylistEntry ble = buddylist.get(addName);
                        if (ble != null && !ble.isVisible()) {
                            c.getSession().write(MaplePacketCreator.buddylistMessage((byte) 13));
                        } else if (buddylist.isFull()) {
                            c.getSession().write(MaplePacketCreator.buddylistMessage((byte) 11));
                        } else {
                            try {
                                BuddylistModifyHandler.CharacterIdNameBuddyCapacity charWithId = null;
                                int channel;
                                MapleCharacter otherChar = c.getChannelServer().getPlayerStorage().getCharacterByName(addName);
                                if (otherChar != null) {
                                    channel = c.getChannel();
                                    charWithId = new BuddylistModifyHandler.CharacterIdNameBuddyCapacity(otherChar.getId(), otherChar.getName(), otherChar.getBuddylist().getCapacity());
                                } else {
                                    channel = worldInterface.find(addName);
                                    charWithId = BuddylistModifyHandler.class.newInstance().getCharacterIdAndNameFromDatabase(addName);
                                }

                                if (charWithId != null) {
                                    BuddyAddResult buddyAddResult = null;
                                    if (channel != -1) {
                                        ChannelWorldInterface channelInterface = worldInterface.getChannelInterface(channel);
                                        buddyAddResult = channelInterface.requestBuddyAdd(addName, c.getChannel(), player.getId(), player.getName());
                                    } else {
                                        Connection con = DatabaseConnection.getConnection();
                                        PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) as buddyCount FROM buddies WHERE characterid = ? AND pending = 0");
                                        ps.setInt(1, charWithId.getId());
                                        ResultSet rs = ps.executeQuery();
                                        if (!rs.next()) {
                                            throw new RuntimeException("Result set expected");
                                        } else {
                                            int count = rs.getInt("buddyCount");
                                            if (count >= charWithId.getBuddyCapacity()) {
                                                buddyAddResult = BuddyAddResult.BUDDYLIST_FULL;
                                            }
                                        }
                                        rs.close();
                                        ps.close();
                                        ps = con.prepareStatement("SELECT pending FROM buddies WHERE characterid = ? AND buddyid = ?");
                                        ps.setInt(1, charWithId.getId());
                                        ps.setInt(2, player.getId());
                                        rs = ps.executeQuery();
                                        if (rs.next()) {
                                            buddyAddResult = BuddyAddResult.ALREADY_ON_LIST;
                                        }
                                        rs.close();
                                        ps.close();
                                    }
                                    if (buddyAddResult == BuddyAddResult.BUDDYLIST_FULL) {
                                        c.getSession().write(MaplePacketCreator.buddylistMessage((byte) 12));
                                    } else {
                                        int displayChannel = -1;
                                        int otherCid = charWithId.getId();
                                        if (buddyAddResult == BuddyAddResult.ALREADY_ON_LIST && channel != -1) {
                                            displayChannel = channel;
                                            BuddylistModifyHandler.class.newInstance().notifyRemoteChannel(c, channel, otherCid, BuddyOperation.ADDED);
                                        } else if (buddyAddResult != BuddyAddResult.ALREADY_ON_LIST && channel == -1) {
                                            Connection con = DatabaseConnection.getConnection();
                                            PreparedStatement ps = con.prepareStatement("INSERT INTO buddies (characterid, `buddyid`, `pending`) VALUES (?, ?, 1)");
                                            ps.setInt(1, charWithId.getId());
                                            ps.setInt(2, player.getId());
                                            ps.executeUpdate();
                                            ps.close();
                                        }
                                        buddylist.put(new BuddylistEntry(charWithId.getName(), otherCid, displayChannel, true));
                                        c.getSession().write(MaplePacketCreator.updateBuddylist(buddylist.getBuddies()));
                                    }
                                } else {
                                    c.getSession().write(MaplePacketCreator.buddylistMessage((byte) 15));
                                }
                            } catch (RemoteException e) {
                            } catch (SQLException e) {
                            }
                        }
                    } else if (splitted[1].equalsIgnoreCase("delete") || splitted[1].equalsIgnoreCase("del")) {
                        BuddyList buddylist = player.getBuddylist();
                        WorldChannelInterface worldInterface = c.getChannelServer().getWorldInterface();
                        int victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[2]).getId();
                        if (buddylist.containsVisible(victim)) {
                            try {
                                BuddylistModifyHandler.class.newInstance().notifyRemoteChannel(c, worldInterface.find(victim), victim, BuddyOperation.DELETED);

                            } catch (RemoteException e) {
                            }
                        }
                        buddylist.remove(victim);
                        c.getSession().write(MaplePacketCreator.updateBuddylist(player.getBuddylist().getBuddies()));
                        BuddylistModifyHandler.class.newInstance().nextPendingRequest(c);
                    } else {
                        mc.dropMessage("Syntax : @buddy <add/del> <IGN>");
                    }
                } else if (splitted[0].equals("@expfix")) {
                    player.setExp(0);
                    player.updateSingleStat(MapleStat.EXP, player.getExp());
                } else if (splitted[0].equals("@dispose")) {
                    NPCScriptManager.getInstance().dispose(c);
                    mc.dropMessage("You have been disposed.");
                } else if (splitted[0].equals("@online") || (splitted[0].equals("!online"))) {
                    int tot = 0;
                    for (ChannelServer cs : ChannelServer.getAllInstances()) {
                        if (cs.getPlayerStorage().getAllCharacters().size() > 0) {
                            StringBuilder sb = new StringBuilder();
                            mc.dropMessage("Channel " + cs.getChannel());
                            for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters()) {
                                if (!chr.isHidden()) {
                                    tot++;
                                    if (sb.length() > 150) { // Chars per line. Could be more or less
                                        mc.dropMessage(sb.toString());
                                        sb = new StringBuilder();
                                    }
                                    sb.append(MapleCharacterUtil.makeMapleReadable(chr.getName() + "   "));
                                }
                            }
                            mc.dropMessage(sb.toString());
                        }
                    }
                    mc.dropMessage("Total : " + Integer.toString(tot));
                } else if (splitted[0].equals("@storage")) {
                    player.getStorage().sendStorage(c, 2080005);
                } else if (splitted[0].equals("@emo")) {
                    if (player.inEvent()) {
                        mc.dropMessage("You can't use this command in event maps");
                    } else {
                        if (player.getHp() != 0) {
                            String funnyemo[] = {"No one went to your funeral",
                                "Life is meaningless",
                                "What's the point of living if living is just a waste of death?",
                                "Remember kids it's down the road not across the street",
                                "You're doing it wrong",
                                "Life is like a dark abyss...",
                                "Your soul is grey",
                                "Nobody dies a virgin because in the end life fucks us all",
                                "Broken hearts never heal.",
                                "No one cared about you anyways"};
                            int random = (int) Math.floor(Math.random() * funnyemo.length);
                            player.setHp(0);
                            player.updateSingleStat(MapleStat.HP, 0);
                            mc.dropMessage(funnyemo[random]);
                        } else {
                            mc.dropMessage("You can't kill yourself. You're already dead.");
                        }
                    }
                    /* else if (splitted[0].equals("@yes") && !player.isInJail()) {
                    MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(player.getflagPromptName());
                    MapleMap map = c.getChannelServer().getMapFactory().getMap(1010000);
                    if (player.getClient().getChannelServer().getMapFactory().getMap(1010000).getCharacters().isEmpty() && !player.isFlagOn() && victim != null && !player.getflagPromptName().equalsIgnoreCase(null) && player.getMapId() != 1010000) {
                    for (MapleCharacter playerparty : cserv.getPlayerStorage().getAllCharacters()) {
                    if (playerparty.getPartyId() == player.getPartyId()) {
                    playerparty.changeMap(1010000, 5);
                    playerparty.setFlagOn(true);
                    playerparty.setBlueTeam(true);
                    }
                    }
                    for (MapleCharacter victimparty : cserv.getPlayerStorage().getAllCharacters()) {
                    if (victimparty.getPartyId() == victim.getPartyId()) {
                    victimparty.changeMap(1010000, 0);
                    victimparty.setFlagOn(true);
                    victimparty.setRedTeam(true);
                    }
                    }
                    player.setBlueTeam();
                    victim.setRedTeam();
                    map.spawnItemDrop(player, player, ii.getEquipById(4001025), c.getChannelServer().getMapFactory().getMap(1010000).getPortal(4).getPosition(), true, true);
                    cserv.getMapFactory().getMap(1010000).addMapTimer(54000, 100000000);
                    player.setJustEntered(true);
                    } else {
                    player.dropMessage(1, "You know you aren't meant to use this now.");
                    }
                    } else if (splitted[0].equalsIgnoreCase("@flag") && !player.isInJail()) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (player.getPartyId() != victim.getPartyId()) {
                    if (player.getClient().getChannelServer().getMapFactory().getMap(1010000).getCharacters().isEmpty()) {
                    if (player.getParty() != null && victim.getParty() != null) {
                    if (victim != null) {
                    victim.dropMessage(player.getName() + " and his/her's party is challenging you to capture thy flag. do you accept? Use @yes if yes. Use @no if no.");
                    player.dropMessage("The Request is pending...");
                    player.setFlagPromptName(victim.getName());
                    victim.setFlagPromptName(player.getName());
                    victim.setFlagOn(false);
                    player.setFlagOn(true);
                    } else {
                    player.dropMessage(1, "The player does not exist or is not online.");
                    }
                    } else {
                    player.dropMessage(1, "You or the person you are challenging is not in a party or offline or not in your channel.");
                    }
                    } else {
                    player.dropMessage(1, "Someone is already in Capture the Flag. Please Change Channels or wait for that party to exit.");
                    }
                    } else {
                    player.dropMessage(1, "You can't challenge someone from your own party");
                    }
                    } else if (splitted[0].equalsIgnoreCase("@no") || splitted[0].equalsIgnoreCase("@resetflag")) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(player.getflagPromptName());
                    if (!player.isFlagOn() && victim.isFlagOn() && player.getMapId() != 1010000) {
                    player.setFlagPromptName("no");
                    victim.setFlagPromptName("no");
                    player.setFlagOn(false);
                    victim.setFlagOn(false);
                    player.setHasFlag(false);
                    victim.setHasFlag(false);
                    player.dropMessage("Done");
                    victim.dropMessage("Declined.");
                    player.resetFlag();
                    } else {
                    player.dropMessage(1, "You know you aren't meant to use this right now...");
                    }

                    }*/
                } else if (splitted[0].equals("@ignore")) {
                    if (splitted.length == 2) {
                        MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                        if (victim != null && !victim.isGM()) {
                            String target = victim.getName();
                            if (player.isIgnored(target)) {
                                player.unignore(target);
                                mc.dropMessage("You stopped ignoring " + victim.getName() + ". Redo this command to stop");
                            } else {
                                player.ignore(target);
                                mc.dropMessage("You started ignoring " + victim.getName());
                            }
                        } else {
                            mc.dropMessage("Player is not online or not in the same channel as you.");
                        }
                    } else {
                        mc.dropMessage("Syntax : @ignore <ign>");
                    }
                } else if (splitted[0].equals("@spawner")) {
                    if (splitted.length == 1) {
                        NPCScriptManager.getInstance().start(c, 1032003);
                    } else if (splitted.length == 2) {
                        int num = 7;
                        try {
                            num = Integer.parseInt(splitted[1]);
                        } catch (NumberFormatException nfe) {
                            num = 7;
                        }
                        switch (num) {
                            case 1:
                                player.changeMap(912010000);
                                break;
                            case 2:
                                player.changeMap(912010200);
                                break;
                            case 3:
                                player.changeMap(980010100);
                                break;
                            case 4:
                                player.changeMap(980010200);
                                break;
                            case 5:
                                player.changeMap(980000504);
                                break;
                            case 6:
                                player.changeMap(980000604);
                                break;
                            default:
                                mc.dropMessage("Incorrect map, choose spawners 1-6");
                                break;
                        }
                    } else {
                        mc.dropMessage("Sytax : @spawner <1/2/3/4/5/6>");
                    }
                } else if (splitted[0].equals("@zombie")) {
                    if (wserv.getChasey() != null) {
                        if (!wserv.getChasey().addPlayer(player.getName())) {
                            mc.dropMessage("[Chasey Event] Event is not currently available.");
                        }
                    }
                    player.unsetChaser();
                } else if (splitted[0].equals("@leave")) {
                    if (wserv.getChasey() != null) {
                        if (!wserv.getChasey().removePlayer(player)) {
                            mc.dropMessage("[Zombies vs Humans] Event is not currently on.");
                        }
                    } else {
                        mc.dropMessage("[Zombies vs Humans] Event is not currently on.");
                    }
                } else if (splitted[0].equals("@togglesmega")) {
                    player.setSmegaEnabled(!player.getSmegaEnabled());
                    mc.dropMessage((!player.getSmegaEnabled() ? "[Disable] Smegas are now disabled." : "[Enable] Smegas are now enabled."));
                } else if (splitted[0].equalsIgnoreCase("@str")) {
                    if (splitted.length == 1) {
                        return;
                    }
                    if (splitted[1].equalsIgnoreCase("all")) {
                        int newStr = player.getRemainingAp() + player.getStr();
                        if (newStr < 32767) {
                            player.setStr(newStr);
                            player.setRemainingAp(0);
                            player.updateSingleStat(MapleStat.STR, newStr);
                            player.updateSingleStat(MapleStat.AVAILABLEAP, 0);
                        } else if (newStr >= 32768) {
                            player.setStr(32767);
                            player.setRemainingAp(newStr - 32767);
                            player.updateSingleStat(MapleStat.STR, 32767);
                            player.updateSingleStat(MapleStat.AVAILABLEAP, newStr - 32767);
                        }
                    } else if (splitted[1].equalsIgnoreCase("remove")) {
                        int str = player.getStr();
                        int ap = player.getRemainingAp() + str;
                        player.updateSingleStat(MapleStat.STR, 0);
                        player.setStr(0);
                        player.setRemainingAp(ap);
                        player.updateSingleStat(MapleStat.AVAILABLEAP, ap);
                    } else {
                        int up = 0;
                        try {
                            up = Integer.parseInt(splitted[1]);
                        } catch (NumberFormatException nfe) {
                            up = 0;
                        }
                        if (up > 0 && player.getRemainingAp() >= up && player.getRemainingAp() > 0 && player.getStr() + up < 32767) {
                            player.setStr(player.getStr() + up);
                            player.setRemainingAp(player.getRemainingAp() - up);
                            player.updateSingleStat(MapleStat.STR, player.getStr());
                            player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
                            mc.dropMessage("Here are your stats!");
                        } else {
                            if (up < 0 && player.getStr() >= 4 && player.getStr() + up >= 4 && player.getStr() > -up) {
                                player.setStr(player.getStr() + up);
                                player.setRemainingAp(player.getRemainingAp() - up);
                                player.updateSingleStat(MapleStat.STR, player.getStr());
                                player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
                                mc.dropMessage("Here are your stats!");
                            } else {
                                mc.dropMessage("You don't have enough stats or that's an invalid ammount!");

                            }

                        }
                    }
                } else if (splitted[0].equalsIgnoreCase("@int")) {
                    if (splitted.length == 1) {
                        return;
                    }
                    if (splitted[1].equalsIgnoreCase("all")) {
                        int newInt = player.getRemainingAp() + player.getInt();
                        if (newInt < 32767) {
                            player.setInt(newInt);
                            player.setRemainingAp(0);
                            player.updateSingleStat(MapleStat.INT, newInt);
                            player.updateSingleStat(MapleStat.AVAILABLEAP, 0);
                        } else if (newInt >= 32768) {
                            player.setInt(32767);
                            player.setRemainingAp(newInt - 32767);
                            player.updateSingleStat(MapleStat.INT, 32767);
                            player.updateSingleStat(MapleStat.AVAILABLEAP, newInt - 32767);
                        }
                    } else if (splitted[1].equalsIgnoreCase("remove")) {
                        int Int = player.getInt();
                        int ap = player.getRemainingAp() + Int;
                        player.updateSingleStat(MapleStat.INT, 0);
                        player.setInt(0);
                        player.setRemainingAp(ap);
                        player.updateSingleStat(MapleStat.AVAILABLEAP, ap);
                    } else {
                        int up = 0;
                        try {
                            up = Integer.parseInt(splitted[1]);
                        } catch (NumberFormatException nfe) {
                            up = 0;
                        }
                        if (up > 0 && player.getRemainingAp() >= up && player.getRemainingAp() > 0 && player.getInt() + up < 30001) {
                            player.setInt(player.getInt() + up);
                            player.setRemainingAp(player.getRemainingAp() - up);
                            player.updateSingleStat(MapleStat.INT, player.getInt());
                            player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
                            mc.dropMessage("Here are your stats!");
                        } else {
                            if (up < 0 && player.getInt() >= 4 && player.getInt() + up >= 4 && player.getInt() > -up) {
                                player.setInt(player.getInt() + up);
                                player.setRemainingAp(player.getRemainingAp() - up);
                                player.updateSingleStat(MapleStat.INT, player.getInt());
                                player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
                                mc.dropMessage("Here are your stats!");
                            } else {
                                mc.dropMessage("You don't have enough stats or that's an invalid ammount!");
                            }
                        }
                    }
                    //DEX
                } else if (splitted[0].equalsIgnoreCase("@dex")) {
                    if (splitted.length == 1) {
                        return;
                    }
                    if (splitted[1].equalsIgnoreCase("all")) {
                        int newDex = player.getRemainingAp() + player.getDex();
                        if (newDex < 32767) {
                            player.setDex(newDex);
                            player.setRemainingAp(0);
                            player.updateSingleStat(MapleStat.DEX, newDex);
                            player.updateSingleStat(MapleStat.AVAILABLEAP, 0);
                        } else if (newDex >= 32768) {
                            player.setDex(32767);
                            player.setRemainingAp(newDex - 32767);
                            player.updateSingleStat(MapleStat.DEX, 32767);
                            player.updateSingleStat(MapleStat.AVAILABLEAP, newDex - 32767);
                        }
                    } else if (splitted[1].equalsIgnoreCase("remove")) {
                        int dex = player.getDex();
                        int ap = player.getRemainingAp() + dex;
                        player.updateSingleStat(MapleStat.DEX, 0);
                        player.setDex(0);
                        player.setRemainingAp(ap);
                        player.updateSingleStat(MapleStat.AVAILABLEAP, ap);
                    } else {
                        int up = 0;
                        try {
                            up = Integer.parseInt(splitted[1]);
                        } catch (NumberFormatException nfe) {
                            up = 0;
                        }
                        if (up > 0 && player.getRemainingAp() >= up && player.getRemainingAp() > 0 && player.getDex() + up < 30001) {
                            player.setDex(player.getDex() + up);
                            player.setRemainingAp(player.getRemainingAp() - up);
                            player.updateSingleStat(MapleStat.DEX, player.getDex());
                            player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
                            mc.dropMessage("Here are your stats!");
                        } else {
                            if (up < 0 && player.getDex() >= 4 && player.getDex() + up >= 4 && player.getDex() > -up) {
                                player.setDex(player.getDex() + up);
                                player.setRemainingAp(player.getRemainingAp() - up);
                                player.updateSingleStat(MapleStat.DEX, player.getDex());
                                player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
                                mc.dropMessage("Here are your stats!");
                            } else {
                                mc.dropMessage("You don't have enough stats or that's an invalid ammount!");
                            }
                        }
                    }
                } else if (splitted[0].equalsIgnoreCase("@luk")) {
                    if (splitted.length == 1) {
                        return;
                    }
                    if (splitted[1].equalsIgnoreCase("all")) {
                        int newLuk = player.getRemainingAp() + player.getLuk();
                        if (newLuk < 32767) {
                            player.setLuk(newLuk);
                            player.setRemainingAp(0);
                            player.updateSingleStat(MapleStat.LUK, newLuk);
                            player.updateSingleStat(MapleStat.AVAILABLEAP, 0);
                        } else if (newLuk >= 32768) {
                            player.setLuk(32767);
                            player.setRemainingAp(newLuk - 32767);
                            player.updateSingleStat(MapleStat.LUK, 32767);
                            player.updateSingleStat(MapleStat.AVAILABLEAP, newLuk - 32767);
                        }
                    } else if (splitted[1].equalsIgnoreCase("remove")) {
                        int luk = player.getLuk();
                        int ap = player.getRemainingAp() + luk;
                        player.updateSingleStat(MapleStat.LUK, 0);
                        player.setLuk(0);
                        player.setRemainingAp(ap);
                        player.updateSingleStat(MapleStat.AVAILABLEAP, ap);
                    } else {
                        int up = 0;
                        try {
                            up = Integer.parseInt(splitted[1]);
                        } catch (NumberFormatException nfe) {
                            up = 0;
                        }
                        if (up > 0 && player.getRemainingAp() >= up && player.getRemainingAp() > 0 && player.getLuk() + up < 30001) {
                            player.setLuk(player.getLuk() + up);
                            player.setRemainingAp(player.getRemainingAp() - up);
                            player.updateSingleStat(MapleStat.LUK, player.getLuk());
                            player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
                            mc.dropMessage("Here are your stats!");
                        } else {
                            if (up < 0 && player.getLuk() >= 4 && player.getLuk() + up >= 4 && player.getLuk() > -up) {
                                player.setLuk(player.getLuk() + up);
                                player.setRemainingAp(player.getRemainingAp() - up);
                                player.updateSingleStat(MapleStat.LUK, player.getLuk());
                                player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
                                mc.dropMessage("Here are your stats!");
                            } else {
                                mc.dropMessage("You don't have enough stats or that's an invalid ammount!");
                            }
                        }
                    }
                } else if (splitted[0].equals("@gmmsg")) {
                    if (splitted.length < 2) {
                        return;
                    }

                    c.getChannelServer().getWorldInterface().broadcastGMMessage(null, MaplePacketCreator.serverNotice(6, "Channel: " + c.getChannel() + "  " + player.getName() + ": " + StringUtil.joinStringFrom(splitted, 1)).getBytes());

                    mc.dropMessage("Message sent.");
                } else if (splitted[0].equals("@goafk")) {
                    if (!player.inEvent() || !player.getMap().isMuted() || player.getCanTalk()) {
                        player.setChalkboard("I'm AFK! Drop me a message!");
                    } else {
                        mc.dropMessage("If you want to AFK, Get out the event. Otherwise, you are muted.");
                    }
                } else if (splitted[0].equals("@revive")) {
                    if (player.inEvent()) {
                        mc.dropMessage("You can't use this command in event maps");
                    } else {
                        if (splitted.length == 2) {
                            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
                            if (player != victim) {
                                if (!victim.isInJail() && !victim.inEvent()) {
                                    if (player.getMeso() >= 500000000) { // 500 mil
                                        if (victim != null) {
                                            if (!victim.isAlive()) {
                                                victim.setHp((victim.getMaxHp() / 2));
                                                player.gainMeso(-500000000);
                                                victim.updateSingleStat(MapleStat.HP, (victim.getMaxHp() / 2));
                                                mc.dropMessage("You have revived " + victim.getName() + ".");
                                            } else {
                                                mc.dropMessage(victim.getName() + " is not dead.");
                                            }
                                        } else {
                                            mc.dropMessage("The player is not online.");
                                        }
                                    } else {
                                        mc.dropMessage("You can't revive someone in that map");
                                    }
                                } else {
                                    mc.dropMessage("You need 50 million mesos to execute revival.");
                                }
                            } else {
                                mc.dropMessage("You can't revive yourself.");
                            }
                        } else {
                            mc.dropMessage("Syntax: @revive <player name>");
                        }
                    }
                } else if (splitted[0].equals("@afkworld")) {
                    int afkCount = 0;
                    int count = 0;
                    for (ChannelServer cservs : ChannelServer.getAllInstances()) {
                        for (MapleCharacter players : cservs.getPlayerStorage().getAllCharacters()) {
                            if (!players.isHidden()) {
                                long afkTime = System.currentTimeMillis() - players.getAfkTime();
                                if (Math.floor(afkTime / 120000) > 0) {
                                    afkCount++;
                                }
                                count++;
                            }
                        }
                    }
                    mc.dropMessage("There are: " + afkCount + "/" + count + " players AFK for longer than two minutes");
                } else if (splitted[0].equals("@afk")) {
                    if (splitted.length >= 2) {
                        String name = splitted[1];
                        MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(name);
                        if (victim == null) {
                            try {
                                WorldChannelInterface wci = c.getChannelServer().getWorldInterface();
                                int channel = wci.find(name);
                                if (channel == -1 || victim.isGM() || wci.find(player.getName()) != channel) {
                                    mc.dropMessage("This player is not online or is in another channel.");
                                    return;
                                }
                                victim = ChannelServer.getInstance(channel).getPlayerStorage().getCharacterByName(name);
                            } catch (RemoteException re) {
                                c.getChannelServer().reconnectWorld();
                            }
                        }
                        if (victim.isHidden()) {
                            mc.dropMessage("This player is not online or is in another channel.");
                        } else {
                            long blahblah = System.currentTimeMillis() - victim.getAfkTime();
                            if (Math.floor(blahblah / 60000) == 0) { // less than a minute
                                mc.dropMessage("Player has not been afk!");
                            } else {
                                StringBuilder sb = new StringBuilder();
                                sb.append(victim.getName());
                                sb.append(" has been afk for");
                                compareTime(sb, blahblah);
                                mc.dropMessage(sb.toString());
                            }
                        }
                    } else {
                        mc.dropMessage("Incorrect Syntax.");
                    }
                } else if (splitted[0].equals("@latestplayers")) {
                    mc.dropMessage("10 Latest Players:");
                    for (String plr : player.getLatestPlayers()) {
                        mc.dropMessage(plr);
                    }
                } else if (splitted[0].equals("@onlinetime")) {
                    if (splitted.length == 2) {
                        String name = splitted[1];
                        MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(name);
                        if (victim == null) {
                            try {
                                WorldChannelInterface wci = c.getChannelServer().getWorldInterface();
                                int channel = wci.find(name);
                                if (channel == -1 || victim.isGM()) {
                                    mc.dropMessage("This player is not online.");
                                    return;
                                }
                                victim = ChannelServer.getInstance(channel).getPlayerStorage().getCharacterByName(name);
                            } catch (RemoteException re) {
                                c.getChannelServer().reconnectWorld();
                            }
                        }
                        long blahblah = System.currentTimeMillis() - victim.getLastLogin();
                        StringBuilder sb = new StringBuilder();
                        sb.append(victim.getName());
                        sb.append(" has been online for");
                        compareTime(sb, blahblah);
                        mc.dropMessage(sb.toString());
                    } else {
                        long blahblah = System.currentTimeMillis() - player.getLastLogin();
                        StringBuilder sb = new StringBuilder();
                        sb.append("You have been online for");
                        compareTime(sb, blahblah);
                        mc.dropMessage(sb.toString());
                    }
                } else if (splitted[0].equals("@refill")) {
                    if (player.getMapId() == 260000201) {
                        if (player.getItemQuantity(2100067, false) > 0) {
                            player.gainItem(2100067, 10);
                            player.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                            player.getMap().broadcastMessage(player, MaplePacketCreator.showChair(player.getId(), 0), false);
                            player.giveDebuff(MapleDisease.getType(123), MobSkillFactory.getMobSkill(123, 1));
                            player.getMap().broadcastMessage(MaplePacketCreator.sendYellowTip("[BOMBERMAN] " + player.getName() + " has just refilled his ammunition!"));
                        } else {
                            mc.dropMessage("You already have some bombs");
                        }
                    } else {
                        mc.dropMessage("You can only do this during bomberman");
                    }
                } else if (splitted[0].equalsIgnoreCase("@jq")) {
                    /*             // remember to check if they are already jqing or already in a JQ.
                    int[] gotomapid = {
                    109040000, //MapleStory Fitness Test
                    109040001, //MapleStory Fitness Test 1
                    101000100, //Forest of Patience 1
                    682000200, //Ghost Chimney
                    280020000, //Breath of Lava 1
                    105040310, //Deep Forest of Patience 1
                    103000900, //b1
                    103000903, //b2
                    103000906, //b3
                    610020001, // Valley of Heroes 1
                    220000006, // Ludibrium Pet Walkway
                    100000202, // Pet-Walking Road
                    };
                    String[] gotomapname = {
                    "fitness",
                    "fitness1",
                    "fop",
                    "gc",
                    "lava1",
                    "dfop",
                    "b1",
                    "b2",
                    "b3",
                    "voh1",
                    "ludi",
                    "hene"
                    };*/
                    if (!player.getMap().isJQ()) {
                        NPCScriptManager.getInstance().start(c, 9300006);
                    } else {
                        mc.dropMessage("Finish this JQ first!");
                    }
                } else if (splitted[0].equals("@all") || splitted[0].equals("@fmnpc")) {
                    if (player.isJQing()) {
                        player.giveUp();
                        return;
                    }
                    if (player.inEvent()) {
                        mc.dropMessage("You can't use this command here. You're meant to answer questions fairly");
                    } else {
                        NPCScriptManager.getInstance().start(c, 22000);
                    }
                } else if (splitted[0].equals("@job") || splitted[0].equals("@cody")) {
                    NPCScriptManager.getInstance().start(c, 9200000);
                } else if (splitted[0].equals("@shop")) {
                    if (player.inEvent()) {
                        mc.dropMessage("You can't use this command here. You're meant to answer questions fairly");
                    } else {
                        NPCScriptManager.getInstance().start(c, 1061008);
                    }
                } else if (splitted[0].equalsIgnoreCase("@reset")) {
                    NPCScriptManager.getInstance().start(c, 2003);
                } else if (splitted[0].equalsIgnoreCase("@hideout")) {
                    if (!player.getGuild().hideoutExpired()) {
                        player.changeMap(player.getGuild().getHideout());
                        mc.dropMessage("Warped to your guild's hideout");
                    } else {
                        mc.dropMessage("You either do not have a hideout, or it is expired");
                    }
                } else if (splitted[0].equalsIgnoreCase("@go") || splitted[0].equalsIgnoreCase("@spinel") && !player.inEvent()) {
                    if (player.isJQing()) {
                        player.giveUp();
                        return;
                    }
                    HashMap<String, Integer> maps = new HashMap<String, Integer>();
                    maps.put("fm", 910000000);
                    maps.put("henesys", 100000000);
                    maps.put("ellinia", 101000000);
                    maps.put("perion", 102000000);
                    maps.put("kerning", 103000000);
                    maps.put("lith", 104000000);
                    maps.put("sleepywood", 105040300);
                    maps.put("florina", 110000000);
                    maps.put("orbis", 200000000);
                    maps.put("elnath", 211000000);
                    maps.put("ludi", 220000000);
                    maps.put("aqua", 230000000);
                    maps.put("leafre", 240000000);
                    maps.put("mulung", 250000000);
                    maps.put("herb", 251000000);
                    maps.put("omega", 221000000);
                    maps.put("korean", 222000000);
                    maps.put("nlc", 600000000);
                    maps.put("excavation", 990000000);
                    maps.put("mushmom", 100000005);
                    maps.put("griffey", 240020101);
                    maps.put("manon", 240020401);
                    maps.put("horseman", 682000001);
                    maps.put("pianus", 230040420);
                    maps.put("horntail", 240060200);
                    maps.put("balrog", 105090900);
                    maps.put("zakum", 280030000);
                    maps.put("papu", 220080001);
                    maps.put("showa", 801000000);
                    maps.put("guild", 200000301);
                    maps.put("shrine", 800000000);
                    maps.put("skele", 240040511);
                    maps.put("nest", 120000000);
                    maps.put("cbd", 540000000);
                    maps.put("nautilus", 120000000); // nautilus
                    maps.put("amoria", 680000000);
                    maps.put("ariant", 260000200);
                    maps.put("dead", 610010004);
                    maps.put("haunted", 682000000);
                    maps.put("fm1", 910000001);
                    maps.put("fm2", 910000002);
                    maps.put("fm3", 910000003);
                    maps.put("fm4", 910000004);
                    maps.put("fm5", 910000005);
                    maps.put("fm6", 910000006);
                    maps.put("fm7", 910000007);
                    maps.put("fm8", 910000008);
                    maps.put("fm9", 910000009);
                    maps.put("fm10", 910000010);
                    maps.put("fm11", 910000011);
                    maps.put("fm12", 910000012);
                    maps.put("fm13", 910000013);
                    maps.put("fm14", 910000014);
                    maps.put("fm15", 910000015);
                    maps.put("fm16", 910000016);
                    maps.put("fm17", 910000017);
                    maps.put("fm18", 910000018);
                    maps.put("fm19", 910000019);
                    maps.put("fm20", 910000020);
                    maps.put("fm21", 910000021);
                    maps.put("fm22", 910000022);
                    maps.put("casino", 925100000);

                    if (splitted.length == 1) {
                        NPCScriptManager.getInstance().start(c, 9000020);
                    } else if (splitted.length == 2) {
                        String smap = splitted[1].toLowerCase();
                        if (maps.containsKey(smap)) {
                            int map = maps.get(smap);
                            if (map == 910000000) {
                                player.saveLocation(SavedLocationType.FREE_MARKET);
                            }
                            player.changeMap(map);
                            mc.dropMessage("Please feel free to suggest any more locations.");
                        } else {
                            mc.dropMessage("I could not find the map that you requested, make sure the name is correct.");
                        }
                        maps.clear();
                    } else {
                        StringBuilder builder = new StringBuilder("Syntax: @go <mapname>");
                        int i = 0;
                        for (String mapss : maps.keySet()) {
                            if (1 % 10 == 0) { // 10 maps per line
                                mc.dropMessage(builder.toString());
                            } else {
                                builder.append(mapss + ", ");
                            }
                        }
                        mc.dropMessage(builder.toString());
                    }
                } else if (splitted[0].equals("!gmsonlinew")) {
                    StringBuilder sb = new StringBuilder("GMs online: ");
                    mc.dropMessage(sb.toString());
                    for (ChannelServer cs : ChannelServer.getAllInstances()) {
                        sb = new StringBuilder("[Channel " + cs.getChannel() + "]");
                        mc.dropMessage(sb.toString());
                        sb = new StringBuilder();
                        for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters()) {
                            if (chr.getGMLevel() > 1) {
                                if (sb.length() > 150) {
                                    sb.setLength(sb.length() - 2);
                                    mc.dropMessage(sb.toString());
                                    sb = new StringBuilder();
                                }
                                sb.append(MapleCharacterUtil.makeMapleReadable(chr.getName()));
                                sb.append(", ");
                            }
                        }
                        if (sb.length() >= 2) {
                            sb.setLength(sb.length() - 2);
                        }
                        mc.dropMessage(sb.toString());
                    }
                } else if ((splitted[0].equalsIgnoreCase("@donate") || splitted[0].equalsIgnoreCase("moneybags")) && !player.inEvent() && !player.getMap().isJQ()) {
                    NPCScriptManager.getInstance().start(c, 9300010);
                } else if (splitted[0].equalsIgnoreCase("@nimakin") && player.getMapId() != 109010100 && !player.inEvent()) {
                    NPCScriptManager.getInstance().start(c, 9900001);
                } else if (splitted[0].equalsIgnoreCase("@pvp")) {
                    NPCScriptManager.getInstance().start(c, 9201073);
                } else if (splitted[0].equalsIgnoreCase("@joko")) {
                    NPCScriptManager.getInstance().start(c, 9201097);
                } else if (splitted[0].equalsIgnoreCase("@kin") && player.getMapId() != 109010100 && !player.inEvent()) {
                    NPCScriptManager.getInstance().start(c, 9900000);
                } else if ((splitted[0].equalsIgnoreCase("@buffme") || splitted[0].equalsIgnoreCase("@buff")) && player.isAlive()) {
                    if (splitted.length == 1) {
                        if (player.getBuff().equals("none")) {
                            mc.dropMessage("You don't have any buffs set, use '@buff set' to set your buffs.");
                            return;
                        }
                        if (player.hasMorph()) {
                            mc.dropMessage("You cannot buff while you are morphed");
                            return;
                        }
                        String[] buffs = player.getBuff().split(",");
                        for (String s : buffs) {
                            int i = Integer.parseInt(s);
                            SkillFactory.getSkill(i).getEffect(SkillFactory.getSkill(i).getMaxLevel()).applyTo(player);
                        }
                    } else {
                        NPCScriptManager.getInstance().start(c, 9201091);
                    }

                } else if (splitted[0].equalsIgnoreCase("@p2p")) {
                    String commands[] = {"sex", "cyberslap", "vroom", "lapdance", "kanyewest", "singlish", "climax", "cybersex", "1337", "hack", "wallet", "gmhat", "fakeevent", "touch", "pedobear", "roar", "wtfbbq", "eat", "noob", "pwn", "chopstick", "lick", "shoot", "cum", "hugo", "pokemon", "vista", "stfu", "tear", "served", "itson", "chucknorris", "nudge", "poke", "pan", "murder", "steal", "educate ,moderator", "rape", "smexy", "love", "hate", "torture", "masturbate", "lame", "bitch", "kiss", "fap", "hug", "cuddle", "mangasm", "sammich", "cms", "fakegm", "cool", "ditch", "facebook", "twitter", "paperbag", "stfu", "kitchen", "play", "hump", "picture", "sexyback", "reproduce", "yomama", "bigmac", "hooker", "terrorist", "call", "hit", "spank", "seduce", "slap", "grope", "punch", "choke", "pet"};
                    mc.dropMessage("============================================================");
                    mc.dropMessage("                     P2P Commands - Created by the VoidDev Community");
                    mc.dropMessage("============================================================");
                    for (int i = 0; i < commands.length; i += 3) {
                        if (i + 3 < commands.length) {
                            mc.dropMessage("@" + commands[i] + "  @" + commands[i + 1] + "  @" + commands[i + 2]);
                        }
                    }
                } else if (splitted[0].equalsIgnoreCase("@pokemon") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " has chosen you!"));
                } else if (splitted[0].equalsIgnoreCase("@vista") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " tried to open two programs at the same time on your vista computer! You're going to get the Blue Screen of Death in 10 seconds"));
                } else if (splitted[0].equalsIgnoreCase("@stfu") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " stapled your tongue into a table!"));
                } else if (splitted[0].equalsIgnoreCase("@tear") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " ripped out your genital area!"));
                } else if (splitted[0].equalsIgnoreCase("@itson") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "You, " + player.getName() + " outside, now. It's on!"));
                } else if (splitted[0].equalsIgnoreCase("@chucknorris") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " got Chuck Norris to round-house kick you in the face. (Your remains lay scattered around VoidMS)"));
                } else if (splitted[0].equalsIgnoreCase("@nudge") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " sent you a nudge"));
                } else if (splitted[0].equalsIgnoreCase("@poke") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " poked you in the butt"));
                } else if (splitted[0].equalsIgnoreCase("@pan") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " hit you over the head with a frying pan!"));
                } else if (splitted[0].equalsIgnoreCase("@murder") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "You have been killed by " + player.getName() + "!"));
                } else if (splitted[0].equalsIgnoreCase("@steal") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "You have successfuly transfered your slime stamps to " + player.getName() + "!"));
                } else if (splitted[0].equalsIgnoreCase("@educate") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "You've been given an epic lecture by " + player.getName() + "!"));
                } else if (splitted[0].equalsIgnoreCase("@moderator") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " turned you into a moderator. Check out the forum!"));
                } else if (splitted[0].equalsIgnoreCase("@rape") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " raped you! (You feel violated)"));
                } else if (splitted[0].equalsIgnoreCase("@accent") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " thinks your accent is dreamy."));
                } else if (splitted[0].equalsIgnoreCase("@smexy") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " thinks you are smexy!"));
                } else if (splitted[0].equalsIgnoreCase("@love") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " loves you!"));
                } else if (splitted[0].equalsIgnoreCase("@hate") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " hates you!"));
                } else if (splitted[0].equalsIgnoreCase("@torture") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " tortured you! (You beg for mercy)"));
                } else if (splitted[0].equalsIgnoreCase("@masturbate") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " helped you masturbate!"));
                } else if (splitted[0].equalsIgnoreCase("@lame") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " and everyone else thinks you are lame!"));
                } else if (splitted[0].equalsIgnoreCase("@bitch") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " thinks you are a bitch!"));
                } else if (splitted[0].equalsIgnoreCase("@kiss") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " kissed you sensually! (You beg for more)"));
                } else if (splitted[0].equalsIgnoreCase("@cuddle") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " cuddled you! (you feel warm and fuzzy)"));
                } else if (splitted[0].equalsIgnoreCase("@hug") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " hugged you!"));
                } else if (splitted[0].equalsIgnoreCase("@sex") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    player.achieve(Achievement.FSEX);
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " sexed you!"));
                } else if (splitted[0].equalsIgnoreCase("@cyberslap") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " given you a big pimpin' cyber slap!"));
                } else if (splitted[0].equalsIgnoreCase("@vroom") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " went vroom vroom into your butt!"));
                } else if (splitted[0].equalsIgnoreCase("@mangasm") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " stared at you with a manly stare, and jizzed."));
                } else if (splitted[0].equalsIgnoreCase("@lapdance") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " gave u a sexy lapdance"));
                } else if (splitted[0].equalsIgnoreCase("@kanyewest") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "Imma let you finish, but " + player.getName() + " is one of the best VoidMs players of all time!"));
                } else if (splitted[0].equalsIgnoreCase("@singlish") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " ish thinkin yu ar so 1337 wa lao"));
                } else if (splitted[0].equalsIgnoreCase("@bicboi") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "Hey BicBoi " + player.getName() + " is on sale for 50 dollah!"));
                } else if (splitted[0].equalsIgnoreCase("@climax") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " missed, making you all sticky!"));
                } else if (splitted[0].equalsIgnoreCase("@1337") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "You now have 1337 rebirths due to " + player.getName() + "'s hacking!"));
                } else if (splitted[0].equalsIgnoreCase("@cybersex") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " asked you to sex his pixels!"));
                } else if (splitted[0].equalsIgnoreCase("@wallet") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, " Your wallet has just been stolen by " + player.getName() + "!"));
                } else if (splitted[0].equalsIgnoreCase("@hack") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "You have been hacked by " + player.getName() + "!"));
                } else if (splitted[0].equalsIgnoreCase("@gmhat") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "Congratulations! You have won a free GM Hat from " + player.getName() + ". If you didn't get it please do not ask the staff for another one."));
                } else if (splitted[0].equalsIgnoreCase("@fakeevent") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "[event] : Type @emo in the next 5 seconds to become a GM! You're competing with " + player.getName() + " so HURRY!"));
                } else if (splitted[0].equalsIgnoreCase("@touch") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "You have been touched by " + player.getName()));
                } else if (splitted[0].equalsIgnoreCase("@pedobear") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "Watch out! " + player.getName() + " is watching you through the bushes"));
                } else if (splitted[0].equalsIgnoreCase("@roar") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "You have been roared at by " + player.getName()));
                } else if (splitted[0].equalsIgnoreCase("@wtfbbq") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "You have been wtfbbq'd by " + player.getName()));
                } else if (splitted[0].equalsIgnoreCase("@eat") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "You have been eaten by " + player.getName() + "!"));
                } else if (splitted[0].equalsIgnoreCase("@noob") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "Apparently " + player.getName() + " thinks you are a noob!"));
                } else if (splitted[0].equalsIgnoreCase("@pwn") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "You have been pwned by " + player.getName() + "!"));
                } else if (splitted[0].equalsIgnoreCase("@chopstick") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " threw a chopstick at you!"));
                } else if (splitted[0].equalsIgnoreCase("@lick") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "You have been licked all over by " + player.getName() + "!"));
                } else if (splitted[0].equalsIgnoreCase("@shoot") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " popped a cap in your ass!"));
                } else if (splitted[0].equalsIgnoreCase("@cum") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " came all over you! (it's so sticky you can't open your mouth)"));
                } else if (splitted[0].equalsIgnoreCase("@hugo") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " forced Hugo up your ass!"));
                } else if (splitted[0].equalsIgnoreCase("@davidm") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " got DavidM to slam yo' nigga' ass hard in the paint."));
                } else if (splitted[0].equalsIgnoreCase("@served") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "You just got servvvedddd by " + player.getName() + "!"));
                } else if (splitted[0].equalsIgnoreCase("@murder") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "You have been killed by " + player.getName() + "!"));
                    //start of a p2p command
                } else if (splitted[0].equalsIgnoreCase("@fap") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " suggests you go home and fap some more"));
                    // end of a p2p command (start of next one)
                } else if (splitted[0].equalsIgnoreCase("@sammich") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " made you a sandwich!"));
                } else if (splitted[0].equalsIgnoreCase("@cms") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " shoved a massive Cock Meat Sandwich down your throat!"));
                } else if (splitted[0].equalsIgnoreCase("@fakegm") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " promoted you to a GM!"));
                } else if (splitted[0].equalsIgnoreCase("@ditch") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " ditched you"));
                } else if (splitted[0].equalsIgnoreCase("@cool") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " thinks you're cool!"));
                } else if (splitted[0].equalsIgnoreCase("@twitter") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " is now following you on Twitter."));
                } else if (splitted[0].equalsIgnoreCase("@facebook") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " is stalking you on facebook"));
                } else if (splitted[0].equalsIgnoreCase("@paperbag") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " suggests you cover your face with a paper bag"));
                } else if (splitted[0].equalsIgnoreCase("@stfu") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " thinks you should shut up"));
                } else if (splitted[0].equalsIgnoreCase("@yomama") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " just told a really lame Yo Mama joke"));
                } else if (splitted[0].equalsIgnoreCase("@kitchen") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " wants you to go back into the kitchen, woman!"));
                } else if (splitted[0].equalsIgnoreCase("@play") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " wants to play with you"));
                } else if (splitted[0].equalsIgnoreCase("@reproduce") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " thinks that the two of you should reproduce"));
                } else if (splitted[0].equalsIgnoreCase("@picture") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " took your picture"));
                } else if (splitted[0].equalsIgnoreCase("@hump") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " humped you"));
                } else if (splitted[0].equalsIgnoreCase("@sexyback") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " thinks you're bringing sexy back"));
                } else if (splitted[0].equalsIgnoreCase("@bigmac") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " shoved a Big Mac down your throat!"));
                } else if (splitted[0].equalsIgnoreCase("@hooker") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " thinks you're a hooker"));
                } else if (splitted[0].equalsIgnoreCase("@terrorist") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " threw a bomb at you!"));
                } else if (splitted[0].equalsIgnoreCase("@call") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " called you!"));
                } else if (splitted[0].equalsIgnoreCase("@hit") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " hit you!"));
                } else if (splitted[0].equalsIgnoreCase("@spank") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " spanked you!"));
                } else if (splitted[0].equalsIgnoreCase("@seduce") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " made an obscence gesture with " + (victim.getGender() == 1 ? "her" : "his") + " pelvic region. You feel aroused."));
                } else if (splitted[0].equalsIgnoreCase("@slap") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " slapped you!"));
                } else if (splitted[0].equalsIgnoreCase("@grope") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " groped your breasts!"));
                } else if (splitted[0].equalsIgnoreCase("@punch") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                    } else {
                        victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " punched you, right in the face"));
                    }
                } else if (splitted[0].equalsIgnoreCase("@choke") && splitted.length > 0) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " came up from behind and choked you! You're looking a bit blue"));
                } else if (splitted[0].equalsIgnoreCase("@pat") && splitted.length > 0) { // was checking if there wajusts any other command called "@pet". Since there's not.... we go..
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim == null || victim.isIgnored(player.getName())) {
                        mc.dropMessage("Player not online");
                        return;
                    }
                    victim.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " has just patted you, you purr softly"));

                    // end of p2p commands
                } else if (splitted[0].equalsIgnoreCase("@time")) {
                    int dow = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                    int dom = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    String hour = Integer.toString(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                    String minute = Integer.toString(Calendar.getInstance().get(Calendar.MINUTE));
                    String second = Integer.toString(Calendar.getInstance().get(Calendar.SECOND));
                    String dayofthemonth = Integer.toString(dom);
                    String foo = "th";
                    switch (dom) {
                        case 1:
                            foo = "st";
                            break;
                        case 2:
                            foo = "nd";
                            break;
                        case 3:
                            foo = "rd";
                            break;
                    }
                    String str = " the " + dayofthemonth + foo + ". The current server time is " + (hour.length() == 2 ? hour : "0" + hour) + ":" + (minute.length() == 2 ? minute : "0" + minute) + ":" + (second.length() == 2 ? second : second + "0") + ".";
                    String day = "";
                    switch (dow) {
                        case 1:
                            day = "Sunday";
                            break;
                        case 2:
                            day = "Monday";
                            break;
                        case 3:
                            day = "Tuesday";
                            break;
                        case 4:
                            day = "Wednesday";
                            break;
                        case 5:
                            day = "Thursday";
                            break;
                        case 6:
                            day = "Friday";
                            break;
                        case 7:
                            day = "Saturday";
                            break;
                    }
                    mc.dropMessage("The current server date is " + day + str);
                } else if (splitted[0].equals("@exchange")) {
                    if (splitted.length == 1) {
                        NPCScriptManager.getInstance().start(c, 1040001);
                    } else {
                        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                        String f = splitted[1];
                        String s = splitted[2];
                        int fid = -1, sid = -1, give = -1, take = -1;
                        if (f.equalsIgnoreCase("ywt") && s.equalsIgnoreCase("gwt")) {
                            fid = 4031543;
                            sid = 4031544;
                            give = 1;
                            take = 10;
                        } else if (f.equalsIgnoreCase("ywt") && s.equalsIgnoreCase("bwt")) {
                            fid = 4031543;
                            sid = 4031545;
                            give = 1;
                            take = 100;
                        } else if (f.equalsIgnoreCase("gwt") && s.equalsIgnoreCase("ywt")) {
                            fid = 4031544;
                            sid = 4031543;
                            give = 10;
                            take = 1;
                        } else if (f.equalsIgnoreCase("gwt") && s.equalsIgnoreCase("bwt")) {
                            fid = 4031544;
                            sid = 4031545;
                            give = 1;
                            take = 10;
                        } else if (f.equalsIgnoreCase("bwt") && s.equalsIgnoreCase("ywt")) {
                            fid = 4031545;
                            sid = 4031543;
                            give = 100;
                            take = 1;
                        } else if (f.equalsIgnoreCase("bwt") && s.equalsIgnoreCase("gwt")) {
                            fid = 4031545;
                            sid = 4031544;
                            give = 10;
                            take = 1;
                        } else if (f.equalsIgnoreCase("sls") && s.equalsIgnoreCase("sns")) {
                            fid = 4002003;
                            sid = 4002000;
                            give = 10;
                            take = 1;
                            mc.dropMessage("You can alternatively use @snail for this transfer");
                        } else if (f.equalsIgnoreCase("sns") && s.equalsIgnoreCase("sls")) {
                            fid = 4002000;
                            sid = 4002003;
                            give = 1;
                            take = 10;
                            mc.dropMessage("You can alternatively use @sls for this transfer");
                        } else if (f.equalsIgnoreCase("meso") && s.equalsIgnoreCase("sns")) {
                            if (player.getMeso() >= 1000000000) {
                                if (player.gainItem(4002000)) {
                                    player.gainMeso(-1000000000);
                                    mc.dropMessage("You successfully exchanged 1,000,000,000 mesos for a snail stamp");
                                } else {
                                    mc.dropMessage("You do not have enough inventory space");
                                }
                            } else {
                                mc.dropMessage("You don't have enough mesos");
                            }
                            mc.dropMessage("You can alternatively use @sns for this transfer");
                            return;
                        } else if (f.equalsIgnoreCase("sns") && s.equalsIgnoreCase("meso")) {
                            if (player.getItemQuantity(4002000, false) >= 1) {
                                if (player.getMeso() <= (Integer.MAX_VALUE - 1000000000)) {
                                    MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4002000, 1, true, true);
                                    mc.dropMessage("You successfully exchanged a snail stamp for 1,000,000,000 mesos");
                                } else {
                                    mc.dropMessage("You don't have enough meso space");
                                }
                                player.gainMeso(1000000000);
                            } else {
                                mc.dropMessage("You don't have a Snail Stamp");
                            }
                            mc.dropMessage("You can alternatively use @meso for this transfer");
                            return;
                        } else {
                            mc.dropMessage("Wrong syntax. Syntax : @exchange item1 item2. Available exchanges : ywt gwt, ywt bwt, gwt ywt, gwt bwt, bwt ywt, bwt gwt, sns sls, sls sns, meso sns, sns meso");
                            return;
                        }
                        if (player.getItemQuantity(fid, false) >= take) {
                            if (MapleInventoryManipulator.addById(c, sid, (short) give)) {
                                MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, fid, take, true, false);
                                mc.dropMessage("You successfully exchanged " + ii.getItemName(fid) + " into " + ii.getItemName(sid) + ".");
                            } else {
                                mc.dropMessage("You do not have enough space in your inventory");
                            }
                        } else {
                            mc.dropMessage("You do not have enough of " + ii.getItemName(fid) + " for this transfer");
                        }

                    }
                } else if (splitted[0].equalsIgnoreCase("@reborns") || splitted[0].equalsIgnoreCase("@ranking") || splitted[0].equalsIgnoreCase("@rank")) {
                    ResultSet rs = reborns();
                    try {
                        mc.dropMessage("Reborn Ranking Top 10:");
                        while (rs.next()) {
                            mc.dropMessage("[" + rs.getRow() + "] " + rs.getString("name") + "        |      Reborns: " + rs.getInt("reborns") + " / Level: " + rs.getInt("level"));
                        }
                        rs.close();
                    } catch (Exception ex) {
                    }
                } else if (splitted[0].equalsIgnoreCase("@pvprank") || splitted[0].equalsIgnoreCase("@pvpkills")) {
                    ResultSet rs = pvpkills();
                    try {
                        mc.dropMessage("PVP Ranking Top 10:");
                        while (rs.next()) {
                            mc.dropMessage("[" + rs.getRow() + "] " + rs.getString("name") + "        |      PvP Kills: " + rs.getInt("pvpkills"));
                        }
                        rs.close();
                    } catch (Exception ex) {
                    }
                } else if (splitted[0].equalsIgnoreCase("@inactive")) {
                    if (player.getGuildId() > 0) {
                        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT characters.name,accounts.lastlogin FROM accounts,characters WHERE characters.guildid = ? AND characters.accountid = accounts.id ORDER BY lastlogin ASC LIMIT 20");
                        ps.setInt(1, player.getGuildId());
                        ResultSet rs = ps.executeQuery();
                        mc.dropMessage("Twenty most inactive players in the guild:");
                        mc.dropMessage("------------------------------------------");
                        while (rs.next()) {
                            mc.dropMessage("Name: " + rs.getString("name") + " | Last Logged In: " + (new SimpleDateFormat("dd/MM/yyyy").format(rs.getTimestamp("lastlogin"))));
                        }
                    } else {
                        mc.dropMessage("You are not in a guild");
                    }
                } else if (splitted[0].equalsIgnoreCase("@stafflist")) {
                    try {
                        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT name, gm FROM characters WHERE gm > 1 order by gm desc");
                        ResultSet rs = ps.executeQuery();
                        String admin = "[Admins] : ";
                        String sgm = "[SuperGMs] : ";
                        String gm = "[GMs] : ";
                        String intern = "[Interns] : ";
                        while (rs.next()) {
                            int gmlvl = rs.getInt("gm");
                            String name = rs.getString("name");
                            if (gmlvl == 2) {
                                intern += name + ", ";
                            } else if (gmlvl == 3) {
                                gm += name + ", ";
                            } else if (gmlvl == 4) {
                                sgm += name + ", ";
                            } else if (gmlvl == 5) {
                                admin += name + ", ";
                            }
                        }
                        mc.dropMessage(admin);
                        mc.dropMessage(sgm);
                        mc.dropMessage(gm);
                        mc.dropMessage(intern);
                        rs.close();
                        ps.close();
                    } catch (SQLException ex) {
                    }
                } else if (splitted[0].equalsIgnoreCase("@donatorlist") || splitted[0].equalsIgnoreCase("@donorlist")) {
                    String[] gmRanks = {"[Donors]"};
                    //Admin = GM Level 5, SGM = GM Level 4, and so on...

                    //If you have only 4 levels and no Donator rank, just delete ,"[Donators]" from the above list.
                    //This will make Admin = GM Level 4, SGM = GM Level 3, and so on...
                    try {
                        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT name, gm FROM characters where gm > 0 && gm <= " + gmRanks.length + " order by gm desc, name");
                        ResultSet rs = ps.executeQuery();
                        String gmList = "";
                        for (int gmlevel = gmRanks.length; gmlevel > 0; gmlevel--) {
                            gmList = gmRanks[gmRanks.length - gmlevel] + " ";
                            while (rs.next()) {
                                if (rs.getInt("gm") == gmlevel) {
                                    gmList += rs.getString("name") + ", ";
                                    if (rs.isLast()) { //Fix.
                                        if (gmList.equals(gmRanks[gmRanks.length - gmlevel] + " ")) {
                                            gmList += "<None>";
                                        }
                                        mc.dropMessage(gmList);
                                    }
                                } else {
                                    rs.previous();
                                    if (gmList.equals(gmRanks[gmRanks.length - gmlevel] + " ")) {
                                        gmList += "<None>";
                                    }
                                    mc.dropMessage(gmList);
                                    break;
                                }
                            }
                        }
                        rs.close();
                        ps.close();
                    } catch (SQLException ex) {
                    }
                } else if (splitted[0].equals("@recharge")) {
                    MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                    int i = 0;
                    for (IItem item : player.getInventory(MapleInventoryType.USE).list()) {
                        if (ii.isRechargable(item.getItemId()) || ii.isArrowForBow(item.getItemId()) || ii.isArrowForCrossBow(item.getItemId())) {
                            if (item.getQuantity() < ii.getSlotMax(c, item.getItemId())) {
                                item.setQuantity(ii.getSlotMax(c, item.getItemId()));
                                c.getSession().write(MaplePacketCreator.updateInventorySlot(MapleInventoryType.USE, item));
                                i++;
                            }
                        }
                    }
                    if (i < 1) {
                        mc.dropMessage("You have nothing to recharge.");
                    } else {
                        mc.dropMessage("You have recharged " + Integer.toString(i) + " items");
                    }
                } else if (splitted[0].equals("@staffonline") || splitted[0].equals("@gmsonline") || splitted[0].equals("@gmonline")) {
                    StringBuilder sb = new StringBuilder("Staff online: ");
                    mc.dropMessage(sb.toString());
                    for (ChannelServer cs : ChannelServer.getAllInstances()) {
                        sb = new StringBuilder("[Channel " + cs.getChannel() + "]");
                        mc.dropMessage(sb.toString());
                        sb = new StringBuilder();
                        for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters()) {
                            if (chr.getGMLevel() > 1 && !chr.isHidden()) {
                                if (sb.length() > 150) {
                                    sb.setLength(sb.length() - 2);
                                    mc.dropMessage(sb.toString());
                                    sb = new StringBuilder();
                                }
                                sb.append(MapleCharacterUtil.makeMapleReadable(chr.getName()));
                                sb.append(", ");
                            }
                        }
                        if (sb.length() >= 2) {
                            sb.setLength(sb.length() - 2);
                        }
                        mc.dropMessage(sb.toString());
                    }
                } else if (splitted[0].equals("@meso")) {
                    if (player.getItemQuantity(4002000, false) > 0 && player.getMeso() <= (Integer.MAX_VALUE - 1000000000)) {
                        MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4002000, 1, true, false);
                        player.gainMeso(1000000000, true);
                    } else {
                        mc.dropMessage("You either don't have a snail stamps or not enough mesos");
                    }
                } else if (splitted[0].equals("@sns")) {
                    if (player.getMeso() >= 1000000000) {
                        if (MapleInventoryManipulator.addById(c, 4002000, (short) 1)) {
                            player.gainMeso(-1000000000, true);
                        } else {
                            mc.dropMessage("You don't have enough space in your inventory");
                        }
                    } else {
                        mc.dropMessage("You don't have 1 billion mesos");
                    }
                } else if (splitted[0].equals("@sls")) {
                    if (player.getItemQuantity(4002000, false) > 9) {
                        if (MapleInventoryManipulator.addById(c, 4002003, (short) 1)) {
                            MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4002000, 10, true, false);
                        } else {
                            mc.dropMessage("You don't have enough space in your inventory");
                        }
                    } else {
                        mc.dropMessage("You don't have 10 snail stamps");
                    }
                } else if (splitted[0].equals("@snail")) {
                    if (player.getItemQuantity(4002003, false) > 0) {
                        if (MapleInventoryManipulator.addById(c, 4002000, (short) 10)) {
                            MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4002003, 1, true, false);
                        } else {
                            mc.dropMessage("You don't have enough space in your inventory");
                        }
                    } else {
                        mc.dropMessage("You don't have a slime stamp");
                    }
                } else if (splitted[0].equals("@donatorsonline") || splitted[0].equals("@donorsonline")) {
                    StringBuilder sb = new StringBuilder("Donors online: ");
                    mc.dropMessage(sb.toString());
                    for (ChannelServer cs : ChannelServer.getAllInstances()) {
                        sb = new StringBuilder("[Channel " + cs.getChannel() + "]");
                        mc.dropMessage(sb.toString());
                        sb = new StringBuilder();
                        for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters()) {
                            if (chr.getGMLevel() == 1) {
                                if (sb.length() > 150) {
                                    sb.setLength(sb.length() - 2);
                                    mc.dropMessage(sb.toString());
                                    sb = new StringBuilder();
                                }
                                sb.append(MapleCharacterUtil.makeMapleReadable(chr.getName()));
                                sb.append(", ");
                            }
                        }
                        if (sb.length() >= 2) {
                            sb.setLength(sb.length() - 2);
                        }
                        mc.dropMessage(sb.toString());
                    }
                } else if (splitted[0].equals("@rebirth") || splitted[0].equals("@reborn")) {
                    if (player.getLevel() > 199) {
                        player.doReborn();
                        player.changeJob(MapleJob.getById(0));
                    } else {
                        mc.dropMessage("You must be at least level 200.");
                    }

                } else if (splitted[0].equalsIgnoreCase("@rebirthw")) {
                    if (c.getPlayer().getLevel() > 199) {
                        player.doReborn();
                        player.changeJob(MapleJob.getById(100));
                    } else {
                        mc.dropMessage("You must be level 200.");
                    }
                } else if (splitted[0].equalsIgnoreCase("@rebirthm")) {
                    if (c.getPlayer().getLevel() > 199) {
                        player.doReborn();
                        player.changeJob(MapleJob.getById(200));
                    } else {
                        mc.dropMessage("You must be level 200.");
                    }
                } else if (splitted[0].equalsIgnoreCase("@rebirthb")) {
                    if (player.getLevel() > 199) {
                        player.doReborn();
                        player.changeJob(MapleJob.getById(300));
                    } else {
                        mc.dropMessage("You must be at least level 200.");
                    }
                } else if (splitted[0].equalsIgnoreCase("@rebirtht")) {
                    if (c.getPlayer().getLevel() > 199) {
                        player.doReborn();
                        player.changeJob(MapleJob.getById(400));
                    } else {
                        mc.dropMessage("You must be level 200.");
                    }
                } else if (splitted[0].equalsIgnoreCase("@rebirthp")) {
                    if (player.getLevel() > 199) {
                        player.doReborn();
                        player.changeJob(MapleJob.getById(500));
                    } else {
                        mc.dropMessage("You must be at least level 200.");
                    }
                } else if (splitted[0].equals("@clearslot")) {
                    if (splitted.length == 2) {
                        if (player.isWarned()) {
                            if (splitted[1].equalsIgnoreCase("all")) {
                                clearSlot(c, 1);
                                clearSlot(c, 2);
                                clearSlot(c, 3);
                                clearSlot(c, 4);
                                clearSlot(c, 5);
                            } else if (splitted[1].equalsIgnoreCase("equip")) {
                                clearSlot(c, 1);
                            } else if (splitted[1].equalsIgnoreCase("use")) {
                                clearSlot(c, 2);
                            } else if (splitted[1].equalsIgnoreCase("etc")) {
                                clearSlot(c, 3);
                            } else if (splitted[1].equalsIgnoreCase("setup")) {
                                clearSlot(c, 4);
                            } else if (splitted[1].equalsIgnoreCase("cash")) {
                                clearSlot(c, 5);
                            } else {
                                mc.dropMessage("!clearslot " + splitted[1] + " does not exist!");
                                return;
                            }
                            mc.dropMessage(splitted[1] + " inventory wiped.");
                            player.setWarned(false);
                        } else {
                            mc.dropMessage("This command will wipe all of a selected inventory. If you want to proceed please re-use the command");
                            player.setWarned(true);
                        }
                    } else {
                        mc.dropMessage("syntax : @clearslot <all/equip/use/etc/setup/cash>");
                    }
                } else if (splitted[0].equalsIgnoreCase("@eventstatus") || splitted[0].equalsIgnoreCase("@eventinfo")) {
                    if (wserv.hasEvent()) {
                        mc.dropMessage("Event Infomation:");
                        mc.dropMessage("Channel: " + wserv.getEventChannel());
                        mc.dropMessage("Hoster: " + wserv.getEventHost());
                        mc.dropMessage("Description: " + wserv.getEventName());
                        mc.dropMessage("Event Map: " + cserv.getMapFactory().getMap(wserv.getEventMap()).getMapName());
                        wserv.getEventName();
                    } else {
                        mc.dropMessage("There is no event on at this time.");
                    }
                } else if (splitted[0].equalsIgnoreCase("@joinevent") || splitted[0].equalsIgnoreCase("@join") && !player.isInJail()) {
                    if (wserv.hasEvent()) {
                        mc.dropMessage("Debug string: " + Long.toString( c.getPlayer().timeSinceLastVote()/60 ));
                        if(c.getPlayer().timeSinceLastVote()/60 > 1440) {
                            mc.dropMessage("You haven't voted in the last 24 hours, please go on www.voidms.com and vote!");
                            return;
                        }
                        if (wserv.getEventChannel() == player.getClient().getChannel()) {
                            MapleMap map = cserv.getMapFactory().getMap(wserv.getEventMap());
                            if (wserv.canEventWarp()) {
                                if (player.getMapId() != wserv.getEventMap()) {
                                    player.changeMap(map, map.getPortal(0));
                                    mc.dropMessage("Joined event");
                                } else {
                                    mc.dropMessage("You are already in the event");
                                }
                            } else {
                                mc.dropMessage("Event on this channel has already started");
                            }
                        } else {
                            MapleMap map = cserv.getMapFactory().getMap(wserv.getEventMap());
                            if (wserv.canEventWarp()) {
                                if (player.getMapId() != wserv.getEventMap()) {
                                    player.changeMap(map, map.getPortal(0));
                                    player.saveToDB(true, true);
                                    TimerManager.getInstance().schedule(new Runnable() {

                                        public void run() {
                                            player.dropMessage("Joined Event");
                                            ChangeChannelHandler.changeChannel(wserv.getEventChannel(), player.getClient());
                                        }
                                    }, 1 * 1000);
                                } else {
                                    mc.dropMessage("You are already in the event");
                                }
                            } else {
                                mc.dropMessage("Event on Channel " + wserv.getEventChannel() + " has already started");
                            }
                        }
                    } else {
                        mc.dropMessage("There is no event on at this time.");
                    }
                } else if (splitted[0].equalsIgnoreCase("@warrior") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(100));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@beginner") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(0));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@mage") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(200));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@archer") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(300));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@rogue") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(400));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@fighter") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(110));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@page") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(120));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@spearman") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(130));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@fpmage") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(210));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@ilmage") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(220));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@cleric") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(230));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@hunter") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(310));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@crossbowman") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(320));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@assassin") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(410));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@bandit") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(420));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@crusader") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(111));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@whiteknight") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(121));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@dragonknight") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(131));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@advfpmage") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(211));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@advilmage") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(221));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@priest") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(231));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@ranger") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(311));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@sniper") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(321));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@hermit") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(411));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@chiefbandit") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(421));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@hero") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(112));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@paladin") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(122));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@darkknight") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(132));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@fparchmage") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(212));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@ilarchmage") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(222));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@bishop") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(232));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@bowmaster") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(312));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@marksman") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(322));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@nightlord") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(412));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@shadower") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(422));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@pirate") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(500));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@brawler") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(510));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@gunslinger") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(520));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@marauder") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(511));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@outlaw") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(521));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@buccaneer") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(512));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@corsair") && player.isAlive()) {
                    if (player.getReborns() > 200 || player.getGMLevel() > 0) {
                        c.getPlayer().changeJob(MapleJob.getById(522));
                    } else {
                        mc.dropMessage("You don't have 200 rebirths");
                    }
                } else if (splitted[0].equalsIgnoreCase("@cdrops") && player.isAlive()) {
                    int price = 1000000;
                    MapleMap map = player.getMap();
                    if ((player.getMapId() == 801040101 || player.getMap().getId() == 980000604 || player.getMap().getId() == 980000504 || player.getMap().getId() == 980010200 || player.getMap().getId() == 980010100 || player.getMap().getId() == 912010200 || player.getMap().getId() == 912010000 || player.getMap().getId() == 910000001 || player.getMap().getId() == 910000002 || player.getMap().getId() == 910000003 || player.getMap().getId() == 910000004 || player.getMap().getId() == 910000005 || player.getMap().getId() == 910000006 || player.getMap().getId() == 910000007 || player.getMap().getId() == 910000008 || player.getMap().getId() == 910000009 || player.getMap().getId() == 910000010 || player.getMap().getId() == 910000011 || player.getMap().getId() == 910000012 || player.getMap().getId() == 910000013 || player.getMap().getId() == 910000014 || player.getMap().getId() == 910000015 || player.getMap().getId() == 910000016 || player.getMap().getId() == 910000017 || player.getMap().getId() == 910000018 || player.getMap().getId() == 910000019 || player.getMap().getId() == 910000020 || player.getMap().getId() == 910000021 || player.getMap().getId() == 910000022) && player.getMeso() >= price) {
                        List<MapleMapObject> items = map.getMapObjectsInRange(player.getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.ITEM));
                        for (MapleMapObject i : items) {
                            map.removeMapObject(i);
                            map.broadcastMessage(MaplePacketCreator.removeItemFromMap(i.getObjectId(), 0, player.getId()));
                        }
                        mc.dropMessage("You have destroyed " + items.size() + " items on the ground.");
                        player.gainMeso(-price, true);
                    } else {
                        mc.dropMessage("You can't do this here or you don't have 1 mil mesos.");
                    }
                } else if (splitted[0].equals("@maxskills")) {
                    if (!player.inEvent()) {
                        player.maxAllSkills();
                    }
                } else if (splitted[0].equalsIgnoreCase("@debuff")) {
                    player.cancelAllBuffs();
                    mc.dropMessage("Buffs cleared");
                } else if (splitted[0].equalsIgnoreCase("@fixkeys") || splitted[0].equalsIgnoreCase("@keyfix")) {
                    player.sendKeymap();
                } else if (splitted[0].equalsIgnoreCase("@srb")) {
                    NPCScriptManager npc = NPCScriptManager.getInstance();
                    npc.start(c, 9010002);
                } else if (splitted[0].equalsIgnoreCase("@msi") && !player.isInJail()) {
                    NPCScriptManager npc = NPCScriptManager.getInstance();
                    npc.start(c, 2030014);
                } else if (splitted[0].equals("@changegender") && !player.inEvent()) {
                    if (player.getGender() == 2) {
                        if (splitted.length > 1) {
                            if (splitted[1].equalsIgnoreCase("alien")) {
                                player.setGender(0);
                                mc.dropMessage("Gender changed from alien to male");
                            }
                        } else {
                            mc.dropMessage("Are you sure you want to change your gender from Alien? If so do @changegender 'alien'");
                        }
                    } else {
                        if (player.getMeso() >= 1000000) {
                            player.setGender(player.getGender() == 1 ? 0 : 1);
                            player.getClient().getSession().write(MaplePacketCreator.getCharInfo(player));
                            player.getMap().removePlayer(player);
                            player.getMap().addPlayer(player);
                            player.gainMeso(-1000000);
                            String gender = (player.getGender() == 1 ? "female" : "male");
                            mc.dropMessage("Gender changed to " + gender + ".");
                        } else {
                            mc.dropMessage("You don't have 100,000 mesos.");
                        }
                    }
                } else if (splitted[0].equalsIgnoreCase("@casino") && !player.isInJail()) {
                    player.changeMap(925100000);
                } else if (splitted[0].equals("@showjobs")) {
                    String jobs = "@warrior, @beginner,@mage, @archer, @rogue, @fighter, @page, @spearman,@fpmage, @ilmage, @cleric, @hunter, @crossbowman, @assassin, @bandit, @crusader, @whiteknight, @dragonknight,@advfpmage, @advilmage, @priest, @ranger, @sniper, @hermit, @chiefbandit, @hero, @paladin,@darkknight, @fparchmage, @ilarchmage, @bishop, @bowmaster, @marksman, @nightlord, @shadower, @pirate, @brawler, @gunslinger, @marauder, @outlaw, @buccaneer, @corsair";
                    mc.dropMessage("These are the jobs you can become using the @[jobname] command. Remember, you need 200 Rebirths or more to use this command.");
                    mc.dropMessage(jobs);
                } else if (splitted[0].equals("@changebgm")) {
                    NPCScriptManager.getInstance().start(c, 9100200);
                } else if (splitted[0].equalsIgnoreCase("@achievementrank") || splitted[0].equalsIgnoreCase("@arank")) {
                    ResultSet rs = achievements();
                    try {
                        mc.dropMessage("Achievement Ranking Top 20:");
                        while (rs.next()) {
                            mc.dropMessage("[" + rs.getRow() + "] " + rs.getString("name") + "        |      Achievement Points: " + rs.getInt("achievements"));
                        }
                        rs.close();
                    } catch (Exception ex) {
                    }
                } else if (splitted[0].equalsIgnoreCase("@jqrank")) {
                    ResultSet rs = karma();
                    try {
                        mc.dropMessage("JQ Ranking Top 20:");
                        while (rs.next()) {
                            mc.dropMessage("[" + rs.getRow() + "] " + rs.getString("name") + "        |      JQ Points: " + rs.getInt("karma"));
                        }
                        rs.close();
                    } catch (Exception ex) {
                    }
                } else {
                    mc.dropMessage("Player command " + splitted[0] + " does not exist");
                }
            }
        }
    }

    private void compareTime(StringBuilder sb, long timeDiff) {
        double secondsAway = timeDiff / 1000;


        double minutesAway = 0;


        double hoursAway = 0;


        while (secondsAway > 60) {
            minutesAway++;
            secondsAway -= 60;


        }
        while (minutesAway > 60) {
            hoursAway++;
            minutesAway -= 60;


        }
        boolean hours = false;


        boolean minutes = false;


        if (hoursAway > 0) {
            sb.append(" ");
            sb.append((int) hoursAway);
            sb.append(" hours");
            hours = true;


        }
        if (minutesAway > 0) {
            if (hours) {
                sb.append(" -");


            }
            sb.append(" ");
            sb.append((int) minutesAway);
            sb.append(" minutes");
            minutes = true;


        }
        if (secondsAway > 0) {
            if (minutes) {
                sb.append(" and");


            }
            sb.append(" ");
            sb.append((int) secondsAway);
            sb.append(" seconds !");


        }
    }

    @Override
    public CommandDefinition[] getDefinition() {
        return new CommandDefinition[]{
                    new CommandDefinition("command", 0),
                    new CommandDefinition("commands", 0),
                    new CommandDefinition("help", 0),
                    new CommandDefinition("checkstats", 0),
                    new CommandDefinition("save", 0),
                    new CommandDefinition("expfix", 0),
                    new CommandDefinition("dispose", 0),
                    new CommandDefinition("emo", 0),
                    new CommandDefinition("rebirth", 0),
                    new CommandDefinition("reborn", 0),
                    new CommandDefinition("changegender", 0),
                    new CommandDefinition("rebirthp", 0),
                    new CommandDefinition("rebirthw", 0),
                    new CommandDefinition("rebirthm", 0),
                    new CommandDefinition("rebirthb", 0),
                    new CommandDefinition("rebirtht", 0),
                    new CommandDefinition("togglesmega", 0),
                    new CommandDefinition("str", 0),
                    new CommandDefinition("dex", 0),
                    new CommandDefinition("int", 0),
                    new CommandDefinition("luk", 0),
                    new CommandDefinition("hp", 0),
                    new CommandDefinition("mp", 0),
                    new CommandDefinition("gmmsg", 0),
                    new CommandDefinition("revive", 0),
                    new CommandDefinition("afk", 0),
                    new CommandDefinition("kin", 0),
                    new CommandDefinition("nimakin", 0),
                    new CommandDefinition("donate", 0),
                    new CommandDefinition("moneybags", 0),
                    new CommandDefinition("all", 0),
                    new CommandDefinition("shop", 0),
                    new CommandDefinition("go", 0),
                    new CommandDefinition("job", 0),
                    new CommandDefinition("reset", 0),
                    new CommandDefinition("spinel", 0),
                    new CommandDefinition("storage", 0),
                    new CommandDefinition("online", 0),
                    new CommandDefinition("onlinetime", 0),
                    new CommandDefinition("buffme", 0),
                    new CommandDefinition("warrior", 0),
                    new CommandDefinition("beginner", 0),
                    new CommandDefinition("mage", 0),
                    new CommandDefinition("archer", 0),
                    new CommandDefinition("rogue", 0),
                    new CommandDefinition("fighter", 0),
                    new CommandDefinition("page", 0),
                    new CommandDefinition("spearman", 0),
                    new CommandDefinition("fpmage", 0),
                    new CommandDefinition("ilmage", 0),
                    new CommandDefinition("cleric", 0),
                    new CommandDefinition("hunter", 0),
                    new CommandDefinition("crossbowman", 0),
                    new CommandDefinition("assassin", 0),
                    new CommandDefinition("bandit", 0),
                    new CommandDefinition("crusader", 0),
                    new CommandDefinition("whiteknight", 0),
                    new CommandDefinition("dragonknight", 0),
                    new CommandDefinition("advfpmage", 0),
                    new CommandDefinition("advilmage", 0),
                    new CommandDefinition("priest", 0),
                    new CommandDefinition("ranger", 0),
                    new CommandDefinition("sniper", 0),
                    new CommandDefinition("hermit", 0),
                    new CommandDefinition("chiefbandit", 0),
                    new CommandDefinition("hero", 0),
                    new CommandDefinition("paladin", 0),
                    new CommandDefinition("darkknight", 0),
                    new CommandDefinition("fparchmage", 0),
                    new CommandDefinition("ilarchmage", 0),
                    new CommandDefinition("bishop", 0),
                    new CommandDefinition("bowmaster", 0),
                    new CommandDefinition("marksman", 0),
                    new CommandDefinition("nightlord", 0),
                    new CommandDefinition("shadower", 0),
                    new CommandDefinition("pirate", 0),
                    new CommandDefinition("brawler", 0),
                    new CommandDefinition("gunslinger", 0),
                    new CommandDefinition("marauder", 0),
                    new CommandDefinition("outlaw", 0),
                    new CommandDefinition("buccaneer", 0),
                    new CommandDefinition("corsair", 0),
                    new CommandDefinition("joinevent", 0),
                    new CommandDefinition("staffonline", 0),
                    new CommandDefinition("gmonline", 0),
                    new CommandDefinition("gmsonline", 0),
                    new CommandDefinition("stafflist", 0),
                    new CommandDefinition("reborns", 0),
                    new CommandDefinition("recharge", 0),
                    new CommandDefinition("sex", 0),
                    new CommandDefinition("cyberslap", 0),
                    new CommandDefinition("vroom", 0),
                    new CommandDefinition("lapdance", 0),
                    new CommandDefinition("kanyewest", 0),
                    new CommandDefinition("singlish", 0),
                    new CommandDefinition("bicboi", 0),
                    new CommandDefinition("climax", 0),
                    new CommandDefinition("cybersex", 0),
                    new CommandDefinition("1337", 0),
                    new CommandDefinition("hack", 0),
                    new CommandDefinition("wallet", 0),
                    new CommandDefinition("gmhat", 0),
                    new CommandDefinition("fakeevent", 0),
                    new CommandDefinition("touch", 0),
                    new CommandDefinition("pedobear", 0),
                    new CommandDefinition("roar", 0),
                    new CommandDefinition("wtfbbq", 0),
                    new CommandDefinition("eat", 0),
                    new CommandDefinition("noob", 0),
                    new CommandDefinition("pwn", 0),
                    new CommandDefinition("chopstick", 0),
                    new CommandDefinition("lick", 0),
                    new CommandDefinition("shoot", 0),
                    new CommandDefinition("cum", 0),
                    new CommandDefinition("hugo", 0),
                    new CommandDefinition("pokemon", 0),
                    new CommandDefinition("vista", 0),
                    new CommandDefinition("stfu", 0),
                    new CommandDefinition("tear", 0),
                    new CommandDefinition("served", 0),
                    new CommandDefinition("itson", 0),
                    new CommandDefinition("chucknorris", 0),
                    new CommandDefinition("nudge", 0),
                    new CommandDefinition("poke", 0),
                    new CommandDefinition("pan", 0),
                    new CommandDefinition("murder", 0),
                    new CommandDefinition("steal", 0),
                    new CommandDefinition("educate", 0),
                    new CommandDefinition("accent", 0),
                    new CommandDefinition("moderator", 0),
                    new CommandDefinition("rape", 0),
                    new CommandDefinition("smexy", 0),
                    new CommandDefinition("love", 0),
                    new CommandDefinition("hate", 0),
                    new CommandDefinition("torture", 0),
                    new CommandDefinition("masturbate", 0),
                    new CommandDefinition("lame", 0),
                    new CommandDefinition("bitch", 0),
                    new CommandDefinition("kiss", 0),
                    new CommandDefinition("fap", 0),
                    new CommandDefinition("hug", 0),
                    new CommandDefinition("cuddle", 0),
                    new CommandDefinition("mangasm", 0),
                    new CommandDefinition("p2p", 0),
                    new CommandDefinition("p2plong", 0),
                    new CommandDefinition("fixkeys", 0),
                    new CommandDefinition("clearslot", 0),
                    new CommandDefinition("goafk", 0),
                    new CommandDefinition("afkworld", 0),
                    new CommandDefinition("keyfix", 0),
                    new CommandDefinition("jq", 0),
                    new CommandDefinition("pvp", 0),
                    new CommandDefinition("cdrops", 0),
                    new CommandDefinition("buddy", 0),
                    new CommandDefinition("jqrank", 0),
                    new CommandDefinition("srb", 0),
                    new CommandDefinition("msi", 0),
                    new CommandDefinition("casino", 0),
                    new CommandDefinition("yes", 0),
                    new CommandDefinition("no", 0),
                    new CommandDefinition("flag", 0),
                    new CommandDefinition("buddy", 0),
                    new CommandDefinition("pvprank", 0),
                    new CommandDefinition("pvpkills", 0),
                    new CommandDefinition("changegender", 0),
                    new CommandDefinition("refill", 0),
                    new CommandDefinition("showjobs", 0),
                    new CommandDefinition("fmnpc", 0),
                    new CommandDefinition("topkarma", 0),
                    new CommandDefinition("debuff", 0),
                    new CommandDefinition("joko", 0),
                    new CommandDefinition("donatorlist", 0),
                    new CommandDefinition("donorlist", 0),
                    new CommandDefinition("donatorsonline", 0),
                    new CommandDefinition("donorsonline", 0),
                    new CommandDefinition("cody", 0),
                    new CommandDefinition("inactive", 0),
                    new CommandDefinition("leave", 0),
                    new CommandDefinition("spawner", 0),
                    new CommandDefinition("buff", 0),
                    new CommandDefinition("sns", 0),
                    new CommandDefinition("sls", 0),
                    new CommandDefinition("meso", 0),
                    new CommandDefinition("snail", 0),
                    new CommandDefinition("exchange", 0),
                    new CommandDefinition("eventinfo", 0),
                    new CommandDefinition("eventstatus", 0),
                    new CommandDefinition("zombie", 0),
                    new CommandDefinition("latestplayers", 0),
                    new CommandDefinition("giveup", 0),
                    new CommandDefinition("achievementrank", 0),
                    new CommandDefinition("arank", 0),
                    new CommandDefinition("join", 0),
                    new CommandDefinition("hideout", 0),
                    new CommandDefinition("maxskills", 0),
                    new CommandDefinition("time", 0),
                    new CommandDefinition("stamp", 0),
                    new CommandDefinition("stump", 0),
                    new CommandDefinition("ignore", 0),
                    new CommandDefinition("pat", 0),
                    new CommandDefinition("sammich", 0),
                    new CommandDefinition("cms", 0),
                    new CommandDefinition("fakegm", 0),
                    new CommandDefinition("ditch", 0),
                    new CommandDefinition("cool", 0),
                    new CommandDefinition("twitter", 0),
                    new CommandDefinition("facebook", 0),
                    new CommandDefinition("paperbag", 0),
                    new CommandDefinition("stfu", 0),
                    new CommandDefinition("yomama", 0),
                    new CommandDefinition("kitchen", 0),
                    new CommandDefinition("play", 0),
                    new CommandDefinition("reproduce", 0),
                    new CommandDefinition("picture", 0),
                    new CommandDefinition("hump", 0),
                    new CommandDefinition("sexyback", 0),
                    new CommandDefinition("bigmac", 0),
                    new CommandDefinition("hooker", 0),
                    new CommandDefinition("terrorist", 0),
                    new CommandDefinition("call", 0),
                    new CommandDefinition("hit", 0),
                    new CommandDefinition("spank", 0),
                    new CommandDefinition("seduce", 0),
                    new CommandDefinition("slap", 0),
                    new CommandDefinition("grope", 0),
                    new CommandDefinition("choke", 0),
                    new CommandDefinition("punch", 0),
                    new CommandDefinition("rank", 0),
                    new CommandDefinition("ranking", 0),
                    new CommandDefinition("changebgm", 0),
                    new CommandDefinition("davidm", 0)
                };

    }
}
