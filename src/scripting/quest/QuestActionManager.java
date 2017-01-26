package scripting.quest;

import client.MapleClient;
import client.MapleQuestStatus.Status;
import scripting.npc.NPCConversationManager;
import server.quest.MapleQuest;

public class QuestActionManager extends NPCConversationManager {

    private boolean start;
    private int quest;

    public QuestActionManager(MapleClient c, int npc, int quest, boolean start) {
        super(c, npc, null, null);
        this.quest = quest;
        this.start = start;
    }

    public int getQuest() {
        return quest;
    }

    public boolean isStart() {
        return start;
    }

    @Override
    public void dispose() {
        QuestScriptManager.getInstance().dispose(this, getClient());
    }

    public void forceStartQuest() {
        if (getQuestStatus(quest) != Status.COMPLETED) {
            forceStartQuest(quest);
        } else {
            dispose();
        }
    }

    public void forceStartQuest(int id) {
        if (getQuestStatus(id) != Status.COMPLETED) {
            MapleQuest.getInstance(id).forceStart(getPlayer(), getNpc());
        } else {
            dispose();
        }
    }

    public void forceCompleteQuest() {
            forceCompleteQuest(quest);
    }

    public void forceCompleteQuest(int id) {
            MapleQuest.getInstance(id).forceComplete(getPlayer(), getNpc());
    }
}