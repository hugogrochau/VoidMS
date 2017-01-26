package net.channel.handler;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import client.BuddylistEntry;
import client.CharacterNameAndId;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleQuestStatus;
import client.SkillFactory;
import client.achievement.Achievement;
import database.DatabaseConnection;
import net.AbstractMaplePacketHandler;
import net.channel.ChannelServer;
import net.world.CharacterIdChannelPair;
import net.world.MaplePartyCharacter;
import net.world.PartyOperation;
import net.world.PlayerBuffValueHolder;
import net.world.PlayerCoolDownValueHolder;
import net.world.WorldServer;
import net.world.guild.MapleAlliance;
import net.world.remote.WorldChannelInterface;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class PlayerLoggedinHandler extends AbstractMaplePacketHandler {

    @Override
    public boolean validateState(MapleClient c) {
        return !c.isLoggedIn();
    }

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        int cid = slea.readInt();
        MapleCharacter player = null;
        WorldServer wserv = WorldServer.getInstance();
        try {
            player = MapleCharacter.loadCharFromDB(cid, c, true);
            c.setPlayer(player);
        } catch (SQLException e) {
            System.out.println("Loading the char failed" + e);
        }
        c.setAccID(player.getAccountID());
        int state = c.getLoginState();
        boolean allowLogin = true;
        ChannelServer channelServer = c.getChannelServer();
        synchronized (this) {
            if (state != MapleClient.LOGIN_SERVER_TRANSITION || !allowLogin) {
                c.setPlayer(null);
                c.getSession().close();
                return;
            }
            c.updateLoginState(MapleClient.LOGIN_LOGGEDIN);
        }
        ChannelServer cserv = ChannelServer.getInstance(c.getChannel());
        cserv.addPlayer(player);
        try {
            WorldChannelInterface wci = ChannelServer.getInstance(c.getChannel()).getWorldInterface();
            List<PlayerBuffValueHolder> buffs = wci.getBuffsFromStorage(cid);
            if (buffs != null) {
                c.getPlayer().silentGiveBuffs(buffs);
            }
            List<PlayerCoolDownValueHolder> cooldowns = wci.getCooldownsFromStorage(cid);
            if (cooldowns != null) {
                c.getPlayer().giveCoolDowns(cooldowns);
            }
        } catch (RemoteException e) {
            c.getChannelServer().reconnectWorld();
            e.printStackTrace();
            System.out.println("Problem with loading buffs for " + player.getName());
        }
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT SkillID,StartTime,length FROM CoolDowns WHERE charid = ?");
            ps.setInt(1, c.getPlayer().getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getLong("length") + rs.getLong("StartTime") - System.currentTimeMillis() <= 0) {
                    continue;
                }
                c.getPlayer().giveCoolDowns(rs.getInt("SkillID"), rs.getLong("StartTime"), rs.getLong("length"));
            }
            ps = con.prepareStatement("DELETE FROM CoolDowns WHERE charid = ?");
            ps.setInt(1, c.getPlayer().getId());
            ps.executeUpdate();
            rs.close();
            ps.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        c.getSession().write(MaplePacketCreator.getCharInfo(player));
        /*
         * try { PreparedStatement ps = con.prepareStatement("SELECT * FROM
         * dueypackages WHERE RecieverId = ? and checked = 1"); ps.setInt(1,
         * c.getPlayer().getId()); ResultSet rs = ps.executeQuery(); if
         * (rs.next()) { DueyHandler.reciveMsg(c, c.getPlayer().getId()); }
         * rs.close(); ps.close(); } catch (SQLException se) {
         * se.printStackTrace(); }
         */
        /*
         * if (player.votePoints() > 0) { int id = 4031543; int vote =
         * player.votePoints(); int qty = vote * 3; if (qty < 100) {
         * player.setVotePoints(0); MapleInventoryManipulator.addById(c, id,
         * (short) qty); player.dropMessage(1, "You have traded " + vote + "
         * vote points for " + qty + " Yellow Wish Tickets."); } else {
         * MapleInventoryManipulator.addById(c, id, (short) 100);
         * player.changeVotePoints(-100); player.dropMessage(1, "You have traded
         * 100 vote points for 300 Yellow Wish Tickets. Change channels to trade
         * the rest."); } }
         */
        if (player.getGMLevel() > 1) {
            int[] skills = {4201003, 2301004, 1301007, 2311003, 9101004};
            for (int i : skills) {
                SkillFactory.getSkill(i).getEffect(SkillFactory.getSkill(i).getMaxLevel()).applyTo(player);
            }
        }
        c.getSession().write(MaplePacketCreator.serverMessage(c.getChannelServer().getServerMessage()));
        player.getMap().addPlayer(player);
        if (!player.isGM()) {
            if (player.inEvent() && !wserv.canEventWarp()) {
                player.changeMap(109050001);
                player.dropMessage("You were warped out of the event map");
            } else if (player.getMapId() == 920011200) {
                player.changeMap(100000000);
                player.dropMessage("You were warped out of the Event training map");
            } else if (player.getMap().isJQ()) {
                player.changeMap(100000000);
                player.dropMessage("You were warped out of the JQ map");
            } else if (player.getMap().isHideout() && player.getGuild().getHideout() != player.getMapId()) {
                player.changeMap(100000000);
                player.dropMessage("You were expelled from your guild and warped out of the Hideout map");
            }
        }
        try {
            Collection<BuddylistEntry> buddies = player.getBuddylist().getBuddies();
            int buddyIds[] = player.getBuddylist().getBuddyIds();
            cserv.getWorldInterface().loggedOn(player.getName(), player.getId(), c.getChannel(), buddyIds);
            if (player.getParty() != null) {
                channelServer.getWorldInterface().updateParty(player.getParty().getId(), PartyOperation.LOG_ONOFF, new MaplePartyCharacter(player));
            }
            CharacterIdChannelPair[] onlineBuddies = cserv.getWorldInterface().multiBuddyFind(player.getId(), buddyIds);
            for (CharacterIdChannelPair onlineBuddy : onlineBuddies) {
                BuddylistEntry ble = player.getBuddylist().get(onlineBuddy.getCharacterId());
                ble.setChannel(onlineBuddy.getChannel());
                player.getBuddylist().put(ble);
            }
            c.getSession().write(MaplePacketCreator.updateBuddylist(buddies));
            c.getPlayer().sendMacros();
            try {
                c.getPlayer().showNote();
            } catch (SQLException e) {
            }
            //   if (c.getPlayer().getGMLevel() > 2) {
            //        c.getPlayer().scheduleAllowed();
            //     }
            if (player.getGuildId() > 0) {
                c.getChannelServer().getWorldInterface().setGuildMemberOnline(player.getMGC(), true, c.getChannel());
                c.getSession().write(MaplePacketCreator.showGuildInfo(player));
                int allianceId = player.getGuild().getAllianceId();
                if (allianceId > 0) {
                    MapleAlliance newAlliance = channelServer.getWorldInterface().getAlliance(allianceId);
                    if (newAlliance == null) {
                        newAlliance = MapleAlliance.loadAlliance(allianceId);
                        channelServer.getWorldInterface().addAlliance(allianceId, newAlliance);
                    }
                    c.getSession().write(MaplePacketCreator.getAllianceInfo(newAlliance));
                    c.getSession().write(MaplePacketCreator.getGuildAlliances(newAlliance, c));
                    c.getChannelServer().getWorldInterface().allianceMessage(allianceId, MaplePacketCreator.allianceMemberOnline(player, true), player.getId(), -1);
                }
            }
        } catch (RemoteException e) {
            System.out.println("ERROR: Some error with buddies. Account: " + player.getName());
            channelServer.reconnectWorld();
        }
        player.updatePartyMemberHP();
        player.sendKeymap();
        for (MapleQuestStatus status : player.getStartedQuests()) {
            if (status.hasMobKills()) {
                c.getSession().write(MaplePacketCreator.updateQuestMobKills(status));
            }
        }
        CharacterNameAndId pendingBuddyRequest = player.getBuddylist().pollPendingRequest();
        if (pendingBuddyRequest != null) {
            player.getBuddylist().put(new BuddylistEntry(pendingBuddyRequest.getName(), pendingBuddyRequest.getId(), -1, false));
            c.getSession().write(MaplePacketCreator.requestBuddylistAdd(pendingBuddyRequest.getId(), pendingBuddyRequest.getName()));
        }
        player.achieve(Achievement.LOGIN);
        if (player.isGM()) {
            player.startExpMultiplier();
            player.voteReminder();
        }
        player.checkMessenger();
        player.checkBerserk();
    }
}
