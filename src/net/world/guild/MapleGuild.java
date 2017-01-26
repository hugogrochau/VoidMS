package net.world.guild;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import client.MapleCharacter;
import client.MapleClient;
import database.DatabaseConnection;
import net.MaplePacket;
import net.channel.ChannelServer;
import net.channel.remote.ChannelWorldInterface;
import net.world.WorldRegistryImpl;
import tools.MaplePacketCreator;

public class MapleGuild implements java.io.Serializable {

    public final static int CREATE_GUILD_COST = 5000000;
    public final static int CHANGE_EMBLEM_COST = 15000000;
    public final static int INCREASE_CAPACITY_COST = 5000000;
    public final static boolean ENABLE_BBS = true;

    private enum BCOp {

        NONE, DISBAND, EMBELMCHANGE
    }
    public static final long serialVersionUID = 6322150443228168192L;
    private List<MapleGuildCharacter> members;
    private String rankTitles[] = new String[5];
    private String name;
    private int id;
    private int gp;
    private int logo;
    private int logoColor;
    private int leader;
    private int capacity;
    private int logoBG;
    private int logoBGColor;
    private String notice;
    private int signature;
    private Map<Integer, List<Integer>> notifications = new LinkedHashMap<Integer, List<Integer>>();
    private boolean bDirty = true;
    private int allianceId;
    final long ONE_DAY = 86400000;
    private int hideout;
    private long expiration;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public MapleGuild(int guildid, MapleGuildCharacter initiator) {
        members = new ArrayList<MapleGuildCharacter>();
        Connection con;
        try {
            con = DatabaseConnection.getConnection();
        } catch (Exception e) {
            System.out.println("Unable to connect to database to load guild information." + e);
            return;
        }
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM guilds WHERE guildid=" + guildid);
            ResultSet rs = ps.executeQuery();
            if (!rs.first()) {
                rs.close();
                ps.close();
                id = -1;
                return;
            }
            id = guildid;
            name = rs.getString("name");
            gp = rs.getInt("GP");
            logo = rs.getInt("logo");
            logoColor = rs.getInt("logoColor");
            logoBG = rs.getInt("logoBG");
            logoBGColor = rs.getInt("logoBGColor");
            capacity = rs.getInt("capacity");
            for (int i = 1; i <= 5; i++) {
                rankTitles[i - 1] = rs.getString("rank" + i + "title");
            }
            leader = rs.getInt("leader");
            notice = rs.getString("notice");
            signature = rs.getInt("signature");
            allianceId = rs.getInt("allianceId");
            hideout = rs.getInt("hideout");
            expiration = rs.getLong("hideout_expire");
            ps.close();
            rs.close();
            ps = con.prepareStatement("SELECT id, name, level, job, guildrank, allianceRank FROM characters WHERE guildid = ? ORDER BY guildrank ASC, name ASC");
            ps.setInt(1, guildid);
            rs = ps.executeQuery();
            if (!rs.first()) {
                rs.close();
                ps.close();
                System.out.println("No members in guild.");
                return;
            }
            do {
                members.add(new MapleGuildCharacter(rs.getInt("id"), rs.getInt("level"), rs.getString("name"), -1, rs.getInt("job"), rs.getInt("guildrank"), guildid, false, rs.getInt("allianceRank")));
            } while (rs.next());
            if (initiator != null) {
                setOnline(initiator.getId(), true, initiator.getChannel());
            }
            rs.close();
            ps.close();
        } catch (SQLException se) {
            System.out.println("unable to read guild information from sql " + se);
            se.printStackTrace();
            return;
        }
    }

    public void buildNotifications() {
        if (!bDirty) {
            return;
        }
        Set<Integer> chs = WorldRegistryImpl.getInstance().getChannelServer();
        if (notifications.keySet().size() != chs.size()) {
            notifications.clear();
            for (Integer ch : chs) {
                notifications.put(ch, new java.util.LinkedList<Integer>());
            }
        } else {
            for (List<Integer> l : notifications.values()) {
                l.clear();
            }
        }
        synchronized (members) {
            for (MapleGuildCharacter mgc : members) {
                if (!mgc.isOnline()) {
                    continue;
                }
                List<Integer> ch = notifications.get(mgc.getChannel());
                if (ch == null) {
                    System.out.println("Unable to connect to channel " + mgc.getChannel());
                } else {
                    ch.add(mgc.getId());
                }
            }
        }
        bDirty = false;
    }

    public void writeToDB() {
        writeToDB(false);
    }

    public void writeToDB(boolean bDisband) {
        Connection con;
        try {
            con = DatabaseConnection.getConnection();
        } catch (Exception e) {
            System.out.println("unable to connect to database to write guild information." + e);
            return;
        }
        try {
            if (!bDisband) {
                String sql = "UPDATE guilds SET " + "GP = ?, " + "logo = ?, " + "logoColor = ?, " + "logoBG = ?, " + "logoBGColor = ?, ";
                for (int i = 0; i < 5; i++) {
                    sql += "rank" + (i + 1) + "title = ?, ";
                }
                sql += "capacity = ?, " + "notice = ?, " + "hideout = ?, " + "hideout_expire = ?" + " WHERE guildid = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, gp);
                ps.setInt(2, logo);
                ps.setInt(3, logoColor);
                ps.setInt(4, logoBG);
                ps.setInt(5, logoBGColor);
                for (int i = 6; i < 11; i++) {
                    ps.setString(i, rankTitles[i - 6]);
                }
                ps.setInt(11, capacity);
                ps.setString(12, notice);
                ps.setInt(13, hideout);
                ps.setLong(14, expiration);
                ps.setInt(15, this.id);
                ps.execute();
                ps.close();
            } else {
                PreparedStatement ps = con.prepareStatement("UPDATE characters SET guildid = 0, guildrank = 5 WHERE guildid = ?");
                ps.setInt(1, this.id);
                ps.execute();
                ps.close();
                ps = con.prepareStatement("DELETE FROM guilds WHERE guildid = ?");
                ps.setInt(1, this.id);
                ps.execute();
                ps.close();
                removeHideoutNpcs();
                this.broadcast(MaplePacketCreator.guildDisband(this.id));
            }
        } catch (SQLException se) {
            System.out.println(se.getLocalizedMessage() + se);
        }
    }

    public boolean createHideout(int map) {
        if (hideoutExpired() && hideoutMapAvailable(map)) {
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("DELETE FROM `hideout_npcs` WHERE `mapid` = ?");
                ps.setInt(1, map);
                ps.executeUpdate();
                ps.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                return false;
            }
            this.hideout = map;
            this.expiration = System.currentTimeMillis() + (this.ONE_DAY * 3);
            this.writeToDB();
            return true;
        }
        return false;
    }

    public boolean hideoutExpired() {
        return (expiration < System.currentTimeMillis());
    }

    public boolean extendHideoutTime() {
        if (expiration < (System.currentTimeMillis() + (ONE_DAY * 6))) {
            expiration += ONE_DAY;
            this.writeToDB();
            return true;
        }
        return false;
    }

    public long getHideoutTimeLeft() {
        return ((expiration - System.currentTimeMillis()) / 1000 / 60 / 60);
    }

    public int getHideout() {
        return hideout;
    }

    public void removeHideout() {
        removeHideoutNpcs();
        hideout = 0;
        expiration = 0;
        this.writeToDB();
    }
