package net.channel.handler;

import java.awt.Rectangle;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import client.Equip;
import client.ExpTable;
import client.IItem;
import client.ISkill;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleInventoryType;
import client.MapleJob;
import client.MaplePet;
import client.MapleStat;
import client.SkillFactory;
import client.messages.ServernoticeMapleClientMessageCallback;
import net.AbstractMaplePacketHandler;
import server.AutobanManager;
import server.MapleInventoryManipulator;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;
import server.MapleItemInformationProvider;
import server.MapleStatEffect;
import server.maps.MapleMap;
import server.maps.MapleMist;
import server.maps.MapleTVEffect;
import tools.Pair;

public class UseCashItemHandler extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        MapleCharacter player = c.getPlayer();
        ServernoticeMapleClientMessageCallback cm = new ServernoticeMapleClientMessageCallback(1, c);
        byte slot = (byte) slea.readShort();
        int itemId = slea.readInt();
        int itemType = itemId / 10000;
        boolean whisper;
        IItem item = player.getInventory(MapleInventoryType.CASH).getItem(slot);
        if (item.getItemId() != itemId || item == null) { // TODO: FIND AN AUTOBAN THAT ACTUALLY WORKS.
            if (c.getPlayer().getFalseItemCount() > 1) { // on the third try
                AutobanManager.getInstance().autoban(c, c.getPlayer().getName() + "': Packet Editing (using a non-existing item or quantity < 1)");
            }
            c.getPlayer().addFalseItemCount();
            return;
        }
        c.getPlayer().clearFalseItemCount();
        try {
            switch (itemType) {
                case 505: // AP/SP reset
                    if (itemId > 5050000) {
                        int SPTo = slea.readInt();
                        int SPFrom = slea.readInt();

                        ISkill skillSPTo = SkillFactory.getSkill(SPTo);
                        ISkill skillSPFrom = SkillFactory.getSkill(SPFrom);

                        int maxlevel = skillSPTo.getMaxLevel();
                        int curLevel = player.getSkillLevel(skillSPTo);
                        int curLevelSPFrom = player.getSkillLevel(skillSPFrom);

                        if (curLevel + 1 <= maxlevel && curLevelSPFrom > 0) {
                            player.changeSkillLevel(skillSPFrom, curLevelSPFrom - 1, player.getMasterLevel(skillSPFrom));
                            player.changeSkillLevel(skillSPTo, curLevel + 1, player.getMasterLevel(skillSPTo));
                        }
                    } else {
                        List<Pair<MapleStat, Integer>> statupdate = new ArrayList<Pair<MapleStat, Integer>>(2);
                        int APTo = slea.readInt();
                        int APFrom = slea.readInt();

                        switch (APFrom) {
                            case 64: // str
                                if (player.getStr() <= 4 || ((player.getJob().getId() / 100) == 1 && player.getStr() <= 35)) {
                                    break;
                                }
                                player.setStr(player.getStr() - 1);
                                statupdate.add(new Pair<MapleStat, Integer>(MapleStat.STR, player.getStr()));
                                break;
                            case 128: // dex
                                if (player.getDex() <= 4
                                        || (((player.getJob().getId() / 100) == 4
                                        || (player.getJob().getId() / 100) == 3)
                                        && player.getDex() <= 25)) {
                                    break;
                                }
                                player.setDex(player.getDex() - 1);
                                statupdate.add(new Pair<MapleStat, Integer>(MapleStat.DEX, player.getDex()));
                                break;
                            case 256: // int
                                if (player.getInt() <= 4 || ((player.getJob().getId() / 100) == 2 && player.getInt() <= 20)) {
                                    break;
                                }
                                player.setInt(player.getInt() - 1);
                                statupdate.add(new Pair<MapleStat, Integer>(MapleStat.INT, player.getInt()));
                                break;
                            case 512: // luk
                                if (player.getLuk() <= 4 || ((player.getJob().getId() / 100) == 4 && player.getLuk() <= 35)) {
                                    break;
                                }
                                player.setLuk(player.getLuk() - 1);
                                statupdate.add(new Pair<MapleStat, Integer>(MapleStat.LUK, player.getLuk()));
                                break;
                            case 2048: // HP
                                if (player.getHpApUsed() <= 1 || player.getHpApUsed() == 10000) {
                                    break;
                                }
                                int maxhp = player.getMaxHp();
                                if (player.getJob().isA(MapleJob.BEGINNER)) {
                                    maxhp -= 12;
                                } else if (player.getJob().isA(MapleJob.WARRIOR)) {
                                    ISkill improvingMaxHP = SkillFactory.getSkill(1000001);
                                    int improvingMaxHPLevel = player.getSkillLevel(improvingMaxHP);
                                    maxhp -= 24;
                                    if (improvingMaxHPLevel >= 1) {
                                        maxhp -= improvingMaxHP.getEffect(improvingMaxHPLevel).getY();
                                    }
                                } else if (player.getJob().isA(MapleJob.MAGICIAN)) {
                                    maxhp -= 10;
                                } else if (player.getJob().isA(MapleJob.BOWMAN)) {
                                    maxhp -= 20;
                                } else if (player.getJob().isA(MapleJob.THIEF)) {
                                    maxhp -= 20;
                                } else if (player.getJob().isA(MapleJob.PIRATE)) {
                                    ISkill improvingMaxHP = SkillFactory.getSkill(5100000);
                                    int improvingMaxHPLevel = player.getSkillLevel(improvingMaxHP);
                                    maxhp -= 20;
                                    if (improvingMaxHPLevel >= 1) {
                                        maxhp -= improvingMaxHP.getEffect(improvingMaxHPLevel).getY();
                                    }
                                }
                                if (maxhp < ((player.getLevel() * 2) + 148)) {
                                    break;
                                }
                                player.setHpApUsed(player.getHpApUsed() - 1);
                                player.setHp(maxhp);
                                player.setMaxHp(maxhp);
                                statupdate.add(new Pair<MapleStat, Integer>(MapleStat.HP, player.getMaxHp()));
                                statupdate.add(new Pair<MapleStat, Integer>(MapleStat.MAXHP, player.getMaxHp()));
                                break;
                            case 8192: // MP
                                if (player.getHpApUsed() <= 1 || player.getMpApUsed() == 10000) {
                                    break;
                                }
                                int maxmp = player.getMaxMp();
                                if (player.getJob().isA(MapleJob.BEGINNER)) {
                                    maxmp -= 8;
                                } else if (player.getJob().isA(MapleJob.WARRIOR)) {
                                    maxmp -= 4;
                                } else if (player.getJob().isA(MapleJob.MAGICIAN)) {
                                    ISkill improvingMaxMP = SkillFactory.getSkill(2000001);
                                    int improvingMaxMPLevel = player.getSkillLevel(improvingMaxMP);
                                    maxmp -= 20;
                                    if (improvingMaxMPLevel >= 1) {
                                        maxmp -= improvingMaxMP.getEffect(improvingMaxMPLevel).getY();
                                    }
                                } else if (player.getJob().isA(MapleJob.BOWMAN)) {
                                    maxmp -= 12;
                                } else if (player.getJob().isA(MapleJob.THIEF)) {
                                    maxmp -= 12;
                                } else if (player.getJob().isA(MapleJob.PIRATE)) {
                                    maxmp -= 16;
                                }
                                if (maxmp < ((player.getLevel() * 2) + 148)) {
                                    break;
                                }
                                player.setMpApUsed(player.getMpApUsed() - 1);
                                player.setMp(maxmp);
                                player.setMaxMp(maxmp);
                                statupdate.add(new Pair<MapleStat, Integer>(MapleStat.MP, player.getMaxMp()));
                                statupdate.add(new Pair<MapleStat, Integer>(MapleStat.MAXMP, player.getMaxMp()));
                                break;
                            default:
                                c.getSession().write(MaplePacketCreator.updatePlayerStats(MaplePacketCreator.EMPTY_STATUPDATE, true));
                                break;
                        }
                        switch (APTo) {
                            case 64: // str
                                if (player.getStr() >= 999) {
                                    break;
                                }
                                player.setStr(player.getStr() + 1);
                                statupdate.add(new Pair<MapleStat, Integer>(MapleStat.STR, player.getStr()));
                                break;
                            case 128: // dex
                                if (player.getDex() >= 999) {
                                    break;
                                }
                                player.setDex(player.getDex() + 1);
                                statupdate.add(new Pair<MapleStat, Integer>(MapleStat.DEX, player.getDex()));
                                break;
                            case 256: // int
                                if (player.getInt() >= 999) {
                                    break;
                                }
                                player.setInt(player.getInt() + 1);
                                statupdate.add(new Pair<MapleStat, Integer>(MapleStat.INT, player.getInt()));
                                break;
                            case 512: // luk
                                if (player.getLuk() >= 999) {
                                    break;
                                }
                                player.setLuk(player.getLuk() + 1);
                                statupdate.add(new Pair<MapleStat, Integer>(MapleStat.LUK, player.getLuk()));
                                break;
                            case 2048: // hp
                                int maxhp = player.getMaxHp();
                                if (maxhp >= 30000) {
                                    c.getSession().write(MaplePacketCreator.updatePlayerStats(MaplePacketCreator.EMPTY_STATUPDATE, true));
                                    break;
                                } else {
                                    if (player.getJob().isA(MapleJob.BEGINNER)) {
                                        maxhp += rand(8, 12);
                                    } else if (player.getJob().isA(MapleJob.WARRIOR)) {
                                        ISkill improvingMaxHP = SkillFactory.getSkill(1000001);
                                        int improvingMaxHPLevel = player.getSkillLevel(improvingMaxHP);
                                        maxhp += rand(20, 25);
                                        if (improvingMaxHPLevel >= 1) {
                                            maxhp += improvingMaxHP.getEffect(improvingMaxHPLevel).getY();
                                        }
                                    } else if (player.getJob().isA(MapleJob.MAGICIAN)) {
                                        maxhp += rand(10, 20);
                                    } else if (player.getJob().isA(MapleJob.BOWMAN)) {
                                        maxhp += rand(16, 20);
                                    } else if (player.getJob().isA(MapleJob.THIEF)) {
                                        maxhp += rand(16, 20);
                                    } else if (player.getJob().isA(MapleJob.PIRATE)) {
                                        ISkill improvingMaxHP = SkillFactory.getSkill(5100000);
                                        int improvingMaxHPLevel = player.getSkillLevel(improvingMaxHP);
                                        maxhp += 20;
                                        if (improvingMaxHPLevel >= 1) {
                                            maxhp += improvingMaxHP.getEffect(improvingMaxHPLevel).getY();
                                        }
                                    }
                                    maxhp = Math.min(30000, maxhp);
                                    player.setHpApUsed(player.getHpApUsed() + 1);
                                    player.setMaxHp(maxhp);
                                    statupdate.add(new Pair<MapleStat, Integer>(MapleStat.MAXHP, player.getMaxHp()));
                                    break;
                                }
                            case 8192: // mp
                                int maxmp = player.getMaxMp();
                                if (maxmp >= 30000) {
                                    break;
                                } else {
                                    if (player.getJob().isA(MapleJob.BEGINNER)) {
                                        maxmp += rand(6, 8);
                                    } else if (player.getJob().isA(MapleJob.WARRIOR)) {
                                        maxmp += rand(2, 4);
                                    } else if (player.getJob().isA(MapleJob.MAGICIAN)) {
                                        ISkill improvingMaxMP = SkillFactory.getSkill(2000001);
                                        int improvingMaxMPLevel = player.getSkillLevel(improvingMaxMP);
                                        maxmp += rand(18, 20);
                                        if (improvingMaxMPLevel >= 1) {
                                            maxmp += improvingMaxMP.getEffect(improvingMaxMPLevel).getY();
                                        }
                                    } else if (player.getJob().isA(MapleJob.BOWMAN)) {
                                        maxmp += rand(10, 12);
                                    } else if (player.getJob().isA(MapleJob.THIEF)) {
                                        maxmp += rand(10, 12);
                                    } else if (player.getJob().isA(MapleJob.PIRATE)) {
                                        maxmp += rand(10, 12);
                                    }
                                    maxmp = Math.min(30000, maxmp);
                                    player.setMpApUsed(player.getMpApUsed() + 1);
                                    player.setMaxMp(maxmp);
                                    statupdate.add(new Pair<MapleStat, Integer>(MapleStat.MAXMP, player.getMaxMp()));
                                    break;
                                }
                            default:
                                c.getSession().write(MaplePacketCreator.updatePlayerStats(MaplePacketCreator.EMPTY_STATUPDATE, true));
                                break;
                        }
                        c.getSession().write(MaplePacketCreator.updatePlayerStats(statupdate, true));
                    }
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false);
                    break;

                case 506: // hmm npe when double clicking..
                    int tagType = itemId % 10;
                    IItem eq = null;
                    if (tagType == 0) { // Item tag.
                        int equipSlot = slea.readShort();
                        if (equipSlot == 0) {
                            break;
                        }
                        eq = player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) equipSlot);
                        eq.setOwner(player.getName());
                    } else if (tagType == 1) { // Sealing lock
                        byte type = (byte) slea.readInt();
                        if (type == 2) { // we can't do setLocked() for stars =/
                            break;
                        }
                        byte slot_ = (byte) slea.readInt();
                        eq = player.getInventory(MapleInventoryType.getByType(type)).getItem(slot_);
                        Equip equip = (Equip) eq;
                        equip.setLocked((byte) 1);
                    } else if (tagType == 2) { // Incubator
                    }
                    slea.readInt();
                    c.getSession().write(MaplePacketCreator.updateEquipSlot(eq));
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false);
                    break;
                case 507:
                    if (player.getCanSmega() && !player.getCheatTracker().Spam(5000, 3) && player.getCanTalk() == true && !player.isInJail()) {
                        switch (itemId / 1000 % 10) {
                            case 1: // Megaphone
                                if (player.getLevel() >= 10) {
                                    String txt = slea.readMapleAsciiString();
                                    if (txt.length() > 5) {
                                        player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(2, player.getName() + " : " + txt));
                                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false);
                                    } else {
                                        player.dropMessage("Please do not use megaphones for such short messages");
                                    }
                                } else {
                                    player.dropMessage("You may not use this until you're level 10");
                                }
                                break;
                            case 6: //item megaphone
                                String txt = slea.readMapleAsciiString();
                                String msg = c.getPlayer().getName() + " : " + txt;
                                whisper = slea.readByte() == 1;
                                IItem smegaitem = null;
                                if (slea.readByte() == 1) { //item
                                    smegaitem = c.getPlayer().getInventory(MapleInventoryType.getByType((byte) slea.readInt())).getItem((byte) slea.readInt());
                                    if (smegaitem == null) {
                                        return;
                                    }
                                }
                                if (txt.length() < 81) {
                                    if (txt.length() > 5) {
                                        c.getChannelServer().getWorldInterface().broadcastSMega(null, MaplePacketCreator.itemMegaphone(msg, whisper, c.getChannel(), smegaitem).getBytes());
                                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false);
                                    } else {
                                        player.dropMessage("Please do not use megaphones for such short messages");
                                    }
                                } else {
                                    c.getPlayer().dropMessage(1, "Keep your messages under 80 characters.");
                                }
                                break;
                            case 2: // Super megaphone
                                String text = slea.readMapleAsciiString();
                                if (text.length() > 5) {
                                    c.getChannelServer().getWorldInterface().broadcastSMega(null, MaplePacketCreator.serverNotice(3, c.getChannel(), player.getName() + " : " + text, (slea.readByte() != 0)).getBytes());
                                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false);
                                } else {
                                    player.dropMessage("Please do not use megaphones for such short messages");
                                }
                                break;
                            case 3: // Heart megaphone
                            case 4: // Skull megaphone
                                System.out.println("Unhandled Megaphone Packet : " + slea.toString());
                                System.out.println("Megaphone ID: " + itemId);
                                break;
                            case 5: // Maple TV
                                int tvType = itemId % 10;
                                boolean megassenger = false;
                                boolean ear = false;
                                MapleCharacter victim = null;

                                if (tvType != 1) {
                                    if (tvType >= 3) {
                                        megassenger = true;
                                        if (tvType == 3) {
                                            slea.readByte();
                                        }
                                        ear = 1 == slea.readByte();
                                    } else if (tvType != 2) {
                                        slea.readByte();
                                    }
                                    if (tvType != 4) {
                                        String name = slea.readMapleAsciiString();
                                        if (name.length() > 0) {
                                            int channel = c.getChannelServer().getWorldInterface().find(name);
                                            if (channel == -1) {
                                                player.dropMessage(1, "Player could not be found.");
                                                break;
                                            }
                                            victim = net.channel.ChannelServer.getInstance(channel).getPlayerStorage().getCharacterByName(name);
                                        }
                                    }
                                }
                                List<String> messages = new LinkedList<String>();
                                StringBuilder builder = new StringBuilder();
                                for (int i = 0; i < 5; i++) {
                                    String message = slea.readMapleAsciiString();
                                    if (megassenger) {
                                        builder.append(" ");
                                        builder.append(message); // builder.append(" "+message);
                                    }
                                    messages.add(message);
                                }
                                slea.readInt();
                                if (!MapleTVEffect.active) {
                                    if (megassenger) {
                                        c.getChannelServer().getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(3, c.getChannel(), player.getName() + " : " + builder.toString(), ear).getBytes());
                                    }
                                    new MapleTVEffect(player, victim, messages, tvType);
                                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false);
                                } else {
                                    player.dropMessage(1, "The Maple TV is already in use.");
                                }
                        }
                    } else {
                        cm.dropMessage("You have lost your megaphone privilages.");
                    }
                    break;
                case 539:
                    if (player.getCanSmega() && !player.getCheatTracker().Spam(5000, 3) && player.getCanTalk() == true && !player.isInJail()) {
                        List<String> lines = new LinkedList<String>();
                        for (int i = 0; i < 4; i++) {
                            lines.add(slea.readMapleAsciiString());
                        }
                        String tottxt = "";
                        for (String s : lines) {
                            tottxt += s;
                        }
                        if (tottxt.length() > 5) {
                            c.getChannelServer().getWorldInterface().broadcastSMega(null, MaplePacketCreator.getAvatarMega(c.getPlayer(), c.getChannel(), itemId, lines, (slea.readByte() != 0)).getBytes());
                            MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false);
                        } else {
                            player.dropMessage("Please do not use megaphones for such short messages");
                        }
                    } else {
                        cm.dropMessage("You have lost your megaphone privilages.");
                    }
                    break;

                case 520:
                    if (ii.getMeso(itemId) + player.getMeso() < Integer.MAX_VALUE) {
                        player.gainMeso(ii.getMeso(itemId), true, false, true);
                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false);
                        c.getSession().write(MaplePacketCreator.enableActions()); // do we really need this?
                    } else {
                        player.dropMessage(1, "Cannot hold anymore mesos.");
                    }
                    break;

                case 510:
                    player.getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.musicChange("Jukebox/Congratulation"), true);
                    break;

                case 512:
                    player.getMap().startMapEffect(ii.getMsg(itemId).replaceFirst("%s", player.getName()).replaceFirst("%s", slea.readMapleAsciiString()), itemId);
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false);
                    break;
                case 524:
                    for (int i = 0; i < 3; i++) {
                        if (player.getPet(i) == null) {
                            break;
                        }
                        MaplePet pet = player.getPet(i);
                        if (player.getInventory(MapleInventoryType.CASH).getItem(slot) != null) {
                            int petID = pet.getItemId();
                            if (itemId == 5240012 && petID >= 5000028 && petID <= 5000033
                                    || itemId == 5240021 && petID >= 5000047 && petID <= 5000053
                                    || itemId == 5240004 && (petID == 5000007 || petID == 5000023)
                                    || itemId == 5240006 && (petID == 5000003 || petID == 5000007 || petID >= 5000009 && petID <= 5000010 || petID == 5000012 || petID == 5000044)) {
                                pet.setFullness(100);
                                int closeGain = 100 * c.getChannelServer().getPetExpRate();
                                if (pet.getCloseness() + closeGain > 30000) {
                                    pet.setCloseness(30000);
                                } else {
                                    pet.setCloseness(pet.getCloseness() + closeGain);
                                }
                                while (pet.getCloseness() >= ExpTable.getClosenessNeededForLevel(pet.getLevel() + 1)) {
                                    pet.setLevel(pet.getLevel() + 1);
                                    c.getSession().write(MaplePacketCreator.showOwnPetLevelUp(player.getPetIndex(pet)));
                                    player.getMap().broadcastMessage(MaplePacketCreator.showPetLevelUp(c.getPlayer(), player.getPetIndex(pet)));
                                }
                                c.getSession().write(MaplePacketCreator.updatePet(pet, true));
                                player.getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.commandResponse(player.getId(), (byte) 1, 0, true, true), true);
                                MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false);
                            } else if (pet.canConsume(itemId)) {
                                pet.setFullness(100);
                                int closeGain = 100 * c.getChannelServer().getPetExpRate();
                                if (pet.getCloseness() + closeGain > 30000) {
                                    pet.setCloseness(30000);
                                } else {
                                    pet.setCloseness(pet.getCloseness() + closeGain);
                                }
                                while (pet.getCloseness() >= ExpTable.getClosenessNeededForLevel(pet.getLevel() + 1)) {
                                    pet.setLevel(pet.getLevel() + 1);
                                    c.getSession().write(MaplePacketCreator.showOwnPetLevelUp(player.getPetIndex(pet)));
                                    player.getMap().broadcastMessage(MaplePacketCreator.showPetLevelUp(c.getPlayer(), player.getPetIndex(pet)));
                                }
                                c.getSession().write(MaplePacketCreator.updatePet(pet, true));
                                player.getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.commandResponse(player.getId(), (byte) 1, 0, true, true), true);
                                MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false);
                            }
                            c.getSession().write(MaplePacketCreator.enableActions());
                        } else {
                            break;
                        }
                    }
                    break;

                case 528:
                    if (itemId == 5281000) { // Credits to purplemadness
                        Rectangle bounds = new Rectangle((int) player.getPosition().getX(), (int) player.getPosition().getY(), 1, 1);
                        MapleStatEffect mse = new MapleStatEffect();
                        mse.setSourceId(2111003);
                        MapleMist mist = new MapleMist(bounds, c.getPlayer(), mse);
                        player.getMap().spawnMist(mist, 10000, false, true);
                        player.getMap().broadcastMessage(MaplePacketCreator.getChatText(player.getId(), "Oh no, I farted!", false, 1));
                    }
                    break;

                case 509:
                    String sendTo = slea.readMapleAsciiString();
                    String msg = slea.readMapleAsciiString();
                    int recipientId = MapleCharacter.getIdByName(sendTo, c.getWorld());
                    if (recipientId > -1) {
                        try {
                            player.sendNote(recipientId, msg);
                        } catch (SQLException ex) {
                            Logger.getLogger(UseCashItemHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false);
                    } else {
                        player.dropMessage(5, "This player was not found in the database :)");
                    }
                    break;
                case 517:
                    MaplePet pet = player.getPet(0);
                    if (pet != null) {
                        String newName = slea.readMapleAsciiString();
                        if (newName.length() > 2 && newName.length() < 14) {
                            pet.setName(newName);
                            c.getSession().write(MaplePacketCreator.updatePet(pet, true));
                            c.getSession().write(MaplePacketCreator.enableActions());
                            player.getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.changePetName(c.getPlayer(), newName, 1), true);
                            MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false);
                        } else {
                            cm.dropMessage("Names must be 2 - 14 characters.");
                        }
                    } else {
                        c.getSession().write(MaplePacketCreator.enableActions());
                    }
                    break;

                case 537:
                    if (player.getCanTalk() || !player.inEvent() || !player.getMap().isMuted()) {
                        String text = slea.readMapleAsciiString();
                        slea.readInt();
                        player.setChalkboard(text);
                    } else {
                        cm.dropMessage("You can't use chalkboards if you're muted or in an Event");
                    }
                    break;
                case 530:
                    ii.getItemEffect(itemId).applyTo(player);
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false);
                    break;

                case 504:
                    byte rocktype = slea.readByte();
                    boolean vip = itemId == 5041000;
                    if (rocktype == 0x00) {
                        int mapId = slea.readInt();
                        if (c.getChannelServer().getMapFactory().getMap(mapId).getReturnMapId() == 999999999) {
                            player.changeMap(mapId);
                        }
                    } else {
                        String name = slea.readMapleAsciiString();
                        MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(name);
                        if (victim == null) {
                            int channel = c.getChannelServer().getWorldInterface().find(name);
                            if (channel == -1) {
                                player.dropMessage(1, "This player is not online.");
                                break;
                            }
                            ChangeChannelHandler.changeChannel(channel, player.getClient());
                            victim = c.getChannelServer().getPlayerStorage().getCharacterByName(name);
                        }
                        if (victim == null) { // player dcs / ccs while you cc o____o
                            //  player.dropMessage("System error !");
                        } else {
                            MapleMap map = victim.getMap();
                            switch (map.getId()) {
                                case 980000404:
                                case 109050000:
                                case 180000000:
                                case 0: // lol bored.

                                    break;
                            }
                            if (map.getId() != 980000404 && map.getId() != 980000603 && map.getId() != 180000000 && map.getId() != 0) {
                                if ((victim.isGM() && player.isGM()) || !victim.isGM()) { // I should really handle this before the switch
                                    if ((player.getMap().getMapName().equals(victim.getMap().getMapName()) && !vip) || vip) {
                                        player.changeMap(map, map.findClosestSpawnpoint(victim.getPosition()));
                                    } else {
                                        player.dropMessage(1, "You cannot warp to this player because he's not in the same continent.");
                                    }
                                } else {
                                    player.dropMessage(1, "You cannot use VIP teleport rocks on a GM.");
                                }
                            } else {
                                player.dropMessage(1, "You cannot warp to this map.");
                            }
                        }
                    }
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false);
                    break;
                default:
                    System.out.println("Non-existant cash item was used itemid: " + itemId);
            }
            c.getSession().write(MaplePacketCreator.enableActions());
        } catch (RemoteException e) {
            c.getChannelServer().reconnectWorld();
            System.out.println("REMOTE ERROR" + e);
        }
    }

    private int rand(int lbound, int ubound) {
        return MapleCharacter.rand(lbound, ubound);
    }
}
