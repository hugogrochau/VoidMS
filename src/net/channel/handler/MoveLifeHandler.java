package net.channel.handler;

import java.awt.Point;
import java.util.List;
import java.util.Random;
import client.MapleClient;
import net.MaplePacket;
import server.life.MapleMonster;
import server.life.MobSkill;
import server.life.MobSkillFactory;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.movement.LifeMovementFragment;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.data.input.SeekableLittleEndianAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveLifeHandler extends AbstractMovementPacketHandler {

    private static Logger log = LoggerFactory.getLogger(MoveLifeHandler.class);

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        int objectid = slea.readInt();
        short moveid = slea.readShort();
        MapleMapObject mmo = c.getPlayer().getMap().getMapObject(objectid);
        if (mmo == null || mmo.getType() != MapleMapObjectType.MONSTER) {
            /*if (mmo != null) {
            log.warn("[dc] Player {} is trying to move something which is not a monster. It is a {}.", new Object[] {
            c.getPlayer().getName(), c.getPlayer().getMap().getMapObject(objectid).getClass().getCanonicalName() });
            }*/
            return;
        }
        MapleMonster monster = (MapleMonster) mmo;
        List<LifeMovementFragment> res = null;
        int skillByte = slea.readByte();
        int skill = slea.readByte();
        int skill_1 = slea.readByte() & 0xFF;
        int skill_2 = slea.readByte();
        int skill_3 = slea.readByte();
        @SuppressWarnings("unused")
        int skill_4 = slea.readByte();
        MobSkill toUse = null;
        Random rand = new Random();
        if (skillByte == 1 && monster.getNoSkills() > 0) {
            int random = rand.nextInt(monster.getNoSkills());
            Pair<Integer, Integer> skillToUse = monster.getSkills().get(random);
            toUse = MobSkillFactory.getMobSkill(skillToUse.getLeft(), skillToUse.getRight());
            int percHpLeft = (int) ((monster.getHp() / monster.getMaxHp()) * 100);
            if (toUse.getHP() < percHpLeft || !monster.canUseSkill(toUse)) {
                toUse = null;
            }
        }
        if (skill_1 >= 100 && skill_1 <= 200 && monster.hasSkill(skill_1, skill_2)) {
            MobSkill skillData = MobSkillFactory.getMobSkill(skill_1, skill_2);
            if (skillData != null && monster.canUseSkill(skillData)) {
                skillData.applyEffect(c.getPlayer(), monster, true);
            }
        }
        slea.readByte();
        slea.readInt();
        int start_x = slea.readShort();
        int start_y = slea.readShort();
        Point startPos = new Point(start_x, start_y);
        res = parseMovement(slea);
        if (monster.getController() != c.getPlayer()) {
            if (monster.isAttackedBy(c.getPlayer())) { // Aggro and controller change.
                monster.switchController(c.getPlayer(), true);
            } else {
                return;
            }
        } else {
            if (skill == -1 && monster.isControllerKnowsAboutAggro() && !monster.isMobile() && !monster.isFirstAttack()) {
                monster.setControllerHasAggro(false);
                monster.setControllerKnowsAboutAggro(false);
            }
        }
        boolean aggro = monster.isControllerHasAggro();

        if (toUse != null) {
            c.getSession().write(MaplePacketCreator.moveMonsterResponse(objectid, moveid, monster.getMp(), aggro, toUse.getSkillId(), toUse.getSkillLevel()));
        } else {
            c.getSession().write(MaplePacketCreator.moveMonsterResponse(objectid, moveid, monster.getMp(), aggro));
        }
        if (aggro) {
            monster.setControllerKnowsAboutAggro(true);
        }
        if (res != null) {
            if (slea.available() != 9) {
                log.warn("slea.available != 9 (movement parsing error)");
                return;
            }
            MaplePacket packet = MaplePacketCreator.moveMonster(skillByte, skill, skill_1, skill_2, skill_3, objectid, startPos, res);
            c.getPlayer().getMap().broadcastMessage(c.getPlayer(), packet, monster.getPosition());
            updatePosition(res, monster, -1);
            c.getPlayer().getMap().moveMonster(monster, monster.getPosition());
            c.getPlayer().getCheatTracker().checkMoveMonster(monster.getPosition());
        }
    }
}
