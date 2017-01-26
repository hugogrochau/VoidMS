package scripting.npc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.script.Invocable;
import client.MapleClient;
import client.MapleCharacter;
import scripting.AbstractScriptManager;
import server.TimerManager;
import tools.Pair;

public class NPCScriptManager extends AbstractScriptManager {

    private Map<MapleClient, NPCConversationManager> cms = new HashMap<MapleClient, NPCConversationManager>();
    private Map<MapleClient, NPCScript> scripts = new HashMap<MapleClient, NPCScript>();
    private static NPCScriptManager instance = new NPCScriptManager();
    private Map<Pair<Integer, Integer>, Integer> npcTalk = new HashMap<Pair<Integer, Integer>, Integer>();

    public synchronized static NPCScriptManager getInstance() {
        return instance;
    }

    public void start(MapleClient c, int npc) {
        start(c, npc, null, null);
    }

    public void start(final MapleClient c, int npc, String filename, MapleCharacter chr) {
        int time;
        if (c.getPlayer().canNpc()) {
            try {
                NPCConversationManager cm = new NPCConversationManager(c, npc, chr, filename);
                if (cms.containsKey(c)) {
                    dispose(c);
                    return;
                }
                cms.put(c, cm);
                Invocable iv = getInvocable("npc/" + npc + ".js", c);
                if (filename != null) {
                    iv = getInvocable("npc/" + filename + ".js", c);
                }
                if (iv == null || NPCScriptManager.getInstance() == null) {
                    if (iv == null) {
                        if (c.getPlayer().getGMLevel() > 2) {
                            cm.sendOk("#eHi I'm an uncoded npc.\r\nMy ID is #r" + npc + "#k.");
                        }
                    }
                    cm.dispose();
                    return;
                }
                addNpcTalkTimes(c.getPlayer().getId(), npc);
                engine.put("cm", cm);
                NPCScript ns = iv.getInterface(NPCScript.class);
                scripts.put(c, ns);
                ns.start();
                c.getPlayer().setNpc(false);
                if (npc != 1002002 && npc != 22000 && npc != 2131007 && npc != 2131002 && npc != 2132002) {
                    time = 750;
                } else {
                    time = 250;
                }
                TimerManager.getInstance().schedule(new Runnable() {

                    public void run() {
                        c.getPlayer().setNpc(true);
                    }
                }, time);
            } catch (Exception e) {
                log.error("Error executing NPC script. NPCID = " + npc, e);
                dispose(c);
                cms.remove(c);
            }
        } else {
            c.getPlayer().dropMessage(1, "Woah, calm down there cowboy. Slow down on the clicking please.");
            dispose(c);
        }
    }

    public void action(MapleClient c, byte mode, byte type, int selection) {
        NPCScript ns = scripts.get(c);
        if (ns != null) {
            try {
                ns.action(mode, type, selection);
            } catch (Exception e) {
                c.getPlayer().dropMessage("Something went wrong. Please tell a staff member what you tried to do. Use @staffonline");
                log.error("Error executing NPC script. NPC: " + c.getCM().getNpc(), e);
                dispose(c);
            }
        }
    }

    public void dispose(NPCConversationManager cm) {
        cms.remove(cm.getC());
        scripts.remove(cm.getC());
        if (cm.getFileName() != null) {
            resetContext("npc/" + cm.getFileName() + ".js", cm.getC());
        } else {
            resetContext("npc/" + cm.getNpc() + ".js", cm.getC());
        }
    }

    public void dispose(MapleClient c) {
        NPCConversationManager npccm = cms.get(c);
        if (npccm != null) {
            dispose(npccm);
        }
    }

    public NPCConversationManager getCM(MapleClient c) {
        return cms.get(c);
    }

    public int getNpcTalkTimes(int chrid, int npc) {
        Pair<Integer, Integer> pplayer = new Pair<Integer, Integer>(chrid, npc); // first time <3 looks wrong.
        if (!npcTalk.containsKey(pplayer)) {
            npcTalk.put(pplayer, 0);
        }
        return npcTalk.get(pplayer);
    }

    public void addNpcTalkTimes(int chrid, int npc) {
        Pair<Integer, Integer> pplayer = new Pair<Integer, Integer>(chrid, npc);
        if (!npcTalk.containsKey(pplayer)) {
            npcTalk.put(pplayer, 0);
        }
        int talk = 1 + npcTalk.get(pplayer);
        npcTalk.remove(pplayer);
        npcTalk.put(pplayer, talk);
    }

    public void setNpcTalkTimes(int chrid, int npc, int amount) {
        Pair<Integer, Integer> pplayer = new Pair<Integer, Integer>(chrid, npc);
        if (!npcTalk.containsKey(pplayer)) {
            npcTalk.put(pplayer, 0);
        }
        npcTalk.remove(pplayer);
        npcTalk.put(pplayer, amount);
    }

    public List<Integer> listTalkedNpcsByID(int chrid) {
        List<Integer> npcs = new ArrayList<Integer>();
        for (Pair<Integer, Integer> rawr : npcTalk.keySet()) {
            if (rawr.getLeft().equals(chrid)) {
                npcs.add(rawr.getRight());
            }
        }
        return npcs;
    }

    public List<Integer> listAllTalkedNpcs() {
        List<Integer> npcs = new ArrayList<Integer>();
        for (Pair<Integer, Integer> rawr : npcTalk.keySet()) {
            npcs.add(rawr.getRight());
        }
        return npcs;
    }

    public int talkedTimesByNpc(int npc) {
        int i = 0;
        for (Pair<Integer, Integer> rawr : npcTalk.keySet()) {
            if (rawr.getRight().equals(npc)) {
                i += npcTalk.get(rawr);
            }
        }
        return i;
    }
}
