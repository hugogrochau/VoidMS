package net.login.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import client.MapleCharacter;
import client.MapleClient;
import database.DatabaseConnection;
import net.AbstractMaplePacketHandler;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewCharHandler extends AbstractMaplePacketHandler {
    private static Logger log = LoggerFactory.getLogger(ViewCharHandler.class);

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characters WHERE accountid = ?");
            ps.setInt(1, c.getAccID());
            int charsNum = 0;
            List<Integer> worlds = new ArrayList<Integer>();
            List<MapleCharacter> chars = new ArrayList<MapleCharacter>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int cworld = rs.getInt("world");
                boolean inside = false;
                for (int w : worlds)
                    if (w == cworld)
                        inside = true;

                    if (!inside)
                        worlds.add(cworld);

                MapleCharacter chr = MapleCharacter.loadCharFromDB(rs.getInt("id"), c, false);
                chars.add(chr);
                charsNum++;
            }
            rs.close();
            ps.close();
            int unk = charsNum + (3 - charsNum % 3);
            c.getSession().write(MaplePacketCreator.showAllCharacter(charsNum, unk));
            for (int w : worlds) {
                List<MapleCharacter> chrsinworld = new ArrayList<MapleCharacter>();
                for (MapleCharacter chr : chars)
                    if (chr.getWorld() == w)
                        chrsinworld.add(chr);
                c.getSession().write(MaplePacketCreator.showAllCharacterInfo(w, chrsinworld));
            }
        } catch (Exception e) {
                log.error("Viewing all chars failed", e);
        }
    }
}