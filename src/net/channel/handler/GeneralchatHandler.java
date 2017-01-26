package net.channel.handler;

import java.util.HashMap;
import java.util.Map;
import client.MapleCharacter;
import client.MapleClient;
import client.messages.CommandProcessor;
import net.AbstractMaplePacketHandler;
import net.channel.ChannelServer;

import server.PublicChatHandler;
import tools.MaplePacketCreator;
import tools.StringUtil;
import tools.data.input.SeekableLittleEndianAccessor;

public class GeneralchatHandler extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        String text = slea.readMapleAsciiString();
        int show = slea.readByte();
        if (!CommandProcessor.getInstance().processCommand(c, text) && !PublicChatHandler.doChat(c, text)) {
            if (text.charAt(0) != '!') {
                if (text.charAt(0) != '@') {
                    if (c.getPlayer().inStaffChat()) {
                        Map<Integer, String> ranks = new HashMap<Integer, String>();
                        ranks.put(2, "INTERN");
                        ranks.put(3, "GM");
                        ranks.put(4, "SGM");
                        ranks.put(5, "ADMIN");
                        for (ChannelServer cservs : ChannelServer.getAllInstances()) {
                            for (MapleCharacter players : cservs.getPlayerStorage().getAllCharacters()) {
                                if (players.getGMLevel() > 1) {
                                    players.getClient().getSession().write(MaplePacketCreator.sendYellowTip("[" + ranks.get(c.getPlayer().getGMLevel()) + " " + c.getPlayer().getName() + "] " + text));
                                }
                            }
                        }
                        return;
                    }
                    if (c.getPlayer().getMap().isMuted() && !c.getPlayer().isGM() && !c.getPlayer().isExempted()) {
                        c.getPlayer().dropMessage("This map is muted by a GM, you cannot talk here.");
                        return;
                    }
                    if (c.getPlayer().getCanTalk()) {
                        if ((StringUtil.countCharacters(text, '@') > 4 || StringUtil.countCharacters(text, '#') > 4 || StringUtil.countCharacters(text, '&') > 4 || StringUtil.countCharacters(text, '%') > 4 || StringUtil.countCharacters(text, '$') > 4 || StringUtil.countCharacters(text, '!') > 4) && c.getPlayer().getGMLevel() < 3) {
                            text = "I tried to spam, I'm a bad " + (c.getPlayer().getGender() == 1 ? "girl" : "boy") + ".";
                        }
                        if (!c.getPlayer().isHidden()) {
                            switch (c.getPlayer().getGMText()) {
                                case 0:
                                    // Regular chat
                                    c.getPlayer().getMap().broadcastLimitedMessage(c.getPlayer(), MaplePacketCreator.getChatText(c.getPlayer().getId(), text, false, show));
                                    break;
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                    //MultiChat
                                    c.getPlayer().getMap().broadcastLimitedMessage(c.getPlayer(), MaplePacketCreator.multiChat(c.getPlayer().getName(), text, c.getPlayer().getGMText() - 1));
                                    break;
                                case 5:
                                case 6:
                                    //Server Notice
                                    c.getPlayer().getMap().broadcastLimitedMessage(c.getPlayer(), MaplePacketCreator.serverNotice(c.getPlayer().getGMText(), c.getPlayer().getName() + " : " + text));
                                    break;
                                case 7:
                                    //White Chat
                                    c.getPlayer().getMap().broadcastLimitedMessage(c.getPlayer(), MaplePacketCreator.getChatText(c.getPlayer().getId(), text, true, show));
                                    break;
                                case 8:
                                    //Whisper
                                    c.getPlayer().getMap().broadcastLimitedMessage(c.getPlayer(), MaplePacketCreator.getWhisper(c.getPlayer().getName(), c.getChannel(), text));
                                    break;
                                case 9:
                                    //MapleTip
                                    c.getPlayer().getMap().broadcastLimitedMessage(c.getPlayer(), MaplePacketCreator.sendYellowTip(c.getPlayer().getName() + " : " + text));
                                    break;
                            }
                            c.getPlayer().getMap().broadcastLimitedMessage(c.getPlayer(), MaplePacketCreator.getChatText(c.getPlayer().getId(), text, false, 1));

                        } else {
                            if (c.getPlayer().getHideChat()) {
                                c.getPlayer().getMap().broadcastLimitedMessage(c.getPlayer(), MaplePacketCreator.serverNotice(5, c.getPlayer().getName() + " : " + text));
                            } else {
                                c.getPlayer().getMap().broadcastGMMessage(c.getPlayer(), MaplePacketCreator.getChatText(c.getPlayer().getId(), text, true, show), true);
                            }
                        }
                    } else {
                        c.getPlayer().dropMessage("You cannot talk since you are muted.");
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
            if (c.getPlayer().hasWatchers()) {
                c.getPlayer().sendWatcherMessage("[" + c.getPlayer().getName() + " All] : " + text);
            }
        }
    }
}
