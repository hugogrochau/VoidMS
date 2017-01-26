package net.channel.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import client.MapleCharacter;
import client.MapleClient;
import database.DatabaseConnection;
import net.AbstractMaplePacketHandler;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VIPAddMapHandler extends AbstractMaplePacketHandler {

    private static Logger log = LoggerFactory.getLogger(VIPAddMapHandler.class);

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        Connection con = DatabaseConnection.getConnection();
        int operation = slea.readByte();
        int type = slea.readByte();
        MapleCharacter player = c.getPlayer();

        switch (operation) {
            case 0: // Remove map
                int mapid = slea.readInt();
                try {
                    PreparedStatement ps = con.prepareStatement("DELETE FROM VIPRockMaps WHERE cid = ? AND mapid = ? AND type = ?");
                    ps.setInt(1, player.getId());
                    ps.setInt(2, mapid);
                    ps.setInt(3, type);
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException lawl) {
                }
                break;
            case 1: // Add map
                try {
                    if (player.getMapId() != 980000404 && player.getMapId() != 980000603){
                    PreparedStatement ps = con.prepareStatement("INSERT INTO VIPRockMaps (`cid`, `mapid`, `type`) VALUES (?, ?, ?)");
                    ps.setInt(1, player.getId());
                    ps.setInt(2, player.getMapId());
                    ps.setInt(3, type);
                    ps.executeUpdate();
                    ps.close();
                    }else{
                        player.dropMessage(1, "You cannot add this map to your VIP Rock.");
                    }
                } catch (SQLException lawl) {
                }
                break;
            default:
                log.info("Unhandled VIP Rock operation: " + slea.toString());
                break;
        }
        c.getSession().write(MaplePacketCreator.refreshVIPRockMapList(player.getVIPRockMaps(type), type));
    }
}