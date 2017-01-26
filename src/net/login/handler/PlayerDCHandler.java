package net.login.handler;

import client.MapleClient;
import net.AbstractMaplePacketHandler;
import tools.data.input.SeekableLittleEndianAccessor;

public class PlayerDCHandler extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        /* Connection con = DatabaseConnection.getConnection();
        try {
        PreparedStatement ps = con.prepareStatement("UPDATE accounts SET loggedin WHERE name = ?");
        ps.setString(1, c.getAccountName());
        ps.executeUpdate();
        ps.close();
        } catch (SQLException se) {
        }
        }*/
    }
}