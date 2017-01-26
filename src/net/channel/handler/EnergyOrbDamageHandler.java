package net.channel.handler;

import client.MapleCharacter;
import client.MapleClient;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class EnergyOrbDamageHandler extends AbstractDealDamageHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        player.resetAfkTime();
        if (player.getEnergyBar() == 10000) {
            AttackInfo attack = parseDamage(slea, false);
            applyAttack(attack, player, 999999, 1);
            player.getMap().broadcastMessage(player, MaplePacketCreator.closeRangeAttack(player.getId(), attack.skill, attack.stance, attack.numAttackedAndDamage, attack.allDamage, attack.speed), false, true);
        }
    }
}