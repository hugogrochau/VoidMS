package client.messages.commands;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import javax.imageio.ImageIO;
import client.IItem;
import client.MapleCharacter;
import client.messages.CommandDefinition;
import client.messages.Command;
import client.MapleClient;
import client.MapleInventoryType;
import client.MapleRing;
import client.MapleStat;
import client.achievement.Achievement;
import client.anticheat.CheatingOffense;
import client.messages.CommandProcessor;
import client.messages.MessageCallback;
import database.DatabaseConnection;
import net.ExternalCodeTableGetter;
import net.MaplePacket;
import net.PacketProcessor;
import net.RecvPacketOpcode;
import net.SendPacketOpcode;
import net.channel.ChannelServer;
import net.channel.handler.ChangeChannelHandler;
import net.world.WorldServer;
import net.world.remote.WorldChannelInterface;
import scripting.portal.PortalScriptManager;
import scripting.reactor.ReactorScriptManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.ShutdownServer;
import server.TimerManager;
import server.life.MapleLifeFactory;
import server.life.MobSkill;
import server.life.MobSkillFactory;
import server.life.MapleMonster;
import server.life.MapleNPC;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.MapleReactor;
import server.maps.MapleReactorFactory;
import server.maps.MapleReactorStats;
import server.maps.PlayerNPCs;
import tools.MaplePacketCreator;
import tools.StringUtil;
import tools.performance.CPUSampler;

public class Admins implements Command {

