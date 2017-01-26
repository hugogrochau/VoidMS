package net.channel.handler;

import java.awt.Point;
import java.util.Collection;
import java.util.List;
import client.MapleCharacter;
import client.MapleClient;
import server.maps.MapleSummon;
import server.movement.LifeMovementFragment;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;
import tools.data.input.StreamUtil;

public class MoveSummonHandler extends AbstractMovementPacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        int oid = slea.readInt();
        Point startPos = StreamUtil.readShortPoint(slea);
        List<LifeMovementFragment> res = parseMovement(slea);
        MapleCharacter player = c.getPlayer();
        Collection<MapleSummon> summons = player.getSummons().values();
        MapleSummon summon = null;
        for (MapleSummon sum : summons) {
            if (sum.getObjectId() == oid) {
                summon = sum;
            }
        }
        if (summon != null) {
            updatePosition(res, summon, 0);
            // player = ((MapleCharacter) c.getPlayer().getMap().getMapObject(30000));
            player.getMap().broadcastMessage(player, MaplePacketCreator.moveSummon(player.getId(), oid, startPos, res), summon.getPosition());
        } else {
            player.getSummons().remove(summon);
        }
    }
}