//
//    public static boolean addHideoutNpc(int guildid, int map, int npcid, int x, int y, int fh) {
//        try {
//            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO `hideout_npcs` (`npcid`, `guildid`, `x`, `y`, `fh`, `mapid`) VALUES (?, ?, ?, ?, ?, ?)");
//            ps.setInt(1, npcid);
//            ps.setInt(2, guildid);
//            ps.setInt(3, x);
//            ps.setInt(4, y);
//            ps.setInt(5, fh);
//            ps.setInt(6, map);
//            ps.executeUpdate();
//            ps.close();
//        } catch (SQLException sqle) {
//            sqle.printStackTrace();
//            return false;
//        }
//        return true;
//    }

    public static boolean removeHideoutNpc(int guildid, int npcid) {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("DELETE FROM `hideout_npcs` WHERE `npcid` = ? AND `guildid` = ?");
            ps.setInt(1, npcid);
            ps.setInt(2, guildid);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
    }

    public boolean removeHideoutNpcs() {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("DELETE FROM `hideout_npcs` WHERE guildid = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
    }

    public String listHideoutNpcs() {
        try {
            StringBuilder sb = new StringBuilder("");
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT `npcid` FROM `hideout_npcs` WHERE `guildid` = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sb.append("\r\n\r\n#L" + rs.getInt("npcid") + "##fNpc/" + rs.getInt("npcid") + "/stand/0#\r\n#p" + rs.getInt("npcid") + "#");
            }
            rs.close();
            ps.close();
            return sb.toString();
        } catch (SQLException sqle) {
        }
        return "You have no hideout npcs";
    }

    public boolean hasHideoutNpc(int npcid) {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT `id` FROM `hideout_npcs` WHERE `npcid` = ? AND `guildid` = ?");
            ps.setInt(1, npcid);
            ps.setInt(2, id);
            ResultSet rs = ps.executeQuery();
            boolean b = rs.next();
            rs.close();
            ps.close();
            return b;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return false;
    }

    public static boolean hideoutMapAvailable(int id) {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT `hideout_expire` FROM `guilds` WHERE `hideout` = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long time = rs.getLong("hideout_expire");
                if (time > System.currentTimeMillis()) {
                    return false;
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException se) {
            System.out.println("Map Available exception");
            return false;
        }
        return true;
    }

    public int getMemberCount() {
        int num = 0;
        List<MapleGuildCharacter> mgc = this.members;
        for(MapleGuildCharacter mg : mgc)
        {
            num++;
        }
        return num;
    }
    
    public int getId() {
        return id;
    }

    public int getLeaderId() {
        return leader;
    }

    public int getGP() {
        return gp;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int l) {
        logo = l;
    }

    public int getLogoColor() {
        return logoColor;
    }

    public void setLogoColor(int c) {
        logoColor = c;
    }

    public int getLogoBG() {
        return logoBG;
    }

    public void setLogoBG(int bg) {
        logoBG = bg;
    }

    public int getLogoBGColor() {
        return logoBGColor;
    }

    public void setLogoBGColor(int c) {
        logoBGColor = c;
    }

    public String getNotice() {
        if (notice == null) {
            return "";
        }
        return notice;
    }

    public String getName() {
        return name;
    }

    public java.util.Collection<MapleGuildCharacter> getMembers() {
        return java.util.Collections.unmodifiableCollection(members);
    }

    public int getCapacity() {
        return capacity;
    }

    public int getSignature() {
        return signature;
    }

    public void broadcast(MaplePacket packet) {
        broadcast(packet, -1, BCOp.NONE);
    }

    public void broadcast(MaplePacket packet, int exception) {
        broadcast(packet, exception, BCOp.NONE);
    }

    public void broadcast(MaplePacket packet, int exceptionId, BCOp bcop) {
        WorldRegistryImpl wr = WorldRegistryImpl.getInstance();
        Set<Integer> chs = wr.getChannelServer();
        synchronized (notifications) {
            if (bDirty) {
                buildNotifications();
            }
            try {
                ChannelWorldInterface cwi;
                for (Integer ch : chs) {
                    cwi = wr.getChannel(ch);
                    if (notifications.get(ch).size() > 0) {
                        if (bcop == BCOp.DISBAND) {
                            cwi.setGuildAndRank(notifications.get(ch), 0, 5, exceptionId);
                        } else if (bcop == BCOp.EMBELMCHANGE) {
                            cwi.changeEmblem(this.id, notifications.get(ch), new MapleGuildSummary(this));
                        } else {
                            cwi.sendPacket(notifications.get(ch), packet, exceptionId);
                        }
                    }
                }
            } catch (java.rmi.RemoteException re) {
                System.out.println("Failed to contact channel(s) for broadcast." + re);
            }
        }
    }

    public void guildMessage(MaplePacket serverNotice) {
        for (MapleGuildCharacter mgc : members) {
            for (ChannelServer cs : ChannelServer.getAllInstances()) {
                if (cs.getPlayerStorage().getCharacterById(mgc.getId()) != null) {
                    MapleCharacter chr = cs.getPlayerStorage().getCharacterById(mgc.getId());
                    chr.getClient().getSession().write(serverNotice);
                    break;
                }
            }
        }
    }

    public void setOnline(int cid, boolean online, int channel) {
        boolean bBroadcast = true;
        for (MapleGuildCharacter mgc : members) {
            if (mgc.getId() == cid) {
                if (mgc.isOnline() && online) {
                    bBroadcast = false;
                }
                mgc.setOnline(online);
                mgc.setChannel(channel);
                break;
            }
        }
        if (bBroadcast) {
            this.broadcast(MaplePacketCreator.guildMemberOnline(id, cid, online), cid);
        }
        bDirty = true;
    }

    public void guildChat(String name, int cid, String msg) {
        this.broadcast(MaplePacketCreator.multiChat(name, msg, 2), cid);
    }

    public String getRankTitle(int rank) {
        return rankTitles[rank - 1];
    }

    public static int createGuild(int leaderId, String name) {
        Connection con;
        try {
            Properties dbProp = new Properties();
            InputStreamReader is = new FileReader("db.properties");
            dbProp.load(is);
            con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT guildid FROM guilds WHERE name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                rs.close();
                ps.close();
                return 0;
            }
            ps.close();
            rs.close();
            ps = con.prepareStatement("INSERT INTO guilds (`leader`, `name`, `signature`) VALUES (?, ?, ?)");
            ps.setInt(1, leaderId);
            ps.setString(2, name);
            ps.setInt(3, (int) System.currentTimeMillis());
            ps.execute();
            ps.close();
            ps = con.prepareStatement("SELECT guildid FROM guilds WHERE leader = ?");
            ps.setInt(1, leaderId);
            rs = ps.executeQuery();
            int guildid = 0;
            if (rs.first()) {
                guildid = rs.getInt("guildid");
            }
            rs.close();
            ps.close();
            return guildid;
        } catch (SQLException se) {
            System.out.println("SQL THROW" + se);
            return 0;
        } catch (Exception e) {
            System.out.println("CREATE GUILD THROW" + e);
            return 0;
        }
    }

    public int addGuildMember(MapleGuildCharacter mgc) {
        synchronized (members) {
            if (members.size() >= capacity) {
                return 0;
            }
            for (int i = members.size() - 1; i >= 0; i--) {
                if (members.get(i).getGuildRank() < 5 || members.get(i).getName().compareTo(mgc.getName()) < 0) {
                    members.add(i + 1, mgc);
                    bDirty = true;
                    break;
                }
            }
        }
        this.broadcast(MaplePacketCreator.newGuildMember(mgc));
        return 1;
    }

    public void leaveGuild(MapleGuildCharacter mgc) {
        this.broadcast(MaplePacketCreator.memberLeft(mgc, false));
        synchronized (members) {
            members.remove(mgc);
            bDirty = true;
        }
    }

    public void expelMember(MapleGuildCharacter initiator, String name, int cid) {
        synchronized (members) {
            java.util.Iterator<MapleGuildCharacter> itr = members.iterator();
            MapleGuildCharacter mgc;
            while (itr.hasNext()) {
                mgc = itr.next();
                if (mgc.getId() == cid && initiator.getGuildRank() < mgc.getGuildRank()) {
                    this.broadcast(MaplePacketCreator.memberLeft(mgc, true));
                    itr.remove();
                    bDirty = true;
                    this.broadcast(MaplePacketCreator.serverNotice(5, initiator.getName() + " has expelled " + mgc.getName() + "."));
                    try {
                        if (mgc.isOnline()) {
                            WorldRegistryImpl.getInstance().getChannel(mgc.getChannel()).setGuildAndRank(cid, 0, 5);
                        } else {
                            int sendTo = mgc.getId();
                            String sendFrom = initiator.getName();
                            String msg = "You have been expelled from the guild.";
                            try {
                                initiator.getName();
                                MaplePacketCreator.sendUnkwnNote(sendTo, msg, sendFrom);
                            } catch (SQLException e) {
                                System.out.println("SAVING NOTE" + e);
                            }
                            WorldRegistryImpl.getInstance().getChannel(1).setOfflineGuildStatus((short) 0, (byte) 5, cid);
                        }
                    } catch (RemoteException re) {
                        re.printStackTrace();
                        return;
                    }
                    return;
                }
            }
            System.out.println("Unable to find member with name " + name + " and id " + cid);
        }
    }

    public void changeRank(int cid, int newRank) {
        for (MapleGuildCharacter mgc : members) {
            if (cid == mgc.getId()) {
                try {
                    if (mgc.isOnline()) {
                        WorldRegistryImpl.getInstance().getChannel(mgc.getChannel()).setGuildAndRank(cid, this.id, newRank);
                    } else {
                        WorldRegistryImpl.getInstance().getChannel(1).setOfflineGuildStatus((short) this.id, (byte) newRank, cid);
                    }
                } catch (RemoteException re) {
                    re.printStackTrace();
                    return;
                }
                mgc.setGuildRank(newRank);
                this.broadcast(MaplePacketCreator.changeRank(mgc));
                return;
            }
        }
        System.out.println("INFO: unable to find the correct id for changeRank(" + cid + ", " + newRank + ")");
    }

    public void setGuildNotice(String notice) {
        this.notice = notice;
        writeToDB();
        this.broadcast(MaplePacketCreator.guildNotice(this.id, notice));
    }

    public void memberLevelJobUpdate(MapleGuildCharacter mgc) {
        for (MapleGuildCharacter member : members) {
            if (mgc.equals(member)) {
                member.setJobId(mgc.getJobId());
                member.setLevel(mgc.getLevel());
                this.broadcast(MaplePacketCreator.guildMemberLevelJobUpdate(mgc));
                break;
            }
        }
    }

    @SuppressWarnings("ManualArrayToCollectionCopy")
    public void changeRankTitle(String[] ranks) {
        for (int i = 0; i < 5; i++) {
            rankTitles[i] = ranks[i];
        }
        this.broadcast(MaplePacketCreator.rankTitleChange(this.id, ranks));
        this.writeToDB();
    }

    public void disbandGuild() {
        this.writeToDB(true);
        this.broadcast(null, -1, BCOp.DISBAND);
    }

    public void setGuildEmblem(short bg, byte bgcolor, short logo, byte logocolor) {
        this.logoBG = bg;
        this.logoBGColor = bgcolor;
        this.logo = logo;
        this.logoColor = logocolor;
        this.writeToDB();
        this.broadcast(null, -1, BCOp.EMBELMCHANGE);
    }

    public MapleGuildCharacter getMGC(int cid) {
        for (MapleGuildCharacter mgc : members) {
            if (mgc.getId() == cid) {
                return mgc;
            }
        }
        return null;
    }

    public boolean increaseCapacity() {
        if (capacity >= 200) {
            return false;
        }
        capacity += 5;
        this.writeToDB();
        this.broadcast(MaplePacketCreator.guildCapacityChange(this.id, this.capacity));
        return true;
    }

    public void gainGP(int amount) {
        this.gp += amount;
        this.writeToDB();
        this.guildMessage(MaplePacketCreator.updateGP(this.id, this.gp));
    }

    public static MapleGuildResponse sendInvite(MapleClient c, String targetName) {
        MapleCharacter mc = c.getChannelServer().getPlayerStorage().getCharacterByName(targetName);
        if (mc == null) {
            return MapleGuildResponse.NOT_IN_CHANNEL;
        }
        if (mc.getGuildId() > 0) {
            return MapleGuildResponse.ALREADY_IN_GUILD;
        }
        mc.getClient().getSession().write(MaplePacketCreator.guildInvite(c.getPlayer().getGuildId(), c.getPlayer().getName()));
        return null;
    }

    public static void displayGuildRanks(MapleClient c, int npcid) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT `name`, `GP`, `logoBG`, `logoBGColor`, `logo`, `logoColor` FROM guilds ORDER BY `GP` DESC LIMIT 50");
            ResultSet rs = ps.executeQuery();
            c.getSession().write(MaplePacketCreator.showGuildRanks(npcid, rs));
            ps.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("failed to display guild ranks." + e);
        }
    }

    public int getAllianceId() {
        return this.allianceId;
    }

    public void setAllianceId(int aid) {
        this.allianceId = aid;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE guilds SET allianceId = ? WHERE guildid = ?");
            ps.setInt(1, aid);
            ps.setInt(2, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
        }
    }
}
