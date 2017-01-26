package net.channel.handler;

import java.util.Arrays;
import java.util.List;
import client.MapleCharacter;
import client.MapleClient;
import client.anticheat.CheatingOffense;
import net.MaplePacket;
import server.maps.FakeCharacter;
import server.movement.AbsoluteLifeMovement;
import server.TimerManager;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.movement.LifeMovementFragment;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class MovePlayerHandler extends AbstractMovementPacketHandler {

    //private static Logger log = LoggerFactory.getLogger(MovePlayerHandler.class);
    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        slea.readByte();
        slea.readInt();
        // log.trace("Movement command received: unk1 {} unk2 {}", new Object[] { unk1, unk2 });
        final List<LifeMovementFragment> res = parseMovement(slea);
        if (res != null) {
            if (slea.available() != 18) {
                return;
            }
            MapleCharacter player = c.getPlayer();
            MaplePacket packet = MaplePacketCreator.movePlayer(player.getId(), res);
            if (!player.isHidden()) {
                c.getPlayer().getMap().broadcastMessage(player, packet, false);
            } else {
                c.getPlayer().getMap().broadcastGMMessage(player, packet, false);
            }
            // c.getSession().write(MaplePacketCreator.movePlayer(30000, res));
            if (CheatingOffense.FAST_MOVE.isEnabled() || CheatingOffense.HIGH_JUMP.isEnabled()) {
                checkMovementSpeed(c.getPlayer(), res);
            }
            updatePosition(res, c.getPlayer(), 0);
            c.getPlayer().getMap().movePlayer(c.getPlayer(), c.getPlayer().getPosition());
            //   if (player.getHp() < 1) {
            //  c.getPlayer().getCheatTracker().registerOffense(CheatingOffense.ANTI_DEATH);
            //    }
            if (player.isChaser()) {
                if (player.getMapId() == 109010100) {
                    List<MapleMapObject> players = player.getMap().getMapObjectsInRange(player.getPosition(), (double) 5000, Arrays.asList(MapleMapObjectType.PLAYER));
                    for (MapleMapObject closeplayers : players) {
                        MapleCharacter playernear = (MapleCharacter) closeplayers;
                        if (!playernear.isChaser() && playernear != c.getPlayer()) {
                            player.setChaser(playernear);
                        }
                    }
                }
            }
            /*  if (player.getMapId() == 1010000) {
            if (player.getPosition().x < 152 && player.getPosition().y < 274 && player.getPosition().y > 90 && player.isBlueTeam() && player.hasFlag()) {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            player.changeMap(1010000, 5);
            for (MapleCharacter playerparty : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
            if (playerparty.getPartyId() == player.getPartyId()) {
            playerparty.gainBlueTeamWin();
            MapleInventoryManipulator.removeAllById(playerparty.getClient(), 4001025, true);
            playerparty.setHasFlag(false);
            }
            }
            player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(5, player.getName() + " has scored for the Blue Team! They now have " + player.getBlueTeamWins() + " points!"));
            player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(5, "The flag has gone back to the middle!"));
            player.getMap().spawnItemDrop(player, player, ii.getEquipById(4001025), player.getMap().getPortal(4).getPosition(), true, true);
            if (player.getBlueTeamWins() >= 3) {
            player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(5, "Congratulations Blue Team! You have won!"));
            player.getMap().clearMapTimer();
            for (MapleCharacter mch : player.getMap().getCharacters()) {
            mch.changeMap(100000000, 0);
            mch.resetFlag();
            if (mch.isBlueTeam()) {
            for (MaplePartyCharacter mpch : player.getParty().getMembers()) {
            // mpch.addMarriageQuestLevel(player);
            }
            } else if (mch.isRedTeam()) {
            for (MaplePartyCharacter mpch : player.getParty().getMembers()) {
            // mpch.subtractMarriageQuestLevel(player);
            }
            }
            }
            for (MapleCharacter all : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
            all.resetFlag();
            }
            }
            }
            if (player.getPosition().x > 2790 && player.getPosition().y < 274 && player.getPosition().y > 90 && player.isRedTeam() && player.hasFlag()) {// player.haveItem(4001025, 1, false, true)) { // player.hasflag == true) {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            player.changeMap(1010000, 0);
            for (MapleCharacter playerparty : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
            if (playerparty.getPartyId() == player.getPartyId()) {
            playerparty.gainRedTeamWin();
            MapleInventoryManipulator.removeAllById(playerparty.getClient(), 4001025, true);
            playerparty.setHasFlag(false);
            }
            }
            player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(5, player.getName() + " has scored for he Red Team! They now have " + player.getRedTeamWins() + " points!"));
            player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(5, "The flag has gone back to the middle!"));
            player.getMap().spawnItemDrop(player, player, ii.getEquipById(4001025), player.getMap().getPortal(4).getPosition(), true, true);
            if (player.getRedTeamWins() >= 3) {
            player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(5, "Congratulations Red Team! You have won!"));
            player.getMap().clearMapTimer();
            for (MapleCharacter mch : player.getMap().getCharacters()) {
            mch.resetFlag();
            mch.changeMap(100000000, 0);
            if (mch.isRedTeam()) {
            for (MaplePartyCharacter mpch : player.getParty().getMembers()) {
            // mpch.addMarriageQuestLevel(player);
            }
            } else if (mch.isBlueTeam()) {
            for (MaplePartyCharacter mpch : player.getParty().getMembers()) {
            //mpch.subtractMarriageQuestLevel(player);
            }
            }
            }
            for (MapleCharacter all : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
            all.resetFlag();
            }
            }
            }
            if (player.isJustEntered()) {
            for (MapleCharacter chr : c.getChannelServer().getMapFactory().getMap(1010000).getCharacters()) {
            chr.setHasFlag(false);
            chr.setJustEntered(false);
            chr.getClient().getSession().write(MaplePacketCreator.getClock(player.getMap().getMapTimedTimer()));
            chr.dropMessage("Get the flag from the middle and try to get it to the opposite side! If you get hit you will lose the flag.");
            chr.dropMessage("Red team is on the left and Blue Team on the right.");
            if (chr.haveItem(4001025, 1, true, true)) {
            MapleInventoryManipulator.removeAllById(chr.getClient(), 4001025, true);
            }
            }
            }
            }*/
            if (c.getPlayer().hasFakeChar()) {
                int i = 1;
                for (final FakeCharacter ch : c.getPlayer().getFakeChars()) {
                    if (ch.follow() && ch.getFakeChar().getMap() == player.getMap()) {
                        TimerManager.getInstance().schedule(new Runnable() {

                            @Override
                            public void run() {
                                ch.getFakeChar().getMap().broadcastMessage(ch.getFakeChar(), MaplePacketCreator.movePlayer(ch.getFakeChar().getId(), res), false);
                                updatePosition(res, ch.getFakeChar(), 0);
                                ch.getFakeChar().getMap().movePlayer(ch.getFakeChar(), ch.getFakeChar().getPosition());
                            }
                        }, i * 300);
                        i++;
                    }
                }
            }
        }
    }

    private static void checkMovementSpeed(MapleCharacter chr, List<LifeMovementFragment> moves) {
        // boolean wasALM = true;
        // Point oldPosition = new Point (c.getPlayer().getPosition());
        double playerSpeedMod = chr.getSpeedMod() + 0.005;
        // double playerJumpMod = c.getPlayer().getJumpMod() + 0.005;
        boolean encounteredUnk0 = false;
        for (LifeMovementFragment lmf : moves) {
            if (lmf.getClass() == AbsoluteLifeMovement.class) {
                final AbsoluteLifeMovement alm = (AbsoluteLifeMovement) lmf;
                double speedMod = Math.abs(alm.getPixelsPerSecond().x) / 125.0;
                // int distancePerSec = Math.abs(alm.getPixelsPerSecond().x);
                // double jumpMod = Math.abs(alm.getPixelsPerSecond().y) / 525.0;
                // double normalSpeed = distancePerSec / playerSpeedMod;
                // System.out.println(speedMod + "(" + playerSpeedMod + ") " + alm.getUnk());
                if (speedMod > playerSpeedMod) {
                    if (alm.getUnk() == 0) { // to prevent FJ messing up
                        encounteredUnk0 = true;
                    }
                    if (!encounteredUnk0) {
                        if (speedMod > playerSpeedMod) {
                            // chr.getCheatTracker().registerOffense(CheatingOffense.FAST_MOVE);
                        }
                    }
                }
                // if (wasALM && (oldPosition.y == newPosition.y)) {
                // int distance = Math.abs(oldPosition.x - newPosition.x);
                // if (alm.getDuration() > 60) { // short durations are strange and show too fast movement
                // double distancePerSec = (distance / (double) ((LifeMovement) move).getDuration()) * 1000.0;
                // double speedMod = distancePerSec / 125.0;
                // double normalSpeed = distancePerSec / playerSpeedMod;
                // System.out.println(speedMod + " " + normalSpeed + " " + distancePerSec + " " + distance + " "
                // + alm.getWobble());
                // }
                // }
                // oldPosition = newPosition;
                // wasALM = true;
                // } else {
                // wasALM = false;
            }
        }
    }
}
