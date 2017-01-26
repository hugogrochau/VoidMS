package net.channel.handler;

import client.MapleCharacter;
import client.MapleClient;
import client.MapleStat;
import client.achievement.Achievement;
import client.anticheat.CheatingOffense;
import net.AbstractMaplePacketHandler;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class GiveFameHandler extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        int who = slea.readInt();
        int mode = slea.readByte();
        int famechange = mode == 0 ? -1 : 1;
        MapleCharacter target = (MapleCharacter) c.getPlayer().getMap().getMapObject(who);
        if (target == c.getPlayer()) {
            c.getPlayer().getCheatTracker().registerOffense(CheatingOffense.FAMING_SELF);
            return;
        } else if (c.getPlayer().getLevel() < 15) {
            c.getPlayer().getCheatTracker().registerOffense(CheatingOffense.FAMING_UNDER_15);
            return;
        }
        switch (c.getPlayer().canGiveFame(target)) {
            case OK:
                if (Math.abs(target.getFame()) < 30000) {
                    target.addFame(famechange);
                    target.updateSingleStat(MapleStat.FAME, target.getFame());
                    target.achieve(Achievement.FFAME);
                }
                c.getPlayer().hasGivenFame(target);
                c.getSession().write(MaplePacketCreator.giveFameResponse(mode, target.getName(), target.getFame()));
                target.getClient().getSession().write(MaplePacketCreator.receiveFame(mode, c.getPlayer().getName()));
                break;
            case NOT_TODAY:
                c.getSession().write(MaplePacketCreator.giveFameErrorResponse(3));
                break;
            case NOT_THIS_MONTH:
                c.getSession().write(MaplePacketCreator.giveFameErrorResponse(4));
                break;
        }
    }
}
