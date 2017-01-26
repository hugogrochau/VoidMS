package net.channel.handler;

import java.rmi.RemoteException;
import client.MapleCharacter;
import client.MapleClient;
import client.messages.CommandProcessor;
import net.AbstractMaplePacketHandler;
import net.channel.ChannelServer;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class WhisperHandler extends AbstractMaplePacketHandler {

    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        byte mode = slea.readByte();
        String recipient = slea.readMapleAsciiString();
        int channel;
        try {
            channel = c.getChannelServer().getWorldInterface().find(recipient);
        } catch (RemoteException re) {
            c.getSession().write(MaplePacketCreator.getWhisperReply(recipient, (byte) 0));
            c.getChannelServer().reconnectWorld();
            return;
        }
        if (channel == -1) {
            c.getSession().write(MaplePacketCreator.getWhisperReply(recipient, (byte) 0));
        } else {
            if (mode == 6) { // Whisper
                String text = slea.readMapleAsciiString();

                if (!CommandProcessor.getInstance().processCommand(c, text)) {
                    if (text.charAt(0) != '!') {
                        if (text.charAt(0) != '@') {
                            ChannelServer pserv = ChannelServer.getInstance(channel);
                            MapleCharacter victim = pserv.getPlayerStorage().getCharacterByName(recipient);
                            String words[] = {"story", "rydahms", "aurasea", "janusms", "dupe", "duping", "hack", "ganja", "davnous", "vade", "flight"};
                            boolean said = false;
                            for (String word : words) {
                                if (text.toLowerCase().contains(word) && !text.toLowerCase().contains("bannedstory")) {
                                    if (!said) {
                                        said = true;
                                    } else {
                                        break;
                                    }
                                    for (ChannelServer cservs : ChannelServer.getAllInstances()) {
                                        for (MapleCharacter players : cservs.getPlayerStorage().getAllCharacters()) {
                                            if (players.getGMLevel() > 2) {
                                                players.getClient().getSession().write(MaplePacketCreator.sendYellowTip(c.getPlayer().getName() + " is possibly doing something illegal. Message sent to " + victim.getName() + ":"));
                                                players.getClient().getSession().write(MaplePacketCreator.sendYellowTip(text));
                                            }
                                        }
                                    }
                                }
                            }
                            if (!victim.isIgnored(c.getPlayer().getName())) {
                                victim.getClient().getSession().write(MaplePacketCreator.getWhisper(c.getPlayer().getName(), c.getChannel(), text));
                                c.getSession().write(MaplePacketCreator.getWhisperReply(recipient, (byte) 1));
                            }
                        } else {
                            c.getPlayer().dropMessage("Player command " + text + " does not exist");
                        }
                    } else {
                        if (c.getPlayer().getGMLevel() < 1) {
                            c.getPlayer().dropMessage("You cannot use staff commands like " + text + ". Feel free to apply for a staff position on the forums");
                        } else {
                            c.getPlayer().dropMessage("Staff command " + text + " does not exist");
                        }
                    }
                }
                if (c.getPlayer().hasWatchers()) {
                    c.getPlayer().sendWatcherMessage("[" + c.getPlayer().getName() + " to " + recipient + " Whisper] : " + text);
                }
            } else if (mode == 5) { // Find
                ChannelServer pserv = ChannelServer.getInstance(channel);
                MapleCharacter victim = pserv.getPlayerStorage().getCharacterByName(recipient);
                if (victim != null && (!victim.isHidden() || c.getPlayer().isGM())) {
                    if (victim.getGuild() != null && victim.inHideout()) {
                        c.getPlayer().dropMessage(5, "'" + victim.getName() + "' is at the Guild Hideout");
                    } else {
                        if (victim.inCS()) {
                            c.getSession().write(MaplePacketCreator.getFindReplyWithCSorMTS(victim.getName(), false));
                        } else if (victim.inMTS()) {
                            c.getSession().write(MaplePacketCreator.getFindReplyWithCSorMTS(victim.getName(), true));
                        } else if (c.getChannel() == victim.getClient().getChannel()) {
                            c.getSession().write(MaplePacketCreator.getFindReplyWithMap(victim.getName(), victim.getMapId()));
                        } else {
                            c.getPlayer().dropMessage(5, "'" + victim.getName() + "' is at '" + victim.getMap().getMapName() + "'" + " in channel " + victim.getClient().getChannel());
                        }
                    }
                } else {
                    c.getSession().write(MaplePacketCreator.getWhisperReply(recipient, (byte) 0));
                }
            }
        }
    }
}