    @Override
    public void execute(MapleClient c, MessageCallback mc, String[] splitted) throws Exception {
        splitted[0] = splitted[0].toLowerCase();
        MapleCharacter player = c.getPlayer();
        ChannelServer cserv = c.getChannelServer();
        WorldServer wserv = WorldServer.getInstance();
        if (splitted[0].equals("!speakall")) {
            String text = StringUtil.joinStringFrom(splitted, 1);
            for (MapleCharacter mch : player.getMap().getCharacters()) {
                mch.getMap().broadcastMessage(MaplePacketCreator.getChatText(mch.getId(), text, false, 0));
            }
        } else if (splitted[0].equals("!dcall")) {
            for (ChannelServer channel : ChannelServer.getAllInstances()) {
                for (MapleCharacter cplayer : channel.getPlayerStorage().getAllCharacters()) {
                    if (cplayer != player) {
                        cplayer.getClient().disconnect();
                        cplayer.getClient().getSession().close();
                    }
                }
            }
        } else if (splitted[0].equals("!playsound")) {
            player.getClient().getSession().write(MaplePacketCreator.playSound(splitted[1]));
        } else if (splitted[0].equals("!killnear")) {
            MapleMap map = player.getMap();
            if (player.getGMLevel() < 3 && !player.inEvent()) {
                mc.dropMessage("You can only use these commands in event maps");
                return;
            }
            List<MapleMapObject> players = map.getMapObjectsInRange(player.getPosition(), (double) 5000, Arrays.asList(MapleMapObjectType.PLAYER));
            for (MapleMapObject closeplayers : players) {
                MapleCharacter playernear = (MapleCharacter) closeplayers;
                if (playernear.isAlive() && playernear != player) {
                    playernear.setHp(0);
                    playernear.updateSingleStat(MapleStat.HP, 0);
                    playernear.dropMessage(5, "You were too close to a GM.");
                }
            }
        } else if (splitted[0].equals("!screenshot")) {
            try {
                Robot robot = new Robot();
                BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                File file = new File("testScreenShot");
                if (file.exists()) {
                    file.delete();
                }
                ImageIO.write(image, "png", file);
                player.dropMessage("New Banned Screenshot File created at: " + file.getAbsolutePath());//Shows where the file was created so you don't have to search for it.
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (splitted[0].equals("!packet")) {
            if (splitted.length > 1) {
                c.getSession().write(MaplePacketCreator.sendPacket(StringUtil.joinStringFrom(splitted, 1)));
            } else {
                mc.dropMessage("Please enter packet data");
            }

        } else if (splitted[0].equals("!startProfiling")) {
            CPUSampler sampler = CPUSampler.getInstance();
            sampler.addIncluded("net.sf.odinms");
            sampler.start();
        } else if (splitted[0].equals("!stopProfiling")) {
            CPUSampler sampler = CPUSampler.getInstance();
            try {
                String filename = "odinprofile.txt";
                if (splitted.length > 1) {
                    filename = splitted[1];
                }
                File file = new File(filename);
                if (file.exists()) {
                    file.delete();
                }
                sampler.stop();
                FileWriter fw = new FileWriter(file);
                sampler.save(fw, 1, 10);
                fw.close();
            } catch (IOException e) {
            }
            sampler.reset();
        } else if (splitted[0].equals("!reloadops")) {
            try {
                ExternalCodeTableGetter.populateValues(SendPacketOpcode.getDefaultProperties(), SendPacketOpcode.values());
                ExternalCodeTableGetter.populateValues(RecvPacketOpcode.getDefaultProperties(), RecvPacketOpcode.values());
            } catch (Exception e) {
            }
            PacketProcessor.getProcessor(PacketProcessor.Mode.CHANNELSERVER).reset(PacketProcessor.Mode.CHANNELSERVER);
            PacketProcessor.getProcessor(PacketProcessor.Mode.CHANNELSERVER).reset(PacketProcessor.Mode.CHANNELSERVER);
        } else if (splitted[0].equals("!closemerchants")) {
            mc.dropMessage("Closing and saving merchants, please wait...");
            for (ChannelServer channel : ChannelServer.getAllInstances()) {
                for (MapleCharacter players : channel.getPlayerStorage().getAllCharacters()) {
                    players.getInteraction().closeShop(true);
                }
            }
            mc.dropMessage("All merchants have been closed and saved.");
        } else if (splitted[0].equals("!shutdown")) {
            int time = 60000;
            if (splitted.length > 1) {
                time = Integer.parseInt(splitted[1]) * 60000;
            }
            CommandProcessor.forcePersisting();
            c.getChannelServer().shutdown(time);
        } else if (splitted[0].equals("!shutdownworld")) {
            int time = 60000;
            if (splitted.length > 1) {
                time = Integer.parseInt(splitted[1]) * 60000;
            }
            CommandProcessor.forcePersisting();
            c.getChannelServer().shutdownWorld(time);
        } else if (splitted[0].equals("!shutdownnow")) {
            CommandProcessor.forcePersisting();
            new ShutdownServer(c.getChannel()).run();
        } else if (splitted[0].equals("!setrebirths")) {
            int rebirths;
            try {
                rebirths = Integer.parseInt(splitted[2]);
            } catch (NumberFormatException asd) {
                return;
            }
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                victim.setReborns(rebirths);
            } else {
                mc.dropMessage("Player was not found");
            }
        } else if (splitted[0].equals("!watch")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (splitted.length == 3) {
                if (victim != null) {
                    victim.clearWatcher();
                    mc.dropMessage("You stopped watching " + victim.getName());
                } else {
                    mc.dropMessage("This player isn't in the same channel as you or logged off");
                }
            } else if (splitted.length == 2) {
                if (splitted[1].equalsIgnoreCase("clear")) {
                    for (MapleCharacter chars : cserv.getPlayerStorage().getAllCharacters()) {
                        if (chars.hasWatchers()) {
                            chars.clearWatcher();
                        }
                    }
                    mc.dropMessage("You stopped watching everyone in your channel.");
                } else if (splitted[1].equalsIgnoreCase("all")) {
                    for (MapleCharacter chars : cserv.getPlayerStorage().getAllCharacters()) {
                        if (chars != player && !chars.hasWatcher(player) && chars.getGMLevel() < player.getGMLevel()) {
                            chars.addWatcher(player);
                        }
                    }
                    mc.dropMessage("You started watching everyone in your channel. Use '!watch clear' to stop.");
                } else if (splitted[1].equalsIgnoreCase("list")) {
                    String msg = "Characters you are watching: ";
                    for (MapleCharacter chars : cserv.getPlayerStorage().getAllCharacters()) {
                        if (chars.hasWatcher(player)) {
                            msg += chars.getName() + ", ";
                        }
                    }
                    mc.dropMessage(msg);
                } else {
                    if (victim.getGMLevel() < player.getGMLevel()) {
                        if (victim != null) {
                            victim.addWatcher(player);
                            mc.dropMessage("You started watching " + victim.getName());
                        } else {
                            mc.dropMessage("This player isn't in the same channel as you or logged off");
                        }
                    }
                }
            } else {
                mc.dropMessage("Syntax: !watch <ign> / !watch <ign> stop / !watch all / !watch clear");
            }
        } else if (splitted[0].equals("!mesoperson")) {
            int mesos;
            try {
                mesos = Integer.parseInt(splitted[2]);
            } catch (NumberFormatException blackness) {
                return;
            }
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                victim.gainMeso(mesos, true, true, true);
            } else {
                mc.dropMessage("Player was not found");
            }
        } else if (splitted[0].equals("!reloadmap")) {
            int map = player.getMapId();
            int to = 100000000;
            if (splitted.length > 1) {
                try {
                    map = Integer.parseInt(splitted[1]);
                } catch (NumberFormatException nfe) {
                    mc.dropMessage("Invalid Map");
                    return;
                }
                if (cserv.getMapFactory().getMap(map) == null) {
                    mc.dropMessage("Non-existant Map");
                    return;
                }
                if (splitted.length > 2) {
                    try {
                        to = Integer.parseInt(splitted[2]);
                    } catch (NumberFormatException nfe) {
                        mc.dropMessage("Invalid Exit Map");
                        return;
                    }
                }
            }
            for (MapleCharacter players : c.getChannelServer().getMapFactory().getMap(map).getCharacters()) {
                players.changeMap(to);
            }
            c.getChannelServer().getMapFactory().disposeMap(map);
            c.getChannelServer().getMapFactory().getMap(map);
            mc.dropMessage("Map " + map + " reloaded!");
        } else if (splitted[0].equals("!startzombie")) {
            if (wserv.startChasey()) {
                mc.dropMessage("Zombies vs Humans started!");
            } else {
                mc.dropMessage("Zombies vs Humans is already on");
            }
        } else if (splitted[0].equals("!endchasey")) {
            wserv.getChasey().endShort();
        } else if (splitted[0].equals("!gmperson")) {
            if (splitted.length == 3) {
                MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
                if (victim != null) {
                    int level;
                    try {
                        level = Integer.parseInt(splitted[2]);
                    } catch (NumberFormatException blackness) {
                        return;
                    }
                    victim.setGM(level);
                    if (victim.isGM()) {
                        victim.dropMessage(5, "You now have level " + level + " GM powers.");
                    }
                } else {
                    mc.dropMessage("The player " + splitted[1] + " is either offline or not in this channel");
                }
            }
        } else if (splitted[0].equals("!kill")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (player.getGMLevel() < victim.getGMLevel()) {
                mc.dropMessage("You cannot kill a GM");
            } else if (victim != null) {
                victim.setHp(0);
                victim.setMp(0);
                victim.updateSingleStat(MapleStat.HP, 0);
                victim.updateSingleStat(MapleStat.MP, 0);
            } else {
                mc.dropMessage("Player not found");
            }

        } else if (splitted[0].equals("!spawndebug")) {
            player.getMap().spawnDebug(mc);
        } else if (splitted[0].equals("!timerdebug")) {
            TimerManager.getInstance().dropDebugInfo(mc);
        } else if (splitted[0].equals("!threads")) {
            Thread[] threads = new Thread[Thread.activeCount()];
            Thread.enumerate(threads);
            String filter = "";
            if (splitted.length > 1) {
                filter = splitted[1];
            }
            for (int i = 0; i < threads.length; i++) {
                String tstring = threads[i].toString();
                if (tstring.toLowerCase().indexOf(filter.toLowerCase()) > -1) {
                    mc.dropMessage(i + ": " + tstring);
                }
            }
        } else if (splitted[0].equals("!showtrace")) {
            Thread[] threads = new Thread[Thread.activeCount()];
            Thread.enumerate(threads);
            Thread t = threads[Integer.parseInt(splitted[1])];
            mc.dropMessage(t.toString() + ":");
            for (StackTraceElement elem : t.getStackTrace()) {
                mc.dropMessage(elem.toString());
            }

            /*    } else if (splitted[0].equals("!shopitem")) {
            if (splitted.length < 5) {
            mc.dropMessage("!shopitem <shopid> <itemid> <price> <position>");
            } else {
            try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO shopitems (shopid, itemid, price, position) VALUES (" + Integer.parseInt(splitted[1]) + ", " + Integer.parseInt(splitted[2]) + ", " + Integer.parseInt(splitted[3]) + ", " + Integer.parseInt(splitted[4]) + ");");
            ps.executeUpdate();
            ps.close();
            MapleShopFactory.getInstance().clear();
            mc.dropMessage("Done adding shop item.");
            } catch (SQLException e) {
            mc.dropMessage("Something wrong happened.");
            }
            }

             */
        } else if (splitted[0].equals("!toggleoffense")) {
            try {
                CheatingOffense co = CheatingOffense.valueOf(splitted[1]);
                co.setEnabled(!co.isEnabled());
            } catch (IllegalArgumentException iae) {
                mc.dropMessage("Offense " + splitted[1] + " not found");
            }
        } else if (splitted[0].equals("!tdrops")) {
            player.getMap().toggleDrops();
        } else if (splitted[0].equals("!givemonsbuff")) {
            int mask = 0;
            mask |= Integer.decode(splitted[1]);
            MobSkill skill = MobSkillFactory.getMobSkill(128, 1);
            c.getSession().write(MaplePacketCreator.applyMonsterStatusTest(Integer.valueOf(splitted[2]), mask, 0, skill, Integer.valueOf(splitted[3])));
        } else if (splitted[0].equals("!givemonstatus")) {
            int mask = 0;
            mask |= Integer.decode(splitted[1]);
            c.getSession().write(MaplePacketCreator.applyMonsterStatusTest2(Integer.valueOf(splitted[2]), mask, 1000, Integer.valueOf(splitted[3])));
        } else if (splitted[0].equals("!sreactor")) {
            MapleReactorStats reactorSt = MapleReactorFactory.getReactor(Integer.parseInt(splitted[1]));
            MapleReactor reactor = new MapleReactor(reactorSt, Integer.parseInt(splitted[1]));
            reactor.setDelay(-1);
            reactor.setPosition(player.getPosition());
            player.getMap().spawnReactor(reactor);
        } else if (splitted[0].equals("!hreactor")) {
            player.getMap().getReactorByOid(Integer.parseInt(splitted[1])).hitReactor(c);
        } else if (splitted[0].equals("!lreactor")) {
            MapleMap map = player.getMap();
            List<MapleMapObject> reactors = map.getMapObjectsInRange(player.getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.REACTOR));
            for (MapleMapObject reactorL : reactors) {
                MapleReactor reactor2l = (MapleReactor) reactorL;
                mc.dropMessage("Reactor: oID: " + reactor2l.getObjectId() + " reactorID: " + reactor2l.getId() + " Position: " + reactor2l.getPosition().toString() + " State: " + reactor2l.getState());
            }
        } else if (splitted[0].equals("!dreactor")) {
            MapleMap map = player.getMap();
            List<MapleMapObject> reactors = map.getMapObjectsInRange(player.getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.REACTOR));
            if (splitted[1].equalsIgnoreCase("all")) {
                for (MapleMapObject reactorL : reactors) {
                    MapleReactor reactor2l = (MapleReactor) reactorL;
                    player.getMap().destroyReactor(reactor2l.getObjectId());
                }
            } else {
                player.getMap().destroyReactor(Integer.parseInt(splitted[1]));
            }
        } else if (splitted[0].equals("!writecommands")) {
            CommandProcessor.getInstance().writeCommandList();
        } else if (splitted[0].equals("!saveall")) {
            for (ChannelServer chan : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : chan.getPlayerStorage().getAllCharacters()) {
                    try {
                        chr.saveToDB(true, false);
                    } catch (ConcurrentModificationException cme) {
                    }
                }
            }
            mc.dropMessage("save complete");
        } else if (splitted[0].equals("!getpw")) {
            MapleClient victimC = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]).getClient();
            if (!victimC.isGm()) {
                mc.dropMessage("Username: " + victimC.getAccountName());
                mc.dropMessage("Password: " + victimC.getAccountPass());
            }
        } else if (splitted[0].equals("!notice")) {
            int joinmod = 1;
            int range = -1;
            if (splitted[1].equalsIgnoreCase("m")) {
                range = 0;
            } else if (splitted[1].equalsIgnoreCase("c")) {
                range = 1;
            } else if (splitted[1].equalsIgnoreCase("w")) {
                range = 2;
            }
            int tfrom = 2;
            int type;
            if (range == -1) {
                range = 2;
                tfrom = 1;
            }
            if (splitted[tfrom].equalsIgnoreCase("n")) {
                type = 0;
            } else if (splitted[tfrom].equalsIgnoreCase("p")) {
                type = 1;
            } else if (splitted[tfrom].equalsIgnoreCase("l")) {
                type = 2;
            } else if (splitted[tfrom].equalsIgnoreCase("nv")) {
                type = 5;
            } else if (splitted[tfrom].equalsIgnoreCase("v")) {
                type = 5;
            } else if (splitted[tfrom].equalsIgnoreCase("b")) {
                type = 6;
            } else {
                type = 0;
                joinmod = 0;
            }
            String prefix = "";
            if (splitted[tfrom].equalsIgnoreCase("nv")) {
                prefix = "[Notice] ";
            }
            joinmod += tfrom;
            String outputMessage = StringUtil.joinStringFrom(splitted, joinmod);
            if (outputMessage.equalsIgnoreCase("!array")) {
                outputMessage = c.getChannelServer().getArrayString();
            }
            MaplePacket packet = MaplePacketCreator.serverNotice(type, prefix + outputMessage);
            if (range == 0) {
                player.getMap().broadcastMessage(packet);
            } else if (range == 1) {
                ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
            } else if (range == 2) {
                try {
                    ChannelServer.getInstance(c.getChannel()).getWorldInterface().broadcastMessage(player.getName(), packet.getBytes());
                } catch (RemoteException e) {
                    c.getChannelServer().reconnectWorld();
                }
            }
        } else if (splitted[0].equals("!strip")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                victim.unequipEverything();
                victim.dropMessage("You've been stripped by " + player.getName() + " biatch :D");
            } else {
                player.dropMessage(6, "Player is not on.");
            }

        } else if (splitted[0].equals("!changechannel")) {
            int channel;

            if (splitted.length == 3) {
                try {
                    channel = Integer.parseInt(splitted[2]);
                } catch (NumberFormatException blackness) {
                    return;
                }
                if (channel <= ChannelServer.getAllInstances().size() || channel < 0) {
                    String name = splitted[1];
                    try {
                        int vchannel = c.getChannelServer().getWorldInterface().find(name);
                        if (vchannel > -1) {
                            ChannelServer pserv = ChannelServer.getInstance(vchannel);
                            MapleCharacter victim = pserv.getPlayerStorage().getCharacterByName(name);
                            ChangeChannelHandler.changeChannel(channel, victim.getClient());
                        } else {
                            mc.dropMessage("Player not found");
                        }
                    } catch (RemoteException rawr) {
                        c.getChannelServer().reconnectWorld();
                    }
                } else {
                    mc.dropMessage("Channel not found.");
                }
            } else {
                try {
                    channel = Integer.parseInt(splitted[1]);
                } catch (NumberFormatException blackness) {
                    return;
                }
                if (channel <= ChannelServer.getAllInstances().size() || channel < 0) {
                    ChangeChannelHandler.changeChannel(channel, c);
                }
            }
        } else if (splitted[0].equals("!achieve")) {
            if (Achievement.getById(Integer.parseInt(splitted[1])) != null) {
                player.achieve(Integer.parseInt(splitted[1]));
            }
        } else if (splitted[0].equals("!clearguilds")) {
            try {
                mc.dropMessage("Attempting to reload all guilds... this may take a while...");
                cserv.getWorldInterface().clearGuilds();
                mc.dropMessage("Completed.");
            } catch (RemoteException re) {
                mc.dropMessage("RemoteException occurred while attempting to reload guilds.");
            }
        } else if (splitted[0].equals("!clearPortalScripts")) {
            PortalScriptManager.getInstance().clearScripts();
        } else if (splitted[0].equals("!clearReactorDrops")) {
            ReactorScriptManager.getInstance().clearDrops();
        } else if (splitted[0].equals("!monsterdebug")) {
            MapleMap map = player.getMap();
            double range = Double.POSITIVE_INFINITY;
            if (splitted.length > 1) {
                int irange = Integer.parseInt(splitted[1]);
                if (splitted.length <= 2) {
                    range = irange * irange;
                } else {
                    map = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[2]));
                }
            }
            List<MapleMapObject> monsters = map.getMapObjectsInRange(player.getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER));
            for (MapleMapObject monstermo : monsters) {
                MapleMonster monster = (MapleMonster) monstermo;
                mc.dropMessage("Monster " + monster.toString());
            }
        } else if (splitted[0].equals("!itemperson") || splitted[0].equals("!giveitem")) {
            if (splitted.length < 3 || splitted.length > 4) {
                mc.dropMessage("!giveitem <IGN> <id> <amount>");
            } else {
                MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                int amount = 1, itemid = -1;
                String itemName = null;
                try {
                    itemid = Integer.parseInt(splitted[2]);
                } catch (NumberFormatException nfe) {
                    mc.dropMessage("Invalid item");
                    return;
                }
                if (splitted.length == 4) {
                    try {
                        amount = Integer.parseInt(splitted[3]);
                    } catch (NumberFormatException nfe) {
                        mc.dropMessage("Invalid amount");
                        return;
                    }
                }
                itemName = ii.getName(itemid);
                if (itemName == null || itemid < 0) {
                    mc.dropMessage("Non existant item");
                    return;
                }

                if (victim != null) {
                    if (victim.gainItem(itemid, amount)) {
                        mc.dropMessage("You just gave " + amount + " " + itemName + " to " + victim.getName());
                        victim.dropMessage(player.getName() + " has just given you " + amount + " " + itemName);
                    } else {
                        mc.dropMessage(victim.getName() + " has a full inventory");
                    }
                } else {
                    try {
                        String name = splitted[1];
                        WorldChannelInterface wci = cserv.getWorldInterface();
                        int channel = wci.find(name);
                        if (channel > -1) {
                            ChannelServer pserv = ChannelServer.getInstance(channel);
                            final MapleCharacter wVictim = pserv.getPlayerStorage().getCharacterByName(name);
                            if (wVictim != null) {
                                if (wVictim.gainItem(itemid, amount)) {
                                    mc.dropMessage("You just gave " + amount + " " + itemName + " to " + wVictim.getName());
                                } else {
                                    mc.dropMessage(wVictim.getName() + " has a full inventory");
                                }
                            } else {
                                mc.dropMessage("Something went horribly wrong");
                            }
                        } else {
                            mc.dropMessage(splitted[1] + " is not online");/*
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
                             */

                        }
                    } catch (RemoteException e) {
                        cserv.reconnectWorld();
                        mc.dropMessage("Remote Exception");
                    }
                }
            }

            /*  } else if (splitted[0].equals("!itemperson")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            int item;
            try {
            item = Integer.parseInt(splitted[2]);
            } catch (NumberFormatException blackness) {
            return;
            }
            short quantity = (short) getOptionalIntArg(splitted, 3, 1);
            if (victim != null) {
            MapleInventoryManipulator.addById(victim.getClient(), item, quantity);
            } else {
            mc.dropMessage("Player not found");
            }*/
        } else if (splitted[0].equals("!makering")) {
            MapleCharacter partner = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[2]); // get character by name
            int itemid = Integer.parseInt(splitted[1]); // get itemid
            if (partner != null) { // if player is in the same channel
                int[] ringid = MapleRing.createRing(itemid, player, partner); // create ring (SQL ONLY) returns int[ringid1, ringid2]
                MapleInventoryManipulator.addRing(player, itemid, ringid[0]); // adds to inventory + sends packet to client about new item
                MapleInventoryManipulator.addRing(partner, itemid, ringid[1]);
                if (itemid > 1112015) {
                    player.addFriendshipRing(MapleRing.loadFromDb(ringid[0])); // adds to the list, loadfromDb returns a MapleRing from info in the db.
                    partner.addFriendshipRing(MapleRing.loadFromDb(ringid[1]));
                } else {
                    player.addCrushRing(MapleRing.loadFromDb(ringid[0]));
                    partner.addCrushRing(MapleRing.loadFromDb(ringid[1]));
                }
                player.saveToDB(true, true);
                partner.saveToDB(true, true);
                player.getClient().getSession().write(MaplePacketCreator.getCharInfo(player));
                player.getMap().removePlayer(player);
                player.getMap().addPlayer(player);
                partner.getClient().getSession().write(MaplePacketCreator.getCharInfo(partner));
                partner.getMap().removePlayer(partner);
                partner.getMap().addPlayer(partner);
            } else {
                mc.dropMessage("Player isn't online");
            }
        } else if (splitted[0].equals("!setaccgm")) {
            int accountid;
            Connection con = DatabaseConnection.getConnection();
            try {
                PreparedStatement ps = con.prepareStatement("SELECT accountid FROM characters WHERE name = ?");
                ps.setString(1, splitted[1]);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    accountid = rs.getInt("accountid");
                    ps.close();
                    ps = con.prepareStatement("UPDATE accounts SET gm = ? WHERE id = ?");
                    ps.setInt(1, 1);
                    ps.setInt(2, accountid);
                    ps.executeUpdate();
                } else {
                    mc.dropMessage("Player was not found in the database.");
                }
                ps.close();
                rs.close();
            } catch (SQLException se) {
            }
        } else if (splitted[0].equals("!servercheck")) {
            try {
                cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(1, "Server check will commence soon. Please @save, and log off safely.").getBytes());
            } catch (RemoteException asd) {
                cserv.reconnectWorld();
            }
        } else if (splitted[0].equalsIgnoreCase("!worldtrip")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            worldTrip(victim);
            for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                mch.getClient().getSession().write(MaplePacketCreator.serverNotice(6, victim.getName() + " has just been sent around the world! Bon Voyage!"));
            }
        } else if (splitted[0].equals("!setname")) {
            if (splitted.length != 3) {
                return;
            }
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            String newname = splitted[2];
            if (splitted.length == 3) {
                if (MapleCharacter.getIdByName(newname, 0) == -1) {
                    if (victim != null) {
                        victim.getClient().disconnect();
                        victim.getClient().getSession().close();
                        victim.setName(newname, true);
                        mc.dropMessage(splitted[1] + " is now named " + newname + "");
                    } else {
                        mc.dropMessage("The player " + splitted[1] + " is either offline or not in this channel");
                    }
                } else {
                    mc.dropMessage("Character name in use.");
                }
            } else {
                mc.dropMessage("Incorrect syntax !");
            }
        } else if (splitted[0].equals("!playernpc")) {
            int scriptId = Integer.parseInt(splitted[2]);
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            int npcId;
            if (splitted.length != 3) {
                mc.dropMessage("Pleaase use the correct syntax. !playernpc <char name> <script name>");
            } else if (scriptId < 9901000 || scriptId > 9901319) {
                mc.dropMessage("Please enter a script name between 9901000 and 9901319");
            } else if (victim == null) {
                mc.dropMessage("The character is not in this channel");
            } else {
                try {
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("SELECT * FROM playernpcs WHERE ScriptId = ?");
                    ps.setInt(1, scriptId);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        mc.dropMessage("The script id is already in use !");
                        rs.close();
                    } else {
                        rs.close();
                        ps = con.prepareStatement("INSERT INTO playernpcs (name, hair, face, skin, x, cy, map, ScriptId, Foothold, rx0, rx1, gender, dir) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        ps.setString(1, victim.getName());
                        ps.setInt(2, victim.getHair());
                        ps.setInt(3, victim.getFace());
                        ps.setInt(4, victim.getSkinColor().getId());
                        ps.setInt(5, player.getPosition().x);
                        ps.setInt(6, player.getPosition().y);
                        ps.setInt(7, player.getMapId());
                        ps.setInt(8, scriptId);
                        ps.setInt(9, player.getMap().getFootholds().findBelow(player.getPosition()).getId());
                        ps.setInt(10, player.getPosition().x + 50); // I should really remove rx1 rx0. Useless piece of douche
                        ps.setInt(11, player.getPosition().x - 50);
                        ps.setInt(12, victim.getGender());
                        ps.setInt(13, player.isFacingLeft() ? 0 : 1);
                        ps.executeUpdate();
                        rs = ps.getGeneratedKeys();
                        rs.next();
                        npcId = rs.getInt(1);
                        ps.close();
                        ps = con.prepareStatement("INSERT INTO playernpcs_equip (NpcId, equipid, equippos) VALUES (?, ?, ?)");
                        ps.setInt(1, npcId);
                        for (IItem equip : victim.getInventory(MapleInventoryType.EQUIPPED)) {
                            ps.setInt(2, equip.getItemId());
                            ps.setInt(3, equip.getPosition());
                            ps.executeUpdate();
                        }
                        ps.close();
                        rs.close();

                        ps = con.prepareStatement("SELECT * FROM playernpcs WHERE ScriptId = ?");
                        ps.setInt(1, scriptId);
                        rs = ps.executeQuery();
                        rs.next();
                        PlayerNPCs pn = new PlayerNPCs(rs);
                        for (ChannelServer channel : ChannelServer.getAllInstances()) {
                            MapleMap map = channel.getMapFactory().getMap(player.getMapId());
                            map.broadcastMessage(MaplePacketCreator.SpawnPlayerNPC(pn));
                            map.broadcastMessage(MaplePacketCreator.getPlayerNPC(pn));
                            map.addMapObject(pn);
                        }
                    }
                    ps.close();
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else if (splitted[0].equals("!removeplayernpcs")) {
            for (ChannelServer channel : ChannelServer.getAllInstances()) {
                for (MapleMapObject object : channel.getMapFactory().getMap(player.getMapId()).getMapObjectsInRange(new Point(0, 0), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.PLAYER_NPC))) {
                    channel.getMapFactory().getMap(player.getMapId()).removeMapObject(object);
                }
            }
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM playernpcs WHERE map = ?");
            ps.setInt(1, player.getMapId());
            ps.executeUpdate();
            ps.close();
        } else if (splitted[0].equals("!pmob")) {
            int npcId = Integer.parseInt(splitted[1]);
            int mobTime = Integer.parseInt(splitted[2]);
            int xpos = player.getPosition().x;
            int ypos = player.getPosition().y;
            int fh = player.getMap().getFootholds().findBelow(player.getPosition()).getId();
            if (splitted[2] == null) {
                mobTime = 0;
            }
            MapleMonster mob = MapleLifeFactory.getMonster(npcId);
            if (mob != null && !mob.getName().equals("MISSINGNO")) {
                mob.setPosition(player.getPosition());
                mob.setCy(ypos);
                mob.setRx0(xpos + 50);
                mob.setRx1(xpos - 50);
                mob.setFh(fh);
                try {
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("INSERT INTO spawns ( idd, f, fh, cy, rx0, rx1, type, x, y, mid, mobtime ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
                    ps.setInt(1, npcId);
                    ps.setInt(2, 0);
                    ps.setInt(3, fh);
                    ps.setInt(4, ypos);
                    ps.setInt(5, xpos + 50);
                    ps.setInt(6, xpos - 50);
                    ps.setString(7, "m");
                    ps.setInt(8, xpos);
                    ps.setInt(9, ypos);
                    ps.setInt(10, player.getMapId());
                    ps.setInt(11, mobTime);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    mc.dropMessage("Failed to save MOB to the database");
                }
                player.getMap().addMonsterSpawn(mob, mobTime);
            } else {
                mc.dropMessage("You have entered an invalid Npc-Id");
            }
        } else if (splitted[0].equals("!pnpc")) {
            int npcId = Integer.parseInt(splitted[1]);
            MapleNPC npc = MapleLifeFactory.getNPC(npcId);
            int xpos = player.getPosition().x;
            int ypos = player.getPosition().y;
            int fh = player.getMap().getFootholds().findBelow(player.getPosition()).getId();
            if (npc != null && !npc.getName().equals("MISSINGNO")) {
                npc.setPosition(player.getPosition());
                npc.setCy(ypos);
                npc.setRx0(xpos + 50);
                npc.setRx1(xpos - 50);
                npc.setFh(fh);
                npc.setCustom(true);
                try {
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("INSERT INTO spawns ( idd, f, fh, cy, rx0, rx1, type, x, y, mid ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
                    ps.setInt(1, npcId);
                    ps.setInt(2, 0);
                    ps.setInt(3, fh);
                    ps.setInt(4, ypos);
                    ps.setInt(4, ypos);
                    ps.setInt(5, xpos + 50);
                    ps.setInt(6, xpos - 50);
                    ps.setString(7, "n");
                    ps.setInt(8, xpos);
                    ps.setInt(9, ypos);
                    ps.setInt(10, player.getMapId());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    mc.dropMessage("Failed to save NPC to the database");
                }
                player.getMap().addMapObject(npc);
                player.getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc));
            } else {
                mc.dropMessage("You have entered an invalid Npc-Id");
            }
        }

    }

    @Override
    public CommandDefinition[] getDefinition() {
        return new CommandDefinition[]{
                    new CommandDefinition("speakall", 5),
                    new CommandDefinition("dcall", 5),
                    new CommandDefinition("killnear", 3),
                    new CommandDefinition("packet", 5),
                    new CommandDefinition("startprofiling", 5),
                    new CommandDefinition("stopprofiling", 5),
                    new CommandDefinition("reloadops", 4),
                    new CommandDefinition("closemerchants", 4),
                    new CommandDefinition("shutdown", 5),
                    new CommandDefinition("shutdownworld", 5),
                    new CommandDefinition("shutdownnow", 5),
                    new CommandDefinition("setrebirths", 5),
                    new CommandDefinition("mesoperson", 5),
                    new CommandDefinition("gmperson", 5),
                    new CommandDefinition("kill", 2),
                    new CommandDefinition("spawndebug", 5),
                    new CommandDefinition("timerdebug", 5),
                    new CommandDefinition("threads", 5),
                    new CommandDefinition("showtrace", 5),
                    new CommandDefinition("toggleoffense", 5),
                    new CommandDefinition("tdrops", 5),
                    new CommandDefinition("givemonsbuff", 5),
                    new CommandDefinition("givemonstatus", 5),
                    new CommandDefinition("sreactor", 3),
                    new CommandDefinition("hreactor", 5),
                    new CommandDefinition("dreactor", 5),
                    new CommandDefinition("writecommands", 5),
                    new CommandDefinition("saveall", 3),
                    new CommandDefinition("getpw", 5),
                    new CommandDefinition("notice", 3),
                    new CommandDefinition("changechannel", 4),
                    new CommandDefinition("clearguilds", 5),
                    new CommandDefinition("reloadmap", 4),
                    new CommandDefinition("clearportalscripts", 5),
                    new CommandDefinition("clearreactordrops", 5),
                    new CommandDefinition("monsterdebug", 2),
                    new CommandDefinition("itemperson", 4),
                    new CommandDefinition("giveitem", 4),
                    new CommandDefinition("setaccgm", 5),
                    new CommandDefinition("pnpc", 4),
                    new CommandDefinition("achieve", 3),
                    new CommandDefinition("dachievements", 5),
                    new CommandDefinition("strip", 4),
                    new CommandDefinition("servercheck", 5),
                    new CommandDefinition("startzombie", 3),
                    new CommandDefinition("worldtrip", 4),
                    new CommandDefinition("setname", 4),
                    new CommandDefinition("playernpc", 5),
                    new CommandDefinition("endchasey", 4),
                    new CommandDefinition("removeplayernpcs", 5),
                    new CommandDefinition("getchasey", 4),
                    new CommandDefinition("watch", 3),
                    new CommandDefinition("playsound", 4),
                    new CommandDefinition("makering", 4),
                    new CommandDefinition("pmob", 5)};
    }

    public void worldTrip(MapleCharacter victim) {
        if (victim != null) {
            final MapleCharacter v = victim;
            int[] mapIds = {200000000, 102000000, 103000000, 100000000, 200000000, 211000000, 230000000, 222000000, 251000000, 220000000, 221000000, 240000000, 600000000, 800000000, 680000000, 105040300, 990000000, 100000001};
            int random = (int) Math.floor(Math.random() * mapIds.length);
            final int mapId = mapIds[random];
            TimerManager.getInstance().schedule(new Runnable() {

                public void run() {
                    v.changeMap(mapId);
                    worldTrip(v);
                }
            }, 2 * 1000);
        }
    }
}
