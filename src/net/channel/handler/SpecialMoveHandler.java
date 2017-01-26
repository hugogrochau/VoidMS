package net.channel.handler;

import java.awt.Point;
import client.ISkill;
import client.MapleClient;
import client.SkillFactory;
import client.messages.ServernoticeMapleClientMessageCallback;
import net.AbstractMaplePacketHandler;
import server.AutobanManager;
import server.MapleInventoryManipulator;
import server.MapleStatEffect;
import server.life.MapleMonster;
import server.maps.FakeCharacter;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class SpecialMoveHandler extends AbstractMaplePacketHandler {

    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SpecialMoveHandler.class);

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        slea.readShort();
        slea.readShort();
        int skillid = slea.readInt();
        Point pos = null;
        int __skillLevel = slea.readByte();
        ISkill skill = SkillFactory.getSkill(skillid);
        int skillLevel = c.getPlayer().getSkillLevel(skill);
        MapleStatEffect effect = skill.getEffect(skillLevel);

        if (skill.isGMSkill() && c.getPlayer().getGMLevel() == 0 && c.getPlayer().getDonorLevel() == 0) {
            AutobanManager.getInstance().autoban(c.getPlayer().getClient(), c.getPlayer().getName() + " used a GM skill");
            return;
        }

        if (c.getPlayer().getMapId() == 260000201) {
            switch (skillid) {
                case 4111006:
                case 9001002:
                case 2301001:
                    c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.sendYellowTip("[BOMBERMAN] BOOM! " + c.getPlayer().getName() + " has just self-destructed!"));
                    MapleInventoryManipulator.removeAllById(c, 2100067, false);
                    c.getPlayer().changeMap(100000000);
                    return;
            }
        }
        /* if (effect.getCooldown() > 0) {
        if (c.getPlayer().skillisCooling(skillid)) {
        c.getSession().write(MaplePacketCreator.enableActions());
        //c.getPlayer().getCheatTracker().registerOffense(CheatingOffense.COOLDOWN_HACK);
        return;
        } else {
        c.getSession().write(MaplePacketCreator.skillCooldown(skillid, effect.getCooldown()));
        ScheduledFuture<?> timer = TimerManager.getInstance().schedule(new CancelCooldownAction(c.getPlayer(), skillid), effect.getCooldown() * 1000);
        c.getPlayer().addCooldown(skillid, System.currentTimeMillis(), effect.getCooldown() * 1000, timer);
        }
        }*/
        //monster magnet
        try {
            switch (skillid) {
                case 1121001:
                case 1221001:
                case 1321001:
                    int num = slea.readInt();
                    int mobId;
                    byte success;
                    for (int i = 0; i < num; i++) {
                        mobId = slea.readInt();
                        success = slea.readByte();
                        c.getPlayer().getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.showMagnet(mobId, success), false);
                        MapleMonster monster = c.getPlayer().getMap().getMonsterByOid(mobId);
                        if (monster != null) {
                            monster.switchController(c.getPlayer(), monster.isControllerHasAggro());
                        }
                    }
                    byte direction = slea.readByte();
                    c.getPlayer().getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.showBuffeffect(c.getPlayer().getId(), skillid, 1, direction), false);
                    for (FakeCharacter ch : c.getPlayer().getFakeChars()) {
                        ch.getFakeChar().getMap().broadcastMessage(ch.getFakeChar(), MaplePacketCreator.showBuffeffect(ch.getFakeChar().getId(), skillid, 1, direction), false);
                    }
                    c.getSession().write(MaplePacketCreator.enableActions());
                    break;
            }
        } catch (Exception e) {
            log.warn("Failed to handle monster magnet.", e);
        }
        if (slea.available() == 5) {
            pos = new Point(slea.readShort(), slea.readShort());
        }
        if (skillLevel == 0 || skillLevel != __skillLevel) {
            log.warn(c.getPlayer().getName() + " is using a move skill he doesn't have. ID: " + skill.getId());
            c.disconnect();
            return;
        } else {
            if (c.getPlayer().isAlive()) {
                if (skill.getId() != 2311002 || c.getPlayer().canDoor()) {
                    skill.getEffect(skillLevel).applyTo(c.getPlayer(), pos);
                } else {
                    new ServernoticeMapleClientMessageCallback(5, c).dropMessage("Please wait 5 seconds before casting Mystic Door again.");
                    c.getSession().write(MaplePacketCreator.enableActions());
                }
            } else {
                c.getSession().write(MaplePacketCreator.enableActions());
            }
        }
    }
}
