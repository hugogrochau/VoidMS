package net.channel.handler;

import java.rmi.RemoteException;
import client.MapleCharacter;
import client.MapleClient;
import client.messages.CommandProcessor;
import net.AbstractMaplePacketHandler;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class PartychatHandler extends AbstractMaplePacketHandler {
    // private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PartychatHandler.class);

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        int type = slea.readByte(); // 0 for buddys, 1 for parties
        int numRecipients = slea.readByte();
        int recipients[] = new int[numRecipients];
        for (int i = 0; i < numRecipients; i++) {
            recipients[i] = slea.readInt();
        }
        String chattext = slea.readMapleAsciiString();
        if (!CommandProcessor.getInstance().processCommand(c, chattext)) {
            MapleCharacter player = c.getPlayer();
            if (chattext.charAt(0) != '!') {
                if (chattext.charAt(0) != '@') {
                    if (c.getPlayer().getCanTalk()) {
                        try {
                            if (type == 0) {
                                c.getChannelServer().getWorldInterface().buddyChat(recipients, player.getId(), player.getName(), chattext);
                            } else if (type == 1 && player.getParty() != null) {
                                c.getChannelServer().getWorldInterface().partyChat(player.getParty().getId(), chattext, player.getName());
                            } else if (type == 2 && player.getGuildId() > 0) {
                                c.getChannelServer().getWorldInterface().guildChat(c.getPlayer().getGuildId(), c.getPlayer().getName(), c.getPlayer().getId(), chattext);
                            } else if (type == 3 && player.getGuild() != null) {
                                int allianceId = player.getGuild().getAllianceId();
                                if (allianceId > 0) {
                                    c.getChannelServer().getWorldInterface().allianceMessage(allianceId, MaplePacketCreator.multiChat(player.getName(), chattext, 3), player.getId(), -1);
                                }
                            }
                        } catch (RemoteException e) {
                            c.getChannelServer().reconnectWorld();
                        }
                    } else {
                        c.getPlayer().dropMessage("You cannot talk since you are muted.");
                    }
                } else {
                    c.getPlayer().dropMessage("Player command " + chattext + " does not exist");
                }
            } else {
                if (c.getPlayer().getGMLevel() < 1) {
                    c.getPlayer().dropMessage("You cannot use staff commands like " + chattext + ". Feel free to apply for a staff position on the forums");
                } else {
                    c.getPlayer().dropMessage("Staff command " + chattext + " does not exist");

                }
            }
            if (c.getPlayer().hasWatchers()) {
                switch (type) {
                    case 0:
                        c.getPlayer().sendWatcherMessage("[" + c.getPlayer().getName() + " Buddy] : " + chattext);
                        break;
                    case 1:
                        c.getPlayer().sendWatcherMessage("[" + c.getPlayer().getName() + " Party] : " + chattext);
                        break;
                    case 2:
                        c.getPlayer().sendWatcherMessage("[" + c.getPlayer().getName() + " Guild] : " + chattext);
                        break;
                    case 3:
                        c.getPlayer().sendWatcherMessage("[" + c.getPlayer().getName() + " Alliance] : " + chattext);
                        break;
                }
            }
        }
    }
}
