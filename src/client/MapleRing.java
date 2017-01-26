package client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import database.DatabaseConnection;

public class MapleRing implements Comparable<MapleRing> {

    private int ringId;
    private int ringId2;
    private int partnerId;
    private int itemId;
    private String partnerName;
    private boolean equipped;

    private MapleRing(int id, int id2, int partnerId, int itemid, String partnername) {
        this.ringId = id;
        this.ringId2 = id2;
        this.partnerId = partnerId;
        this.itemId = itemid;
        this.partnerName = partnername;
    }

    public static MapleRing loadFromDb(int ringId) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM rings WHERE id = ?");
            ps.setInt(1, ringId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            MapleRing ret = new MapleRing(ringId, rs.getInt("partnerRingId"), rs.getInt("partnerChrId"), rs.getInt("itemid"), rs.getString("partnerName"));
            rs.close();
            ps.close();
            return ret;
        } catch (SQLException ex) {
            return null;
        }
    }

    public static int[] createRing(int itemid, final MapleCharacter partner1, final MapleCharacter partner2) {
        try {
            if (partner1 == null) {
                return null;
            } else if (partner2 == null) {
                return null;
            }
            int[] ringID = new int[2];
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO rings (itemid, partnerChrId, partnername) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, itemid);
            ps.setInt(2, partner2.getId());
            ps.setString(3, partner2.getName());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            ringID[0] = rs.getInt(1); // ID.
            rs.close();
            ps.close();
            ps = con.prepareStatement("INSERT INTO rings (itemid, partnerRingId, partnerChrId, partnername) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, itemid);
            ps.setInt(2, ringID[0]);
            ps.setInt(3, partner1.getId());
            ps.setString(4, partner1.getName());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            rs.next();
            ringID[1] = rs.getInt(1);
            rs.close();
            ps.close();
            ps = con.prepareStatement("UPDATE rings SET partnerRingId = ? WHERE id = ?");
            ps.setInt(1, ringID[1]);
            ps.setInt(2, ringID[0]);
            ps.executeUpdate();
            ps.close();
            return ringID;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int getRingId() {
        return ringId;
    }

    public int getPartnerRingId() {
        return ringId2;
    }

    public int getPartnerChrId() {
        return partnerId;
    }

    public int getItemId() {
        return itemId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public boolean equipped() {
        return equipped;
    }

    public void equip() {
        equipped = true;
    }

    public void unequip() {
        equipped = false;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MapleRing) {
            if (((MapleRing) o).getRingId() == getRingId()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.ringId;
        return hash;
    }

    @Override
    public int compareTo(MapleRing other) {
        if (ringId < other.getRingId()) {
            return -1;
        } else if (ringId == other.getRingId()) {
            return 0;
        } else {
            return 1;
        }
    }

    public static boolean hasRing(int id, int ringid) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT id FROM rings WHERE id = ? AND charId = ?");
            ps.setInt(1, ringid);
            ps.setInt(2, id);
            ResultSet rs = ps.executeQuery();
            boolean has = rs.next();
            rs.close();
            ps.close();
            return has;
        } catch (SQLException ex) {
            return true;
        }
    }

    public static void removeFromDb(int ringid) {
        try {
            Connection con = DatabaseConnection.getConnection();
            int otherId;
            PreparedStatement ps = con.prepareStatement("SELECT partnerRingId FROM rings WHERE id = ?");
            ps.setInt(1, ringid);
            ResultSet rs = ps.executeQuery();
            rs.next();
            otherId = rs.getInt("partnerRingId");
            rs.close();
            ps = con.prepareStatement("DELETE FROM rings WHERE partnerChrId = ?");
            ps.setInt(1, ringid);
            ps.executeUpdate();
            ps = con.prepareStatement("DELETE FROM rings WHERE partnerChrId = ?");
            ps.setInt(1, otherId);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
        }
    }

//    public static boolean checkRingDB(MapleCharacter player) {
//        try {
//            Connection con = DatabaseConnection.getConnection();
//            PreparedStatement ps = con.prepareStatement("SELECT id FROM rings WHERE partnerChrId = ?");
//            ps.setInt(1, player.getId());
//            ResultSet rs = ps.executeQuery();
//            boolean has = rs.next();
//            rs.close();
//            ps.close();
//            return has;
//        } catch (SQLException ex) {
//            return true;
//        }
//    }
//
//    public static void removeRingFromDb(MapleCharacter player) {
//        try {
//            Connection con = DatabaseConnection.getConnection();
//            int otherId;
//            PreparedStatement ps = con.prepareStatement("SELECT partnerRingId FROM rings WHERE partnerChrId = ?");
//            ps.setInt(1, player.getId());
//            ResultSet rs = ps.executeQuery();
//            rs.next();
//            otherId = rs.getInt("partnerRingId");
//            rs.close();
//            ps = con.prepareStatement("DELETE FROM rings WHERE partnerChrId = ?");
//            ps.setInt(1, player.getId());
//            ps.executeUpdate();
//            ps = con.prepareStatement("DELETE FROM rings WHERE partnerChrId = ?");
//            ps.setInt(1, otherId);
//            ps.executeUpdate();
//            ps.close();
//        } catch (SQLException ex) {
//        }
//    }
}
