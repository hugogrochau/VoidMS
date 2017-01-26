package net.channel.handler;

import java.util.concurrent.ScheduledFuture;
import client.ISkill;
import client.MapleBuffStat;
import client.MapleCharacter;
import client.MapleCharacter.CancelCooldownAction;
import client.MapleClient;
import client.MapleJob;
import client.MapleStat;
import client.SkillFactory;
import net.MaplePacket;
import server.MapleStatEffect;
import server.TimerManager;
import server.maps.FakeCharacter;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class CloseRangeDamageHandler extends AbstractDealDamageHandler {

    private boolean isFinisher(int skillId) {
        return skillId >= 1111003 && skillId <= 1111006;
    }

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        try {
            AttackInfo attack = parseDamage(slea, false);
            MapleCharacter player = c.getPlayer();
            player.resetAfkTime();
            MaplePacket packet = MaplePacketCreator.closeRangeAttack(player.getId(), attack.skill, attack.stance, attack.numAttackedAndDamage, attack.allDamage, attack.speed);
            player.getMap().broadcastMessage(player, packet, false, true);
            int numFinisherOrbs = 0;
            Integer comboBuff = player.getBuffedValue(MapleBuffStat.COMBO);
            if (isFinisher(attack.skill)) {
                if (comboBuff != null) {
                    numFinisherOrbs = comboBuff.intValue() - 1;
                }
                player.handleOrbconsume();
            } else if (attack.numAttacked > 0) {
                // Handle combo orbgain.
                if (comboBuff != null) {
                    if (attack.skill != 1111008) { // Shout should not give orbs.
                        player.handleOrbgain();
                    }
                } else if ((player.getJob().equals(MapleJob.BUCCANEER) || player.getJob().equals(MapleJob.MARAUDER)) && player.getSkillLevel(SkillFactory.getSkill(5110001)) > 0) {
                    for (int i = 0; i < attack.numAttacked; i++) {
                        player.handleEnergyChargeGain();
                    }
                }
            }

            // Handle sacrifice hp loss.
            if (attack.numAttacked > 0 && attack.skill == 1311005) {
                int totDamageToOneMonster = attack.allDamage.get(0).getRight().get(0).intValue(); // sacrifice attacks only 1 mob with 1 attack
                int remainingHP = player.getHp() - totDamageToOneMonster * attack.getAttackEffect(player).getX() / 100;
                if (remainingHP > 1) {
                    player.setHp(remainingHP);
                } else {
                    player.setHp(1);
                }
                player.updateSingleStat(MapleStat.HP, player.getHp());
            }
            // Handle charged blow.
            if (attack.numAttacked > 0 && attack.skill == 1211002) {
                boolean advcharge_prob = false;
                int advcharge_level = player.getSkillLevel(SkillFactory.getSkill(1220010));
                if (advcharge_level > 0) {
                    MapleStatEffect advcharge_effect = SkillFactory.getSkill(1220010).getEffect(advcharge_level);
                    advcharge_prob = advcharge_effect.makeChanceResult();
                } else {
                    advcharge_prob = false;
                }
                if (!advcharge_prob) {
                    player.cancelEffectFromBuffStat(MapleBuffStat.WK_CHARGE);
                }
            }
            int maxdamage = c.getPlayer().getCurrentMaxBaseDamage();
            int attackCount = 1;
            if (attack.skill != 0) {
                MapleStatEffect effect = attack.getAttackEffect(c.getPlayer());
                attackCount = effect.getAttackCount();
                maxdamage *= effect.getDamage() / 100.0;
                maxdamage *= attackCount;
            }
            maxdamage = Math.min(maxdamage, 99999);
            if (attack.skill == 4211006) {
                maxdamage = 700000;
            } else if (numFinisherOrbs > 0) {
                maxdamage *= numFinisherOrbs;
            } else if (comboBuff != null) {
                ISkill combo = SkillFactory.getSkill(1111002);
                int comboLevel = player.getSkillLevel(combo);
                MapleStatEffect comboEffect = combo.getEffect(comboLevel);
                double comboMod = 1.0 + (comboEffect.getDamage() / 100.0 - 1.0) * (comboBuff.intValue() - 1);
                maxdamage *= comboMod;
            }
            if (numFinisherOrbs == 0 && isFinisher(attack.skill)) {
                return; // Can only happen when lagging.
            }
            if (isFinisher(attack.skill)) {
                maxdamage = 99999;
            }
            if (attack.skill > 0) {
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
            }
            applyAttack(attack, player, maxdamage, attackCount);
            if (c.getPlayer().hasFakeChar()) {
                for (FakeCharacter ch : c.getPlayer().getFakeChars()) {
                    player.getMap().broadcastMessage(ch.getFakeChar(), MaplePacketCreator.closeRangeAttack(ch.getFakeChar().getId(), attack.skill, attack.stance, attack.numAttackedAndDamage, attack.allDamage, attack.speed), false, true);
                    applyAttack(attack, ch.getFakeChar(), maxdamage, attackCount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
