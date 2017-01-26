package tools;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import client.IItem;
import client.MapleCharacter;
import client.MapleDisease;
import client.MapleSkinColor;
import client.MapleStat;
import client.achievement.Achievement;
import net.channel.ChannelServer;
import net.channel.handler.ChangeChannelHandler;
import net.world.remote.WorldChannelInterface;
import scripting.npc.NPCScriptManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MaplePortal;
import server.TimerManager;
import server.life.MobSkill;
import server.life.MobSkillFactory;
import server.maps.MapleMap;
import server.maps.MapleMapObject;

/**
 *
 * @author Hugo
 */
public class ChaseyEvent {

    private LinkedList<MapleCharacter> players = new LinkedList<MapleCharacter>();
    private static final int[] itemids = {4002000, 4002003, 4002000, 4031543, 4031543, 4031544, 4031545, 4000038};
    private int count = 0;
    private boolean on = false;
    private boolean open = false;
    private ChannelServer c = ChannelServer.getInstance(1);
    private Calendar predictedStopTime;
    private ScheduledFuture<?> timer;
    private ScheduledFuture<?> bot;
    private WorldChannelInterface wci = c.getWorldInterface();
    private Random rand = new Random();
    private MapleCharacter lastCaught = null;

    public ChaseyEvent() {
        open();
    }

