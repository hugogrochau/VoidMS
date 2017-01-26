package client.messages.commands;

import java.util.Random;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import client.SkillFactory;
import net.channel.ChannelServer;
import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.MapleDisease;
import client.MapleStat;
import client.messages.Command;
import client.messages.CommandDefinition;
import client.messages.MessageCallback;
import database.DatabaseConnection;
import java.util.Collection;
import net.MaplePacket;
import net.world.MapleParty;
import net.world.MaplePartyCharacter;
import net.world.WorldServer;
import server.TimerManager;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.MaplePacketCreator;
import tools.StringUtil;

public class Donator implements Command {

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

    @Override
    public void execute(MapleClient c, MessageCallback mc, String[] splitted) throws Exception {
        splitted[0] = splitted[0].toLowerCase();
        MapleCharacter player = c.getPlayer();
        ChannelServer cserv = c.getChannelServer();
        WorldServer wserv = WorldServer.getInstance();
        if (player.getGMLevel() < 2 && player.isInJail()) {
            mc.dropMessage("Player command " + splitted[0] + "does not exist.");
        } else {
            if (splitted[0].equals("!dbuff")) {
                int[] array = {9001000, 9101002, 9101003, 9101008, 2001002, 1101007, 1005, 2301003, 5121009, 1111002, 4111001, 4111002, 4211003, 4211005, 1321000, 2321004, 3121002};
                for (int i = 0; i < array.length; i++) {
                    SkillFactory.getSkill(array[i]).getEffect(SkillFactory.getSkill(array[i]).getMaxLevel()).applyTo(player);
                }
            } else if (splitted[0].equals("!goto")) {
                Map<String, Integer> maps = new HashMap<String, Integer>();
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
                maps.put("pianus", 230040420);
                maps.put("horntail", 240060200);
                maps.put("mushmom", 100000005);
                maps.put("griffey", 240020101);
                maps.put("manon", 240020401);
                maps.put("headless", 682000001);
                maps.put("balrog", 105090900);
                maps.put("zakum", 280030000);
                maps.put("papu", 220080001);
                maps.put("showa", 801000000);
                maps.put("guild", 200000301);
                maps.put("shrine", 800000000);
                maps.put("fm", 910000000);
                maps.put("skelegon", 240040511);
                maps.put("ariant", 260000100);
                maps.put("ox", 109020001);
                maps.put("minigame", 109070000);
                maps.put("rr", 922010800);
                maps.put("bob", 109010104);
                maps.put("spawner", 801040101);
                if (player.getGMLevel() > 1) {
                    maps.put("southperry", 60000);
                    maps.put("amherst", 1010000);
                    maps.put("happy", 209000000);
                    maps.put("gmmap", 180000000);
                    maps.put("elevator", 222020211);
                    maps.put("bomb", 260000201);//109070000
                }
                if (splitted.length < 2) {
                    mc.dropMessage("Syntax: !goto <mapname> <optional_target>, Targets only for Interns and Up.");
                    StringBuilder builder = new StringBuilder();
                    int i = 0;
                    for (String mapss : maps.keySet()) {
                        if (1 % 10 == 0) {
                            mc.dropMessage(builder.toString());
                        } else {
                            builder.append(mapss + ", ");
                        }
                    }
                    mc.dropMessage(builder.toString());
                } else {
                    String message = splitted[1].toLowerCase();
                    if( (message.equals("ox") || message.equals("minigame") || message.equals("bob"))  && (c.getPlayer().timeSinceLastVote()/60 > 1440) ) {
                            mc.dropMessage("You haven't voted in the last 24 hours, please go on www.voidms.com and vote!");
                            return;
                    } else 
                    if (message.equals("ox") && wserv.hasEvent() && wserv.getEventOwner() != player.getId() && player.getGMLevel() < 3) {
                        mc.dropMessage("You cannot warp to the ox map while there's an event on");
                    } else {
                        if (maps.containsKey(message)) {
                            if (splitted.length == 2) {
                                player.changeMap(maps.get(message));
                            } else if (splitted.length == 3) {
                                if (player.getGMLevel() < 2) {
                                    mc.dropMessage("You can't warp people as a donator");
                                } else {
                                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[2]);
                                    victim.changeMap(maps.get(message));
                                }
                            }
                        } else {
                            mc.dropMessage("Could not find map");
                        }
                    }
                }
                maps.clear();
            } else if (splitted[0].equals("!closechalkboard") || splitted[0].equals("!removechalkboard")) {
                if (splitted.length == 2) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim != null) {
                        if (((player.getMap().isEvent() && victim.getMap().isEvent()) && (wserv.getEventOwner() == player.getId() || wserv.getEventHost().equalsIgnoreCase(player.getName()))) || player.isGM()) {
                            if (victim.getChalkboard() != null) {
                                victim.setChalkboard(null);
                                mc.dropMessage("You closed" + victim.getName() + "'s chalkboard");
                            }
                        } else {
                            mc.dropMessage("You can only do this in event maps");
                        }
                    } else {
                        mc.dropMessage(splitted[1] + " is either offline or not in your channel");
                    }
                } else {
                    mc.dropMessage("Syntax: !closechalkboard <IGN>");
                }
            } else if (splitted[0].equals("!closechalkboards") || splitted[0].equals("!removechalkboards")) {
                if (splitted.length == 1) {
                    if ((player.getMap().isEvent() && (wserv.getEventOwner() == player.getId() || wserv.getEventHost().equalsIgnoreCase(player.getName()))) || player.isGM()) {
                        for (MapleCharacter chr : player.getMap().getCharacters()) {
                            if (chr.getChalkboard() != null) {
                                chr.setChalkboard(null);
                            }
                        }
                        mc.dropMessage("You closed all chalkboards in your map");
                    } else {
                        mc.dropMessage("You can only do this in event maps");
                    }
                } else {
                    mc.dropMessage("Syntax: !closechalkboards");
                }
            } else if (splitted[0].equals("!tagmap")) {
                if ((player.getMap().isEvent() && (wserv.getEventOwner() == player.getId() || wserv.getEventHost().equalsIgnoreCase(player.getName()))) || player.isGM()) {
                    if (!player.getMap().isTagged()) {
                        player.getMap().tag();
                        for (MapleCharacter chars : player.getMap().getCharacters()) {
                            chars.dropMessage(player.getName() + " has just tagged the whole map!");
                        }
                    } else {
                        player.getMap().untag();
                        for (MapleCharacter chars : player.getMap().getCharacters()) {
                            chars.dropMessage(player.getName() + " has just untagged the map");
                        }
                    }
                } else {
                    mc.dropMessage("You can only do this during events");
                }
            } else if (splitted[0].equalsIgnoreCase("!jaillist")) {
                final int[] jaillist = {980000404, 222020211, 222020111, 980000304};
                for (int jail : jaillist) {
                    ArrayList<String> list = new ArrayList<String>();
                    try {
                        java.sql.Connection con = DatabaseConnection.getConnection();
                        PreparedStatement ps = con.prepareStatement("SELECT name FROM characters WHERE map = ?");
                        ps.setInt(1, jail);
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            list.add(MapleCharacterUtil.makeMapleReadable(rs.getString("name")));
                        }
                        rs.close();
                        ps.close();
                    } catch (SQLException ex) {
                        mc.dropMessage(ex.toString());
                    }
                    String chanlist = null;
                    int channel = -1;
                    for (ChannelServer cser : ChannelServer.getAllInstances()) {
                        for (MapleCharacter chr : cser.getPlayerStorage().getAllCharacters()) {
                            channel = chr.getClient().getChannel();
                            if (list.contains(chr.getName())) {
                                list.remove(chr.getName());
                            }
                            if (chr.getMapId() == jail) {
                                if (chanlist == null) {
                                    chanlist = "[Channel " + channel + "] ";
                                }
                                if (chanlist.length() + chr.getName().length() >= 80) {
                                    list.add(chanlist);
                                    chanlist = "[Channel " + channel + "] " + MapleCharacterUtil.makeMapleReadable(chr.getName()) + " ";
                                } else {
                                    chanlist += (MapleCharacterUtil.makeMapleReadable(chr.getName()) + " ");
                                }
                            }
                        }
                        if (chanlist != null) {
                            list.add(chanlist);
                        }
                        chanlist = null;
                    }
                    mc.dropMessage("=== Jail List (Map: " + jail + ") (People on top are offline) ===");
                    String send = null;
                    if (!list.isEmpty()) {
                        for (String str : list) {
                            if (str.startsWith("[Channel")) {
                                mc.dropMessage(str);
                            } else {
                                if (send == null) {
                                    send = str + " ";
                                } else {
                                    if (send.length() + str.length() >= 80) {
                                        mc.dropMessage(send);
                                        send = str + " ";
                                    } else {
                                        send += str + " ";
                                        if (list.indexOf(str) == (list.size() - 1)) {
                                            mc.dropMessage(send);
                                        } else if (list.get(list.indexOf(str) + 1).startsWith("[Channel")) {
                                            mc.dropMessage(send);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (splitted[0].equalsIgnoreCase("!unjail")) {
                List<Integer> jaillist = Arrays.asList(980000404, 980000304, 109060000, 222020211, 222020111); //Add Jail maps here, separated by commas
                MapleCharacter unjail = null;
                for (ChannelServer cser : ChannelServer.getAllInstances()) {
                    if (unjail == null) {
                        unjail = cser.getPlayerStorage().getCharacterByName(splitted[1]);
                    }
                }
                if (unjail == null) {
                    mc.dropMessage("Character is not found online. Trying offline unjail.");
                    try {
                        java.sql.Connection con = DatabaseConnection.getConnection();
                        PreparedStatement ps = con.prepareStatement("SELECT map FROM characters WHERE name = ?");
                        ps.setString(1, splitted[1]);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            if (jaillist.contains(rs.getInt("map"))) {
                                PreparedStatement ps2 = con.prepareStatement("UPDATE characters SET map = 100000000, spawnpoint = 0 WHERE name = ?");
                                ps2.setString(1, splitted[1]);
                                ps2.executeUpdate();
                                ps2.close();
                                mc.dropMessage("Unjail succeded.");
                            } else {
                                mc.dropMessage("That player is not in Jail!");

                            }
                        } else {
                            mc.dropMessage("Player does not exist!");
                        }
                        rs.close();
                        ps.close();

                    } catch (Exception e) {
                        mc.dropMessage(e.toString());
                    }
                }
                if (jaillist.contains(unjail.getMapId())) {
                    unjail.changeMap(unjail.getClient().getChannelServer().getMapFactory().getMap(100000000), unjail.getClient().getChannelServer().getMapFactory().getMap(100000000).getPortal(0));
                    unjail.saveToDB(true, true);
                    try {
                        cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(6, c.getChannel(), "[Unjail] " + unjail.getName() + " has been unjailed!").getBytes());
                    } catch (RemoteException e) {
                        cserv.reconnectWorld();
                    }
                } else {
                    mc.dropMessage("That player is not in jail! He/She is on map: " + unjail.getMap().getMapName() + ".");
                }
            } else if (splitted[0].equals("!stalk")) {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                if (victim != null) {
                    if (player.getBuddylist().contains(victim.getId()) || player.getGuildId() == victim.getGuildId()) {
                        if (!victim.inEvent() && victim.getGMLevel() < 2 && !victim.isInJail() && !player.isInJail() && !victim.inHideout() && !victim.getMap().isJQ()) {
                            MapleMap target = victim.getMap();
                            player.changeMap(target, target.findClosestSpawnpoint(victim.getPosition()));
                        } else {
                            mc.dropMessage("That player is either in an event, in the guild hideout or is a GM.");
                        }
                    } else {
                        mc.dropMessage("That player isn't in your buddy list or your guild");
                    }
                } else {
                    mc.dropMessage("Player not online or not in the same channel as you");
                }
            } else if (splitted[0].equals("!warpout")) {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                if (victim != null) {
                    if ((victim.inEvent() && victim.getGMLevel() < 3 && wserv.getEventOwner() == player.getId()) || player.getGMLevel() > 1) {
                        victim.changeMap(109050001);
                    } else {
                        mc.dropMessage("That player isn't in an event Map.");
                    }
                } else {
                    mc.dropMessage("That player isn't online or not in the same channel as you.");
                }
            } else if (splitted[0].equals("!youlose")) {
                if (player.inEvent()) {
                    for (MapleCharacter victim : player.getMap().getCharacters()) {
                        if (victim != null) {
                            if (victim.getHp() <= 0) {
                                victim.dropMessage("You have lost the event.");
                                victim.changeMap(109050001);
                            }
                            victim.setHp(victim.getCurrentMaxHp());
                            victim.updateSingleStat(MapleStat.HP, victim.getHp());
                            victim.setMp(victim.getCurrentMaxMp());
                            victim.updateSingleStat(MapleStat.MP, victim.getMp());
                        }
                    }
                } else {
                    mc.dropMessage("You aren't in an event map!");
                }
            } else if (splitted[0].equals("!practicemap")) {
                if (splitted.length == 2) {
                    if (splitted[1].equalsIgnoreCase("on")) {
                        for (MapleCharacter players : player.getMap().getCharacters()) {
                            if (players.getMap().isJQ()) {
                                players.practiceOn();
                            }
                        }
                        mc.dropMessage("Practice mode turned on for all players in your map");
                    } else if (splitted[1].equalsIgnoreCase("off")) {
                        for (MapleCharacter players : player.getMap().getCharacters()) {
                            if (players.getMap().isJQ()) {
                                players.practiceOff();
                            }
                        }
                        mc.dropMessage("Practice mode turned off for all players in your map");
                    } else {
                        mc.dropMessage("Syntax: !practicemode <on/off>");
                    }
                } else {
                    mc.dropMessage("Syntax: !practicemode <on/off>");
                }
            } else if (splitted[0].equals("!warphosterhere")) {
                if (splitted.length == 1) {
                    if (player.inEvent()) {
                        MapleCharacter hoster = cserv.getPlayerStorage().getCharacterByName(wserv.getEventHost());
                        if (hoster != null) {
                            if (hoster != player) {
                                if (hoster.getMapId() != player.getMapId()) {
                                    hoster.changeMapSpecial(player.getMap(), player.getPosition());
                                } else {
                                    mc.dropMessage("Hoster is already in the same map as you");
                                }
                            } else {
                                mc.dropMessage("You cannot use this on yourself");
                            }
                        } else {
                            mc.dropMessage("Hoster isn't online or not in the same channel as you");
                        }
                    } else {
                        mc.dropMessage("You are not in an event");
                    }
                } else {
                    mc.dropMessage("Syntax: !warphosterhere");
                }
            } else if (splitted[0].equals("!removepets")) {
                if (splitted.length == 2) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim != null) {
                        if ((victim.inEvent() && player.inEvent() && wserv.getEventOwner() == player.getId()) || player.isGM()) {
                            if (victim.getPets().length > 0) {
                                victim.unequipAllPets();
                                mc.dropMessage("Removed all of " + victim.getName() + "'s pets");
                                victim.dropMessage(player.getName() + " has just removed all of your pets");
                            } else {
                                mc.dropMessage(victim.getName() + " does not have any pets");
                            }
                        } else {
                            mc.dropMessage("You can only do this in event maps that you're hosting an event in");
                        }
                    } else {
                        mc.dropMessage(splitted[1] + " is not online or not in your channel");
                    }
                } else {
                    mc.dropMessage("Syntax: !removepets <ign>");
                }

            } else if (splitted[0].equals("!removemappets") || splitted[0].equals("!removeallpets")) {
                if ((player.getMap().isEvent() && wserv.getEventOwner() == player.getId()) || player.isGM()) {
                    for (MapleCharacter chars : player.getMap().getCharacters()) {
                        if (chars.getPets()[0] != null) {
                            chars.unequipAllPets();
                            mc.dropMessage("Removed all of " + chars.getName() + "'s pets");
                            chars.dropMessage(player.getName() + " has just removed all of your pets");
                        }
                    }
                } else {
                    mc.dropMessage("You can only do this in event maps that you're hosting an event in");
                }
            } else if (splitted[0].equals("!killbobs")) {
                if (player.getMapId() == 109010104) {
                    int count = 0;
                    List<MapleMapObject> monsters = player.getMap().getMapObjectsInRange(player.getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.MONSTER));
                    for (MapleMapObject monstermo : monsters) {
                        MapleMonster monster = (MapleMonster) monstermo;
                        player.getMap().killMonster(monster, player, false);
                        count++;
                    }
                    for (MapleCharacter chars : player.getMap().getCharacters()) {
                        chars.dropMessage("All " + count + " bobs were cleared.");
                    }
                } else {
                    mc.dropMessage("You can only do this in the bob map. Use '!goto bob' to get there.");
                }

            } else if (splitted[0].equals("!bobleft")) {
                for (MapleMapObject obj : player.getMap().getAllPlayer()) {
                    MapleCharacter players = (MapleCharacter) obj;
                    if (!players.isHidden() && players.getGMLevel() < 3 && players.getPosition().x < 2100 && players != player) {
                        players.changeMap(109050001);
                    }
                }
                player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(6, "[Event] Losers warped out"));
            } else if (splitted[0].equals("!bobright")) {
                for (MapleMapObject obj : player.getMap().getAllPlayer()) {
                    MapleCharacter players = (MapleCharacter) obj;
                    if (!players.isHidden() && players.getGMLevel() < 3 && players.getPosition().x > 180 && players != player) {
                        players.changeMap(109050001);
                    }
                }
                player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(6, "[Event] Losers warped out"));
            } else if (splitted[0].equals("!bobevent")) {
                MapleMap map = cserv.getMapFactory().getMap(109010104); // x = 300 -> 2000 ; y = 34
                if (map.getId() == player.getMap().getId()) {
                    int amount = 8;
                    if (splitted.length > 1) {
                        try {
                            amount = Integer.parseInt(splitted[1]);
                        } catch (NumberFormatException nfe) {
                            mc.dropMessage("Invalid amount of bobs");
                            return;
                        }
                        if (amount > 30) {
                            amount = 30;
                            mc.dropMessage("The max bobs you can spawn is 30");
                        }
                    }
                    if (map.getMonsterCount() + amount > 30) {
                        mc.dropMessage("There were already " + map.getMonsterCount() + " bobs spawned");
                        amount = 30 - map.getMonsterCount();
                    }
                    for (int i = 0; i < amount; i++) {
                        MapleMonster mob = MapleLifeFactory.getMonster(9400551);
                        double x = Math.floor((Math.random() * 1700) + 300);
                        map.spawnMonsterOnGroundBelow(mob, new Point((int) x, 34));
                    }
                    map.broadcastMessage(MaplePacketCreator.serverNotice(6, "[Bob Event] " + amount + " Bobs were spawned across the map!"));
                } else {
                    mc.dropMessage("You can only do this in the bob map. Use '!goto bob' to get there.");
                }
            } else if (splitted[0].equals("!text")) {
                String msg = "normal, green";
                if (player.getDonorLevel() == 2) {
                    msg += ", yellow";
                }
                if (splitted.length == 1) {
                    mc.dropMessage("Wrong color. Available colors: " + msg);
                    return;
                }
                String type = splitted[1];
                int text = 0;
                if (type.equalsIgnoreCase("normal")) {
                    text = 0;
                } else if (type.equalsIgnoreCase("yellow") && player.getDonorLevel() > 1) {
                    text = 9;
                } else if (type.equalsIgnoreCase("green")) {
                    text = 4;
                } else {
                    mc.dropMessage("Wrong color. Available colors: " + msg);
                    return;
                }
                player.setGMText(text);
            } else if (splitted[0].equals("!sexchange")) {
                player.setGender(player.getGender() == 1 ? 0 : 1);
                mc.dropMessage("You are now a little " + (player.getGender() == 0 ? "boy" : "girl"));
            } else if (splitted[0].equalsIgnoreCase("!event")) {
                if (splitted.length > 1) {
                    if (splitted[1].equalsIgnoreCase("create") || splitted[1].equalsIgnoreCase("c")) {
                        
                        int mapId = player.getMap().getId();
                        String eventName = StringUtil.joinStringFrom(splitted, 2);
                        String front = "[GM Event] " + eventName + " : ";
                        
                        if(c.getPlayer().timeSinceLastVote()/60 > 1440) {
                            mc.dropMessage("You haven't voted in the last 24 hours, please go on www.voidms.com and vote!");
                            return;
                        } else if (player.getHoster() != null) {
                            front = "[Player Hosted Event] " + eventName + " : ";
                        } else {
                            if (player.getGMLevel() == 1) {
                                front = "[Donor Event] " + eventName + " : ";
                            } else if (player.getGMLevel() == 2) {
                                front = "[Intern Event] " + eventName + " : ";
                            } else if (player.getGMLevel() == 4) {
                                front = "[SGM Event] " + eventName + " : ";
                            } else if (player.getGMLevel() == 5) {
                                front = "[Admin Event] " + eventName + " : ";
                            }
                        }
                        if (!wserv.hasEvent()) {
                            if (splitted.length >= 3) {
                                if (player.getHoster() == null) {
                                    wserv.startEvent(mapId, player.getName(), player.getId(), eventName, cserv.getChannel());
                                    try {
                                        cserv.getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, front + "Channel " + cserv.getChannel() + ", hosted by " + player.getName() + ".").getBytes());
                                        cserv.getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, "To join type @join.").getBytes());
                                    } catch (Exception e) {
                                    }
                                } else {
                                    MapleCharacter host = cserv.getPlayerStorage().getCharacterByName(player.getHoster());
                                    if (host != null) {
                                        wserv.startEvent(mapId, host.getName(), player.getId(), eventName, cserv.getChannel());
                                        try {
                                            cserv.getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, front + "Channel " + cserv.getChannel() + ", hosted by " + host.getName() + ".").getBytes());
                                            cserv.getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, "To join type @join.").getBytes());
                                        } catch (Exception e) {
                                        }
                                    } else {
                                        mc.dropMessage(splitted[4] + " is offline or doesnt exist");
                                        player.setHoster(null);
                                    }
                                }
                            } else {
                                mc.dropMessage("!event <create/c> <eventName/'none'> - You host an event");
                                mc.dropMessage("!event hoster <hosterIGN> - You register <hosterIGN> as the hoster instead of yourself");
                                mc.dropMessage("EX : !event create Hide and Seek");
                                mc.dropMessage("EX(with hoster) : !event hoster supersin THEN !event <create> <eventname>");
                                mc.dropMessage("!event start - Starts the created event and disables @join");
                                mc.dropMessage("!event join <on/off> - Enables/Disables the @join command");
                                mc.dropMessage("!event remind - Send a reminder about the event");
                                mc.dropMessage("!event end - Ends the event on your channel");
                                mc.dropMessage("!event end <1st> <2nd> <3rd> - Ends the event on your channel and shows the winners");
                                mc.dropMessage("!event status - Shows the information about an on-going event");
                            }
                        } else {
                            mc.dropMessage("There is already an event started");
                            mc.dropMessage("Hosted by: " + wserv.getEventHost());
                            mc.dropMessage("Map: " + wserv.getEventMap());
                            mc.dropMessage("Channel " + wserv.getEventChannel());
                            if (!wserv.getEventName().equalsIgnoreCase("none")) {
                                mc.dropMessage("Kind: " + wserv.getEventName());
                            }
                        }
                    } else if (splitted[1].equalsIgnoreCase("start")) {
                        if (wserv.hasEvent()) {
                            wserv.setEventWarp(false);
                            String front = "[Event] : ";
                            if (!wserv.getEventName().equalsIgnoreCase("none")) {
                                front = "[Event] " + wserv.getEventName() + " : ";
                            }
                            try {
                                cserv.getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, front + "Channel " + cserv.getChannel() + ", hosted by " + wserv.getEventHost() + " has started!").getBytes());
                                cserv.getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, "You will no longer be able to join the event on channel " + cserv.getChannel() + " unless the hoster re-enables @join.").getBytes());
                                player.clearReminds();
                            } catch (Exception e) {
                            }
                        } else {
                            mc.dropMessage("There is no event going on right now");
                        }
                    } else if (splitted[1].equalsIgnoreCase("join")) {
                        if (wserv.hasEvent()) {
                            String front = "[Event] : ";
                            if (!wserv.getEventName().equalsIgnoreCase("none")) {
                                front = "[Event] " + wserv.getEventName() + " : ";
                            }
                            if (splitted[2].equalsIgnoreCase("on")) {
                                if (!wserv.canEventWarp()) {
                                    wserv.setEventWarp(true);
                                    try {
                                        cserv.getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, front + "Channel " + cserv.getChannel() + ", hosted by " + wserv.getEventHost() + " has re-enabled @join!").getBytes());
                                        cserv.getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, "To join the event type @join.").getBytes());
                                    } catch (Exception e) {
                                    }
                                } else {
                                    mc.dropMessage("@join command is already enabled");
                                }
                            } else if (splitted[2].equalsIgnoreCase("off")) {
                                if (wserv.canEventWarp()) {
                                    wserv.setEventWarp(false);
                                    try {
                                        cserv.getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, front + "Channel " + cserv.getChannel() + ", hosted by " + wserv.getEventHost() + " has disabled @join.").getBytes());
                                        cserv.getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, "You will no longer be able to join the event on channel " + cserv.getChannel() + " unless the hoster re-enables @join.").getBytes());
                                    } catch (Exception e) {
                                    }
                                } else {
                                    mc.dropMessage("@join command is already disabled");
                                }
                            } else {
                                mc.dropMessage("!event <create/c> <eventName/'none'> - You host an event");
                                mc.dropMessage("!event hoster <hosterIGN> - You register <hosterIGN> as the hoster instead of yourself");
                                mc.dropMessage("EX : !event create Hide and Seek");
                                mc.dropMessage("EX(with hoster) : !event hoster supersin THEN !event <create> <eventname>");
                                mc.dropMessage("!event start - Starts the created event and disables @join");
                                mc.dropMessage("!event join <on/off> - Enables/Disables the @join command");
                                mc.dropMessage("!event remind - Send a reminder about the event");
                                mc.dropMessage("!event end - Ends the event on your channel");
                                mc.dropMessage("!event end <1st> <2nd> <3rd> - Ends the event on your channel and shows the winners");
                                mc.dropMessage("!event status - Shows the information about an on-going event");
                            }
                        } else {
                            mc.dropMessage("There is no event going on right now");
                        }
                    } else if (splitted[1].equalsIgnoreCase("status")) {
                        if (wserv.hasEvent()) {
                            if (wserv.canEventWarp()) {
                                mc.dropMessage("Status: Waiting for players");
                            } else {
                                mc.dropMessage("Status: Started");
                            }
                            mc.dropMessage("Host: " + wserv.getEventHost());
                            mc.dropMessage("Map: " + wserv.getEventMap());
                            if (!wserv.getEventName().equalsIgnoreCase("none")) {
                                mc.dropMessage("Kind: " + wserv.getEventName());
                            }
                            if (wserv.canEventWarp()) {
                                mc.dropMessage("New players are currently allowed to join the event");
                            } else {
                                mc.dropMessage("New players are not allowed to join the event");
                            }
                        } else {
                            mc.dropMessage("There is no event going on right now");
                        }
                    } else if (splitted[1].equalsIgnoreCase("remind")) {
                        if (wserv.hasEvent()) {
                            if (player.getGMLevel() < 2 && wserv.getEventOwner() != player.getId()) {
                                mc.dropMessage("You cannot use this command during someone else's event");
                                return;
                            }
                            if (player.getReminds() < 4) {
                                if (wserv.canEventWarp()) {
                                    String front = "[Player Hosted Event] ";
                                    if (player.getHoster() == null) {
                                        if (player.getGMLevel() == 1) {
                                            front = "[Donor Event] ";
                                        } else if (player.getGMLevel() == 2) {
                                            front = "[Intern Event] ";
                                        } else if (player.getGMLevel() == 3) {
                                            front = "[GM Event] ";
                                        } else if (player.getGMLevel() == 4) {
                                            front = "[SGM Event] ";
                                        } else if (player.getGMLevel() == 5) {
                                            front = "[Admin Event] ";
                                        }
                                    }
                                    if (!wserv.getEventName().equalsIgnoreCase("none")) {
                                        front += wserv.getEventName() + " : ";
                                    } else {
                                        front += " : ";
                                    }
                                    try {
                                        cserv.getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, front + "Channel " + cserv.getChannel() + ", hosted by " + wserv.getEventHost() + " is waiting!").getBytes());
                                        cserv.getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, "To join the event type @join.").getBytes());
                                        player.addRemind();
                                    } catch (Exception e) {
                                    }
                                } else {
                                    mc.dropMessage("@join command is disabled. Type !event join on to re-enable it");
                                }
                            } else {
                                mc.dropMessage("You cannot remind players about your event more than 3 times. Please use !event start to commence the event");
                                return;
                            }
                        } else {
                            mc.dropMessage("There is no event going on right now");
                        }
                    } else if (splitted[1].equalsIgnoreCase("end")) {
                        if (wserv.hasEvent()) {
                            if (player.getGMLevel() < 2 && wserv.getEventOwner() != player.getId()) {
                                mc.dropMessage("You cannot use this command during someone else's event.");
                                return;
                            }
                            String front = "[Event] : ";
                            String winners = "";
                            if (!wserv.getEventName().equalsIgnoreCase("none")) {
                                front = "[Event] " + wserv.getEventName() + " : ";
                            }
                            if (splitted.length == 3) {
                                winners = splitted[2] + "-[1st]";
                            } else if (splitted.length == 4) {
                                winners = splitted[2] + "-[1st] and " + splitted[3] + "-[2nd]";
                            }
                            if (splitted.length == 5) {
                                winners = splitted[2] + "-[1st], " + splitted[3] + "-[2nd] and " + splitted[4] + "-[3rd]";
                            }
                            if (splitted.length > 5) {
                                winners = "";
                            }
                            try {
                                if (winners.equals("")) {
                                    cserv.getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, front + "Channel " + cserv.getChannel() + ", hosted by " + wserv.getEventHost() + " has now ended.").getBytes());
                                    cserv.getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, "Congratulations to the winner(s).").getBytes());
                                } else {
                                    cserv.getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, front + "Channel " + cserv.getChannel() + ", hosted by " + wserv.getEventHost() + " has now ended.").getBytes());
                                    cserv.getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, "Congratulations to the winner(s) : " + winners).getBytes());
                                }
                            } catch (Exception e) {
                            }
                            wserv.endEvent();
                            player.setHoster(null);
                        } else {
                            mc.dropMessage("There is no event going on right now.");
                        }
                    } else if (splitted[1].equalsIgnoreCase("hoster")) {
                        MapleCharacter host = cserv.getPlayerStorage().getCharacterByName(splitted[2]);
                        if (host != null) {
                            player.setHoster(host.getName());
                            mc.dropMessage(player.getHoster() + " is registered as the hoster. Use !event <c/create> <event name> to start the event");
                        } else {
                            mc.dropMessage("Player '" + splitted[2] + "' is not online");
                        }
                    } else {
                        mc.dropMessage("!event <create/c> <eventName/'none'> - You host an event");
                        mc.dropMessage("!event hoster <hosterIGN> - You register <hosterIGN> as the hoster instead of yourself");
                        mc.dropMessage("EX : !event create Hide and Seek");
                        mc.dropMessage("EX(with hoster) : !event hoster supersin THEN !event <create> <eventname>");
                        mc.dropMessage("!event start - Starts the created event and disables @join");
                        mc.dropMessage("!event join <on/off> - Enables/Disables the @join command");
                        mc.dropMessage("!event remind - Send a reminder about the event");
                        mc.dropMessage("!event end - Ends the event on your channel");
                        mc.dropMessage("!event end <1st> <2nd> <3rd> - Ends the event on your channel and shows the winners");
                        mc.dropMessage("!event status - Shows the information about an on-going event");
                    }
                } else {
                    mc.dropMessage("!event <create/c> <eventName/'none'> - You host an event");
                    mc.dropMessage("!event hoster <hosterIGN> - You register <hosterIGN> as the hoster instead of yourself");
                    mc.dropMessage("EX : !event create Hide and Seek");
                    mc.dropMessage("EX(with hoster) : !event hoster supersin THEN !event <create> <eventname>");
                    mc.dropMessage("!event start - Starts the created event and disables @join");
                    mc.dropMessage("!event join <on/off> - Enables/Disables the @join command");
                    mc.dropMessage("!event remind - Send a reminder about the event");
                    mc.dropMessage("!event end - Ends the event on your channel");
                    mc.dropMessage("!event end <1st> <2nd> <3rd> - Ends the event on your channel and shows the winners");
                    mc.dropMessage("!event status - Shows the information about an on-going event");
                }
            } else if (splitted[0].equalsIgnoreCase("!warpbox")) {
                if (splitted.length < 3) {
                    int box = -1;
                    if (splitted.length == 1) {
                        box = (int) Math.round(Math.ceil(1 + (Math.random() * (8))));
                    } else {
                        try {
                            box = Integer.parseInt(splitted[1]);
                        } catch (NumberFormatException nfe) {
                            mc.dropMessage("Invalid box");
                            mc.dropMessage("Syntax: !warpbox <1/2/3/4/5/6/7/8/9/>");
                            return;
                        }
                    }
                    if (box > 0 && box < 10) {
                        int x1 = -1;
                        int x2 = -1;
                        int y = -1;
                        switch (box) {
                            case 9:
                                y = -145;
                                x1 = -45;
                                x2 = 18;
                                break;
                            case 8:
                                y = -145;
                                x1 = -116;
                                x2 = -45;
                                break;
                            case 7:
                                y = -145;
                                x1 = -185;
                                x2 = -116;
                                break;
                            case 6:
                                y = -145;
                                x1 = -255;
                                x2 = -185;
                                break;
                            case 5:
                                y = -184;
                                x1 = -116;
                                x2 = -45;
                                break;
                            case 4:
                                y = -184;
                                x1 = -185;
                                x2 = -116;
                                break;
                            case 3:
                                y = -184;
                                x1 = -255;
                                x2 = -185;
                                break;
                            case 2:
                                y = -223;
                                x1 = -185;
                                x2 = -116;
                                break;
                            case 1:
                                y = -223;
                                x1 = -255;
                                x2 = -185;
                                break;
                        }
                        for (MapleMapObject people : player.getMap().getCharactersAsMapObjects()) {
                            MapleCharacter person = (MapleCharacter) people;
                            Point pos = person.getPosition();
                            if (pos.y <= (y + 10) && pos.y >= (y - 10) && pos.x >= x1 && pos.x <= x2 & person.getGMLevel() < 3 && person != player) {
                                person.changeMap(100000000, person.getMap().getReturnMap().getPortal(0));
                            }
                        }
                        player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(6, "Players on box " + box + " warped out"));
                    } else {
                        mc.dropMessage("Invalid box");
                        mc.dropMessage("Syntax: !warpbox <1/2/3/4/5/6/7/8/9/>");
                    }
                } else {
                    mc.dropMessage("Syntax: !warpbox <1/2/3/4/5/6/7/8/9/>");
                }
            } else if (splitted[0].equalsIgnoreCase("!top") || splitted[0].equalsIgnoreCase("!false") || splitted[0].equalsIgnoreCase("!true") || splitted[0].equalsIgnoreCase("!middle")) {
                if (player.getMap().getId() == 109020001) {
                    if (splitted[0].equalsIgnoreCase("!top")) {
                        for (MapleMapObject wrappedPerson : player.getMap().getCharactersAsMapObjects()) {
                            MapleCharacter person = (MapleCharacter) wrappedPerson;
                            if (person.getPosition().y <= -206 && person.getGMLevel() < 3 && person != player) {
                                person.changeMap(person.getMap().getReturnMap(), person.getMap().getReturnMap().getPortal(0));
                            }
                        }
                        player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(5, "Top Warped Out."));
                    } else if (splitted[0].equalsIgnoreCase("!false")) {
                        for (MapleMapObject wrappedPerson : player.getMap().getCharactersAsMapObjects()) {
                            MapleCharacter person = (MapleCharacter) wrappedPerson;
                            if (person.getPosition().y > -206 && person.getPosition().y <= 334 && person.getPosition().x >= -952 && person.getPosition().x <= -308 && person.getGMLevel() < 3 && person != player) {
                                person.changeMap(person.getMap().getReturnMap(), person.getMap().getReturnMap().getPortal(0));
                            }
                        }
                        player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(5, "The Answer was False."));
                    } else if (splitted[0].equalsIgnoreCase("!true")) {
                        for (MapleMapObject wrappedPerson : player.getMap().getCharactersAsMapObjects()) {
                            MapleCharacter person = (MapleCharacter) wrappedPerson;
                            if (person.getPosition().y > -206 && person.getPosition().y <= 334 && person.getPosition().x >= -142 && person.getPosition().x <= 502 && person.getGMLevel() < 3 && person != player) {
                                person.changeMap(person.getMap().getReturnMap(), person.getMap().getReturnMap().getPortal(0));
                            }
                        }
                        player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(5, "The Answer was True."));
                    } else if (splitted[0].equalsIgnoreCase("!middle")) {
                        for (MapleMapObject wrappedPerson : player.getMap().getCharactersAsMapObjects()) {
                            MapleCharacter person = (MapleCharacter) wrappedPerson;
                            if (person.getPosition().y > -206 && person.getPosition().y <= 274 && person.getPosition().x >= -308 && person.getPosition().x <= -142 && person.getGMLevel() < 3 && person != player) {
                                person.changeMap(person.getMap().getReturnMap(), person.getMap().getReturnMap().getPortal(0));
                            }
                        }
                        player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(5, "Middle Warped Out."));
                    }
                } else {
                    player.dropMessage("These commands can only be used in the OX Map");

                }
            } else if (splitted[0].equals("!healparty")) {
                if (splitted.length == 2) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim != null) {
                        if (victim.getParty() != null) {
                            if (player.getGMLevel() == 1 && !player.inEvent() && wserv.getEventOwner() != player.getId()) {
                                mc.dropMessage("You need to be in an event map to use this command");
                            } else {
                                for (MapleCharacter pchar : cserv.getPartyMembers(victim.getParty())) {
                                    if (pchar.getHp() < pchar.getCurrentMaxMp()) {
                                        pchar.setHp(pchar.getCurrentMaxHp());
                                        pchar.updateSingleStat(MapleStat.HP, pchar.getHp());
                                        pchar.setMp(pchar.getCurrentMaxMp());
                                        pchar.updateSingleStat(MapleStat.MP, pchar.getMp());
                                        mc.dropMessage("Healed " + pchar.getName());
                                    } else {
                                        mc.dropMessage(pchar.getName() + " is already healed");
                                    }
                                }
                            }
                        } else {
                            mc.dropMessage(victim.getName() + " is not in any party");
                        }
                    } else {
                        mc.dropMessage(splitted[1] + " is either offline or not in your channel");
                    }
                } else {
                    mc.dropMessage("Syntax: !healparty <partymember>");
                }
            } else if (splitted[0].equals("!healmap")) {
                if (player.getGMLevel() == 1 && !player.inEvent() && wserv.getEventOwner() != player.getId()) {
                    mc.dropMessage("You need to be in an event map to use this command");
                } else {
                    for (MapleCharacter map : player.getMap().getCharacters()) {
                        if (map != null) {
                            map.setHp(map.getCurrentMaxHp());
                            map.updateSingleStat(MapleStat.HP, map.getHp());
                            map.setMp(map.getCurrentMaxMp());
                            map.updateSingleStat(MapleStat.MP, map.getMp());
                        }
                    }
                }
            } else if (splitted[0].equals("!mute")) {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                if (victim != null) {
                    if (player.getGMLevel() < 2) {
                        if (victim.inEvent() && wserv.getEventOwner() == player.getId()) {
                            victim.canTalk(!victim.getCanTalk());
                            victim.dropMessage(5, "Your chatting ability is now " + (victim.getCanTalk() ? "on" : "off"));
                            player.dropMessage(6, "Player's chatting ability is now set to " + victim.getCanTalk());
                        } else {
                            mc.dropMessage("You can't mute someone outside of an event map in which you're hosting an event");
                        }
                    } else {
                        victim.canTalk(!victim.getCanTalk());
                        victim.dropMessage(5, "Your chatting ability is now " + (victim.getCanTalk() ? "on" : "off"));
                        player.dropMessage(6, "Player's chatting ability is now set to " + victim.getCanTalk());
                    }
                } else {
                    mc.dropMessage("Player is either not online or not in your channel");
                }
            } else if (splitted[0].equalsIgnoreCase("!random")) {
                if (splitted.length == 3) {
                    long min = Long.parseLong(splitted[1]);
                    long max = Long.parseLong(splitted[2]);
                    long randnum = Math.round(Math.ceil(min + (Math.random() * ((max - min)))));
                    player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(6, "Random number picked was " + Long.toString(randnum) + "."));
                } else if (splitted.length == 2) {
                    player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(6, "Random number picked was " + Math.round(Math.ceil(Math.random() * (Integer.parseInt(splitted[1])))) + "."));
                } else {
                    mc.dropMessage("!Syntax : random <max> OR !random <min> <max>");
                }
            } else if (splitted[0].equalsIgnoreCase("!randomplayer")) {
                Collection<MapleCharacter> chars = null;
                for (MapleCharacter m : player.getMap().getCharacters()) {
                    if (!m.isExempted()) {
                        chars.add(m);
                    }
                }
                if (chars.size() > 0) {
                    Random rand = new Random();
                    int random = rand.nextInt(chars.size());
                    int i = 0;
                    for (MapleCharacter m : chars) {
                        if (i == random) {
                            player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(6, m.getName() + " has been randomly selected."));
                            break;
                        }
                        i++;
                    }
                } else {
                    player.dropMessage("There are no selectable players in your map");
                }
                chars.clear();
            } else if (splitted[0].equalsIgnoreCase("!id")) {
                try {
                    URL url = new URL("http://www.mapletip.com/search_java.php?search_value=" + splitted[1] + "&check=true");
                    URLConnection urlConn = url.openConnection();
                    urlConn.setDoInput(true);
                    url.openConnection();
                    urlConn.setUseCaches(false);
                    BufferedReader dis = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    String s;
                    while ((s = dis.readLine()) != null) {
                        mc.dropMessage(s);
                    }
                    dis.close();
                } catch (MalformedURLException mue) {
                } catch (IOException ioe) {
                }
            } else if (splitted[0].equalsIgnoreCase("!warpmap")) {
                if (!player.isGM()) {
                    if (player.inEvent() && wserv.getEventOwner() == player.getId()) {
                        for (MapleCharacter mch : player.getMap().getCharacters()) {
                            if (mch.getId() != player.getId() && mch != null && mch.getGMLevel() < 3) {
                                mch.changeMap(109050001);
                            }
                        }
                    } else {
                        mc.dropMessage("You are not in an event map in which you're currently hosting an event in!");
                    }
                } else {
                    MapleMap target = cserv.getMapFactory().getMap(Integer.parseInt(splitted[1]));
                    if (target != null) {
                        boolean event = target.isEvent();
                        for (MapleCharacter mch : player.getMap().getCharacters()) {
                            if (mch != null) {
                                if (event) {
                                    mch.changeMapSpecial(target.getId());
                                } else {
                                    mch.changeMap(target.getId());
                                }
                            }
                        }
                    } else {
                        mc.dropMessage("Invalid MapID");
                    }
                }
            } else if (splitted[0].equalsIgnoreCase("!warpparty")) {
                if (splitted.length > 1) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim != null) {
                        if (victim.getParty() != null) {
                            if (splitted.length == 2) {
                                if (player.inEvent() && victim.inEvent() && wserv.getEventOwner() == player.getId()) {
                                    victim.warpParty(109050001, 0);
                                } else {
                                    mc.dropMessage("You or " + victim.getName() + " are not in an event map in which you're hosting an event in");
                                }
                            } else if (player.isGM()) {
                                MapleMap target;
                                try {
                                    target = cserv.getMapFactory().getMap(Integer.parseInt(splitted[2]));
                                } catch (NumberFormatException nfe) {
                                    mc.dropMessage("Invalid Map ID");
                                    return;
                                }
                                if (target != null) {
                                    if (target.isEvent()) {
                                        victim.warpPartySpecial(target.getId());
                                    } else {
                                        victim.warpParty(target.getId(), 0);
                                    }
                                } else {
                                    mc.dropMessage("Invalid map");
                                }
                            } else {
                                mc.dropMessage("Syntax: !warpparty <ign>");
                            }
                        } else {
                            mc.dropMessage(victim.getName() + " is not in a party");
                        }
                    } else {
                        mc.dropMessage(splitted[1] + " is either offline or not in the same channel as you");
                    }
                } else {
                    mc.dropMessage("Syntax: !warpparty <ign> <id>");
                }
            } else if (splitted[0].equals("!jail")) {
                if (splitted.length < 2 || splitted.length > 3) {
                    mc.dropMessage("Syntax: !jail <Person> <[OPTIONAL]1/2/3/4>");
                } else {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim != null) {
                        if (victim.getGMLevel() >= player.getGMLevel()) {
                            mc.dropMessage("You can't warp GMs");
                            return;
                        }
                        if (splitted.length == 2) {
                            if (player.canJail()) {
                                victim.changeMap(980000404);
                            } else {
                                mc.dropMessage("You need to wait 10 minutes before you can jail someone again");
                            }
                        } else {
                            Map<String, Integer> maps = new HashMap<String, Integer>();
                            maps.put("1", 980000404);
                            maps.put("2", 222020211);
                            maps.put("3", 222020111);
                            maps.put("4", 980000304);
                            if (maps.containsKey(splitted[2])) {
                                if (player.canJail()) {
                                    victim.changeMap(maps.get(splitted[2]));
                                } else {
                                    mc.dropMessage("You need to wait 10 minutes before you can jail someone again");
                                }
                            } else {
                                mc.dropMessage("Syntax: !jail <IGN> <[OPTIONAL]1/2/3/4>");
                                return;
                            }
                            maps.clear();
                        }
                        if (player.getGMLevel() < 2) {
                            final MapleCharacter p = player;
                            player.setCanJail(false);
                            TimerManager.getInstance().schedule(new Runnable() {

                                public void run() {
                                    p.setCanJail(true);
                                }
                            }, 10 * 60 * 1000);
                        }
                        mc.dropMessage(victim.getName() + " was jailed!");
                        victim.dropMessage("You've been jailed by " + player.getName());
                    } else {
                        mc.dropMessage(splitted[1] + " is either offline or not in your channel");
                    }
                }
            } else if (splitted[0].equalsIgnoreCase("!say")) {
                if (splitted.length > 1) {
                    if (!player.canSay()) {
                        if (player.getGMLevel() == 1) {
                            mc.dropMessage("You can only use !say once every 10 minutes.");
                        } else {
                            mc.dropMessage("You can only use !say once a minute.");
                        }
                    } else {
                        if (player.getGMLevel() == 1) {
                            final MapleCharacter p = player;
                            player.setCanSay(false);
                            MaplePacket packet = MaplePacketCreator.serverNotice(5, "[Donor " + c.getPlayer().getName() + "] " + StringUtil.joinStringFrom(splitted, 1));
                            try {
                                ChannelServer.getInstance(c.getChannel()).getWorldInterface().broadcastMessage(
                                        c.getPlayer().getName(), packet.getBytes());
                            } catch (RemoteException e) {
                                c.getChannelServer().reconnectWorld();
                            }
                            TimerManager.getInstance().schedule(new Runnable() {

                                public void run() {
                                    p.setCanSay(true);
                                }
                            }, 10 * 60 * 1000);
                        } else if (player.getGMLevel() == 2) {
                            final MapleCharacter p = player;
                            player.setCanSay(false);
                            MaplePacket packet = MaplePacketCreator.serverNotice(5, "[Intern " + c.getPlayer().getName() + "] " + StringUtil.joinStringFrom(splitted, 1));
                            try {
                                ChannelServer.getInstance(c.getChannel()).getWorldInterface().broadcastMessage(c.getPlayer().getName(), packet.getBytes());
                            } catch (RemoteException e) {
                                c.getChannelServer().reconnectWorld();
                            }
                            TimerManager.getInstance().schedule(new Runnable() {

                                public void run() {
                                    p.setCanSay(true);
                                }
                            }, 1 * 60 * 1000);
                        } else {
                            Map<Integer, String> ranks = new HashMap<Integer, String>();
                            ranks.put(3, "GM");
                            ranks.put(4, "SGM");
                            ranks.put(5, "ADMIN");
                            MaplePacket packet = MaplePacketCreator.serverNotice(6, "[" + ranks.get(c.getPlayer().getGMLevel()) + " " + c.getPlayer().getName() + "] " + StringUtil.joinStringFrom(splitted, 1));
                            try {
                                ChannelServer.getInstance(c.getChannel()).getWorldInterface().broadcastMessage(c.getPlayer().getName(), packet.getBytes());
                            } catch (RemoteException e) {
                                c.getChannelServer().reconnectWorld();
                            }
                        }
                    }
                } else {
                    mc.dropMessage("Syntax: !say <message>");
                }
            } else if (splitted[0].equalsIgnoreCase("!killmap")) {
                if (player.getGMLevel() > 1) {
                    for (MapleCharacter mch : player.getMap().getCharacters()) {
                        if (!mch.isHidden() && mch.getGMLevel() < 3 && mch != player) {
                            mch.dispelDebuff(MapleDisease.SEDUCE);
                            mch.dispelDebuffs();
                            mch.dispel();
                            mch.setHp(0);
                            mch.updateSingleStat(MapleStat.HP, 0);
                        }
                    }
                } else if (player.inEvent() && wserv.getEventOwner() == player.getId()) {
                    for (MapleCharacter mch : player.getMap().getCharacters()) {
                        if (mch.getGMLevel() < 3 && !mch.isHidden() && mch != player) {
                            mch.setHp(0);
                            mch.updateSingleStat(MapleStat.HP, 0);
                            player.setHp(player.getMaxHp());
                            player.updateSingleStat(MapleStat.HP, player.getMaxHp());
                            player.setMp(player.getMaxMp());
                            player.updateSingleStat(MapleStat.MP, player.getMaxMp());
                            mch.dispelDebuff(MapleDisease.SEDUCE);
                            mch.dispelDebuffs();
                            mch.dispel();
                        }
                    }
                } else {
                    mc.dropMessage("You are not in an event map in which you're hosting an event in!");
                }
            } else if (splitted[0].equals("!disbandparty")) {
                if (splitted.length == 2) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim != null) {
                        if (((player.getMap().isEvent() && victim.getMap().isEvent()) && (wserv.getEventOwner() == player.getId() || wserv.getEventHost().equalsIgnoreCase(player.getName()))) || player.isGM()) {
                            if (victim.getParty() != null) {
                                MapleParty party = victim.getParty();
                                for (MaplePartyCharacter pchar : party.getMembers()) {
                                    if (pchar.getMapid() == player.getMapId()) {
                                        MapleCharacter victim1 = cserv.getPlayerStorage().getCharacterById(pchar.getId());
                                        victim1.leaveParty();
                                    }
                                }
                                victim.dropMessage(player.getName() + " has disbanded your party");
                                mc.dropMessage("You have disbanded " + victim.getName() + "'s party");
                            }
                        }
                    } else {
                        mc.dropMessage(splitted[1] + " is either offline or not in the same channel as you");
                    }
                } else {
                    mc.dropMessage("Syntax: !disbandparty <ign>");
                }
            } else if (splitted[0].equals("!disbandmapparties") || splitted[0].equals("!disbandparties")) {
                if (((player.getMap().isEvent()) && (wserv.getEventOwner() == player.getId() || wserv.getEventHost().equalsIgnoreCase(player.getName()))) || player.isGM()) {
                    for (MapleCharacter mchar : player.getMap().getCharacters()) {
                        if (mchar.getGMLevel() < 3) {
                            if (mchar.getParty() != null) {
                                mchar.leaveParty();
                                mchar.dropMessage("Your party has been disbanded by " + player.getName());
                                mc.dropMessage("You have disbanded " + mchar.getName() + "'s party");
                            }
                        }
                    }

                } else {
                    mc.dropMessage("You may only do this in your event");
                }
            } else if (splitted[0].equals("!checkmapparties") || splitted[0].equals("!mappartyinfo") || splitted[0].equals("!checkparties")) {
                List<MapleParty> parties = new LinkedList<MapleParty>();
                for (MapleCharacter mchar : player.getMap().getCharacters()) {
                    if (!parties.contains(mchar.getParty())) {
                        parties.add(mchar.getParty());
                    }
                }
                if (!parties.isEmpty()) {
                    mc.dropMessage("Information for parties in your map:");
                    for (MapleParty party : parties) {
                        if (party != null) {
                            mc.dropMessage("ID: " + party.getId() + " || Leader: " + party.getLeader().getName() + " || Total: " + party.getMembers().size());
                            String members = "Members: ";
                            for (MaplePartyCharacter partychar : party.getMembers()) {
                                if (partychar != null) {
                                    members += partychar.getName() + ", ";
                                }
                            }
                            mc.dropMessage(members);
                            mc.dropMessage("--------------------------");
                        }
                    }
                } else {
                    mc.dropMessage("No one in your map has a party");
                }
                parties.clear();
            } else if (splitted[0].equals("!checkparty") || splitted[0].equals("!partyinfo")) {
                if (splitted.length == 2) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim != null) {
                        if (victim.getParty() != null) {
                            mc.dropMessage("Party Information for " + victim.getName() + "'s Party (ID: " + victim.getPartyId() + "):");
                            mc.dropMessage("Leader: " + victim.getParty().getLeader().getName());
                            mc.dropMessage("Members: ");
                            for (MaplePartyCharacter pchar : victim.getParty().getMembers()) {
                                mc.dropMessage("Name: " + pchar.getName() + " Level: " + pchar.getLevel());
                            }
                        } else {
                            mc.dropMessage(victim.getName() + " is not in a party");
                        }
                    } else {
                        mc.dropMessage(splitted[1] + " is not in your channel or offline");
                    }
                } else {
                    mc.dropMessage("Syntax: !checkparty <ign>");
                }
            } else if (splitted[0].equals("!checkalive")) {
                String txt = "";
                int count = 0;
                for (MapleCharacter chars : player.getMap().getCharacters()) {
                    if (chars.isAlive() && !chars.isHidden()) {
                        txt += chars.getName() + ", ";
                        count++;
                    }
                }
                mc.dropMessage("The characters still alive in your map are: " + txt);
                mc.dropMessage("Total: " + count);
            } else if (splitted[0].equals("!checkdead")) {
                String txt = "";
                int count = 0;
                for (MapleCharacter chars : player.getMap().getCharacters()) {
                    if (!chars.isAlive() && !chars.isHidden()) {
                        txt += chars.getName() + ", ";
                        count++;
                    }
                }
                mc.dropMessage("The characters dead in your map are: " + txt);
                mc.dropMessage("Total: " + count);
            } else if (splitted[0].equals("!heal")) {
                MapleCharacter heal = null;
                if (splitted.length == 2) {
                    heal = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (player.getGMLevel() < 2) {
                        if (heal.getGMLevel() > 2 || !heal.inEvent() || wserv.getEventOwner() != player.getId()) {
                            heal = null;
                        }
                    }
                    if (heal == null) {
                        mc.dropMessage("Player was not found.");
                    }
                } else {
                    heal = player;
                }
                heal.setHp(heal.getCurrentMaxHp());
                heal.setMp(heal.getCurrentMaxMp());
                heal.updateSingleStat(MapleStat.HP, heal.getCurrentMaxHp());
                heal.updateSingleStat(MapleStat.MP, heal.getCurrentMaxMp());
            } else if (splitted[0].equals("!unbuff")) {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                if (victim != null) {
                    victim.cancelAllBuffs();
                } else {
                    mc.dropMessage("Player not found");
                }
            } else if (splitted[0].equals("!clock")) {
                player.getMap().broadcastMessage(MaplePacketCreator.getClock(getOptionalIntArg(splitted, 1, 60)));
            } else if (splitted[0].equals("!spy") || splitted[0].equals("!spychar")) {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                if (victim != null) {

                    mc.dropMessage("Players stats are:");
                    mc.dropMessage("Level: " + victim.getLevel() + "  ||  Rebirthed: " + victim.getReborns());
                    mc.dropMessage("JQ Points: " + victim.getKarma() + "  ||  Vote Points: " + victim.votePoints() + "  ||  Donor Points: " + victim.getDP());
                    mc.dropMessage("SNS: " + victim.getItemQuantity(4002000, false) + "  ||  SLS: " + victim.getItemQuantity(4002003, false) + "  ||  YWT: " + victim.getItemQuantity(4031543, false) + "  ||  GWT: " + victim.getItemQuantity(4031544, false) + "  ||  BWT: " + victim.getItemQuantity(4031545, false));
                    mc.dropMessage("Fame: " + victim.getFame() + "  ||  PvP Kills: " + victim.getPvpKills() + "  ||  PvP Deaths: " + victim.getPvpDeaths());
                    mc.dropMessage("AP: " + victim.getRemainingAp() + "  ||  Mesos: " + victim.getMeso() + "  ||  NX Cash: " + victim.getCSPoints(1));
                    mc.dropMessage("Str: " + victim.getStr() + "  ||  Dex: " + victim.getDex() + "  ||  Int: " + victim.getInt() + "  ||  Luk: " + victim.getLuk());
                    mc.dropMessage("Total Str: " + victim.getTotalStr() + "  ||  Total Dex: " + victim.getTotalDex() + "  ||  Total Int: " + victim.getTotalInt() + "  ||  Total Luk: " + victim.getTotalLuk());
                    mc.dropMessage("Hp: " + victim.getHp() + "/" + victim.getCurrentMaxHp() + "  ||  Mp: " + victim.getMp() + "/" + victim.getCurrentMaxMp() + "  ||  Exp: " + victim.getExp());
                    mc.dropMessage("Weapon Attack: " + victim.getTotalWatk() + "  ||  Magic Attack: " + victim.getTotalMagic() + "  ||  Weapon Defence: " + victim.getTotalWdef() + "  ||  Magic Defence: " + victim.getTotalMdef());
                } else {
                    mc.dropMessage("Player not found.");
                }
            } else if (splitted[0].equals("!slap")) {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                int damage;
                try {
                    damage = Integer.parseInt(splitted[2]);
                } catch (NumberFormatException nfe) {
                    mc.dropMessage("Invalid value for HP");
                    return;
                }
                if (!victim.isGM()) {
                    if (victim.getMap().isPvp() || player.getMap().isPvp()) {
                        player.dropMessage("You cannot slap someone in a PvP map");
                    } else if (damage > victim.getMaxHp()) {
                        player.dropMessage("You cannot slap people with more than their max hp of damage. The current max hp of the player you're trying to slap is: " + Integer.toString(victim.getMaxHp()));
                    } else {
                        if (victim.getHp() > damage) {
                            victim.setHp(victim.getHp() - damage);
                            victim.updateSingleStat(MapleStat.HP, victim.getHp());
                            victim.dropMessage(5, player.getName() + " picked up a big fish and slapped you across the head. You've lost " + damage + " hp");
                            mc.dropMessage(victim.getName() + " has " + victim.getHp() + " HP left");
                        } else {
                            player.dropMessage("You can't use !slap to kill players");
                        }
                    }
                } else {
                    player.dropMessage("You can't slap GM's");
                }
            }
        }
    }

    @Override
    public CommandDefinition[] getDefinition() {
        return new CommandDefinition[]{
                    new CommandDefinition("tagmap", 1),
                    new CommandDefinition("dbuff", 1),
                    new CommandDefinition("goto", 1),
                    new CommandDefinition("sexchange", 1),
                    new CommandDefinition("event", 1),
                    new CommandDefinition("true", 1),
                    new CommandDefinition("killmap", 1),
                    new CommandDefinition("top", 1),
                    new CommandDefinition("id", 1),
                    new CommandDefinition("middle", 1),
                    new CommandDefinition("warpbox", 1),
                    new CommandDefinition("spy", 1),
                    new CommandDefinition("random", 1),
                    new CommandDefinition("spychar", 1),
                    new CommandDefinition("disbandparty", 1),
                    new CommandDefinition("disbandparties", 1),
                    new CommandDefinition("disbandmapparties", 1),
                    new CommandDefinition("checkmapparties", 1),
                    new CommandDefinition("checkparties", 1),
                    new CommandDefinition("mappartyinfo", 1),
                    new CommandDefinition("checkparty", 1),
                    new CommandDefinition("partyinfo", 1),
                    new CommandDefinition("false", 1),
                    new CommandDefinition("jaillist", 1),
                    new CommandDefinition("jail", 1),
                    new CommandDefinition("mute", 1),
                    new CommandDefinition("unjail", 1),
                    new CommandDefinition("warpout", 1),
                    new CommandDefinition("youlose", 1),
                    new CommandDefinition("clock", 1),
                    new CommandDefinition("healmap", 1),
                    new CommandDefinition("healparty", 1),
                    new CommandDefinition("bobevent", 1),
                    new CommandDefinition("bobright", 1),
                    new CommandDefinition("bobleft", 1),
                    new CommandDefinition("warphosterhere", 1),
                    new CommandDefinition("closechalkboard", 1),
                    new CommandDefinition("removechalkboard", 1),
                    new CommandDefinition("closechalkboards", 1),
                    new CommandDefinition("removechalkboards", 1),
                    new CommandDefinition("removeallpets", 1),
                    new CommandDefinition("removemappets", 1),
                    new CommandDefinition("removepets", 1),
                    new CommandDefinition("killbobs", 1),
                    new CommandDefinition("checkalive", 1),
                    new CommandDefinition("practicemap", 1),
                    new CommandDefinition("checkdead", 1),
                    new CommandDefinition("heal", 1),
                    new CommandDefinition("text", 1),
                    new CommandDefinition("say", 1),
                    new CommandDefinition("stalk", 1),
                    new CommandDefinition("warpmap", 1),
                    new CommandDefinition("warpparty", 1),
                    new CommandDefinition("slap", 1),
                    new CommandDefinition("randomplayer", 1)
                };
    }
}
