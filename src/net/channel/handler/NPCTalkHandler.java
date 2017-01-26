package net.channel.handler;

import client.MapleClient;
import net.AbstractMaplePacketHandler;
import scripting.npc.NPCScriptManager;
import server.life.MapleNPC;
import server.maps.MapleMapObject;
import server.maps.PlayerNPCs;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class NPCTalkHandler extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        int oid = slea.readInt();
        slea.readInt();
        MapleMapObject obj = c.getPlayer().getMap().getMapObject(oid);
        if (obj instanceof MapleNPC) {
            MapleNPC npc = (MapleNPC) obj;
            if (NPCScriptManager.getInstance() != null) {
                NPCScriptManager.getInstance().dispose(c);
            }
            if (!c.getPlayer().getCheatTracker().Spam(1000, 4)) {
                /*
                 * if (npc.getId() == 9010009) { if (c.isGuest()) {
                 * c.getPlayer().dropMessage(1, "Duey is not available to
                 * Guests");
                 * c.getSession().write(MaplePacketCreator.enableActions());
                 * return; }
                 * c.getSession().write(MaplePacketCreator.sendDuey((byte) 8,
                 * DueyHandler.loadItems(c.getPlayer()))); } else fuck duey
                 * until I can fix these damn exploits
                 */ if (npc.hasShop()) {
                    //destroy the old shop if one exists...
                    if (c.getPlayer().getShop() != null) {
                        c.getPlayer().setShop(null);
                        c.getSession().write(MaplePacketCreator.confirmShopTransaction((byte) 20));
                    }
                    npc.sendShop(c);
                } else {
                    if (c.getCM() != null || c.getQM() != null) {
                        c.getSession().write(MaplePacketCreator.enableActions());
                        return;
                    }
                    NPCScriptManager.getInstance().start(c, npc.getId());
                    // NPCMoreTalkHandler.npc = npc.getId();
                    // 0 = next button
                    // 1 = yes no
                    // 2 = accept decline
                    // 5 = select a link
                    // c.getSession().write(MaplePacketCreator.getNPCTalk(npc.getId(), (byte) 0,
                    // "Yo! I'm #p" + npc.getId() + "#, lulz! I can warp you lululululu.", "00 01"));
                }
            }
        } else if (obj instanceof PlayerNPCs) {
            PlayerNPCs npc = (PlayerNPCs) obj;
            NPCScriptManager.getInstance().start(c, npc.getId(), npc.getName(), null);
        }
    }
}