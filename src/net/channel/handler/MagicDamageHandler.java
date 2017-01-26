package net.channel.handler;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import client.ISkill;
import client.MapleCharacter;
import client.MapleCharacter.CancelCooldownAction;
import client.MapleClient;
import client.SkillFactory;
import net.MaplePacket;
import server.MapleStatEffect;
import server.TimerManager;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.data.input.SeekableLittleEndianAccessor;

public class MagicDamageHandler extends AbstractDealDamageHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        AttackInfo attack = parseDamage(slea, false);
        MapleCharacter player = c.getPlayer();
        MaplePacket packet = MaplePacketCreator.magicAttack(player.getId(), attack.skill, attack.stance, attack.numAttackedAndDamage, attack.allDamage, -1, attack.speed);
        if (attack.skill == 2121001 || attack.skill == 2221001 || attack.skill == 2321001) {
            packet = MaplePacketCreator.magicAttack(player.getId(), attack.skill, attack.stance, attack.numAttackedAndDamage, attack.allDamage, attack.charge, attack.speed);
        }
        player.getMap().broadcastMessage(player, packet, false, true);
        MapleStatEffect effect = attack.getAttackEffect(c.getPlayer());
        int maxdamage;
        // TODO fix magic damage calculation
        maxdamage = 99999;
        ISkill skill = SkillFactory.getSkill(attack.skill);
        int skillLevel = c.getPlayer().getSkillLevel(skill);
        MapleStatEffect effect_ = skill.getEffect(skillLevel);
        if (effect_.getCooldown() > 0 && skill.getId() == 1221011) {
            if (player.skillisCooling(attack.skill)) {
                //player.getCheatTracker().registerOffense(CheatingOffense.COOLDOWN_HACK);
                return;
            } else {
                c.getSession().write(MaplePacketCreator.skillCooldown(attack.skill, effect_.getCooldown()));
                ScheduledFuture<?> timer = TimerManager.getInstance().schedule(new CancelCooldownAction(c.getPlayer(), attack.skill), effect_.getCooldown() * 1000);
                player.addCooldown(attack.skill, System.currentTimeMillis(), effect_.getCooldown() * 1000, timer);
            }
        }
        applyAttack(attack, player, maxdamage, effect.getAttackCount());
        // MP Eater
        for (int i = 1; i <= 3; i++) {
            ISkill eaterSkill = SkillFactory.getSkill(2000000 + i * 100000);
            int eaterLevel = player.getSkillLevel(eaterSkill);
            if (eaterLevel > 0) {
                for (Pair<Integer, List<Integer>> singleDamage : attack.allDamage) {
                    eaterSkill.getEffect(eaterLevel).applyPassive(player, player.getMap().getMapObject(singleDamage.getLeft()), 0);
                }
                break;
            }
        }
    }
}
