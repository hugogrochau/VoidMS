package net.channel.handler;

import client.MapleCharacter;
import client.MapleClient;
import net.AbstractMaplePacketHandler;
import scripting.quest.QuestScriptManager;
import server.quest.MapleQuest;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class QuestActionHandler extends AbstractMaplePacketHandler {

    /** Creates a new instance of QuestActionHandler */
    public QuestActionHandler() {
    }

    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        byte action = slea.readByte();
        short quest = slea.readShort();
        MapleCharacter player = c.getPlayer();
        if (action == 1) { // Start quest.
            int npc = slea.readInt();
            slea.readInt();
            MapleQuest.getInstance(quest).start(player, npc);
        } else if (action == 2) { // Complete quest.
            int npc = slea.readInt();
            slea.readInt();
            if (slea.available() >= 4) {
                int selection = slea.readInt();
                MapleQuest.getInstance(quest).complete(player, npc, selection);
            } else {
                MapleQuest.getInstance(quest).complete(player, npc);
            }
            c.getSession().write(MaplePacketCreator.showOwnBuffEffect(0, 9)); // Quest completion.
            player.getMap().broadcastMessage(player, MaplePacketCreator.showBuffeffect(player.getId(), 0, 9, (byte) 0), false);
            // c.getSession().write(MaplePacketCreator.completeQuest(c.getPlayer(), quest));
            // c.getSession().write(MaplePacketCreator.updateQuestInfo(c.getPlayer(), quest, npc, (byte)14));
            // 6 = start quest
            // 7 = unknown error
            // 8 = equip is full
            // 9 = not enough mesos
            // 11 = due to the equipment currently being worn wtf o.o
            // 12 = you may not posess more than one of this item
        } else if (action == 3) { // Forfeit quest
            MapleQuest.getInstance(quest).forfeit(player);
        } else if (action == 4) { // Scripted start quest
            int npc = slea.readInt();
            slea.readInt();
            QuestScriptManager.getInstance().start(c, npc, quest);
        } else if (action == 5) { // Scripted end quests.
            int npc = slea.readInt();
            slea.readInt();
            QuestScriptManager.getInstance().end(c, npc, quest);
        }
    }
}