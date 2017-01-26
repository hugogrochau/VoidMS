package net.channel.handler;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleInventory;
import client.MapleInventoryType;
import client.anticheat.CheatingOffense;
import net.AbstractMaplePacketHandler;
import server.MapleItemInformationProvider;
import server.maps.FakeCharacter;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class FaceExpressionHandler extends AbstractMaplePacketHandler {

    //private static Logger log = LoggerFactory.getLogger(FaceExpressionHandler.class);
    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        int emote = slea.readInt();
        if (emote > 7) {
            int emoteid = 5159992 + emote;
            MapleInventoryType type = MapleItemInformationProvider.getInstance().getInventoryType(emoteid);
            MapleInventory iv = c.getPlayer().getInventory(type);
            if (iv.findById(emoteid) == null) {
                //log.info("[h4x] Player {} is using a face expression he does not have: {}", c.getPlayer().getName(), Integer.valueOf(emoteid));
                c.getPlayer().getCheatTracker().registerOffense(CheatingOffense.USING_UNAVAILABLE_ITEM, Integer.toString(emoteid));
                return;
            }
        }
        for (FakeCharacter ch : c.getPlayer().getFakeChars()) {
            c.getPlayer().getMap().broadcastMessage(ch.getFakeChar(), MaplePacketCreator.facialExpression(ch.getFakeChar(), emote), false);
        }
        if (emote == 11 && !c.getPlayer().inEvent() && !c.getPlayer().isInJail() && !c.getPlayer().getMap().isJQ()) {
            List<MapleMapObject> players = c.getPlayer().getMap().getMapObjectsInRange(c.getPlayer().getPosition(), (double) 3000, Arrays.asList(MapleMapObjectType.PLAYER));
            for (MapleMapObject closeplayers : players) {
                MapleCharacter playernear = (MapleCharacter) closeplayers;
                if (playernear != c.getPlayer() && !playernear.isHidden()) {
                    if (c.getPlayer().isFacingLeft() == playernear.isFacingLeft()) {
                        playernear.dropMessage(c.getPlayer().getName() + " has just kissed you on the butt!");
                        c.getPlayer().dropMessage("You kissed " + playernear.getName() + " on the butt.");
                    } else {
                        if (playernear.getGender() == 1 && c.getPlayer().getGender() == 1) {
                            playernear.dropMessage(c.getPlayer().getName() + " has just kissed you sensually. She might just want a lesbian relationship with you!");
                        } else if (playernear.getGender() == 0 && c.getPlayer().getGender() == 0) {
                            playernear.dropMessage(c.getPlayer().getName() + " has just kissed you sensually. He might just want a gay relationship with you!");
                        } else if (c.getPlayer().getGender() == 1) {
                            playernear.dropMessage(c.getPlayer().getName() + " has just kissed you sensually. She might just want something more");
                        } else {
                            playernear.dropMessage(c.getPlayer().getName() + " has just kissed you sensually. He might just want something more");
                        }
                        c.getPlayer().dropMessage("You kissed " + playernear.getName() + ".");
                    }

                }
            }
        }
        c.getPlayer().getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.facialExpression(c.getPlayer(), emote), false);
    }
}
