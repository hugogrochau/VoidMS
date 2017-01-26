package net.channel.handler;

import java.net.InetAddress;
import java.rmi.RemoteException;
import client.MapleBuffStat;
import client.MapleCharacter;
import client.MapleClient;
import net.AbstractMaplePacketHandler;
import net.channel.ChannelServer;
import net.world.MapleMessengerCharacter;
import net.world.remote.WorldChannelInterface;
import server.MapleTrade;
import server.PlayerInteraction.HiredMerchant;
import server.PlayerInteraction.IPlayerInteractionManager;
import server.PlayerInteraction.MaplePlayerShop;
import server.PublicChatHandler;
import server.maps.FakeCharacter;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class ChangeChannelHandler extends AbstractMaplePacketHandler {

    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        int channel = slea.readByte() + 1;
        if (c.getPlayer().banned()) {
            c.getPlayer().dropMessage("You may not change channels");
        } else {
            if (c.getPlayer().getFollower() > -1) {
                MapleCharacter follower = c.getChannelServer().getPlayerStorage().getCharacterById(c.getPlayer().getFollower());
                if (follower != null) {
                    follower.dropMessage(c.getPlayer().getName() + " has changed channels, use !follow again");
                    changeChannel(channel, follower.getClient());
                }
            }
            changeChannel(channel, c);
        }
    }

    public static void changeChannel(int channel, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        if (!player.isAlive()) {
            c.getSession().write(MaplePacketCreator.enableActions());
            return;
        }
        if (PublicChatHandler.getPublicChatHolder().containsKey(player.getId())) {
            PublicChatHandler.getPublicChatHolder().remove(player.getId());
            PublicChatHandler.getPublicChatHolder().put(player.getId(), channel);
        }
        String ip = ChannelServer.getInstance(c.getChannel()).getIP(channel);
        String[] socket = ip.split(":");
        if (player.getTrade() != null) {
            MapleTrade.cancelTrade(player);
        }
        player.cancelMagicDoor();
        if (player.getFakeChars().size() > 0) {
            for (FakeCharacter fc : player.getFakeChars()) {
                if (fc.getFakeChar().getMap() == player.getMap()) {
                    c.getChannelServer().getAllClones().remove(fc);
                    player.getMap().removePlayer(fc.getFakeChar());
                }
            }
            player.getFakeChars().clear();
        }
        IPlayerInteractionManager ips = c.getPlayer().getInteraction();
        if (ips != null) {
            if (ips.isOwner(c.getPlayer())) {
                if (ips.getShopType() == 2) {
                    ips.removeAllVisitors(3, 1);
                    ips.closeShop(((MaplePlayerShop) ips).returnItems(c));
                } else if (ips.getShopType() == 1) {
                    c.getSession().write(MaplePacketCreator.shopVisitorLeave(0));
                    if (ips.getItems().size() == 0) {
                        ips.removeAllVisitors(3, 1);
                        ips.closeShop(((HiredMerchant) ips).returnItems(c));
                    }
                } else if (ips.getShopType() == 3 || ips.getShopType() == 4) {
                    ips.removeAllVisitors(3, 1);
                }
            } else {
                ips.removeVisitor(c.getPlayer());
            }
        }
        if (!player.getDiseases().isEmpty()) {
            player.cancelAllDebuffs();
        }
        if (player.getBuffedValue(MapleBuffStat.MONSTER_RIDING) != null) {
            player.cancelEffectFromBuffStat(MapleBuffStat.MONSTER_RIDING);
        }
        if (player.getBuffedValue(MapleBuffStat.SUMMON) != null) {
            player.cancelEffectFromBuffStat(MapleBuffStat.SUMMON);
        }
        if (player.getBuffedValue(MapleBuffStat.PUPPET) != null) {
            player.cancelEffectFromBuffStat(MapleBuffStat.PUPPET);
        }
        if (player.getBuffedValue(MapleBuffStat.MORPH) != null) {
            player.cancelEffectFromBuffStat(MapleBuffStat.MORPH);
        }
        if (player.getBuffedValue(MapleBuffStat.COMBO) != null) {
            player.cancelEffectFromBuffStat(MapleBuffStat.COMBO);
        }
        try {
            WorldChannelInterface wci = c.getChannelServer().getWorldInterface();
            wci.addBuffsToStorage(player.getId(), player.getAllBuffs());
            wci.addCooldownsToStorage(player.getId(), player.getAllCooldowns());
            if (player.getMessenger() != null) {
                MapleMessengerCharacter messengerplayer = new MapleMessengerCharacter(player);
                wci.silentLeaveMessenger(player.getMessenger().getId(), messengerplayer);
            }
        } catch (RemoteException e) {
            c.getChannelServer().reconnectWorld();
        }
        player.saveToDB(true, true);
        if (player.getCheatTracker() != null) {
            player.getCheatTracker().dispose();
        }
        player.getMap().removePlayer(player);
        c.getChannelServer().removePlayer(player);
        c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION);
        try {
            c.getSession().write(MaplePacketCreator.getChannelChange(InetAddress.getByName(socket[0]), Integer.parseInt(socket[1])));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