    private void rest() {
        predictedStopTime = Calendar.getInstance();
        predictedStopTime.add(Calendar.SECOND, 60);
        TimerManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                start();
            }
        }, 60 * 1000);
    }

    private void checkChasers() {
        bot = TimerManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                if (on && !open) {
                    hasChaser();
                    checkZombies();
                    checkChasers();
                }
            }
        }, 60 * 1000);
    }

    private void time() {
        int time = 10 * 60;
        sendClock(time);
        timer = TimerManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                endShort();
            }
        }, time * 1000);
    }

    public boolean isOn() {
        return on == true;
    }

    private void start() {
        on = true;
        open = false;
        int size = 0;
        size = players.size();
        if (size < 5) {
            try {
                wci.broadcastMessage(null, MaplePacketCreator.serverNotice(5, "[Zombies vs Humans] The event was cancelled. Not enough players joined. Better luck next time.").getBytes());
            } catch (RemoteException ex) {
                c.reconnectWorld();
            }
            end();
        } else {
            try {
                wci.broadcastMessage(null, MaplePacketCreator.serverNotice(5, "[Zombie vs Humans] The doors were locked to the refugee center. Everyone outside will have to fend for themselves.").getBytes());
            } catch (RemoteException ex) {
                c.reconnectWorld();
            }
            assignChaser();
            time();
            checkChasers();
        }
    }

    public void open() {
        on = true;
        open = true;
        try {
            wci.broadcastMessage(null, MaplePacketCreator.serverNotice(5, "[Zombies vs Humans] There seems to be a zombie outbreak going on! Use @zombie to join the refugees!").getBytes());
        } catch (RemoteException ex) {
            c.reconnectWorld();
        }
        rest();
    }

    public boolean addPlayer(String name) {
        if (on && open) {
            MapleCharacter player = c.getPlayerStorage().getCharacterByName(name);
            if (players.contains(player)) {
                return false;
            }
            if (player != null) {
                warpIn(player);
                return true;
            }
            try {
                int channel = wci.find(name);
                if (channel > -1) {
                    ChannelServer pserv = ChannelServer.getInstance(channel);
                    final MapleCharacter world_victim = pserv.getPlayerStorage().getCharacterByName(name);
                    if (world_victim != null) {
                        world_victim.dropMessage("[Zombies vs Humans] You are being warped across channels. This might take a few seconds");
                        ChangeChannelHandler.changeChannel(1, world_victim.getClient());
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } catch (RemoteException e) {
                c.reconnectWorld();
            }
            final String name1 = name;
            TimerManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    MapleCharacter player = c.getPlayerStorage().getCharacterByName(name1);
                    warpIn(player);
                }
            }, 3 * 1000);
            return true;
        } else {
            return false;
        }
    }

    private void checkZombies() {
        boolean b = false;
        for (MapleCharacter player : players) {
            if (!player.isChaser()) {
                b = true;
            }
        }
        if (!b && on && !open) {
            givePrize(lastCaught);
        }
    }

    private boolean hasChaser() { // TODO: make a npc to check this.
        boolean b = false;
        for (MapleCharacter player : players) {
            if (c.getPlayerStorage().getCharacterById(player.getId()) == null || player.getMapId() != 109010100) {
                players.remove(player);
            } else {
                if (player.isChaser()) {
                    b = true;
                }
            }
        }
        if (!b) {
            assignChaser();
        }
        return b;
    }

    private void warpIn(MapleCharacter player) {
        final MapleMap target = c.getMapFactory().getMap(109010100);
        MaplePortal portal = target.getPortal(rand.nextInt(target.getPortals().size()));
        player.changeMap(target, portal);
        NPCScriptManager.getInstance().start(player.getClient(), 9000004);
        sendMessage(player.getName() + " has joined the refugees.");
        player.setOriginalSkin(player.getSkinColor().getId());
        int skin = rand.nextInt(4);
        player.setSkinColor(MapleSkinColor.getById(skin));
        player.updateSingleStat(MapleStat.SKIN, skin);
        player.equipChanged();
        player.getClient().getSession().write(MaplePacketCreator.getClock(getTimeLeft()));
        players.add(player);
    }

    public boolean removePlayer(MapleCharacter player) {
        if (on) {
            if (players.contains(player)) {
                players.remove(player);
                player.changeMap(109050001);
                player.setSkinColor(MapleSkinColor.getById(player.getOriginalSkin()));
                player.updateSingleStat(MapleStat.SKIN, player.getOriginalSkin());
                player.equipChanged();
                player.clearChaseyCount();
                player.unsetChaser();
                player.dispelDebuffs();
                player.setOriginalSkin(0);
                sendMessage(player.getName() + " has left the refugee center.");
                player.dropMessage("[Zombies vs Humans] You left the refugee center.");
                return true;
            }
        }
        return false;
    }

    private void assignChaser() {
        int size = 0;
        MapleCharacter chaser = null;
        size = players.size();
        chaser = players.get(rand.nextInt(players.size()));
        if (count > size - 1) {
            end();
            return;
        }
        if (chaser == null) {
            assignChaser();
            count++;
            return;
        } else {
            chaser.setChaser();
            sendChaserPacket(chaser);
            sendMessage("Watch out! " + chaser.getName() + " was contaminated and the doors are locked. Try to stay away.");
        }
    }

    private int getTimeLeft() {
        int timeLeft;
        long StopTimeStamp = predictedStopTime.getTimeInMillis();
        long CurrentTimeStamp = Calendar.getInstance().getTimeInMillis();
        timeLeft = (int) (StopTimeStamp - CurrentTimeStamp) / 1000;
        return timeLeft;
    }

    private void givePrize(MapleCharacter player) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        int itemid = itemids[rand.nextInt(itemids.length)];
        IItem item = ii.getEquipById(itemid);
        player.achieve(Achievement.ZSURVIVE);
        MapleInventoryManipulator.addFromDrop(player.getClient(), item, true, true);
        try {
            wci.broadcastMessage(null, MaplePacketCreator.serverNotice(5, "[Zombies vs Humans] The zombies have taken over. But " + player.getName() + " has survived and won a prize.").getBytes());
        } catch (RemoteException ex) {
            c.reconnectWorld();
        }
        awardZombie();
        end();
    }

    private void awardZombie() {
        int ccount = 0;
        MapleCharacter winner = null;
        for (MapleCharacter player : players) {
            if (player.getChaseyCount() > ccount) {
                ccount = player.getChaseyCount();
                winner = player;
            }
        }
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        int itemid = itemids[rand.nextInt(itemids.length)];
        IItem item = ii.getEquipById(itemid);
        MapleInventoryManipulator.addFromDrop(winner.getClient(), item, true, true);
        String heshe = (winner.getGender() == 1 ? "She" : "He");
        sendMessage(winner.getName() + " caught the most refugees! " + heshe + " won a prize!");
        winner.achieve(Achievement.ZMOST);
    }

    public String getLeftAlive() {
        String str = "Refugees not contaminated:";
        for (MapleCharacter chars : players) {
            if (!chars.isChaser()) {
                str += chars.getName() + ",";
            }
        }
        return str;
    }

    public void setChaser(MapleCharacter player, MapleCharacter victim) {
        boolean chaser = true;
        if (!on || open) {
            return;
        }
        if (c.getPlayerStorage().getCharacterById(player.getId()) == null || c.getPlayerStorage().getCharacterById(victim.getId()) == null || victim.getMapId() != 109010100 || player.getMapId() != 109010100) {
            players.remove(player);
        } else {
            if (!victim.isChaser() && players.contains(victim)) {
                victim.setChaser();
                player.gainMeso(1000000, true);
                player.addChaseyCount();
                player.achieve(Achievement.ZCATCH);
                sendChaserPacket(victim);
                String heshe = (player.getGender() == 1 ? "She" : "He");
                sendMessage(player.getName() + " has just contaminated " + victim.getName() + " . " + heshe + " has contaminated " + player.getChaseyCount() + " refugees in total.");
                lastCaught = victim;
                for (MapleCharacter chars : players) {
                    if (c.getPlayerStorage().getCharacterById(victim.getId()) == null || victim.getMapId() != 109010100) {
                        players.remove(victim);
                    }
                    if (!chars.isChaser()) {
                        chaser = false;
                    }
                }
                if (chaser) {
                    givePrize(victim);
                }
            }
        }
    }

    private void sendMessage(String txt) {
        for (MapleCharacter player : players) {
            if (c.getPlayerStorage().getCharacterById(player.getId()) == null || player.getMapId() != 109010100) {

                players.remove(player);
            } else {
                player.dropMessage("[Zs vs Hs] " + txt);
            }
        }
    }

    private void sendClock(int time) {
        for (MapleCharacter player : players) {
            if (c.getPlayerStorage().getCharacterById(player.getId()) == null || player.getMapId() != 109010100) {
                players.remove(player);
            } else {
                player.getClient().getSession().write(MaplePacketCreator.getClock(time));
            }
        }
    }

    private void sendChaserPacket(MapleCharacter player) {
        MapleDisease darkness = MapleDisease.DARKNESS;
        MobSkill darknessskill = MobSkillFactory.getMobSkill(121, 1);
        List<Pair<MapleDisease, Integer>> debuff = Collections.singletonList(new Pair<MapleDisease, Integer>(darkness, Integer.valueOf(darknessskill.getX())));
        long mask = 0;
        for (Pair<MapleDisease, Integer> statup : debuff) {
            mask |= statup.getLeft().getValue();
        }
        player.setSkinColor(player.getSkinColor().getById(5));
        player.updateSingleStat(MapleStat.SKIN, 5);
        player.equipChanged();
        player.getMap().broadcastMessage(player, MaplePacketCreator.giveForeignDebuff(player.getId(), mask, darknessskill), false);
    }

    public void endShort() {
        if (on && !open) {
            ArrayList<MapleCharacter> winners = new ArrayList<MapleCharacter>();
            for (MapleCharacter chars : players) {
                if (!chars.isChaser()) {
                    winners.add(chars);
                }
            }
            MapleCharacter winner = winners.get(rand.nextInt(winners.size()));
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            int itemid = itemids[rand.nextInt(itemids.length)];
            IItem item = ii.getEquipById(itemid);
            MapleInventoryManipulator.addFromDrop(winner.getClient(), item, true, true);
            try {
                wci.broadcastMessage(null, MaplePacketCreator.serverNotice(5, "[Zombies vs Humans] Time is over. " + winner.getName() + " was selected as the prize winner! Congratulations to all survivors.").getBytes());
            } catch (RemoteException ex) {
                c.reconnectWorld();
            }
            end();
        }
    }

    public void end() {
        if (timer != null) {
            timer.cancel(true);
        }
        if (bot != null) {
            bot.cancel(true);
        }
        sendMessage("Event is over. You will be warped out shortly.");
        sendClock(30);
        on = false;
        open = false;
        TimerManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                for (MapleCharacter player : players) {
                    if (player != null && player.getMapId() == 109010100) {
                        player.changeMap(109050001);
                    }
                    player.setSkinColor(MapleSkinColor.getById(player.getOriginalSkin()));
                    player.updateSingleStat(MapleStat.SKIN, player.getOriginalSkin());
                    player.equipChanged();
                    player.clearChaseyCount();
                    player.unsetChaser();
                    player.dispelDebuffs();
                    player.setOriginalSkin(0);
                }
                players.clear();
                TimerManager.getInstance().schedule(new Runnable() {

                    public void run() {
                        MapleMap map = c.getMapFactory().getMap(109010100);
                        for (MapleMapObject obj : map.getAllPlayer()) {
                            MapleCharacter player = (MapleCharacter) obj;
                            player.changeMap(109050001);
                            player.setSkinColor(MapleSkinColor.getById(player.getOriginalSkin()));
                            player.updateSingleStat(MapleStat.SKIN, player.getOriginalSkin());
                            player.equipChanged();
                            player.clearChaseyCount();
                            player.unsetChaser();
                            player.dispelDebuffs();
                            player.setOriginalSkin(0);
                        }
                    }
                }, 5 * 1000);
            }
        }, 30 * 1000);
    }
}
